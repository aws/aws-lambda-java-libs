package com.amazonaws.services.lambda.runtime.events.transformers.dynamodb;

import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.utils.ImmutableMap;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class DynamodbAttributeValueTransformerTest {

    private static final String valueN = "101";
    private static final List<String> valueNS = Arrays.asList("1", "2", "3");
    private static final String valueS = "SVal";
    private static final List<String> valueSS = Arrays.asList("first", "second", "third");
    private static final ByteBuffer valueB = ByteBuffer.wrap("BVal".getBytes());
    private static final List<ByteBuffer> valueBS = Arrays.asList(
            ByteBuffer.wrap("first".getBytes()),
            ByteBuffer.wrap("second".getBytes()),
            ByteBuffer.wrap("third".getBytes()));
    private static final boolean valueBOOL = true;
    private static final boolean valueNUL = true;

    private static final String keyM1 = "NestedMapKey1";
    private static final String keyM2 = "NestedMapKey2";

    //region AttributeValue_event
    public static final AttributeValue attributeValueN_event = new AttributeValue().withN(valueN);
    public static final AttributeValue attributeValueNS_event = new AttributeValue().withNS(valueNS);
    public static final AttributeValue attributeValueS_event = new AttributeValue().withS(valueS);
    public static final AttributeValue attributeValueSS_event = new AttributeValue().withSS(valueSS);
    public static final AttributeValue attributeValueB_event = new AttributeValue().withB(valueB);
    public static final AttributeValue attributeValueBS_event = new AttributeValue().withBS(valueBS);
    public static final AttributeValue attributeValueBOOL_event = new AttributeValue().withBOOL(valueBOOL);
    public static final AttributeValue attributeValueNUL_event = new AttributeValue().withNULL(valueNUL);
    public static final AttributeValue attributeValueM_event = new AttributeValue().withM(ImmutableMap.of(
            keyM1, attributeValueN_event,
            keyM2, attributeValueS_event
    ));
    public static final AttributeValue attributeValueL_event = new AttributeValue().withL(Arrays.asList(
            attributeValueN_event,
            attributeValueNS_event,
            attributeValueS_event,
            attributeValueSS_event,
            attributeValueB_event,
            attributeValueBS_event,
            attributeValueBOOL_event,
            attributeValueNUL_event,
            attributeValueM_event,
            new AttributeValue().withL(Arrays.asList(
                    attributeValueN_event,
                    attributeValueNS_event,
                    attributeValueS_event,
                    attributeValueSS_event,
                    attributeValueB_event,
                    attributeValueBS_event,
                    attributeValueBOOL_event,
                    attributeValueNUL_event,
                    attributeValueM_event
            ))
    ));
    //endregion

    //region AttributeValue_v2
    public static final software.amazon.awssdk.services.dynamodb.model.AttributeValue attributeValueN_v2 =
            software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder().n(valueN).build();
    public static final software.amazon.awssdk.services.dynamodb.model.AttributeValue attributeValueNS_v2 =
            software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder().ns(valueNS).build();
    public static final software.amazon.awssdk.services.dynamodb.model.AttributeValue attributeValueS_v2 =
            software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder().s(valueS).build();
    public static final software.amazon.awssdk.services.dynamodb.model.AttributeValue attributeValueSS_v2 =
            software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder().ss(valueSS).build();
    public static final software.amazon.awssdk.services.dynamodb.model.AttributeValue attributeValueB_v2 =
            software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder().b(SdkBytes.fromByteBuffer(valueB)).build();
    public static final software.amazon.awssdk.services.dynamodb.model.AttributeValue attributeValueBS_v2 =
            software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder().bs(valueBS.stream()
                    .map(SdkBytes::fromByteBuffer)
                    .collect(Collectors.toList())).build();
    public static final software.amazon.awssdk.services.dynamodb.model.AttributeValue attributeValueBOOL_v2 =
            software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder().bool(valueBOOL).build();
    public static final software.amazon.awssdk.services.dynamodb.model.AttributeValue attributeValueNUL_v2 =
            software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder().nul(valueNUL).build();
    public static final software.amazon.awssdk.services.dynamodb.model.AttributeValue attributeValueM_v2 =
            software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder().m(ImmutableMap.of(
                    keyM1, attributeValueN_v2,
                    keyM2, attributeValueS_v2
            )).build();
    public static final software.amazon.awssdk.services.dynamodb.model.AttributeValue attributeValueL_v2 =
            software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder().l(Arrays.asList(
                    attributeValueN_v2,
                    attributeValueNS_v2,
                    attributeValueS_v2,
                    attributeValueSS_v2,
                    attributeValueB_v2,
                    attributeValueBS_v2,
                    attributeValueBOOL_v2,
                    attributeValueNUL_v2,
                    attributeValueM_v2,
                    software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder().l(Arrays.asList(
                            attributeValueN_v2,
                            attributeValueNS_v2,
                            attributeValueS_v2,
                            attributeValueSS_v2,
                            attributeValueB_v2,
                            attributeValueBS_v2,
                            attributeValueBOOL_v2,
                            attributeValueNUL_v2,
                            attributeValueM_v2
                    )).build()
            )).build();
    //endregion

    //region AttributeValue
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueN =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withN(valueN);
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueNS =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withNS(valueNS);
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueS =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withS(valueS);
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueSS =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withSS(valueSS);
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueB =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withB(valueB);
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueBS =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withBS(valueBS);
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueBOOL =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withBOOL(valueBOOL);
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueNUL =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withNULL(valueNUL);
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueM =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withM(ImmutableMap.of(
                    keyM1, attributeValueN,
                    keyM2, attributeValueS
            ));
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueL =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withL(Arrays.asList(
                    attributeValueN,
                    attributeValueNS,
                    attributeValueS,
                    attributeValueSS,
                    attributeValueB,
                    attributeValueBS,
                    attributeValueBOOL,
                    attributeValueNUL,
                    attributeValueM,
                    new com.amazonaws.services.dynamodbv2.model.AttributeValue().withL(Arrays.asList(
                            attributeValueN,
                            attributeValueNS,
                            attributeValueS,
                            attributeValueSS,
                            attributeValueB,
                            attributeValueBS,
                            attributeValueBOOL,
                            attributeValueNUL,
                            attributeValueM
                    ))
            ));
    //endregion

    // region test toAttributeValueV2
    @Test
    public void testToAttributeValueV2_N() {
        software.amazon.awssdk.services.dynamodb.model.AttributeValue convertedAttributeValueN =
                DynamodbAttributeValueTransformer.toAttributeValueV2(attributeValueN_event);
        Assertions.assertEquals(attributeValueN_v2, convertedAttributeValueN);
    }

    @Test
    public void testToAttributeValueV2_NS() {
        software.amazon.awssdk.services.dynamodb.model.AttributeValue convertedAttributeValueNS =
                DynamodbAttributeValueTransformer.toAttributeValueV2(attributeValueNS_event);
        Assertions.assertEquals(attributeValueNS_v2, convertedAttributeValueNS);
    }

    @Test
    public void testToAttributeValueV2_S() {
        software.amazon.awssdk.services.dynamodb.model.AttributeValue convertedAttributeValueS =
                DynamodbAttributeValueTransformer.toAttributeValueV2(attributeValueS_event);
        Assertions.assertEquals(attributeValueS_v2, convertedAttributeValueS);
    }

    @Test
    public void testToAttributeValueV2_SS() {
        software.amazon.awssdk.services.dynamodb.model.AttributeValue convertedAttributeValueSS =
                DynamodbAttributeValueTransformer.toAttributeValueV2(attributeValueSS_event);
        Assertions.assertEquals(attributeValueSS_v2, convertedAttributeValueSS);
    }

    @Test
    public void testToAttributeValueV2_B() {
        software.amazon.awssdk.services.dynamodb.model.AttributeValue convertedAttributeValueB =
                DynamodbAttributeValueTransformer.toAttributeValueV2(attributeValueB_event);
        Assertions.assertEquals(attributeValueB_v2, convertedAttributeValueB);
    }

    @Test
    public void testToAttributeValueV2_BS() {
        software.amazon.awssdk.services.dynamodb.model.AttributeValue convertedAttributeValueBS =
                DynamodbAttributeValueTransformer.toAttributeValueV2(attributeValueBS_event);
        Assertions.assertEquals(attributeValueBS_v2, convertedAttributeValueBS);
    }

    @Test
    public void testToAttributeValueV2_BOOL() {
        software.amazon.awssdk.services.dynamodb.model.AttributeValue convertedAttributeValueBOOL =
                DynamodbAttributeValueTransformer.toAttributeValueV2(attributeValueBOOL_event);
        Assertions.assertEquals(attributeValueBOOL_v2, convertedAttributeValueBOOL);
    }

    @Test
    public void testToAttributeValueV2_NUL() {
        software.amazon.awssdk.services.dynamodb.model.AttributeValue convertedAttributeValueNUL =
                DynamodbAttributeValueTransformer.toAttributeValueV2(attributeValueNUL_event);
        Assertions.assertEquals(attributeValueNUL_v2, convertedAttributeValueNUL);
    }

    @Test
    public void testToAttributeValueV2_M() {
        software.amazon.awssdk.services.dynamodb.model.AttributeValue convertedAttributeValueM =
                DynamodbAttributeValueTransformer.toAttributeValueV2(attributeValueM_event);
        Assertions.assertEquals(attributeValueM_v2, convertedAttributeValueM);
    }

    @Test
    public void testToAttributeValueV2_L() {
        software.amazon.awssdk.services.dynamodb.model.AttributeValue convertedAttributeValueL =
                DynamodbAttributeValueTransformer.toAttributeValueV2(attributeValueL_event);
        Assertions.assertEquals(attributeValueL_v2, convertedAttributeValueL);
    }

    @Test
    public void testToAttributeValueV2_IllegalArgumentWhenNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue())
        );
    }

    @Test
    public void testToAttributeValueV2_IllegalArgumentWhenNull_N() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withN(null))
        );
    }

    @Test
    public void testToAttributeValueV2_IllegalArgumentWhenNull_S() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withS(null))
        );
    }

    @Test
    public void testToAttributeValueV2_IllegalArgumentWhenNull_B() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withB(null))
        );
    }

    @Test
    public void testToAttributeValueV2_IllegalArgumentWhenNull_BOOL() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withBOOL(null))
        );
    }

    @Test
    public void testToAttributeValueV2_IllegalArgumentWhenNull_NUL() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withNULL(null))
        );
    }

    @Test
    public void testToAttributeValueV2_IllegalArgumentWhenNull_M() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withM(null))
        );
    }

    @Test
    public void testToAttributeValueV2_DoesNotThrowWhenEmpty_NS() {
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withNS())
        );
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withNS(Collections.emptyList()))
        );
    }

    @Test
    public void testToAttributeValueV2_DoesNotThrowWhenEmpty_SS() {
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withSS())
        );
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withSS(Collections.emptyList()))
        );
    }

    @Test
    public void testToAttributeValueV2_DoesNotThrowWhenEmpty_BS() {
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withBS())
        );
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withBS(Collections.emptyList()))
        );
    }

    @Test
    public void testToAttributeValueV2_DoesNotThrowWhenEmpty_L() {
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withL())
        );
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withL(Collections.emptyList()))
        );
    }

    @Test
    public void testToAttributeValueV2_EmptyV2ObjectWhenEmpty_NS() {
        software.amazon.awssdk.services.dynamodb.model.AttributeValue expectedAttributeValue_v2 =
                software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder().build();
        Assertions.assertEquals(expectedAttributeValue_v2,
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withNS()));
        Assertions.assertEquals(expectedAttributeValue_v2,
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withNS(Collections.emptyList())));
    }

    @Test
    public void testToAttributeValueV2_EmptyV2ObjectWhenEmpty_SS() {
        software.amazon.awssdk.services.dynamodb.model.AttributeValue expectedAttributeValue_v2 =
                software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder().build();
        Assertions.assertEquals(expectedAttributeValue_v2,
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withSS()));
        Assertions.assertEquals(expectedAttributeValue_v2,
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withSS(Collections.emptyList())));
    }

    @Test
    public void testToAttributeValueV2_EmptyV2ObjectWhenEmpty_BS() {
        software.amazon.awssdk.services.dynamodb.model.AttributeValue expectedAttributeValue_v2 =
                software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder().build();
        Assertions.assertEquals(expectedAttributeValue_v2,
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withBS()));
        Assertions.assertEquals(expectedAttributeValue_v2,
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withBS(Collections.emptyList())));
    }

    @Test
    public void testToAttributeValueV2_EmptyV2ObjectWhenEmpty_L() {
        software.amazon.awssdk.services.dynamodb.model.AttributeValue expectedAttributeValue_v2 =
                software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder().build();
        Assertions.assertEquals(expectedAttributeValue_v2,
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withL()));
        Assertions.assertEquals(expectedAttributeValue_v2,
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withL(Collections.emptyList())));
    }
    //endregion

    // region test toAttributeValue
    @Test
    public void testToAttributeValue_N() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueN =
                DynamodbAttributeValueTransformer.toAttributeValue(attributeValueN_event);
        Assertions.assertEquals(attributeValueN, convertedAttributeValueN);
    }

    @Test
    public void testToAttributeValue_NS() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueNS =
                DynamodbAttributeValueTransformer.toAttributeValue(attributeValueNS_event);
        Assertions.assertEquals(attributeValueNS, convertedAttributeValueNS);
    }

    @Test
    public void testToAttributeValue_S() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueS =
                DynamodbAttributeValueTransformer.toAttributeValue(attributeValueS_event);
        Assertions.assertEquals(attributeValueS, convertedAttributeValueS);
    }

    @Test
    public void testToAttributeValue_SS() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueSS =
                DynamodbAttributeValueTransformer.toAttributeValue(attributeValueSS_event);
        Assertions.assertEquals(attributeValueSS, convertedAttributeValueSS);
    }

    @Test
    public void testToAttributeValue_B() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueB =
                DynamodbAttributeValueTransformer.toAttributeValue(attributeValueB_event);
        Assertions.assertEquals(attributeValueB, convertedAttributeValueB);
    }

    @Test
    public void testToAttributeValue_BS() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueBS =
                DynamodbAttributeValueTransformer.toAttributeValue(attributeValueBS_event);
        Assertions.assertEquals(attributeValueBS, convertedAttributeValueBS);
    }

    @Test
    public void testToAttributeValue_BOOL() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueBOOL =
                DynamodbAttributeValueTransformer.toAttributeValue(attributeValueBOOL_event);
        Assertions.assertEquals(attributeValueBOOL, convertedAttributeValueBOOL);
    }

    @Test
    public void testToAttributeValue_NULL() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueNUL =
                DynamodbAttributeValueTransformer.toAttributeValue(attributeValueNUL_event);
        Assertions.assertEquals(attributeValueNUL, convertedAttributeValueNUL);
    }

    @Test
    public void testToAttributeValue_M() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueM =
                DynamodbAttributeValueTransformer.toAttributeValue(attributeValueM_event);
        Assertions.assertEquals(attributeValueM, convertedAttributeValueM);
    }

    @Test
    public void testToAttributeValue_L() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueL =
                DynamodbAttributeValueTransformer.toAttributeValue(attributeValueL_event);
        Assertions.assertEquals(attributeValueL, convertedAttributeValueL);
    }

    @Test
    public void testToAttributeValueIllegalArgumentWhenNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue())
        );
    }

    @Test
    public void testToAttributeValueIllegalArgumentWhenNull_N() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withN(null))
        );
    }

    @Test
    public void testToAttributeValueIllegalArgumentWhenNull_S() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withS(null))
        );
    }

    @Test
    public void testToAttributeValueIllegalArgumentWhenNull_B() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withB(null))
        );
    }

    @Test
    public void testToAttributeValueIllegalArgumentWhenNull_BOOL() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withBOOL(null))
        );
    }

    @Test
    public void testToAttributeValueIllegalArgumentWhenNull_NULL() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withNULL(null))
        );
    }

    @Test
    public void testToAttributeValueIllegalArgumentWhenNull_M() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withM(null))
        );
    }

    @Test
    public void testToAttributeValueDoesNotThrowWhenEmpty_NS() {
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withNS())
        );
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withNS(Collections.emptyList()))
        );
    }

    @Test
    public void testToAttributeValueDoesNotThrowWhenEmpty_SS() {
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withSS())
        );
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withSS(Collections.emptyList()))
        );
    }

    @Test
    public void testToAttributeValueDoesNotThrowWhenEmpty_BS() {
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withBS())
        );
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withBS(Collections.emptyList()))
        );
    }

    @Test
    public void testToAttributeValueDoesNotThrowWhenEmpty_L() {
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withL())
        );
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withL(Collections.emptyList()))
        );
    }

    @Test
    public void testToAttributeValueEmptyObjectWhenEmpty_NS() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue expectedAttributeValue =
                new com.amazonaws.services.dynamodbv2.model.AttributeValue().withNS();
        Assertions.assertEquals(expectedAttributeValue,
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withNS()));
        Assertions.assertEquals(expectedAttributeValue,
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withNS(Collections.emptyList())));
    }

    @Test
    public void testToAttributeValueEmptyObjectWhenEmpty_SS() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue expectedAttributeValue =
                new com.amazonaws.services.dynamodbv2.model.AttributeValue().withSS();
        Assertions.assertEquals(expectedAttributeValue,
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withSS()));
        Assertions.assertEquals(expectedAttributeValue,
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withSS(Collections.emptyList())));
    }

    @Test
    public void testToAttributeValueEmptyObjectWhenEmpty_BS() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue expectedAttributeValue =
                new com.amazonaws.services.dynamodbv2.model.AttributeValue().withBS();
        Assertions.assertEquals(expectedAttributeValue,
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withBS()));
        Assertions.assertEquals(expectedAttributeValue,
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withBS(Collections.emptyList())));
    }

    @Test
    public void testToAttributeValueEmptyObjectWhenEmpty_L() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue expectedAttributeValue =
                new com.amazonaws.services.dynamodbv2.model.AttributeValue().withL();
        Assertions.assertEquals(expectedAttributeValue,
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withL()));
        Assertions.assertEquals(expectedAttributeValue,
                DynamodbAttributeValueTransformer.toAttributeValue(new AttributeValue().withL(Collections.emptyList())));
    }
    //endregion

}
