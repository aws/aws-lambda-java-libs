package com.amazonaws.services.lambda.runtime.events.transformers.dynamodb;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DynamodbAttributeValueTransformer {

    public static AttributeValue toAttributeValueV2(final com.amazonaws.services.dynamodbv2.model.AttributeValue value) {
        if (Objects.nonNull(value.getS())) {
            return AttributeValue.builder()
                    .s(value.getS())
                    .build();

        } else if (Objects.nonNull(value.getSS())) {
            return AttributeValue.builder()
                    .ss(value.getSS())
                    .build();

        } else if (Objects.nonNull(value.getN())) {
            return AttributeValue.builder()
                    .n(value.getN())
                    .build();

        } else if (Objects.nonNull(value.getNS())) {
            return AttributeValue.builder()
                    .ns(value.getNS())
                    .build();

        } else if (Objects.nonNull(value.getB())) {
            return AttributeValue.builder()
                    .b(SdkBytes.fromByteBuffer(value.getB()))
                    .build();

        } else if (Objects.nonNull(value.getBS())) {
            return AttributeValue.builder()
                    .bs(value.getBS().stream()
                            .map(SdkBytes::fromByteBuffer)
                            .collect(Collectors.toList()))
                    .build();

        } else if (Objects.nonNull(value.getBOOL())) {
            return AttributeValue.builder()
                    .bool(value.getBOOL())
                    .build();

        } else if (Objects.nonNull(value.getL())) {
            return AttributeValue.builder()
                    .l(value.getL().stream()
                            .map(DynamodbAttributeValueTransformer::toAttributeValueV2)
                            .collect(Collectors.toList()))
                    .build();

        } else if (Objects.nonNull(value.getM())) {
            return AttributeValue.builder()
                    .m(toAttributeValueMapV2(value.getM()))
                    .build();

        } else if (Objects.nonNull(value.getNULL())) {
            return AttributeValue.builder()
                    .nul(value.getNULL())
                    .build();

        } else {
            throw new IllegalArgumentException(
                    String.format("Unsupported attributeValue type: %s", value));
        }
    }

    static Map<String, AttributeValue> toAttributeValueMapV2(
            final Map<String, com.amazonaws.services.dynamodbv2.model.AttributeValue> attributeValueMap
    ) {
        return attributeValueMap
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> toAttributeValueV2(entry.getValue())
                ));
    }
}
