package com.amazonaws.services.lambda.runtime.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * Represent an event received from SES when it receives an incoming message.
 *
 * See <a href="https://docs.aws.amazon.com/ses/latest/DeveloperGuide/receiving-email-action-lambda.html">documentation</a>.
 */
@Data
@AllArgsConstructor
@Builder(setterPrefix = "with")
@NoArgsConstructor
public class SESEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = 4378251841610238092L;
    
    @JsonProperty("Records")
    private List<Record> records;

    @Override
    public SESEvent clone() {
        try {
            return (SESEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class Record implements Serializable, Cloneable {

        private static final long serialVersionUID = 4689200289017967041L;

        private String eventSource;
        private String eventVersion;
        private Ses ses;

        @Override
        public Record clone() {
            try {
                return (Record) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class Ses implements Serializable, Cloneable {

        private static final long serialVersionUID = -8671051823119970975L;

        private Mail mail;
        private Receipt receipt;

        @Override
        public Ses clone() {
            try {
                return (Ses) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class Mail implements Serializable, Cloneable {

        private static final long serialVersionUID = 4055476288790904705L;

        private Instant timestamp;
        private String source;
        private String messageId;
        private String[] destination;
        private boolean headersTruncated;
        private List<Header> headers;
        private CommonHeaders commonHeaders;

        @Override
        public Mail clone() {
            try {
                return (Mail) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class Header implements Serializable, Cloneable {

        private static final long serialVersionUID = 1059624012149639930L;

        private String name;
        private String value;

        @Override
        public Header clone() {
            try {
                return (Header) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class CommonHeaders implements Serializable, Cloneable {

        private static final long serialVersionUID = 4926097072068505243L;

        private String returnPath;
        private String[] from;
        private String date;
        private String[] to;
        private String messageId;
        private String subject;

        @Override
        public CommonHeaders clone() {
            try {
                return (CommonHeaders) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class Receipt implements Serializable, Cloneable {

        private static final long serialVersionUID = -2378481926296810065L;

        private Instant timestamp;
        private long processingTimeMillis;
        private String[] recipients;
        private Action action;
        private Verdict spamVerdict;
        private Verdict virusVerdict;
        private Verdict spfVerdict;
        private Verdict dkimVerdict;
        private Verdict dmarcVerdict;

        @Override
        public Receipt clone() {
            try {
                return (Receipt) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class Action implements Serializable, Cloneable {

        private static final long serialVersionUID = 292227308414074012L;

        private String type;
        private String functionArn;
        private String invocationType;

        @Override
        public Action clone() {
            try {
                return (Action) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class Verdict implements Serializable, Cloneable {

        private static final long serialVersionUID = 773331220324957901L;

        private String status;

        @Override
        public Verdict clone() {
            try {
                return (Verdict) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }
}
