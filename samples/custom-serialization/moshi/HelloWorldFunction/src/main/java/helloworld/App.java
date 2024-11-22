/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Vehicle, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(Vehicle vehicle, Context context) {
        System.out.println("input: " + vehicle);

        return new APIGatewayProxyResponseEvent().withStatusCode(200);
    }

}
