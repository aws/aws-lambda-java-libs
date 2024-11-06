#!/bin/bash

# S3 bucket name
PROFILER_RESULTS_BUCKET_NAME="aws-lambda-java-profiler-bucket-${GITHUB_RUN_ID}"

# Local directory to store downloaded files
LOCAL_DIR="/tmp/s3-artifacts"

# Create local directory if it doesn't exist
mkdir -p "$LOCAL_DIR"

# Download all files from the S3 bucket
echo "Downloading all files from s3://$PROFILER_RESULTS_BUCKET_NAME to $LOCAL_DIR"
aws s3 sync "s3://$PROFILER_RESULTS_BUCKET_NAME" "$LOCAL_DIR"



# Check if the download was successful
if [ $? -eq 0 ]; then
    echo "Download completed successfully."
    ls -lh $LOCAL_DIR
else
    echo "Error occurred during download."
    exit 1
fi

# rename files as GitHub does not allow ":" in filenames
for file in "$LOCAL_DIR"/*; do
    if [ -f "$file" ]; then
        dir=$(dirname "$file")
        filename=$(basename "$file")
        new_filename=$(echo "$filename" | tr -d ':')
        new_file="$dir/$new_filename"
        if [ "$filename" != "$new_filename" ]; then
            mv "$file" "$new_file"
            echo "Renamed: $filename -> $new_filename"
        else
            echo "No change: $filename"
        fi
    fi
done

echo "All files processed."