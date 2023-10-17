package com.amazonaws.services.lambda.runtime.events;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 Common response class for APIGateway and ALB
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AwsProxyResponseEvent {
    private static final long serialVersionUID = 2263167344670024138L;

    private Integer statusCode;

    private String statusDescription;

    private List<String> cookies;

    private Map<String, String> headers;

    private Map<String, List<String>> multiValueHeaders;

    private String body;

    private boolean isBase64Encoded;
}
