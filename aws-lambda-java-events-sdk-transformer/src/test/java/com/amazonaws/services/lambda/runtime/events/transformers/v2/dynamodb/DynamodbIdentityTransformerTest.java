package com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.Identity;

class DynamodbIdentityTransformerTest {

    private static final String principalId = "1234567890";
    private static final String identityType = "type";

    //region Identity_event
    public static final com.amazonaws.services.lambda.runtime.events.models.dynamodb.Identity identity_event =
            new com.amazonaws.services.lambda.runtime.events.models.dynamodb.Identity()
                    .withPrincipalId(principalId)
                    .withType(identityType);
    //endregion

    //region Identity_v2
    public static final Identity identity_v2 = Identity.builder()
            .principalId(principalId)
            .type(identityType)
            .build();
    //endregion

    @Test
    public void testToIdentityV2() {
        Identity convertedIdentity = DynamodbIdentityTransformer.toIdentityV2(identity_event);
        Assertions.assertEquals(identity_v2, convertedIdentity);
    }

}