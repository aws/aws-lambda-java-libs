package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class S3BatchEvent {

    private String invocationSchemaVersion;
    private String invocationId;
    private Job job;
    private List<Task> tasks;

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Job {

        private String id;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Task {

        private String taskId;
        private String s3Key;
        private String s3VersionId;
        private String s3BucketArn;
    }
}