package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

import static java.net.HttpURLConnection.HTTP_ACCEPTED;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

/**
 * LambdaRuntimeClient is a client of the AWS Lambda Runtime HTTP API for custom runtimes.
 * <p>
 * API definition can be found at https://docs.aws.amazon.com/lambda/latest/dg/runtimes-api.html
 * <p>
 * Copyright (c) 2019 Amazon. All rights reserved.
 */
public class LambdaRuntimeClient {

    private final String hostname;
    private final int port;
    private final String invocationEndpoint;
    private final String nextInvocationEndpoint;

    private static final String DEFAULT_CONTENT_TYPE = "application/json";
    private static final String XRAY_ERROR_CAUSE_HEADER = "Lambda-Runtime-Function-XRay-Error-Cause";
    private static final String ERROR_TYPE_HEADER = "Lambda-Runtime-Function-Error-Type";
    private static final int XRAY_ERROR_CAUSE_MAX_HEADER_SIZE = 1024 * 1024; // 1MiB

    public static final String LOG_TAG = "LAMBDA_RUNTIME";
    public static final String REQUEST_ID_HEADER = "lambda-runtime-aws-request-id";
    public static final String TRACE_ID_HEADER = "lambda-runtime-trace-id";
    public static final String CLIENT_CONTEXT_HEADER = "lambda-runtime-client-context";
    public static final String COGNITO_IDENTITY_HEADER = "lambda-runtime-cognito-identity";
    public static final String DEADLINE_MS_HEADER = "lambda-runtime-deadline-ms";
    public static final String FUNCTION_ARN_HEADER = "lambda-runtime-invoked-function-arn";

    public LambdaRuntimeClient(String hostnamePort) {
        Objects.requireNonNull(hostnamePort, "hostnamePort cannot be null");
        String[] parts = hostnamePort.split(":");
        this.hostname = parts[0];
        this.port = Integer.parseInt(parts[1]);
        this.invocationEndpoint = invocationEndpoint();
        this.nextInvocationEndpoint = nextInvocationEndpoint();
    }

    public InvocationRequest waitForNextInvocation() throws IOException {
        URL url = createUrl(nextInvocationEndpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setInstanceFollowRedirects(true);

        return new InvocationRequest(
                conn.getHeaderField(REQUEST_ID_HEADER),
                conn.getHeaderField(TRACE_ID_HEADER),
                conn.getHeaderField(FUNCTION_ARN_HEADER),
                Optional.ofNullable(conn.getHeaderField(DEADLINE_MS_HEADER)).map(Long::valueOf).orElse(-1L),
                conn.getHeaderField(CLIENT_CONTEXT_HEADER),
                conn.getHeaderField(COGNITO_IDENTITY_HEADER),
                readBody(conn)
        );
    }

    public void postInvocationResponse(String requestId, byte[] response) throws IOException {
        String responseEndpoint = invocationResponseEndpoint(requestId);
        URL url = createUrl(responseEndpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        try (OutputStream outputStream = conn.getOutputStream()) {
            outputStream.write(response, 0, response.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != HTTP_ACCEPTED) {
            throw new LambdaRuntimeClientException(responseEndpoint, responseCode);
        }

        // don't need to read the response, close stream to ensure connection re-use
        closeQuietly(conn.getInputStream());
    }

    public void postInvocationError(String requestId, byte[] errorResponse, String errorType) throws IOException {
        postInvocationError(requestId, errorResponse, errorType, null);
    }

    public void postInvocationError(String requestId, byte[] errorResponse, String errorType, String errorCause)
            throws IOException {
        String endpoint = invocationErrorEndpoint(requestId);
        post(endpoint, errorResponse, errorType, errorCause);
    }

    public void postInitError(byte[] errorResponse, String errorType) throws IOException {
        String endpoint = initErrorEndpoint();
        post(endpoint, errorResponse, errorType, null);
    }

    private void post(String endpoint, byte[] errorResponse, String errorType, String errorCause) throws IOException {
        URL url = createUrl(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", DEFAULT_CONTENT_TYPE);
        if (errorType != null && !errorType.isEmpty()) {
            conn.setRequestProperty(ERROR_TYPE_HEADER, errorType);
        }
        if (errorCause != null && errorCause.getBytes().length < XRAY_ERROR_CAUSE_MAX_HEADER_SIZE) {
            conn.setRequestProperty(XRAY_ERROR_CAUSE_HEADER, errorCause);
        }
        conn.setFixedLengthStreamingMode(errorResponse.length);
        conn.setDoOutput(true);
        try (OutputStream outputStream = conn.getOutputStream()) {
            outputStream.write(errorResponse);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != HTTP_ACCEPTED) {
            throw new LambdaRuntimeClientException(endpoint, responseCode);
        }

        // don't need to read the response, close stream to ensure connection re-use
        closeQuietly(conn.getInputStream());
    }

    private byte[] readBody(HttpURLConnection connection) throws IOException {
        InputStream bodyInputStream;

        if (connection.getResponseCode() < HTTP_BAD_REQUEST) {
            bodyInputStream = connection.getInputStream();
        } else {
            bodyInputStream = connection.getErrorStream();
        }

        try (InputStream inputStream = bodyInputStream;
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            int nRead;
            byte[] data = new byte[8];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            return buffer.toByteArray();
        }
    }

    private String invocationEndpoint() {
        return "http://" + hostname + ":" + port + "/2018-06-01/runtime/invocation/";
    }

    private String invocationErrorEndpoint(String requestId) {
        return invocationEndpoint + requestId + "/error";
    }

    private String initErrorEndpoint() {
        return "http://" + hostname + ":" + port + "/2018-06-01/runtime/init/error";
    }

    private String nextInvocationEndpoint() {
        return "http://" + hostname + ":" + port + "/runtime/invocation/next";
    }

    private String invocationResponseEndpoint(String requestId) {
        return String.
                format("http://" + hostname + ":" + port + "/2018-06-01/runtime/invocation/%s/response", requestId);
    }

    private URL createUrl(String endpoint) {
        try {
            return new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeQuietly(InputStream inputStream) {
        if (inputStream == null) return;
        try {
            inputStream.close();
        } catch (IOException e) {
        }
    }
}
