package com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb;

import com.amazonaws.services.dynamodbv2.model.Identity;

public class DynamodbIdentityTransformer {

    public static Identity toIdentityV1(final com.amazonaws.services.lambda.runtime.events.models.dynamodb.Identity identity) {
        return new Identity()
                .withPrincipalId(identity.getPrincipalId())
                .withType(identity.getType());
    }
}
