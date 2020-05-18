/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
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
