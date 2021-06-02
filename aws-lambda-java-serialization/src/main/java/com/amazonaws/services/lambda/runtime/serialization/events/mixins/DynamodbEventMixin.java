/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class DynamodbEventMixin {

    // needed because jackson expects "records" instead of "Records"
    @JsonProperty("Records") abstract List<?> getRecords();
    @JsonProperty("Records") abstract void setRecords(List<?> records);

    public abstract class DynamodbStreamRecordMixin {

        // needed because Jackson cannot distinguish between Enum eventName from String eventName
        @JsonProperty("eventName") abstract String getEventName();
        @JsonProperty("eventName") abstract void setEventName(String eventName);
        // needed because Jackson expects "eventSourceArn" instead of "eventSourceARN"
        @JsonProperty("eventSourceARN") abstract String getEventSourceArn();
        @JsonProperty("eventSourceARN") abstract void setEventSourceArn(String eventSourceArn);
    }

    public abstract class StreamRecordMixin {

        // needed because Jackson expects "keys" instead of "Keys"
        @JsonProperty("Keys") abstract Map<String, ?> getKeys();
        @JsonProperty("Keys") abstract void setKeys(Map<String, ?> keys);
        // needed because Jackson expects "sizeBytes" instead of "SizeBytes"
        @JsonProperty("SizeBytes") abstract Long getSizeBytes();
        @JsonProperty("SizeBytes") abstract void setSizeBytes(Long sizeBytes);
        // needed because Jackson expects "sequenceNumber" instead of "SequenceNumber"
        @JsonProperty("SequenceNumber") abstract String getSequenceNumber();
        @JsonProperty("SequenceNumber") abstract void setSequenceNumber(String sequenceNumber);
        // needed because Jackson expects "streamViewType" instead of "StreamViewType"
        @JsonProperty("StreamViewType") abstract String getStreamViewType();
        @JsonProperty("StreamViewType") abstract void setStreamViewType(String streamViewType);
        // needed because Jackson expects "newImage" instead of "NewImage"
        @JsonProperty("NewImage") abstract Map<String, ?> getNewImage();
        @JsonProperty("NewImage") abstract void setNewImage(Map<String, ?> newImage);
        // needed because Jackson expects "oldImage" instead of "OldImage"
        @JsonProperty("OldImage") abstract Map<String, ?> getOldImage();
        @JsonProperty("OldImage") abstract void setOldImage(Map<String, ?> oldImage);
        // needed because Jackson expects "approximateCreationDateTime" instead of "ApproximateCreationDateTime"
        @JsonProperty("ApproximateCreationDateTime") abstract Date getApproximateCreationDateTime();
        @JsonProperty("ApproximateCreationDateTime") abstract void setApproximateCreationDateTime(Date approximateCreationDateTime);

    }

    public abstract class AttributeValueMixin {

        // needed because Jackson expects "s" instead of "S"
        @JsonProperty("S") abstract String getS();
        @JsonProperty("S") abstract void setS(String s);
        // needed because Jackson expects "n" instead of "N"
        @JsonProperty("N") abstract String getN();
        @JsonProperty("N") abstract void setN(String n);
        // needed because Jackson expects "b" instead of "B"
        @JsonProperty("B") abstract ByteBuffer getB();
        @JsonProperty("B") abstract void setB(ByteBuffer b);
        // needed because Jackson expects "null" instead of "NULL"
        @JsonProperty("NULL") abstract Boolean isNULL();
        @JsonProperty("NULL") abstract void setNULL(Boolean nU);
        // needed because Jackson expects "bool" instead of "BOOL"
        @JsonProperty("BOOL") abstract Boolean getBOOL();
        @JsonProperty("BOOL") abstract void setBOOL(Boolean bO);
        // needed because Jackson expects "ss" instead of "SS"
        @JsonProperty("SS") abstract List<String> getSS();
        @JsonProperty("SS") abstract void setSS(List<String> sS);
        // needed because Jackson expects "ns" instead of "NS"
        @JsonProperty("NS") abstract List<String> getNS();
        @JsonProperty("NS") abstract void setNS(List<String> nS);
        // needed because Jackson expects "bs" instead of "BS"
        @JsonProperty("BS") abstract List<String> getBS();
        @JsonProperty("BS") abstract void setBS(List<String> bS);
        // needed because Jackson expects "m" instead of "M"
        @JsonProperty("M") abstract Map<String, ?> getM();
        @JsonProperty("M") abstract void setM(Map<String, ?> val);
        // needed because Jackson expects "l" instead of "L"
        @JsonProperty("L") abstract List<?> getL();
        @JsonProperty("L") abstract void setL(List<?> val);

    }
}
