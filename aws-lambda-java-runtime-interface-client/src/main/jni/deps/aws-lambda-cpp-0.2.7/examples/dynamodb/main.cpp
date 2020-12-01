#include <aws/core/Aws.h>
#include <aws/core/utils/logging/LogLevel.h>
#include <aws/core/utils/logging/ConsoleLogSystem.h>
#include <aws/core/utils/logging/LogMacros.h>
#include <aws/core/utils/json/JsonSerializer.h>
#include <aws/core/utils/Outcome.h>
#include <aws/core/utils/Array.h>
#include <aws/core/platform/Environment.h>
#include <aws/core/client/ClientConfiguration.h>
#include <aws/core/auth/AWSCredentialsProvider.h>
#include <aws/dynamodb/DynamoDBClient.h>
#include <aws/dynamodb/model/QueryRequest.h>
#include <aws/lambda-runtime/runtime.h>
// API Gateway Input format
// {
//     "resource": "Resource path",
//     "path": "Path parameter",
//     "httpMethod": "Incoming request's method name"
//     "headers": {String containing incoming request headers}
//     "multiValueHeaders": {List of strings containing incoming request headers}
//     "queryStringParameters": {query string parameters }
//     "multiValueQueryStringParameters": {List of query string parameters}
//     "pathParameters":  {path parameters}
//     "stageVariables": {Applicable stage variables}
//     "requestContext": {Request context, including authorizer-returned key-value pairs}
//     "body": "A JSON string of the request payload."
//     "isBase64Encoded": "A boolean flag to indicate if the applicable request payload is Base64-encode"
// }

static char const TAG[] = "lambda";

struct criteria {
    criteria(Aws::Utils::Json::JsonView data) : error_msg(nullptr)
    {
        using namespace Aws::Utils;
        auto path_params = data.GetObject("pathParameters");
        if (!path_params.ValueExists("productId")) {
            error_msg =  "Missing URL parameter {productId}.";
            return;
        }

        product_id = path_params.GetString("productId");
        auto qs = data.GetObject("queryStringParameters");

        if (!qs.ValueExists("startDate")) {
            error_msg =  "Missing query string parameter 'startDate'.";
            return;
        }
        start_date = DateTime(qs.GetString("startDate"), DateFormat::ISO_8601);
        if (!start_date.WasParseSuccessful()) {
            error_msg =  "Invalid input format. startDate must be in ISO 8601 format.";
            return;
        }

        if (!qs.ValueExists("endDate")) {
            error_msg =  "Missing query string parameter 'endDate'.";
            return;
        }
        end_date = DateTime(qs.GetString("endDate"), DateFormat::ISO_8601);
        if (!end_date.WasParseSuccessful()) {
            error_msg =  "Invalid input format. endDate must be in ISO 8601 format.";
            return;
        }
    }

    std::string product_id;
    Aws::Utils::DateTime start_date;
    Aws::Utils::DateTime end_date;
    char const* error_msg;
};

