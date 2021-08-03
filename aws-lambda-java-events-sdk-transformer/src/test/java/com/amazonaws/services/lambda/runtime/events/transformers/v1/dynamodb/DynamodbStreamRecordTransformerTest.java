package com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb;

import com.amazonaws.services.dynamodbv2.model.StreamRecord;
import com.amazonaws.services.dynamodbv2.model.StreamViewType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;

import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueBOOL_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueBOOL_v1;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueBS_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueBS_v1;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueB_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueB_v1;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueL_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueL_v1;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueM_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueM_v1;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueNS_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueNS_v1;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueNUL_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueNUL_v1;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueN_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueN_v1;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueSS_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueSS_v1;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueS_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueS_v1;

class DynamodbStreamRecordTransformerTest {

    private static final String keyNK = "Id";
    private static final String keyNSK = "KeyNS";

    private static final String keySK = "SKey";
    private static final String keySSK = "KeySS";

    private static final String keyBK = "BKey";
    private static final String keyBSK = "KeyBS";

    private static final String keyBOOLK = "IsBool";
    private static final String keyNULK = "nil";

    private static final String keyMK = "MapKey";

    private static final String keyLK = "LongNum";

    private static final String oldImageSK = "Message";
    private static final String newImageSK = "Message";
    private static final String streamViewType = StreamViewType.NEW_AND_OLD_IMAGES.toString();
    private static final String sequenceNumber = "222";
    private static final Long sizeBytes = 59L;
    private static final Date approximateCreationDateTime = new Date();

    //region StreamRecord_event
    public static final com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord streamRecord_event = new com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord()
            .withKeys(new HashMap<String, com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue>() {
                {
                    put(keyNK, attributeValueN_event);
                    put(keyNSK, attributeValueNS_event);
                    put(keySK, attributeValueS_event);
                    put(keySSK, attributeValueSS_event);
                    put(keyBK, attributeValueB_event);
                    put(keyBSK, attributeValueBS_event);
                    put(keyBOOLK, attributeValueBOOL_event);
                    put(keyNULK, attributeValueNUL_event);
                    put(keyMK, attributeValueM_event);
                    put(keyLK, attributeValueL_event);
                }
            })
            .withOldImage(new HashMap<String, com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue>() {
                {
                    put(oldImageSK, attributeValueS_event);
                    put(keyNK, attributeValueN_event);
                }
            })
            .withNewImage(new HashMap<String, com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue>() {
                {
                    put(newImageSK, attributeValueS_event);
                    put(keyNK, attributeValueN_event);
                }
            })
            .withStreamViewType(com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamViewType.fromValue(streamViewType))
            .withSequenceNumber(sequenceNumber)
            .withSizeBytes(sizeBytes)
            .withApproximateCreationDateTime(approximateCreationDateTime);
    //endregion

    //region StreamRecord_v1
    public static final StreamRecord streamRecord_v1 = new StreamRecord()
            .withApproximateCreationDateTime(approximateCreationDateTime)
            .withKeys(new HashMap<String, com.amazonaws.services.dynamodbv2.model.AttributeValue>() {
                {
                    put(keyNK, attributeValueN_v1);
                    put(keyNSK, attributeValueNS_v1);
                    put(keySK, attributeValueS_v1);
                    put(keySSK, attributeValueSS_v1);
                    put(keyBK, attributeValueB_v1);
                    put(keyBSK, attributeValueBS_v1);
                    put(keyBOOLK, attributeValueBOOL_v1);
                    put(keyNULK, attributeValueNUL_v1);
                    put(keyMK, attributeValueM_v1);
                    put(keyLK, attributeValueL_v1);
                }
            })
            .withOldImage(new HashMap<String, com.amazonaws.services.dynamodbv2.model.AttributeValue>() {
                {
                    put(oldImageSK, attributeValueS_v1);
                    put(keyNK, attributeValueN_v1);
                }
            })
            .withNewImage(new HashMap<String, com.amazonaws.services.dynamodbv2.model.AttributeValue>() {
                {
                    put(newImageSK, attributeValueS_v1);
                    put(keyNK, attributeValueN_v1);
                }
            })
            .withSequenceNumber(sequenceNumber)
            .withSizeBytes(sizeBytes)
            .withStreamViewType(streamViewType);
    //endregion

    @Test
    public void testToStreamRecordV1() {
        StreamRecord convertedStreamRecord = DynamodbStreamRecordTransformer.toStreamRecordV1(streamRecord_event);
        Assertions.assertEquals(streamRecord_v1, convertedStreamRecord);
    }

    @Test
    public void testToStreamRecordV1WhenOldImageIsNull() {
        com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord streamRecord = streamRecord_event.clone();
        streamRecord.setOldImage(null);

        Assertions.assertDoesNotThrow(() -> {
            com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbStreamRecordTransformer.toStreamRecordV1(streamRecord);
        });
    }

    @Test
    public void testToStreamRecordV1WhenNewImageIsNull() {
        com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord streamRecord = streamRecord_event.clone();
        streamRecord.setNewImage(null);

        Assertions.assertDoesNotThrow(() -> {
            com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbStreamRecordTransformer.toStreamRecordV1(streamRecord);
        });
    }
}