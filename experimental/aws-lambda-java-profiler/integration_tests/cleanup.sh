#!/bin/bash

# Set variables
LAYER_ARN=$(cat /tmp/layer_arn)
FUNCTION_NAME="aws-lambda-java-profiler-function-${GITHUB_RUN_ID}"
ROLE_NAME="aws-lambda-java-profiler-role-${GITHUB_RUN_ID}"

# Function to check if a command was successful
check_success() {
    if [ $? -eq 0 ]; then
        echo "Success: $1"
    else
        echo "Error: Failed to $1"
        exit 1
    fi
}

# Delete Lambda Layer
echo "Deleting Lambda Layer..."
aws lambda delete-layer-version --layer-name $(echo $LAYER_ARN | cut -d: -f7) --version-number $(echo $LAYER_ARN | cut -d: -f8)
check_success "delete Lambda Layer"

# Delete Lambda Function
echo "Deleting Lambda Function..."
aws lambda delete-function --function-name $FUNCTION_NAME
check_success "delete Lambda Function"

# Delete IAM Role
echo "Deleting IAM Role..."
# First, detach all policies from the role
for policy in $(aws iam list-attached-role-policies --role-name $ROLE_NAME --query 'AttachedPolicies[*].PolicyArn' --output text); do
    aws iam detach-role-policy --role-name $ROLE_NAME --policy-arn $policy
    check_success "detach policy $policy from role $ROLE_NAME"
done

# Remove s3 inline policy
aws iam delete-role-policy --role-name $ROLE_NAME --policy-name "s3PutObject"
check_success "deleted inline policy"


# Then delete the role
aws iam delete-role --role-name $ROLE_NAME
check_success "delete IAM Role"

echo "All deletions completed successfully."