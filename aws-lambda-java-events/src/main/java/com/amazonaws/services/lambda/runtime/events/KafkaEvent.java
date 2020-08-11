/*
 * Copyright 2015-2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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
package com.amazonaws.services.lambda.runtime.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode
/** Represents a Kafka Event. **/
public class KafkaEvent {
    private Map<String, List<KafkaEventRecord>> records;
    public  String eventSource;
    private String eventSourceArn;

    public Map<String, List<KafkaEventRecord>> getRecords() {
        return records;
    }

    public KafkaEvent(Map<TopicPartition, List<KafkaEventRecord>> eventRecords) {
        if (eventRecords == null) {
            throw new IllegalArgumentException("Records cannot be null");
        }
        Map<String, List<KafkaEventRecord>> records = new HashMap<>();
        for(Map.Entry<KafkaEvent.TopicPartition, List<KafkaEvent.KafkaEventRecord>> entrySet: eventRecords.entrySet()) {
            records.put(entrySet.getKey().toString(), entrySet.getValue());
        }
        this.records = records;
    }

    public String getEventSource () {
        return eventSource;
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class KafkaEventRecord {
        private String topic;
        private int partition;
        private long offset;
        private long timestamp;
        private String timestampType;
        private String key;
        private String value;
    }

    @Data
    @NoArgsConstructor
    public static class TopicPartition {
        private  String topic;
        private  int partition;

        @Override
        public String toString() {
            //Kafka also uses '-' for toString()
            return topic + "-" + partition;
        }
    }
}

