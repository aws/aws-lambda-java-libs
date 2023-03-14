package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.net.HttpURLConnection.HTTP_ACCEPTED;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * LambdaRuntimeClient is a client of the AWS Lambda Runtime HTTP API for custom runtimes.
 *
 * API definition can be found at https://docs.aws.amazon.com/lambda/latest/dg/runtimes-api.html
 *
 * Copyright (c) 2019 Amazon. All rights reserved.
 */
public class LambdaRuntimeClient {

    private final String hostname;
    private final int port;
    private final String invocationEndpoint;

    private static final String DEFAULT_CONTENT_TYPE = "application/json";
    private static final String XRAY_ERROR_CAUSE_HEADER = "Lambda-Runtime-Function-XRay-Error-Cause";
    private static final String ERROR_TYPE_HEADER = "Lambda-Runtime-Function-Error-Type";
    private static final int XRAY_ERROR_CAUSE_MAX_HEADER_SIZE = 1024 * 1024; // 1MiB

    public LambdaRuntimeClient(String hostnamePort) {
        Objects.requireNonNull(hostnamePort, "hostnamePort cannot be null");
        String[] parts = hostnamePort.split(":");
        this.hostname = parts[0];
        this.port = Integer.parseInt(parts[1]);
        this.invocationEndpoint = invocationEndpoint();
    }

    public InvocationRequest waitForNextInvocation() {
        return NativeClient.next();
    }

    public void postInvocationResponse(String requestId, byte[] response) {
        NativeClient.postInvocationResponse(requestId.getBytes(UTF_8), response);
    }

    public void postInvocationError(String requestId, byte[] errorResponse, String errorType) throws IOException {
        postInvocationError(requestId, errorResponse, errorType, null);
    }

    public void postInvocationError(String requestId, byte[] errorResponse, String errorType, String errorCause)
            throws IOException {
        String endpoint = invocationErrorEndpoint(requestId);
        post(endpoint, errorResponse, errorType, errorCause);
    }

    public void getRestoreNext() throws IOException {
        doGet(restoreNextEndpoint(), HTTP_OK);
    }

    public int postRestoreError(byte[] errorResponse, String errorType) throws IOException {
        String endpoint = restoreErrorEndpoint();
        return postError(endpoint, errorResponse, errorType, null);
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
        if(errorType != null && !errorType.isEmpty()) {
            conn.setRequestProperty(ERROR_TYPE_HEADER, errorType);
        }
        if(errorCause != null && errorCause.getBytes().length < XRAY_ERROR_CAUSE_MAX_HEADER_SIZE) {
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

    private String invocationEndpoint() {
        return getBaseUrl() + "/2018-06-01/runtime/invocation/";
    }

    private String invocationErrorEndpoint(String requestId) {
        return invocationEndpoint + requestId + "/error";
    }

    private String initErrorEndpoint() {
        return getBaseUrl() + "/2018-06-01/runtime/init/error";
    }

    private String restoreErrorEndpoint() {
        return getBaseUrl() + "/2018-06-01/runtime/restore/error";
    }

    private String restoreNextEndpoint() {
        return getBaseUrl() + "/2018-06-01/runtime/restore/next";
    }

    private String getBaseUrl() {
        return "http://" + hostname + ":" + port;
    }

    private int postError(String endpoint,
                          byte[] errorResponse,
                          String errorType,
                          String errorCause) throws IOException {

        Map<String, String> headers = new HashMap<>();
        if (errorType != null && !errorType.isEmpty()) {
            headers.put(ERROR_TYPE_HEADER, errorType);
        }
        if (errorCause != null && errorCause.getBytes().length < XRAY_ERROR_CAUSE_MAX_HEADER_SIZE) {
            headers.put(XRAY_ERROR_CAUSE_HEADER, errorCause);
        }

        return doPost(endpoint, DEFAULT_CONTENT_TYPE, headers, errorResponse);
    }

    private int doPost(String endpoint,
                       String contentType,
                       Map<String, String> headers,
                       byte[] payload) throws IOException {

        URL url = createUrl(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", contentType);

        for (Map.Entry<String, String> header : headers.entrySet()) {
            conn.setRequestProperty(header.getKey(), header.getValue());
        }

        conn.setFixedLengthStreamingMode(payload.length);
        conn.setDoOutput(true);

        try (OutputStream outputStream = conn.getOutputStream()) {
            outputStream.write(payload);
        }

        // get response code before closing the stream
        int responseCode = conn.getResponseCode();

        // don't need to read the response, close stream to ensure connection re-use
        closeInputStreamQuietly(conn);

        return responseCode;
    }

    private void doGet(String endpoint, int expectedHttpResponseCode) throws IOException {

        URL url = createUrl(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != expectedHttpResponseCode) {
            throw new LambdaRuntimeClientException(endpoint, responseCode);
        }

        closeInputStreamQuietly(conn);
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

    private void closeInputStreamQuietly(HttpURLConnection conn) {

        InputStream inputStream;
        try {
            inputStream = conn.getInputStream();
        } catch (IOException e) {
            return;
        }

        if (inputStream == null) {
            return;
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            // ignore
        }
    }
}
