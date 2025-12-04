#!/bin/bash

# Set variables
FUNCTION_NAME_CUSTOM_PROFILER_OPTIONS="aws-lambda-java-profiler-function-custom-${GITHUB_RUN_ID}"
PAYLOAD='{"key": "value"}'

# Expected profiler commands (should match create_function.sh)
EXPECTED_START_COMMAND="start,event=wall,interval=1us"
EXPECTED_STOP_COMMAND="stop,file=%s,include=*AWSLambda.main,include=start_thread"

echo "Invoking Lambda function with custom profiler options: $FUNCTION_NAME_CUSTOM_PROFILER_OPTIONS"

# Invoke the Lambda function synchronously and capture the response
RESPONSE=$(aws lambda invoke \
    --function-name "$FUNCTION_NAME_CUSTOM_PROFILER_OPTIONS" \
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

# Verify profiler started
echo "$LOG_RESULT" | base64 --decode | grep "starting the profiler for coldstart" || exit 1

# Verify custom start command is being used
echo "$LOG_RESULT" | base64 --decode | grep "$EXPECTED_START_COMMAND" || exit 1

# Verify no upload on cold start
echo "$LOG_RESULT" | base64 --decode | grep -v "uploading" || exit 1

# Clean up the output file
rm output.json


# Invoke it a second time for warm start
echo "Invoking Lambda function (warm start): $FUNCTION_NAME_CUSTOM_PROFILER_OPTIONS"

# Invoke the Lambda function synchronously and capture the response
RESPONSE=$(aws lambda invoke \
    --function-name "$FUNCTION_NAME_CUSTOM_PROFILER_OPTIONS" \
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

# Verify upload happens on warm start
echo "$LOG_RESULT" | base64 --decode | grep "uploading" || exit 1

# Verify custom stop command is being used
echo "$LOG_RESULT" | base64 --decode | grep "$EXPECTED_STOP_COMMAND" || exit 1

# Clean up the output file
rm output.json
