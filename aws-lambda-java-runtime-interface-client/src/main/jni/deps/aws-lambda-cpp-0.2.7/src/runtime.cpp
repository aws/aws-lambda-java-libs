/*
 * Copyright 2018-present Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

#include "aws/lambda-runtime/runtime.h"
#include "aws/lambda-runtime/version.h"
#include "aws/lambda-runtime/outcome.h"
#include "aws/logging/logging.h"
#include "aws/http/response.h"

#include <curl/curl.h>
#include <curl/curlver.h>
#include <climits> // for ULONG_MAX
#include <cassert>
#include <chrono>
#include <array>
#include <cstdlib> // for strtoul
#include <cinttypes>

#define AWS_LAMBDA_RUNTIME_API __attribute__((visibility("default")))

namespace aws {
namespace lambda_runtime {

static constexpr auto LOG_TAG = "LAMBDA_RUNTIME";
static constexpr auto REQUEST_ID_HEADER = "lambda-runtime-aws-request-id";
static constexpr auto TRACE_ID_HEADER = "lambda-runtime-trace-id";
static constexpr auto CLIENT_CONTEXT_HEADER = "lambda-runtime-client-context";
static constexpr auto COGNITO_IDENTITY_HEADER = "lambda-runtime-cognito-identity";
static constexpr auto DEADLINE_MS_HEADER = "lambda-runtime-deadline-ms";
static constexpr auto FUNCTION_ARN_HEADER = "lambda-runtime-invoked-function-arn";

enum Endpoints {
    INIT,
    NEXT,
    RESULT,
};

static bool is_success(aws::http::response_code httpcode)
{
    constexpr auto http_first_success_error_code = 200;
    constexpr auto http_last_success_error_code = 299;
    auto const code = static_cast<int>(httpcode);
    return code >= http_first_success_error_code && code <= http_last_success_error_code;
}

static size_t write_data(char* ptr, size_t size, size_t nmemb, void* userdata)
{
    if (!ptr) {
        return 0;
    }

    auto const resp = static_cast<http::response*>(userdata);
    assert(size == 1);
    (void)size; // avoid warning in release builds
    assert(resp);
    resp->append_body(ptr, nmemb);
    return nmemb;
}

// std::isspace has a few edge cases that would trigger UB. In particular, the documentation says:
// "The behavior is undefined if the value of the input is not representable as unsigned char and is not equal to EOF."
// So, this function does the simple obvious thing instead.
static inline bool is_whitespace(int ch)
{
    constexpr int space = 0x20;           // space (0x20, ' ')
    constexpr int form_feed = 0x0c;       // form feed (0x0c, '\f')
    constexpr int line_feed = 0x0a;       // line feed (0x0a, '\n')
    constexpr int carriage_return = 0x0d; // carriage return (0x0d, '\r')
    constexpr int horizontal_tab = 0x09;  // horizontal tab (0x09, '\t')
    constexpr int vertical_tab = 0x0b;    // vertical tab (0x0b, '\v')
    switch (ch) {
        case space:
        case form_feed:
        case line_feed:
        case carriage_return:
        case horizontal_tab:
        case vertical_tab:
            return true;
        default:
            return false;
    }
}

static inline std::string trim(std::string s)
{
    // trim right
    s.erase(std::find_if(s.rbegin(), s.rend(), [](int ch) { return !is_whitespace(ch); }).base(), s.end());
    // trim left
    s.erase(s.begin(), std::find_if(s.begin(), s.end(), [](int ch) { return !is_whitespace(ch); }));
    return s;
}

static size_t write_header(char* ptr, size_t size, size_t nmemb, void* userdata)
{
    if (!ptr) {
        return 0;
    }

    logging::log_debug(LOG_TAG, "received header: %s", std::string(ptr, nmemb).c_str());

    auto const resp = static_cast<http::response*>(userdata);
    assert(resp);
    for (size_t i = 0; i < nmemb; i++) {
        if (ptr[i] != ':') {
            continue;
        }
        std::string key{ptr, i};
        std::string value{ptr + i + 1, nmemb - i - 1};
        resp->add_header(trim(key), trim(value));
        break;
    }
    return size * nmemb;
}

static size_t read_data(char* buffer, size_t size, size_t nitems, void* userdata)
{
    auto const limit = size * nitems;
    auto ctx = static_cast<std::pair<std::string const&, size_t>*>(userdata);
    assert(ctx);
    auto const unread = ctx->first.length() - ctx->second;
    if (0 == unread) {
        return 0;
    }

    if (unread <= limit) {
        std::copy_n(ctx->first.begin() + ctx->second, unread, buffer);
        ctx->second += unread;
        return unread;
    }

    std::copy_n(ctx->first.begin() + ctx->second, limit, buffer);
    ctx->second += limit;
    return limit;
}

#ifndef NDEBUG
static int rt_curl_debug_callback(CURL* handle, curl_infotype type, char* data, size_t size, void* userdata)
{
    (void)handle;
    (void)type;
    (void)userdata;
    std::string s(data, size);
    logging::log_debug(LOG_TAG, "CURL DBG: %s", s.c_str());
    return 0;
}
#endif

runtime::runtime(std::string const& endpoint) : runtime(endpoint, "AWS_Lambda_Cpp/" + std::string(get_version())) {}

runtime::runtime(std::string const& endpoint, std::string const& user_agent)
    : m_user_agent_header("User-Agent: " + user_agent), m_endpoints{{endpoint + "/2018-06-01/runtime/init/error",
                                                                     endpoint + "/2018-06-01/runtime/invocation/next",
                                                                     endpoint + "/2018-06-01/runtime/invocation/"}},
      m_curl_handle(curl_easy_init())
{
    if (!m_curl_handle) {
        logging::log_error(LOG_TAG, "Failed to acquire curl easy handle for next.");
    }
}

runtime::~runtime()
{
    curl_easy_cleanup(m_curl_handle);
}

void runtime::set_curl_next_options()
{
    // lambda freezes the container when no further tasks are available. The freezing period could be longer than the
    // request timeout, which causes the following get_next request to fail with a timeout error.
    curl_easy_reset(m_curl_handle);
    curl_easy_setopt(m_curl_handle, CURLOPT_TIMEOUT, 0L);
    curl_easy_setopt(m_curl_handle, CURLOPT_CONNECTTIMEOUT, 1L);
    curl_easy_setopt(m_curl_handle, CURLOPT_NOSIGNAL, 1L);
    curl_easy_setopt(m_curl_handle, CURLOPT_TCP_NODELAY, 1L);
    curl_easy_setopt(m_curl_handle, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);

    curl_easy_setopt(m_curl_handle, CURLOPT_HTTPGET, 1L);
    curl_easy_setopt(m_curl_handle, CURLOPT_URL, m_endpoints[Endpoints::NEXT].c_str());

    curl_easy_setopt(m_curl_handle, CURLOPT_WRITEFUNCTION, write_data);
    curl_easy_setopt(m_curl_handle, CURLOPT_HEADERFUNCTION, write_header);

    curl_easy_setopt(m_curl_handle, CURLOPT_PROXY, "");

#ifndef NDEBUG
    curl_easy_setopt(m_curl_handle, CURLOPT_VERBOSE, 1);
    curl_easy_setopt(m_curl_handle, CURLOPT_DEBUGFUNCTION, rt_curl_debug_callback);
#endif
}

void runtime::set_curl_post_result_options()
{
    curl_easy_reset(m_curl_handle);
    curl_easy_setopt(m_curl_handle, CURLOPT_TIMEOUT, 0L);
    curl_easy_setopt(m_curl_handle, CURLOPT_CONNECTTIMEOUT, 1L);
    curl_easy_setopt(m_curl_handle, CURLOPT_NOSIGNAL, 1L);
    curl_easy_setopt(m_curl_handle, CURLOPT_TCP_NODELAY, 1L);
    curl_easy_setopt(m_curl_handle, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);

    curl_easy_setopt(m_curl_handle, CURLOPT_POST, 1L);
    curl_easy_setopt(m_curl_handle, CURLOPT_READFUNCTION, read_data);
    curl_easy_setopt(m_curl_handle, CURLOPT_WRITEFUNCTION, write_data);
    curl_easy_setopt(m_curl_handle, CURLOPT_HEADERFUNCTION, write_header);

    curl_easy_setopt(m_curl_handle, CURLOPT_PROXY, "");

#ifndef NDEBUG
    curl_easy_setopt(m_curl_handle, CURLOPT_VERBOSE, 1);
    curl_easy_setopt(m_curl_handle, CURLOPT_DEBUGFUNCTION, rt_curl_debug_callback);
#endif
}

runtime::next_outcome runtime::get_next()
{
    http::response resp;
    set_curl_next_options();
    curl_easy_setopt(m_curl_handle, CURLOPT_WRITEDATA, &resp);
    curl_easy_setopt(m_curl_handle, CURLOPT_HEADERDATA, &resp);

    curl_slist* headers = nullptr;
    headers = curl_slist_append(headers, m_user_agent_header.c_str());
    curl_easy_setopt(m_curl_handle, CURLOPT_HTTPHEADER, headers);

    logging::log_debug(LOG_TAG, "Making request to %s", m_endpoints[Endpoints::NEXT].c_str());
    CURLcode curl_code = curl_easy_perform(m_curl_handle);
    logging::log_debug(LOG_TAG, "Completed request to %s", m_endpoints[Endpoints::NEXT].c_str());
    curl_slist_free_all(headers);

    if (curl_code != CURLE_OK) {
        logging::log_debug(LOG_TAG, "CURL returned error code %d - %s", curl_code, curl_easy_strerror(curl_code));
        logging::log_error(LOG_TAG, "Failed to get next invocation. No Response from endpoint");
        return aws::http::response_code::REQUEST_NOT_MADE;
    }

    {
        long resp_code;
        curl_easy_getinfo(m_curl_handle, CURLINFO_RESPONSE_CODE, &resp_code);
        resp.set_response_code(static_cast<aws::http::response_code>(resp_code));
    }

    {
        char* content_type = nullptr;
        curl_easy_getinfo(m_curl_handle, CURLINFO_CONTENT_TYPE, &content_type);
        resp.set_content_type(content_type);
    }

    if (!is_success(resp.get_response_code())) {
        logging::log_error(
            LOG_TAG,
            "Failed to get next invocation. Http Response code: %d",
            static_cast<int>(resp.get_response_code()));
        return resp.get_response_code();
    }

    if (!resp.has_header(REQUEST_ID_HEADER)) {
        logging::log_error(LOG_TAG, "Failed to find header %s in response", REQUEST_ID_HEADER);
        return aws::http::response_code::REQUEST_NOT_MADE;
    }
    invocation_request req;
    req.payload = resp.get_body();
    req.request_id = resp.get_header(REQUEST_ID_HEADER);

    if (resp.has_header(TRACE_ID_HEADER)) {
        req.xray_trace_id = resp.get_header(TRACE_ID_HEADER);
    }

    if (resp.has_header(CLIENT_CONTEXT_HEADER)) {
        req.client_context = resp.get_header(CLIENT_CONTEXT_HEADER);
    }

    if (resp.has_header(COGNITO_IDENTITY_HEADER)) {
        req.cognito_identity = resp.get_header(COGNITO_IDENTITY_HEADER);
    }

    if (resp.has_header(FUNCTION_ARN_HEADER)) {
        req.function_arn = resp.get_header(FUNCTION_ARN_HEADER);
    }

    if (resp.has_header(DEADLINE_MS_HEADER)) {
        auto const& deadline_string = resp.get_header(DEADLINE_MS_HEADER);
        constexpr int base = 10;
        unsigned long ms = strtoul(deadline_string.c_str(), nullptr, base);
        assert(ms > 0);
        assert(ms < ULONG_MAX);
        req.deadline += std::chrono::milliseconds(ms);
        logging::log_info(
            LOG_TAG,
            "Received payload: %s\nTime remaining: %" PRId64,
            req.payload.c_str(),
            static_cast<int64_t>(req.get_time_remaining().count()));
    }
    return next_outcome(req);
}

runtime::post_outcome runtime::post_success(std::string const& request_id, invocation_response const& handler_response)
{
    std::string const url = m_endpoints[Endpoints::RESULT] + request_id + "/response";
    return do_post(url, request_id, handler_response);
}

runtime::post_outcome runtime::post_failure(std::string const& request_id, invocation_response const& handler_response)
{
    std::string const url = m_endpoints[Endpoints::RESULT] + request_id + "/error";
    return do_post(url, request_id, handler_response);
}

runtime::post_outcome runtime::do_post(
    std::string const& url,
    std::string const& request_id,
    invocation_response const& handler_response)
{
    set_curl_post_result_options();
    curl_easy_setopt(m_curl_handle, CURLOPT_URL, url.c_str());
    logging::log_info(LOG_TAG, "Making request to %s", url.c_str());

    curl_slist* headers = nullptr;
    if (handler_response.get_content_type().empty()) {
        headers = curl_slist_append(headers, "content-type: text/html");
    }
    else {
        headers = curl_slist_append(headers, ("content-type: " + handler_response.get_content_type()).c_str());
    }

    headers = curl_slist_append(headers, "Expect:");
    headers = curl_slist_append(headers, "transfer-encoding:");
    headers = curl_slist_append(headers, m_user_agent_header.c_str());
    auto const& payload = handler_response.get_payload();
    logging::log_debug(
        LOG_TAG, "calculating content length... %s", ("content-length: " + std::to_string(payload.length())).c_str());
    headers = curl_slist_append(headers, ("content-length: " + std::to_string(payload.length())).c_str());

    std::pair<std::string const&, size_t> ctx{payload, 0};
    aws::http::response resp;
    curl_easy_setopt(m_curl_handle, CURLOPT_WRITEDATA, &resp);
    curl_easy_setopt(m_curl_handle, CURLOPT_HEADERDATA, &resp);
    curl_easy_setopt(m_curl_handle, CURLOPT_READDATA, &ctx);
    curl_easy_setopt(m_curl_handle, CURLOPT_HTTPHEADER, headers);
    CURLcode curl_code = curl_easy_perform(m_curl_handle);
    curl_slist_free_all(headers);

    if (curl_code != CURLE_OK) {
        logging::log_debug(
            LOG_TAG,
            "CURL returned error code %d - %s, for invocation %s",
            curl_code,
            curl_easy_strerror(curl_code),
            request_id.c_str());
        return aws::http::response_code::REQUEST_NOT_MADE;
    }

    long http_response_code;
    curl_easy_getinfo(m_curl_handle, CURLINFO_RESPONSE_CODE, &http_response_code);

    if (!is_success(aws::http::response_code(http_response_code))) {
        logging::log_error(
            LOG_TAG, "Failed to post handler success response. Http response code: %ld.", http_response_code);
        return aws::http::response_code(http_response_code);
    }

    return post_outcome(no_result{});
}

static bool handle_post_outcome(runtime::post_outcome const& o, std::string const& request_id)
{
    if (o.is_success()) {
        return true;
    }

    if (o.get_failure() == aws::http::response_code::REQUEST_NOT_MADE) {
        logging::log_error(LOG_TAG, "Failed to send HTTP request for invocation %s.", request_id.c_str());
        return false;
    }

    logging::log_info(
        LOG_TAG,
        "HTTP Request for invocation %s was not successful. HTTP response code: %d.",
        request_id.c_str(),
        static_cast<int>(o.get_failure()));
    return false;
}

AWS_LAMBDA_RUNTIME_API
void run_handler(std::function<invocation_response(invocation_request const&)> const& handler)
{
    logging::log_info(LOG_TAG, "Initializing the C++ Lambda Runtime version %s", aws::lambda_runtime::get_version());
    std::string endpoint("http://");
    if (auto ep = std::getenv("AWS_LAMBDA_RUNTIME_API")) {
        assert(ep);
        logging::log_debug(LOG_TAG, "LAMBDA_SERVER_ADDRESS defined in environment as: %s", ep);
        endpoint += ep;
    }

    runtime rt(endpoint);

    size_t retries = 0;
    size_t const max_retries = 3;

    while (retries < max_retries) {
        auto next_outcome = rt.get_next();
        if (!next_outcome.is_success()) {
            if (next_outcome.get_failure() == aws::http::response_code::REQUEST_NOT_MADE) {
                ++retries;
                continue;
            }

            logging::log_info(
                LOG_TAG,
                "HTTP request was not successful. HTTP response code: %d. Retrying..",
                static_cast<int>(next_outcome.get_failure()));
            ++retries;
            continue;
        }

        retries = 0;

        auto const req = std::move(next_outcome).get_result();
        logging::log_info(LOG_TAG, "Invoking user handler");
        invocation_response res = handler(req);
        logging::log_info(LOG_TAG, "Invoking user handler completed.");

        if (res.is_success()) {
            const auto post_outcome = rt.post_success(req.request_id, res);
            if (!handle_post_outcome(post_outcome, req.request_id)) {
                return; // TODO: implement a better retry strategy
            }
        }
        else {
            const auto post_outcome = rt.post_failure(req.request_id, res);
            if (!handle_post_outcome(post_outcome, req.request_id)) {
                return; // TODO: implement a better retry strategy
            }
        }
    }

    if (retries == max_retries) {
        logging::log_error(
            LOG_TAG, "Exhausted all retries. This is probably a bug in libcurl v" LIBCURL_VERSION " Exiting!");
    }
}

static std::string json_escape(std::string const& in)
{
    constexpr char last_non_printable_character = 31;
    std::string out;
    out.reserve(in.length()); // most strings will end up identical
    for (char ch : in) {
        if (ch > last_non_printable_character && ch != '\"' && ch != '\\') {
            out.append(1, ch);
        }
        else {
            out.append(1, '\\');
            switch (ch) {
                case '\\':
                    out.append(1, '\\');
                    break;
                case '"':
                    out.append(1, '"');
                    break;
                case '\b':
                    out.append(1, 'b');
                    break;
                case '\f':
                    out.append(1, 'f');
                    break;
                case '\n':
                    out.append(1, 'n');
                    break;
                case '\r':
                    out.append(1, 'r');
                    break;
                case '\t':
                    out.append(1, 't');
                    break;
                default:
                    // escape and print as unicode codepoint
                    constexpr int printed_unicode_length = 6; // 4 hex + letter 'u' + \0
                    std::array<char, printed_unicode_length> buf;
                    sprintf(buf.data(), "u%04x", ch);
                    out.append(buf.data(), buf.size() - 1); // add only five, discarding the null terminator.
                    break;
            }
        }
    }
    return out;
}

AWS_LAMBDA_RUNTIME_API
invocation_response invocation_response::success(std::string const& payload, std::string const& content_type)
{
    invocation_response r;
    r.m_success = true;
    r.m_content_type = content_type;
    r.m_payload = payload;
    return r;
}

AWS_LAMBDA_RUNTIME_API
invocation_response invocation_response::failure(std::string const& error_message, std::string const& error_type)
{
    invocation_response r;
    r.m_success = false;
    r.m_content_type = "application/json";
    r.m_payload = R"({"errorMessage":")" + json_escape(error_message) + R"(","errorType":")" + json_escape(error_type) +
                  R"(", "stackTrace":[]})";
    return r;
}

} // namespace lambda_runtime
} // namespace aws
