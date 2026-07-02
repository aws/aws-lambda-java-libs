# integration test script for aws-lambda-java-log4j2.
# invokes the deployed lambda function and verifies logs appear in CloudWatch.

set -euo pipefail

FUNCTION_NAME="${LOG4J2_TEST_FUNCTION:?LOG4J2_TEST_FUNCTION env var is required}"
REGION="${AWS_REGION:?AWS_REGION env var is required}"
MARKER="integ-test-$(date +%s)-${RANDOM}"

echo "=== Log4j2 Integration Test ==="
echo "Function: ${FUNCTION_NAME}"
echo "Region:   ${REGION}"
echo "Marker:   ${MARKER}"
echo ""

# invoke the lambda function
echo ">>> Invoking Lambda function..."
INVOKE_OUTPUT=$(aws lambda invoke \
    --function-name "${FUNCTION_NAME}" \
    --region "${REGION}" \
    --payload "{\"marker\": \"${MARKER}\"}" \
    --cli-binary-format raw-in-base64-out \
    --output json \
    /tmp/integ-test-response.json) || {
    echo "FAIL: aws lambda invoke command failed with exit code $?"
    echo "Output: ${INVOKE_OUTPUT:-<empty>}"
    exit 1
}

echo "Invoke output: ${INVOKE_OUTPUT}"
RESPONSE=$(cat /tmp/integ-test-response.json)
echo "Response payload: ${RESPONSE}"

# check for lambda execution errors
FUNCTION_ERROR=$(echo "${INVOKE_OUTPUT}" | jq -r '.FunctionError // empty')
if [ -n "${FUNCTION_ERROR}" ]; then
    echo "FAIL: Lambda function returned an execution error (FunctionError: ${FUNCTION_ERROR})"
    echo "Error response: ${RESPONSE}"
    exit 1
fi

# verify the function executed successfully
if echo "${RESPONSE}" | grep -q "OK:${MARKER}"; then
    echo ">>> Function invocation successful."
else
    echo "FAIL: Unexpected response from Lambda function."
    echo "Expected response containing: OK:${MARKER}"
    echo "Got: ${RESPONSE}"
    exit 1
fi

# query CloudWatch logs for the marker 
LOG_GROUP="/aws/lambda/${FUNCTION_NAME}"
echo ""
echo ">>> Querying CloudWatch Logs group: ${LOG_GROUP}"

MAX_ATTEMPTS=5
WAIT_SECONDS=10
FOUND=false

for attempt in $(seq 1 $MAX_ATTEMPTS); do
    echo ">>> Attempt ${attempt}/${MAX_ATTEMPTS}: waiting ${WAIT_SECONDS}s for log propagation..."
    sleep "${WAIT_SECONDS}"

    LOGS_OUTPUT=$(aws logs filter-log-events \
        --log-group-name "${LOG_GROUP}" \
        --region "${REGION}" \
        --filter-pattern "\"INTEG_TEST_MARKER\" \"${MARKER}\"" \
        --start-time $(($(date +%s) * 1000 - 120000)) \
        --output json 2>&1)

    if echo "${LOGS_OUTPUT}" | grep -q "INTEG_TEST_MARKER: ${MARKER}"; then
        FOUND=true
        break
    fi

    echo "    Marker not found yet."
    WAIT_SECONDS=$((WAIT_SECONDS * 2))
done

# verify the marker was found
if [ "${FOUND}" = true ]; then
    echo ""
    echo "=== PASS: Log4j2 integration test succeeded ==="
    echo "The marker '${MARKER}' was found in CloudWatch Logs (attempt ${attempt})."
    echo "This confirms that the LambdaAppender plugin was discovered by Log4j2"
    echo "and logs are being delivered to CloudWatch correctly."
else
    echo ""
    echo "=== FAIL: Log4j2 integration test failed ==="
    echo "The marker '${MARKER}' was NOT found in CloudWatch Logs after ${MAX_ATTEMPTS} attempts."
    echo "This indicates that the LambdaAppender was not discovered by Log4j2,"
    echo "likely due to a missing Log4j2Plugins.dat in the packaged JAR."
    echo ""
    echo "Dumping all recent log events for debugging:"
    aws logs filter-log-events \
        --log-group-name "${LOG_GROUP}" \
        --region "${REGION}" \
        --start-time $(($(date +%s) * 1000 - 120000)) \
        --limit 50 \
        --output text 2>&1 || true
    exit 1
fi
