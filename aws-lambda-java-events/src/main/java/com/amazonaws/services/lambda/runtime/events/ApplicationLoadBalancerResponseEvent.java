package com.amazonaws.services.lambda.runtime.events;

import com.amazonaws.services.lambda.runtime.events.models.HttpHeaders;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Class to represent the response event to Application Load Balancer.
 *
 * @see <a href="https://docs.aws.amazon.com/lambda/latest/dg/services-alb.html">Using AWS Lambda with an Application Load Balancer</a>
 *
 * @author msailes <msailes@amazon.co.uk>
 */

@NoArgsConstructor
@Data
public class ApplicationLoadBalancerResponseEvent implements Serializable, Cloneable {

    private int statusCode;
    private String statusDescription;
    private boolean isBase64Encoded;
    private HttpHeaders<String> headers;
    private HttpHeaders<List<String>> multiValueHeaders;
    private String body;

}
