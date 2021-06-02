package com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb;

import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamViewType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.StreamRecord;
import software.amazon.awssdk.utils.ImmutableMap;

import java.util.Date;

import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueBOOL_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueBOOL_v2;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueBS_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueBS_v2;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueB_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueB_v2;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueL_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueL_v2;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueM_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueM_v2;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueNS_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueNS_v2;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueNUL_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueNUL_v2;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueN_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueN_v2;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueSS_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueSS_v2;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueS_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbAttributeValueTransformerTest.attributeValueS_v2;

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
    public static final com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord streamRecord_event =
            new com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord()
                    .withKeys(ImmutableMap.<String, AttributeValue>builder()
                            .put(keyNK, attributeValueN_event)
                            .put(keyNSK, attributeValueNS_event)
                            .put(keySK, attributeValueS_event)
                            .put(keySSK, attributeValueSS_event)
                            .put(keyBK, attributeValueB_event)
                            .put(keyBSK, attributeValueBS_event)
                            .put(keyBOOLK, attributeValueBOOL_event)
                            .put(keyNULK, attributeValueNUL_event)
                            .put(keyMK, attributeValueM_event)
                            .put(keyLK, attributeValueL_event)
                            .build()
                    )
                    .withOldImage(ImmutableMap.of(
                            oldImageSK, attributeValueS_event,
                            keyNK, attributeValueN_event
                    ))
                    .withNewImage(ImmutableMap.of(
                            newImageSK, attributeValueS_event,
                            keyNK, attributeValueN_event
                    ))
                    .withStreamViewType(StreamViewType.fromValue(streamViewType))
                    .withSequenceNumber(sequenceNumber)
                    .withSizeBytes(sizeBytes)
                    .withApproximateCreationDateTime(approximateCreationDateTime);
    //endregion

    //region StreamRecord_v2
    public static final StreamRecord streamRecord_v2 = StreamRecord.builder()
            .approximateCreationDateTime(approximateCreationDateTime.toInstant())
            .keys(ImmutableMap.<String, software.amazon.awssdk.services.dynamodb.model.AttributeValue>builder()
                    .put(keyNK, attributeValueN_v2)
                    .put(keyNSK, attributeValueNS_v2)
                    .put(keySK, attributeValueS_v2)
                    .put(keySSK, attributeValueSS_v2)
                    .put(keyBK, attributeValueB_v2)
                    .put(keyBSK, attributeValueBS_v2)
                    .put(keyBOOLK, attributeValueBOOL_v2)
                    .put(keyNULK, attributeValueNUL_v2)
                    .put(keyMK, attributeValueM_v2)
                    .put(keyLK, attributeValueL_v2)
                    .build()
            )
            .oldImage(ImmutableMap.of(
                    oldImageSK, attributeValueS_v2,
                    keyNK, attributeValueN_v2
            ))
            .newImage(ImmutableMap.of(
                    newImageSK, attributeValueS_v2,
                    keyNK, attributeValueN_v2
            ))
            .sequenceNumber(sequenceNumber)
            .sizeBytes(sizeBytes)
            .streamViewType(streamViewType)
            .build();
    //endregion

    @Test
    public void testToStreamRecordV2() {
        StreamRecord convertedStreamRecord = DynamodbStreamRecordTransformer.toStreamRecordV2(streamRecord_event);
        Assertions.assertEquals(streamRecord_v2, convertedStreamRecord);
    }

    @Test
    public void testToStreamRecordV2WhenOldImageIsNull() {
        com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord streamRecord = streamRecord_event.clone();
        streamRecord.setOldImage(null);

        Assertions.assertDoesNotThrow(() -> {
            DynamodbStreamRecordTransformer.toStreamRecordV2(streamRecord);
        });
    }

    @Test
    public void testToStreamRecordV2WhenNewImageIsNull() {
        com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord streamRecord = streamRecord_event.clone();
        streamRecord.setNewImage(null);

        Assertions.assertDoesNotThrow(() -> {
            DynamodbStreamRecordTransformer.toStreamRecordV2(streamRecord);
        });
    }
}