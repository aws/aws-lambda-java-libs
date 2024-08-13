package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.services.lambda.runtime.events.S3BatchEventV2;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class S3BatchEventV2Test {

    @Test
    public void testS3BatchEventV2() {
        S3BatchEventV2 event = EventLoader.loadS3BatchEventV2("s3_batch_event_v2.json");
        assertThat(event).isNotNull();
        assertThat(event.getInvocationId()).isEqualTo("Jr3s8KZqYWRmaiBhc2RmdW9hZHNmZGpmaGFzbGtkaGZzatx7Ruy");
        assertThat(event.getJob()).isNotNull();
        assertThat(event.getJob().getUserArguments().get("MyDestinationBucket")).isEqualTo("destination-directory-bucket-name");
        assertThat(event.getTasks()).hasSize(1);
        assertThat(event.getTasks().get(0).getS3Key()).isEqualTo("s3objectkey");
        assertThat(event.getTasks().get(0).getS3Bucket()).isEqualTo("source-directory-bucket-name");
    }
}
