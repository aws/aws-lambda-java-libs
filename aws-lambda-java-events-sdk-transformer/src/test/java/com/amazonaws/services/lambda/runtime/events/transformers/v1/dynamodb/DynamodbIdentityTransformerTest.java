package com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb;

import com.amazonaws.services.dynamodbv2.model.Identity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DynamodbIdentityTransformerTest {

    private static final String principalId = "1234567890";
    private static final String identityType = "type";

    //region Identity_event
    public static final com.amazonaws.services.lambda.runtime.events.models.dynamodb.Identity identity_event = new com.amazonaws.services.lambda.runtime.events.models.dynamodb.Identity()
            .withPrincipalId(principalId)
            .withType(identityType);
    //endregion

    //region Identity_v1
    public static final Identity identity_v1 = new Identity()
            .withPrincipalId(principalId)
            .withType(identityType);
    //endregion

    @Test
    public void testToIdentityV1() {
        Identity convertedIdentity = DynamodbIdentityTransformer.toIdentityV1(identity_event);
        Assertions.assertEquals(identity_v1, convertedIdentity);
    }

}