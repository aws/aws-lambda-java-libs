/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Handler for requests to Lambda function.
 */

public class App implements RequestStreamHandler {
    private static final Charset usAscii = StandardCharsets.US_ASCII;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, usAscii)); 
            PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream, usAscii)))
        ) { 
            Vehicle vehicle = gson.fromJson(reader, Vehicle.class); System.out.println("input: " + vehicle); 
            APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent().withStatusCode(200); 
            writer.write(gson.toJson(responseEvent)); 
        } catch (IllegalStateException | JsonSyntaxException exception) { 
            exception.printStackTrace(); 
        }
    }
}
