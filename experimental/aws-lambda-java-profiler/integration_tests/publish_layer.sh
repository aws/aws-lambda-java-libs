#!/bin/bash

# Set variables
LAYER_NAME="aws-lambda-java-profiler-test"
DESCRIPTION="AWS Lambda Java Profiler Test Layer"
ZIP_FILE="./extension/extension.zip"
RUNTIME="java21"
ARCHITECTURE="x86_64"

# Check if AWS CLI is installed
if ! command -v aws &> /dev/null; then
    echo "AWS CLI is not installed. Please install it first."
    exit 1
fi

# Check if the ZIP file exists
if [ ! -f "$ZIP_FILE" ]; then
    echo "ZIP file $ZIP_FILE not found. Please make sure it exists."
    exit 1
fi

# Publish the layer
echo "Publishing layer $LAYER_NAME..."
RESPONSE=$(aws lambda publish-layer-version \
    --layer-name "$LAYER_NAME" \
    --description "$DESCRIPTION" \
    --zip-file "fileb://$ZIP_FILE" \
    --compatible-runtimes "$RUNTIME" \
    --compatible-architectures "$ARCHITECTURE")

# Check if the layer was published successfully
if [ $? -eq 0 ]; then
    LAYER_VERSION=$(echo $RESPONSE | jq -r '.Version')
    LAYER_ARN=$(echo $RESPONSE | jq -r '.LayerVersionArn')
    echo "Layer published successfully!"
    echo "Layer Version: $LAYER_VERSION"
    echo "Layer ARN: $LAYER_ARN"
    echo $LAYER_ARN > /tmp/layer_arn
else
    echo "Failed to publish layer. Please check your AWS credentials and permissions."
    exit 1
fi