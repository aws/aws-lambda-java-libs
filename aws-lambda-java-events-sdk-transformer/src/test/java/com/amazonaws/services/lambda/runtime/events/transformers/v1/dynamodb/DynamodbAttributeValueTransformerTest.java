package com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb;

import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
    public static final AttributeValue attributeValueM_event = new AttributeValue().withM(new HashMap<String, AttributeValue>() {{
        put(keyM1, attributeValueN_event);
        put(keyM2, attributeValueS_event);
    }});
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

    //region AttributeValue_v1
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueN_v1 =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withN(valueN);
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueNS_v1 =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withNS(valueNS);
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueS_v1 =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withS(valueS);
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueSS_v1 =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withSS(valueSS);
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueB_v1 =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withB(valueB);
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueBS_v1 =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withBS(valueBS);
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueBOOL_v1 =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withBOOL(valueBOOL);
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueNUL_v1 =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withNULL(valueNUL);
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueM_v1 =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withM(new HashMap<String, com.amazonaws.services.dynamodbv2.model.AttributeValue>() {{
                put(keyM1, attributeValueN_v1);
                put(keyM2, attributeValueS_v1);
            }});
    public static final com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValueL_v1 =
            new com.amazonaws.services.dynamodbv2.model.AttributeValue().withL(Arrays.asList(
                    attributeValueN_v1,
                    attributeValueNS_v1,
                    attributeValueS_v1,
                    attributeValueSS_v1,
                    attributeValueB_v1,
                    attributeValueBS_v1,
                    attributeValueBOOL_v1,
                    attributeValueNUL_v1,
                    attributeValueM_v1,
                    new com.amazonaws.services.dynamodbv2.model.AttributeValue().withL(Arrays.asList(
                            attributeValueN_v1,
                            attributeValueNS_v1,
                            attributeValueS_v1,
                            attributeValueSS_v1,
                            attributeValueB_v1,
                            attributeValueBS_v1,
                            attributeValueBOOL_v1,
                            attributeValueNUL_v1,
                            attributeValueM_v1
                    ))
            ));
    //endregion

    @Test
    public void testToAttributeValueV1_N() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueN =
                DynamodbAttributeValueTransformer.toAttributeValueV1(attributeValueN_event);
        Assertions.assertEquals(attributeValueN_v1, convertedAttributeValueN);
    }

    @Test
    public void testToAttributeValueV1_NS() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueNS =
                DynamodbAttributeValueTransformer.toAttributeValueV1(attributeValueNS_event);
        Assertions.assertEquals(attributeValueNS_v1, convertedAttributeValueNS);
    }

    @Test
    public void testToAttributeValueV1_S() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueS =
                DynamodbAttributeValueTransformer.toAttributeValueV1(attributeValueS_event);
        Assertions.assertEquals(attributeValueS_v1, convertedAttributeValueS);
    }

    @Test
    public void testToAttributeValueV1_SS() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueSS =
                DynamodbAttributeValueTransformer.toAttributeValueV1(attributeValueSS_event);
        Assertions.assertEquals(attributeValueSS_v1, convertedAttributeValueSS);
    }

    @Test
    public void testToAttributeValueV1_B() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueB =
                DynamodbAttributeValueTransformer.toAttributeValueV1(attributeValueB_event);
        Assertions.assertEquals(attributeValueB_v1, convertedAttributeValueB);
    }

    @Test
    public void testToAttributeValueV1_BS() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueBS =
                DynamodbAttributeValueTransformer.toAttributeValueV1(attributeValueBS_event);
        Assertions.assertEquals(attributeValueBS_v1, convertedAttributeValueBS);
    }

    @Test
    public void testToAttributeValueV1_BOOL() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueBOOL =
                DynamodbAttributeValueTransformer.toAttributeValueV1(attributeValueBOOL_event);
        Assertions.assertEquals(attributeValueBOOL_v1, convertedAttributeValueBOOL);
    }

    @Test
    public void testToAttributeValueV1_NUL() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueNUL =
                DynamodbAttributeValueTransformer.toAttributeValueV1(attributeValueNUL_event);
        Assertions.assertEquals(attributeValueNUL_v1, convertedAttributeValueNUL);
    }

    @Test
    public void testToAttributeValueV1_M() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueM =
                DynamodbAttributeValueTransformer.toAttributeValueV1(attributeValueM_event);
        Assertions.assertEquals(attributeValueM_v1, convertedAttributeValueM);
    }

    @Test
    public void testToAttributeValueV1_L() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue convertedAttributeValueL =
                DynamodbAttributeValueTransformer.toAttributeValueV1(attributeValueL_event);
        Assertions.assertEquals(attributeValueL_v1, convertedAttributeValueL);
        Assertions.assertEquals("ArrayList", convertedAttributeValueL.getL().getClass().getSimpleName(), "List is mutable");
    }

    @Test
    public void testToAttributeValueV1_IllegalArgumentWhenNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue())
        );
    }

    @Test
    public void testToAttributeValueV1_IllegalArgumentWhenNull_N() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withN(null))
        );
    }

    @Test
    public void testToAttributeValueV1_IllegalArgumentWhenNull_S() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withS(null))
        );
    }

    @Test
    public void testToAttributeValueV1_IllegalArgumentWhenNull_B() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withB(null))
        );
    }

    @Test
    public void testToAttributeValueV1_IllegalArgumentWhenNull_BOOL() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withBOOL(null))
        );
    }

    @Test
    public void testToAttributeValueV1_IllegalArgumentWhenNull_NUL() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withNULL(null))
        );
    }

    @Test
    public void testToAttributeValueV1_IllegalArgumentWhenNull_M() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withM(null))
        );
    }

    @Test
    public void testToAttributeValueV1_DoesNotThrowWhenEmpty_NS() {
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withNS())
        );
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withNS(Collections.emptyList()))
        );
    }

    @Test
    public void testToAttributeValueV1_DoesNotThrowWhenEmpty_SS() {
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withSS())
        );
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withSS(Collections.emptyList()))
        );
    }

    @Test
    public void testToAttributeValueV1_DoesNotThrowWhenEmpty_BS() {
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withBS())
        );
        Assertions.assertDoesNotThrow(() ->
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withBS(Collections.emptyList()))
        );
    }

    @Test
    public void testToAttributeValueV1_DoesNotThrowWhenEmpty_L() {
        Assertions.assertDoesNotThrow(() -> {
            com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValue = DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withL());
            Assertions.assertEquals("ArrayList", attributeValue.getL().getClass().getSimpleName(), "List is mutable");
        });
        Assertions.assertDoesNotThrow(() -> {
            com.amazonaws.services.dynamodbv2.model.AttributeValue attributeValue = DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withL(Collections.emptyList()));
            Assertions.assertEquals("ArrayList", attributeValue.getL().getClass().getSimpleName(), "List is mutable");
        });
    }

    @Test
    public void testToAttributeValueV1_EmptyV1ObjectWhenEmpty_NS() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue expectedAttributeValue_v1 =
                new com.amazonaws.services.dynamodbv2.model.AttributeValue();
        Assertions.assertEquals(expectedAttributeValue_v1,
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withNS()));
        Assertions.assertEquals(expectedAttributeValue_v1,
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withNS(Collections.emptyList())));
    }

    @Test
    public void testToAttributeValueV1_EmptyV1ObjectWhenEmpty_SS() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue expectedAttributeValue_v1 =
                new com.amazonaws.services.dynamodbv2.model.AttributeValue();
        Assertions.assertEquals(expectedAttributeValue_v1,
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withSS()));
        Assertions.assertEquals(expectedAttributeValue_v1,
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withSS(Collections.emptyList())));
    }

    @Test
    public void testToAttributeValueV1_EmptyV1ObjectWhenEmpty_BS() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue expectedAttributeValue_v1 =
                new com.amazonaws.services.dynamodbv2.model.AttributeValue();
        Assertions.assertEquals(expectedAttributeValue_v1,
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withBS()));
        Assertions.assertEquals(expectedAttributeValue_v1,
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withBS(Collections.emptyList())));
    }

    @Test
    public void testToAttributeValueV1_EmptyV1ObjectWhenEmpty_L() {
        com.amazonaws.services.dynamodbv2.model.AttributeValue expectedAttributeValue_v1 =
                new com.amazonaws.services.dynamodbv2.model.AttributeValue().withL(Collections.emptyList());
        Assertions.assertEquals(expectedAttributeValue_v1,
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withL()));
        Assertions.assertEquals(expectedAttributeValue_v1,
                DynamodbAttributeValueTransformer.toAttributeValueV1(new AttributeValue().withL(Collections.emptyList())));
    }

}