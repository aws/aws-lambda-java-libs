package com.amazonaws.services.lambda.runtime.events.models.s3;

import com.amazonaws.services.lambda.runtime.events.HttpUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class S3EventNotificationTest {

    private List<String> KEYS_REQUIRING_URL_ENCODE = Arrays.asList("foo bar.jpg", "foo/bar.csv", "foo<>bar");

    @Test
    public void testGetUrlDecodedKey() {
        for (String testKey : KEYS_REQUIRING_URL_ENCODE) {
            String urlEncoded = HttpUtils.urlEncode(testKey, false);
            S3EventNotification.S3ObjectEntity entity = new S3EventNotification.S3ObjectEntity(
                    urlEncoded, 1L, "E-Tag", "versionId");
            Assertions.assertEquals(testKey, entity.getUrlDecodedKey());
        }
    }
}