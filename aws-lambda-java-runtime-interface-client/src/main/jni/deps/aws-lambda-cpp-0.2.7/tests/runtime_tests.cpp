#include <aws/core/client/ClientConfiguration.h>
#include <aws/core/utils/Outcome.h>
#include <aws/core/utils/memory/stl/AWSString.h>
#include <aws/core/utils/memory/stl/AWSStringStream.h>
#include <aws/core/platform/Environment.h>
#include <aws/iam/IAMClient.h>
#include <aws/iam/model/GetRoleRequest.h>
#include <aws/lambda/LambdaClient.h>
#include <aws/lambda/model/DeleteFunctionRequest.h>
#include <aws/lambda/model/GetFunctionRequest.h>
#include <aws/lambda/model/CreateFunctionRequest.h>
#include <aws/lambda/model/DeleteFunctionRequest.h>
#include <aws/lambda/model/InvokeRequest.h>
#include "gtest/gtest.h"

extern std::string aws_prefix;

namespace {

using namespace Aws::Lambda;

constexpr auto S3BUCKET = "aws-lambda-cpp-tests";
constexpr auto S3KEY = "lambda-test-fun.zip";
constexpr auto REQUEST_TIMEOUT = 15 * 1000;

struct LambdaRuntimeTest : public ::testing::Test {
    LambdaClient m_lambda_client;
    Aws::IAM::IAMClient m_iam_client;
    static Aws::Client::ClientConfiguration create_iam_config()
    {
        Aws::Client::ClientConfiguration config;
        config.requestTimeoutMs = REQUEST_TIMEOUT;
        config.region = Aws::Region::US_EAST_1;
        return config;
    }

    static Aws::Client::ClientConfiguration create_lambda_config()
    {
        Aws::Client::ClientConfiguration config;
        config.requestTimeoutMs = REQUEST_TIMEOUT;
        config.region = Aws::Environment::GetEnv("AWS_REGION");
        return config;
    }

    static Aws::String build_resource_name(Aws::String const& name)
    {
        return aws_prefix.c_str() + name; // NOLINT
    }

    LambdaRuntimeTest() : m_lambda_client(create_lambda_config()), m_iam_client(create_iam_config()) {}

    ~LambdaRuntimeTest() override
    {
        // clean up in case we exited one test abnormally
        delete_function(build_resource_name("echo_success"), false /*assert*/);
        delete_function(build_resource_name("echo_failure"), false /*assert*/);
        delete_function(build_resource_name("binary_response"), false /*assert*/);
    }

    Aws::String get_role_arn(Aws::String const& role_name)
    {
        using namespace Aws::IAM;
        using namespace Aws::IAM::Model;
        GetRoleRequest request;
        request.WithRoleName(role_name);
        auto outcome = m_iam_client.GetRole(request);
        EXPECT_TRUE(outcome.IsSuccess());
        if (outcome.IsSuccess()) {
            return outcome.GetResult().GetRole().GetArn();
        }
        return {};
    }

    void create_function(Aws::String const& function_name, Aws::String const& handler_name)
    {
        Model::CreateFunctionRequest create_function_request;
        create_function_request.SetHandler(handler_name);
        create_function_request.SetFunctionName(function_name);
        // I ran into eventual-consistency issues when creating the role dynamically as part of the test.
        create_function_request.SetRole(get_role_arn("integration-tests"));
        Model::FunctionCode funcode;
        funcode.WithS3Bucket(S3BUCKET).WithS3Key(build_resource_name(S3KEY));
        create_function_request.SetCode(funcode);
        create_function_request.SetRuntime(Aws::Lambda::Model::Runtime::provided);

        auto outcome = m_lambda_client.CreateFunction(create_function_request);
        ASSERT_TRUE(outcome.IsSuccess()) << "Failed to create function " << function_name;
    }

