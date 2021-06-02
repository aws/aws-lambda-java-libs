#pragma once
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

#include <array>
#include <chrono>
#include <string>
#include <functional>
#include <curl/curl.h>
#include "aws/lambda-runtime/outcome.h"
#include "aws/http/response.h"

namespace aws {
namespace lambda_runtime {

struct invocation_request {
    /**
     * The user's payload represented as a UTF-8 string.
     */
    std::string payload;

    /**
     * An identifier unique to the current invocation.
     */
    std::string request_id;

    /**
     * X-Ray tracing ID of the current invocation.
     */
    std::string xray_trace_id;

    /**
     * Information about the client application and device when invoked through the AWS Mobile SDK.
     */
    std::string client_context;

    /**
     * Information about the Amazon Cognito identity provider when invoked through the AWS Mobile SDK.
     */
    std::string cognito_identity;

    /**
     * The ARN requested. This can be different in each invoke that executes the same version.
     */
    std::string function_arn;

    /**
     * Function execution deadline counted in milliseconds since the Unix epoch.
     */
    std::chrono::time_point<std::chrono::system_clock> deadline;

    /**
     * The number of milliseconds left before lambda terminates the current execution.
     */
    inline std::chrono::milliseconds get_time_remaining() const;
};

class invocation_response {
private:
    /**
     * The output of the function which is sent to the lambda caller.
     */
    std::string m_payload;

    /**
     * The MIME type of the payload.
     * This is always set to 'application/json' in unsuccessful invocations.
     */
    std::string m_content_type;

    /**
     * Flag to distinguish if the contents are for successful or unsuccessful invocations.
     */
    bool m_success;

    /**
     * Instantiate an empty response. Used by the static functions 'success' and 'failure' to create a populated
     * invocation_response
     */
    invocation_response() = default;

public:
    // Create a success or failure response. Typically, you should use the static functions invocation_response::success
    // and invocation_response::failure, however, invocation_response::failure doesn't allow for arbitrary payloads.
    // To support clients that need to control the entire error response body (e.g. adding a stack trace), this
    // constructor should be used instead.
    // Note: adding an overload to invocation_response::failure is not feasible since the parameter types are the same.
    invocation_response(std::string const& payload, std::string const& content_type, bool success)
        : m_payload(payload), m_content_type(content_type), m_success(success)
    {
    }

    /**
     * Create a successful invocation response with the given payload and content-type.
     */
    static invocation_response success(std::string const& payload, std::string const& content_type);

    /**
     * Create a failure response with the given error message and error type.
     * The content-type is always set to application/json in this case.
     */
    static invocation_response failure(std::string const& error_message, std::string const& error_type);

    /**
     * Get the MIME type of the payload.
     */
    std::string const& get_content_type() const { return m_content_type; }

    /**
     * Get the payload string. The string is assumed to be UTF-8 encoded.
     */
    std::string const& get_payload() const { return m_payload; }

    /**
     * Returns true if the payload and content-type are set. Returns false if the error message and error types are set.
     */
    bool is_success() const { return m_success; }
};

struct no_result {
};

class runtime {
public:
    using next_outcome = aws::lambda_runtime::outcome<invocation_request, aws::http::response_code>;
    using post_outcome = aws::lambda_runtime::outcome<no_result, aws::http::response_code>;

    runtime(std::string const& endpoint, std::string const& user_agent);
    runtime(std::string const& endpoint);
    ~runtime();

    /**
     * Ask lambda for an invocation.
     */
    next_outcome get_next();

    /**
     * Tells lambda that the function has succeeded.
     */
    post_outcome post_success(std::string const& request_id, invocation_response const& handler_response);

    /**
     * Tells lambda that the function has failed.
     */
    post_outcome post_failure(std::string const& request_id, invocation_response const& handler_response);

private:
    void set_curl_next_options();
    void set_curl_post_result_options();
    post_outcome do_post(
        std::string const& url,
        std::string const& request_id,
        invocation_response const& handler_response);

private:
    std::string const m_user_agent_header;
    std::array<std::string const, 3> const m_endpoints;
    CURL* const m_curl_handle;
};

inline std::chrono::milliseconds invocation_request::get_time_remaining() const
{
    using namespace std::chrono;
    return duration_cast<milliseconds>(deadline - system_clock::now());
}

// Entry method
void run_handler(std::function<invocation_response(invocation_request const&)> const& handler);

} // namespace lambda_runtime
} // namespace aws
