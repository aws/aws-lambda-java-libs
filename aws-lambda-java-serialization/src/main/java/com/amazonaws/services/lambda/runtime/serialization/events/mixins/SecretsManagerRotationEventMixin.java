/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Jackson annotations for SecretsManagerRotationEvent
 */

public abstract class SecretsManagerRotationEventMixin {

    // needed because Jackson expects "step" instead of "Step"
    @JsonProperty("Step") abstract String getStep();
    @JsonProperty("Step") abstract void setStep(String step);

    // needed because Jackson expects "secretId" instead of "SecretId"
    @JsonProperty("SecretId") abstract String getSecretId();
    @JsonProperty("SecretId") abstract void setSecretId(String secretId);

    // needed because Jackson expects "clientRequestToken" instead of "ClientRequestToken"
    @JsonProperty("ClientRequestToken") abstract String getClientRequestToken();
    @JsonProperty("ClientRequestToken") abstract void setClientRequestToken(String clientRequestToken);
}
