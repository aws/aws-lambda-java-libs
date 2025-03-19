#!/bin/bash

# Check if a function name was provided
if [ $# -eq 0 ]; then
    echo "Please provide a function name as an argument."
    echo "Usage: $0 <function-name>"
    exit 1
fi

FUNCTION_NAME="$1"

# Generate a random lowercase S3 bucket name
RANDOM_SUFFIX=$(uuidgen | tr '[:upper:]' '[:lower:]' | cut -d'-' -f1)
BUCKET_NAME="my-bucket-${RANDOM_SUFFIX}"
echo "Generated bucket name: $BUCKET_NAME"

# Create the S3 bucket with the random name
aws s3 mb "s3://$BUCKET_NAME"

# Create a Lambda layer
aws lambda publish-layer-version \
    --layer-name profiler-layer \
    --description "Profiler Layer" \
    --license-info "MIT" \
    --zip-file fileb://extension/extension.zip \
    --compatible-runtimes java11 java17 java21 \
    --compatible-architectures "arm64" "x86_64"

# Assign the layer to the function
aws lambda update-function-configuration \
    --function-name "$FUNCTION_NAME" \
    --layers $(aws lambda list-layer-versions --layer-name profiler-layer --query 'LayerVersions[0].LayerVersionArn' --output text)

# Wait for the function to be updated
aws lambda wait function-updated \
    --function-name "$FUNCTION_NAME"

# Get existing environment variables (handle null case)
EXISTING_VARS=$(aws lambda get-function-configuration --function-name "$FUNCTION_NAME" --query "Environment.Variables" --output json 2>/dev/null)
if [[ -z "$EXISTING_VARS" || "$EXISTING_VARS" == "null" ]]; then
  EXISTING_VARS="{}"
fi

# Define new environment variables in JSON format
NEW_VARS=$(jq -n --arg bucket "$BUCKET_NAME" \
  --arg java_opts "-XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -javaagent:/opt/profiler-extension.jar" \
  '{AWS_LAMBDA_PROFILER_RESULTS_BUCKET_NAME: $bucket, JAVA_TOOL_OPTIONS: $java_opts}')

# Merge existing and new variables (compact JSON output)
UPDATED_VARS=$(echo "$EXISTING_VARS" | jq -c --argjson new_vars "$NEW_VARS" '. + $new_vars')

# Convert JSON to "Key=Value" format for AWS CLI
ENV_VARS_FORMATTED=$(echo "$UPDATED_VARS" | jq -r 'to_entries | map("\(.key)=\(.value)") | join(",")')

# Update Lambda function with correct format
aws lambda update-function-configuration \
  --function-name "$FUNCTION_NAME" \
  --environment "Variables={$ENV_VARS_FORMATTED}"

# Update the function's permissions to write to the S3 bucket
# Get the function's execution role
ROLE_NAME=$(aws lambda get-function --function-name "$FUNCTION_NAME" --query 'Configuration.Role' --output text | awk -F'/' '{print $NF}')

# Create a policy document
cat << EOF > s3-write-policy.json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "s3:PutObject"
            ],
            "Resource": [
                "arn:aws:s3:::$BUCKET_NAME",
                "arn:aws:s3:::$BUCKET_NAME/*"
            ]
        }
    ]
}
EOF

# Attach the policy to the role
aws iam put-role-policy \
    --role-name "$ROLE_NAME" \
    --policy-name S3WriteAccess \
    --policy-document file://s3-write-policy.json

echo "Setup completed for function $FUNCTION_NAME with S3 bucket $BUCKET_NAME"
echo "S3 write permissions added to the function's execution role"

# Clean up temporary files
rm s3-write-policy.json
