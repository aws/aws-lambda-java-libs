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
package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * Class to represent an Amazon Lex V2 event.
 *
 * @see <a href="https://docs.aws.amazon.com/lexv2/latest/dg/lambda.html#lambda-input-format">Using
 *     an AWS Lambda function</a>
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class LexV2Event implements Serializable, Cloneable {
    private String messageVersion;
    private String invocationSource;
    private String inputMode;
    private String responseContentType;
    private String sessionId;
    private String inputTranscript;
    private Bot bot;
    private Interpretation[] interpretations;
    private ProposedNextState proposedNextState;
    private Map<String, String> requestAttributes;
    private SessionState sessionState;
    private Transcription[] transcriptions;

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Bot implements Serializable, Cloneable {
        private String id;
        private String name;
        private String aliasId;
        private String localeId;
        private String version;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Interpretation implements Serializable, Cloneable {
        private Intent intent;
        private double nluConfidence;
        private SentimentResponse sentimentResponse;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Intent implements Serializable, Cloneable {
        private String confirmationState;
        private String name;
        private Map<String, Slot> slots;
        private String state;
        private Map<String, Object> kendraResponse;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Slot implements Serializable, Cloneable {
        private String shape;
        private SlotValue value;
        private Slot[] values;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SlotValue implements Serializable, Cloneable {
        private String interpretedValue;
        private String originalValue;
        private String[] resolvedValues;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NluConfidence implements Serializable, Cloneable {
        private double score;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SentimentResponse implements Serializable, Cloneable {
        private String sentiment;
        private SentimentScore sentimentScore;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SentimentScore implements Serializable, Cloneable {
        private double mixed;
        private double negative;
        private double neutral;
        private double positiv;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProposedNextState implements Serializable, Cloneable {
        private DialogAction dialogAction;
        private Intent intent;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DialogAction implements Serializable, Cloneable {
        private String slotToElicit;
        private String type;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionState implements Serializable, Cloneable {
        private ActiveContext[] activeContexts;
        private Map<String, String> sessionAttributes;
        private RuntimeHints runtimeHints;
        private DialogAction dialogAction;
        private Intent intent;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActiveContext implements Serializable, Cloneable {
        private String name;
        private Map<String, String> contextAttributes;
        private TimeToLive timeToLive;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeToLive implements Serializable, Cloneable {
        private int timeToLiveInSeconds;
        private int turnsToLive;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RuntimeHints implements Serializable, Cloneable {
        private Map<String, Map<String, Hint>> slotHints;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Hint implements Serializable, Cloneable {
        private RuntimeHintValue[] runtimeHintValues;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RuntimeHintValue implements Serializable, Cloneable {
        private String phrase;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Transcription implements Serializable, Cloneable {
        private String transcription;
        private TranscriptionConfidence transcriptionConfidence;
        private ResolvedContext resolvedContext;
        private Map<String, Slot> ResolvedSlots;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TranscriptionConfidence implements Serializable, Cloneable {
        private double score;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResolvedContext implements Serializable, Cloneable {
        private String intent;
    }
}