Aws::Utils::Json::JsonValue query(criteria const cr, Aws::DynamoDB::DynamoDBClient const& client)
{
    using namespace Aws::DynamoDB;
    using namespace Aws::DynamoDB::Model;
    using namespace Aws::Utils::Json;

    AWS_LOGSTREAM_DEBUG(
        TAG,
        "criteria is: product_id: " << cr.product_id << " start_date epoch: " << cr.start_date.Millis()
                                    << " end_date epoch: " << cr.end_date.Millis());

    QueryRequest query;

    auto const& table_name = Aws::Environment::GetEnv("TABLE_NAME");
    query.SetTableName(table_name);
    query.SetKeyConditionExpression("#H = :h AND #R BETWEEN :s AND :e");
    query.AddExpressionAttributeNames("#H", "product_id");
    query.AddExpressionAttributeNames("#R", "date_time");

    query.AddExpressionAttributeValues(":h", AttributeValue(cr.product_id));
    AttributeValue date;
    date.SetN(std::to_string(cr.start_date.Millis() / 1000));
    query.AddExpressionAttributeValues(":s", date);

    date.SetN(std::to_string(cr.end_date.Millis() / 1000));
    query.AddExpressionAttributeValues(":e", date);

    auto outcome = client.Query(query);
    if (outcome.IsSuccess()) {
        auto const& maps = outcome.GetResult().GetItems(); // returns vector of map<column, attibute value>
        if (maps.empty()) {
            AWS_LOGSTREAM_DEBUG(TAG, "No data returned from query");
            return {};
        }

        // Schema
        // string_attr :product_id, hash_key: true
        // epoch_time_attr :date_time, range_key: true
        // string_attr :product_title
        // string_attr :marketplace
        // string_attr :product_category
        // date_attr :review_date
        // integer_attr :star_rating
        // float_attr :postive
        // float_attr :mixed
        // float_attr :neutral
        // float_attr :negative

        JsonValue output;
        output.WithString("product", maps[0].find("product_title")->second.GetS());
        output.WithString("category", maps[0].find("product_category")->second.GetS());
        Aws::Utils::Array<JsonValue> sentiments(maps.size());
        for (size_t i = 0; i < maps.size(); i++) {
            JsonValue review;
            auto&& m = maps[i];

            auto it = m.find("review_date");
            if (it != m.end()) {
                review.WithString("date", it->second.GetS());
            }

            it = m.find("positive");
            if (it != m.end()) {
                review.WithString("positive", it->second.GetN());
            }

            it = m.find("negative");
            if (it != m.end()) {
                review.WithString("negative", it->second.GetN());
            }

            it = m.find("mixed");
            if (it != m.end()) {
                review.WithString("mixed", it->second.GetN());
            }

            it = m.find("neutral");
            if (it != m.end()) {
                review.WithString("neutral", it->second.GetN());
            }

            sentiments[i] = std::move(review);
        }
        output.WithArray("sentiment", sentiments);
        return output;
    }

    AWS_LOGSTREAM_ERROR(TAG, "database query failed: " << outcome.GetError());
    return {};
}


aws::lambda_runtime::invocation_response my_handler(
    aws::lambda_runtime::invocation_request const& req,
    Aws::DynamoDB::DynamoDBClient const& client)
{
    using namespace Aws::Utils::Json;
    AWS_LOGSTREAM_DEBUG(TAG, "received payload: " << req.payload);
    JsonValue eventJson(req.payload);
    assert(eventJson.WasParseSuccessful());
    const criteria cr(eventJson);
    if (cr.error_msg) {
        JsonValue response;
        response.WithString("body", cr.error_msg).WithInteger("statusCode", 400);
        auto const apig_response = response.View().WriteCompact();
        AWS_LOGSTREAM_ERROR(TAG, "Validation failed. " << apig_response);
        return aws::lambda_runtime::invocation_response::success(apig_response, "application/json");
    }

    auto result = query(cr, client);
    auto const query_response = result.View().WriteCompact();
    AWS_LOGSTREAM_DEBUG(TAG, "query response: " << query_response);

    JsonValue response;
    if (result.View().ValueExists("product")) {
        response.WithString("body", query_response).WithInteger("statusCode", 200);
    }
    else {
        response.WithString("body", "No data found for this product.").WithInteger("statusCode", 400);
    }

    auto const apig_response = response.View().WriteCompact();
    AWS_LOGSTREAM_DEBUG(TAG, "api gateway response: " << apig_response);

    return aws::lambda_runtime::invocation_response::success(apig_response, "application/json");
}

std::function<std::shared_ptr<Aws::Utils::Logging::LogSystemInterface>()> GetConsoleLoggerFactory()
{
    return [] {
        return Aws::MakeShared<Aws::Utils::Logging::ConsoleLogSystem>(
            "console_logger", Aws::Utils::Logging::LogLevel::Trace);
    };
}

int main()
{
    using namespace Aws;
    SDKOptions options;
    options.loggingOptions.logLevel = Aws::Utils::Logging::LogLevel::Trace;
    options.loggingOptions.logger_create_fn = GetConsoleLoggerFactory();
    InitAPI(options);
    {
        Aws::Client::ClientConfiguration config;
        config.region = Aws::Environment::GetEnv("AWS_REGION");
        config.caFile = "/etc/pki/tls/certs/ca-bundle.crt";
        config.disableExpectHeader = true;

        auto credentialsProvider = Aws::MakeShared<Aws::Auth::EnvironmentAWSCredentialsProvider>(TAG);
        Aws::DynamoDB::DynamoDBClient client(credentialsProvider, config);
        auto handler_fn = [&client](aws::lambda_runtime::invocation_request const& req) {
            return my_handler(req, client);
        };
        aws::lambda_runtime::run_handler(handler_fn);
    }
    ShutdownAPI(options);
    return 0;
}
