package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

/**
 * Represent an event received from SES when it receives an incoming message.
 *
 * See <a href="https://docs.aws.amazon.com/ses/latest/DeveloperGuide/receiving-email-action-lambda.html">documentation</a>.
 */
@Data
@AllArgsConstructor
@Builder(setterPrefix = "with")
@NoArgsConstructor
public class SESEvent {

    private List<Record> records;

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class Record {
        private String eventSource;
        private String eventVersion;
        private Ses ses;
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class Ses {
        private Mail mail;
        private Receipt receipt;
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class Mail {
        private DateTime timestamp;
        private String source;
        private String messageId;
        private String[] destination;
        private boolean headersTruncated;
        private Map<String, String> headers;
        private CommonHeaders commonHeaders;
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class CommonHeaders {
        private String returnPath;
        private String[] from;
        private String date;
        private String[] to;
        private String messageId;
        private String subject;
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class Receipt {
        private DateTime timestamp;
        private long processingTimeMillis;
        private String[] recipients;
        private Action action;
        private Verdict spamVerdict;
        private Verdict virusVerdict;
        private Verdict spfVerdict;
        private Verdict dkimVerdict;
        private Verdict dmarcVerdict;
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class Action {
        private String type;
        private String functionArn;
        private String invocationType;
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class Verdict {
        private String status;
    }
}
