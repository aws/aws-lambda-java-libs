package com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb;

import software.amazon.awssdk.services.dynamodb.model.Identity;

public class DynamodbIdentityTransformer {

    public static Identity toIdentityV2(final com.amazonaws.services.lambda.runtime.events.models.dynamodb.Identity identity) {
        return Identity.builder()
                .principalId(identity.getPrincipalId())
                .type(identity.getType())
                .build();
    }
}
