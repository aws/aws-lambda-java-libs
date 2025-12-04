#!/bin/bash

# Set variables
FUNCTION_NAME="aws-lambda-java-profiler-function-${GITHUB_RUN_ID}"
FUNCTION_NAME_CUSTOM_PROFILER_OPTIONS="aws-lambda-java-profiler-function-custom-${GITHUB_RUN_ID}"
ROLE_NAME="aws-lambda-java-profiler-role-${GITHUB_RUN_ID}"
HANDLER="helloworld.Handler::handleRequest"
RUNTIME="java21"
LAYER_ARN=$(cat /tmp/layer_arn)

JAVA_TOOL_OPTIONS="-XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -javaagent:/opt/profiler-extension.jar"
AWS_LAMBDA_PROFILER_RESULTS_BUCKET_NAME="aws-lambda-java-profiler-bucket-${GITHUB_RUN_ID}"
AWS_LAMBDA_PROFILER_START_COMMAND="start,event=wall,interval=1us,file=/tmp/profile.jfr"
AWS_LAMBDA_PROFILER_STOP_COMMAND="stop,file=%s"

# Compile the Hello World project
cd integration_tests/helloworld
gradle :buildZip
cd ../..

# Create IAM role for Lambda
ROLE_ARN=$(aws iam create-role \
    --role-name $ROLE_NAME \
    --assume-role-policy-document '{"Version": "2012-10-17","Statement": [{ "Effect": "Allow", "Principal": {"Service": "lambda.amazonaws.com"}, "Action": "sts:AssumeRole"}]}' \
    --query 'Role.Arn' \
    --output text)

# Attach basic Lambda execution policy to the role
aws iam attach-role-policy \
    --role-name $ROLE_NAME \
    --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole

# Attach s3:PutObject policy to the role so we can upload profiles
POLICY_DOCUMENT=$(cat <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "s3:PutObject",
            "Resource": "arn:aws:s3:::$AWS_LAMBDA_PROFILER_RESULTS_BUCKET_NAME/*"
        }
    ]
}
EOF
)

aws iam put-role-policy \
    --role-name "$ROLE_NAME" \
    --policy-name "s3PutObject" \
    --policy-document "$POLICY_DOCUMENT"

# Wait for the role to be created and policy to be attached
echo "Waiting for IAM role to be ready..."
sleep 10

# Create Lambda function
aws lambda create-function \
    --function-name "$FUNCTION_NAME" \
    --runtime "$RUNTIME" \
    --role "$ROLE_ARN" \
    --handler "$HANDLER" \
    --timeout 30 \
    --memory-size 512 \
    --zip-file fileb://integration_tests/helloworld/build/distributions/code.zip \
    --environment "Variables={JAVA_TOOL_OPTIONS='$JAVA_TOOL_OPTIONS',AWS_LAMBDA_PROFILER_RESULTS_BUCKET_NAME='$AWS_LAMBDA_PROFILER_RESULTS_BUCKET_NAME',AWS_LAMBDA_PROFILER_DEBUG='true'}" \
    --layers "$LAYER_ARN"


# Create Lambda function custom profiler options
aws lambda create-function \
    --function-name "$FUNCTION_NAME_CUSTOM_PROFILER_OPTIONS" \
    --runtime "$RUNTIME" \
    --role "$ROLE_ARN" \
    --handler "$HANDLER" \
    --timeout 30 \
    --memory-size 512 \
    --zip-file fileb://integration_tests/helloworld/build/distributions/code.zip \
    --environment "Variables={JAVA_TOOL_OPTIONS='$JAVA_TOOL_OPTIONS',AWS_LAMBDA_PROFILER_RESULTS_BUCKET_NAME='$AWS_LAMBDA_PROFILER_RESULTS_BUCKET_NAME',AWS_LAMBDA_PROFILER_DEBUG='true',AWS_LAMBDA_PROFILER_START_COMMAND='$AWS_LAMBDA_PROFILER_START_COMMAND',AWS_LAMBDA_PROFILER_STOP_COMMAND='$AWS_LAMBDA_PROFILER_STOP_COMMAND'}" \
    --layers "$LAYER_ARN"

echo "Lambda function '$FUNCTION_NAME' created successfully with Java 21 runtime"

echo "Waiting the function to be ready so we can invoke it..."
sleep 10
