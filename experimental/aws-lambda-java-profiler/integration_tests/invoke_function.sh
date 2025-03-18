#!/bin/bash

# Set variables
FUNCTION_NAME="aws-lambda-java-profiler-function-${GITHUB_RUN_ID}"
PAYLOAD='{"key": "value"}' 

echo "Invoking Lambda function: $FUNCTION_NAME"

# Invoke the Lambda function synchronously and capture the response
RESPONSE=$(aws lambda invoke \
    --function-name "$FUNCTION_NAME" \
    --payload "$PAYLOAD" \
    --cli-binary-format raw-in-base64-out \
    --log-type Tail \
    output.json)

# Extract the status code and log result from the response
STATUS_CODE=$(echo "$RESPONSE" | jq -r '.StatusCode')
LOG_RESULT=$(echo "$RESPONSE" | jq -r '.LogResult')

echo "Function invocation completed with status code: $STATUS_CODE"

# Decode and display the logs
if [ -n "$LOG_RESULT" ]; then
    echo "Function logs:"
    echo "$LOG_RESULT" | base64 --decode
else
    echo "No logs available."
fi

# Display the function output
echo "Function output:"
cat output.json

echo "$LOG_RESULT" | base64 --decode | grep "starting the profiler for coldstart" || exit 1
echo "$LOG_RESULT" | base64 --decode | grep -v "uploading" || exit 1

# Clean up the output file
rm output.json


# Invoke it a second time for warm start
echo "Invoking Lambda function: $FUNCTION_NAME"

# Invoke the Lambda function synchronously and capture the response
RESPONSE=$(aws lambda invoke \
    --function-name "$FUNCTION_NAME" \
    --payload "$PAYLOAD" \
    --cli-binary-format raw-in-base64-out \
    --log-type Tail \
    output.json)

# Extract the status code and log result from the response
STATUS_CODE=$(echo "$RESPONSE" | jq -r '.StatusCode')
LOG_RESULT=$(echo "$RESPONSE" | jq -r '.LogResult')

echo "Function invocation completed with status code: $STATUS_CODE"

# Decode and display the logs
if [ -n "$LOG_RESULT" ]; then
    echo "Function logs:"
    echo "$LOG_RESULT" | base64 --decode
else
    echo "No logs available."
fi

# Display the function output
echo "Function output:"
cat output.json

echo "$LOG_RESULT" | base64 --decode | grep "uploading" || exit 1

# Clean up the output file
rm output.json
