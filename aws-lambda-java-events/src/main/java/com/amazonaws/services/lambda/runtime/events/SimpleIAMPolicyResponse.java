package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * The simplified IAM Policy response object as described in https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api-lambda-authorizer.html
 *
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class SimpleIAMPolicyResponse {

    private boolean isAuthorized;
    private Map<String, String> context;
}