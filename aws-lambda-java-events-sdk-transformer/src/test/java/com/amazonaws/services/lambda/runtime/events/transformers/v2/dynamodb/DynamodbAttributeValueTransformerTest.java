package com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb;

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
        Assertions.assertEquals("UnmodifiableRandomAccessList", convertedAttributeValueL.l().getClass().getSimpleName(), "List is immutable");
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
        Assertions.assertDoesNotThrow(() -> {
            software.amazon.awssdk.services.dynamodb.model.AttributeValue attributeValue = DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withL());
            Assertions.assertEquals("UnmodifiableRandomAccessList", attributeValue.l().getClass().getSimpleName(), "List is immutable");
        });
        Assertions.assertDoesNotThrow(() -> {
            software.amazon.awssdk.services.dynamodb.model.AttributeValue attributeValue = DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withL(Collections.emptyList()));
            Assertions.assertEquals("UnmodifiableRandomAccessList", attributeValue.l().getClass().getSimpleName(), "List is immutable");
        });
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
                software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder().l(Collections.emptyList()).build();
        Assertions.assertEquals(expectedAttributeValue_v2,
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withL()));
        Assertions.assertEquals(expectedAttributeValue_v2,
                DynamodbAttributeValueTransformer.toAttributeValueV2(new AttributeValue().withL(Collections.emptyList())));
    }

}