    void delete_function(Aws::String const& function_name, bool assert = true)
    {
        Model::DeleteFunctionRequest delete_function_request;
        delete_function_request.SetFunctionName(function_name);
        auto outcome = m_lambda_client.DeleteFunction(delete_function_request);
        if (assert) {
            ASSERT_TRUE(outcome.IsSuccess()) << "Failed to delete function " << function_name;
        }
    }
};

TEST_F(LambdaRuntimeTest, echo_success)
{
    Aws::String const funcname = build_resource_name("echo_success");
    constexpr auto payload_content = "Hello, Lambda!";
    create_function(funcname, "echo_success" /*handler_name*/);
    Model::InvokeRequest invoke_request;
    invoke_request.SetFunctionName(funcname);
    invoke_request.SetInvocationType(Model::InvocationType::RequestResponse);
    invoke_request.SetContentType("application/json");

    std::shared_ptr<Aws::IOStream> payload = Aws::MakeShared<Aws::StringStream>("FunctionTest");
    Aws::Utils::Json::JsonValue json_payload;
    json_payload.WithString("barbaz", payload_content);
    *payload << json_payload.View().WriteCompact();
    invoke_request.SetBody(payload);

    Model::InvokeOutcome invoke_outcome = m_lambda_client.Invoke(invoke_request);
    EXPECT_TRUE(invoke_outcome.IsSuccess());
    Aws::StringStream output;
    if (!invoke_outcome.IsSuccess()) {
        delete_function(funcname);
        return;
    }
    EXPECT_EQ(200, invoke_outcome.GetResult().GetStatusCode());
    EXPECT_TRUE(invoke_outcome.GetResult().GetFunctionError().empty());
    auto const json_response = Aws::Utils::Json::JsonValue(invoke_outcome.GetResult().GetPayload());
    EXPECT_TRUE(json_response.WasParseSuccessful());
    EXPECT_STREQ(payload_content, json_response.View().GetString("barbaz").c_str());
    delete_function(funcname);
}

TEST_F(LambdaRuntimeTest, echo_unicode)
{
    Aws::String const funcname = build_resource_name("echo_success"); // re-use the echo method but with Unicode input
    constexpr auto payload_content = "画像は1000語の価値がある";
    create_function(funcname, "echo_success" /*handler_name*/);
    Model::InvokeRequest invoke_request;
    invoke_request.SetFunctionName(funcname);
    invoke_request.SetInvocationType(Model::InvocationType::RequestResponse);
    invoke_request.SetContentType("application/json");

    std::shared_ptr<Aws::IOStream> payload = Aws::MakeShared<Aws::StringStream>("FunctionTest");
    Aws::Utils::Json::JsonValue json_payload;
    json_payload.WithString("UnicodeText", payload_content);
    *payload << json_payload.View().WriteCompact();
    invoke_request.SetBody(payload);

    Model::InvokeOutcome invoke_outcome = m_lambda_client.Invoke(invoke_request);
    EXPECT_TRUE(invoke_outcome.IsSuccess());
    Aws::StringStream output;
    if (!invoke_outcome.IsSuccess()) {
        delete_function(funcname);
        return;
    }
    EXPECT_EQ(200, invoke_outcome.GetResult().GetStatusCode());
    EXPECT_TRUE(invoke_outcome.GetResult().GetFunctionError().empty());
    auto const json_response = Aws::Utils::Json::JsonValue(invoke_outcome.GetResult().GetPayload());
    EXPECT_TRUE(json_response.WasParseSuccessful());
    EXPECT_STREQ(payload_content, json_response.View().GetString("UnicodeText").c_str());
    delete_function(funcname);
}

TEST_F(LambdaRuntimeTest, echo_failure)
{
    Aws::String const funcname = build_resource_name("echo_failure");
    create_function(funcname, "echo_failure" /*handler_name*/);
    Model::InvokeRequest invoke_request;
    invoke_request.SetFunctionName(funcname);
    invoke_request.SetInvocationType(Model::InvocationType::RequestResponse);

    Model::InvokeOutcome invoke_outcome = m_lambda_client.Invoke(invoke_request);
    EXPECT_TRUE(invoke_outcome.IsSuccess());
    EXPECT_EQ(200, invoke_outcome.GetResult().GetStatusCode());
    EXPECT_STREQ("Unhandled", invoke_outcome.GetResult().GetFunctionError().c_str());
    delete_function(funcname);
}

TEST_F(LambdaRuntimeTest, binary_response)
{
    Aws::String const funcname = build_resource_name("binary_response");
    unsigned long constexpr expected_length = 1451;
    create_function(funcname, "binary_response" /*handler_name*/);
    Model::InvokeRequest invoke_request;
    invoke_request.SetFunctionName(funcname);
    invoke_request.SetInvocationType(Model::InvocationType::RequestResponse);

    Model::InvokeOutcome invoke_outcome = m_lambda_client.Invoke(invoke_request);
    EXPECT_TRUE(invoke_outcome.IsSuccess());
    EXPECT_EQ(200, invoke_outcome.GetResult().GetStatusCode());
    EXPECT_TRUE(invoke_outcome.GetResult().GetFunctionError().empty());
    EXPECT_EQ(expected_length, invoke_outcome.GetResult().GetPayload().tellp());
    delete_function(funcname);
}
} // namespace
