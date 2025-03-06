#!/bin/bash

PROFILER_RESULTS_BUCKET_NAME="aws-lambda-java-profiler-bucket-${GITHUB_RUN_ID}"

# Create the S3 bucket
aws s3 mb s3://"$PROFILER_RESULTS_BUCKET_NAME"

# Check if the bucket was created successfully
if [ $? -eq 0 ]; then
    echo "Bucket '$PROFILER_RESULTS_BUCKET_NAME' created successfully."
else
    echo "Error: Failed to create bucket '$PROFILER_RESULTS_BUCKET_NAME'."
    exit 1
fi