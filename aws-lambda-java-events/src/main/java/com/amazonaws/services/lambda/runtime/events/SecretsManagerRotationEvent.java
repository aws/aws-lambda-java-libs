/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package com.amazonaws.services.lambda.runtime.events;

/**
 * Class to represent the events which are sent during a Secrets Manager rotation process.
 *
 * @see <a href="https://docs.aws.amazon.com/secretsmanager/latest/userguide/rotating-secrets-lambda-function-overview.html">Rotating secrets lambda function overview</a>
 *
 * @author msailes <msailes@amazon.co.uk>
 */
public class SecretsManagerRotationEvent {

    private String step;
    private String secretId;
    private String clientRequestToken;

    /**
     * Default constructor
     */
    public SecretsManagerRotationEvent() {
    }

    /**
     * The current step in the rotation process.
     *
     * @return step
     */
    public String getStep() {
        return step;
    }

    /**
     * Sets the current step in the rotation process.
     *
     * @param step
     */
    public void setStep(String step) {
        this.step = step;
    }

    /**
     * The ID or Amazon Resource Name (ARN) for the secret to rotate.
     *
     * @return secretId
     */
    public String getSecretId() {
        return secretId;
    }

    /**
     * Sets the ID or ARN for the secret to rotate.
     *
     * @param secretId
     */
    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    /**
     * The request token Secrets Manager uses this token to ensure the idempotency of requests during any required
     * retries caused by failures of individual calls.
     *
     * @return clientRequestToken
     */
    public String getClientRequestToken() {
        return clientRequestToken;
    }

    /**
     * Sets the request token.
     *
     * @param clientRequestToken
     */
    public void setClientRequestToken(String clientRequestToken) {
        this.clientRequestToken = clientRequestToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SecretsManagerRotationEvent that = (SecretsManagerRotationEvent) o;

        if (step != null ? !step.equals(that.step) : that.step != null) return false;
        if (secretId != null ? !secretId.equals(that.secretId) : that.secretId != null) return false;
        return clientRequestToken != null ? clientRequestToken.equals(that.clientRequestToken) : that.clientRequestToken == null;
    }

    @Override
    public int hashCode() {
        int result = step != null ? step.hashCode() : 0;
        result = 31 * result + (secretId != null ? secretId.hashCode() : 0);
        result = 31 * result + (clientRequestToken != null ? clientRequestToken.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SecretsManagerRotationEvent{");
        sb.append("step='").append(step).append('\'');
        sb.append(", secretId='").append(secretId).append('\'');
        sb.append(", clientRequestToken='").append(clientRequestToken).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public SecretsManagerRotationEvent clone() {
        try {
            return (SecretsManagerRotationEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }
}
