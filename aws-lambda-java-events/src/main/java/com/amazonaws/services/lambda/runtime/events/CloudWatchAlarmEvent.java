package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class CloudWatchAlarmEvent {

    private String source;
    private String alarmArn;
    private String accountId;
    private String time;
    private String region;
    private AlarmData alarmData;

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmData {
        private String alarmName;
        private State state;
        private State previousState;
        private Configuration configuration;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    private static class State {
        private String value;
        private String reason;
        private String reasonData;
        private String timestamp;
        private String actionsSuppressedBy;
        private String actionsSuppressedReason;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Configuration {
        private String description;
        private List<Metric> metrics;
        private Boolean returnData;

        private String alarmRule;
        private String actionsSuppressor;
        private Integer actionsSuppressorWaitPeriod;
        private Integer actionsSuppressorExtensionPeriod;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Metric {
        private String id;
        private MetricStat metricStat;
        private Integer period;
        private String stat;
        private String unit;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MetricStat {
        private String namespace;
        private String name;
        private Map<String, String> dimensions;
    }
}
