package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Represents an CloudWatch Metric Alarm event. This event occurs when a metric alarm is triggered.
 *
 * @see <a href="https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/AlarmThatSendsEmail.html#alarms-and-actions">Using Amazon CloudWatch alarms</a>
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class CloudWatchMetricAlarmEvent {
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
        private PreviousState previousState;
        private Configuration configuration;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class State {
        private String value;
        private String reason;
        private String timestamp;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreviousState {
        private String value;
        private String reason;
        private String reasonData;
        private String timestamp;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Configuration {
        private String description;
        private List<Metric> metrics;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metric {
        private String id;
        private MetricStat metricStat;
        private Boolean returnData;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetricStat {
        private MetricDetail metric;
        private Integer period;
        private String stat;
        private String unit;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetricDetail {
        private String namespace;
        private String name;
        private Map<String, String> dimensions;
    }
}
