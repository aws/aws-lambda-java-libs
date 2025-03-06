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

# Add environment variables
aws lambda update-function-configuration \
    --function-name "$FUNCTION_NAME" \
    --environment "Variables={AWS_LAMBDA_PROFILER_RESULTS_BUCKET_NAME=$BUCKET_NAME, JAVA_TOOL_OPTIONS=-XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -javaagent:/opt/profiler-extension.jar}"

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