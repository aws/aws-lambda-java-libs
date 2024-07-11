package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an CloudWatch Composite Alarm event. This event occurs when a composite alarm is triggered.
 *
 * @see <a href="https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/AlarmThatSendsEmail.html#alarms-and-actions">Using Amazon CloudWatch alarms</a>
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class CloudWatchCompositeAlarmEvent {
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
        private String reasonData;
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
        private String actionsSuppressedBy;
        private String actionsSuppressedReason;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Configuration {
        private String alarmRule;
        private String actionsSuppressor;
        private Integer actionsSuppressorWaitPeriod;
        private Integer actionsSuppressorExtensionPeriod;
    }
}
