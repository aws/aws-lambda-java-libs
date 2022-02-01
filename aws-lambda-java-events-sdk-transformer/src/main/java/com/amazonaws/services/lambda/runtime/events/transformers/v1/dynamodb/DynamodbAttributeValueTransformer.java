package com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DynamodbAttributeValueTransformer {

    public static AttributeValue toAttributeValueV1(final com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue value) {
        if (Objects.nonNull(value.getS())) {
            return new AttributeValue()
                    .withS(value.getS());

        } else if (Objects.nonNull(value.getSS())) {
            return new AttributeValue()
                    .withSS(value.getSS().isEmpty() ? null : value.getSS());

        } else if (Objects.nonNull(value.getN())) {
            return new AttributeValue()
                    .withN(value.getN());

        } else if (Objects.nonNull(value.getNS())) {
            return new AttributeValue()
                    .withNS(value.getNS().isEmpty() ? null : value.getNS());

        } else if (Objects.nonNull(value.getB())) {
            return new AttributeValue()
                    .withB(value.getB());

        } else if (Objects.nonNull(value.getBS())) {
            return new AttributeValue()
                    .withBS(value.getBS().isEmpty() ? null : value.getBS());

        } else if (Objects.nonNull(value.getBOOL())) {
            return new AttributeValue()
                    .withBOOL(value.getBOOL());

        } else if (Objects.nonNull(value.getL())) {
            return new AttributeValue()
                    .withL(value.getL().isEmpty()
                            ? Collections.emptyList()
                            : value.getL().stream()
                                .map(DynamodbAttributeValueTransformer::toAttributeValueV1)
                                .collect(Collectors.toList()));

        } else if (Objects.nonNull(value.getM())) {
            return new AttributeValue()
                    .withM(toAttributeValueMapV1(value.getM()));

        } else if (Objects.nonNull(value.getNULL())) {
            return new AttributeValue()
                    .withNULL(value.getNULL());

        } else {
            throw new IllegalArgumentException(
                    String.format("Unsupported attributeValue type: %s", value));
        }
    }

    public static Map<String, AttributeValue> toAttributeValueMapV1(
            final Map<String, com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue> attributeValueMap
    ) {
        return attributeValueMap
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> toAttributeValueV1(entry.getValue())
                ));
    }
}
