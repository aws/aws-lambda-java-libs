// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.services.lambda.extension;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.time.LocalDate;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public class S3Manager {

    private static final String RESULTS_BUCKET = "AWS_LAMBDA_PROFILER_RESULTS_BUCKET_NAME";
    private static final String FUNCTION_NAME = System.getenv().getOrDefault("AWS_LAMBDA_FUNCTION_NAME", "function");
    private S3Client s3Client;
    private String bucketName;

    public S3Manager() {
        final String bucketName = System.getenv(RESULTS_BUCKET);
        Logger.debug("creating S3Manager with bucketName = " + bucketName);
        if (null == bucketName || bucketName.isEmpty()) {
            throw new IllegalArgumentException("please set the bucket name using AWS_LAMBDA_PROFILER_RESULTS_BUCKET_NAME environment variable");
        }
        this.s3Client = S3Client.builder().build();
        this.bucketName = bucketName;
        Logger.debug("S3Manager successfully created");
    }

    public void upload(String fileName, boolean isShutDownEvent) {
        try {
            final String suffix = isShutDownEvent ? "shutdown" : fileName;
            final String key = buildKey(FUNCTION_NAME, fileName);
            Logger.debug("uploading profile to key = " + key);
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            File file = new File(String.format("/tmp/profiling-data-%s.html", suffix));
            if (file.exists()) {
                Logger.debug("file size is " + file.length());
                RequestBody requestBody = RequestBody.fromFile(file);
                PutObjectResponse response = s3Client.putObject(putObjectRequest, requestBody);
                Logger.debug("profile uploaded successfully. ETag: " + response.eTag());
                if(file.delete()) {
                    Logger.debug("file deleted");
                }
            } else {
                throw new IllegalArgumentException("could not find the profile to upload");
            }
        } catch (Exception e) {
            Logger.error("could not upload the profile");
            e.printStackTrace();
        }
    }

    private String buildKey(String functionName, String fileName) {
        final LocalDate currentDate = LocalDate.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        final String formattedDate = currentDate.format(formatter);
        return String.format("%s/%s/%s", formattedDate, functionName, fileName);
    }

}