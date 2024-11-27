/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.InvocationRequest;
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

public class LambdaRuntimeApiClientImpl implements LambdaRuntimeApiClient {

    static final String USER_AGENT = String.format(
        "aws-lambda-java/%s-%s",
        System.getProperty("java.vendor.version"),
        LambdaRuntimeApiClientImpl.class.getPackage().getImplementationVersion());

    private static final String DEFAULT_CONTENT_TYPE = "application/json";
    private static final String XRAY_ERROR_CAUSE_HEADER = "Lambda-Runtime-Function-XRay-Error-Cause";
    private static final String ERROR_TYPE_HEADER = "Lambda-Runtime-Function-Error-Type";
    // 1MiB
    private static final int XRAY_ERROR_CAUSE_MAX_HEADER_SIZE = 1024 * 1024;

    private final String baseUrl;
    private final String invocationEndpoint;

    public LambdaRuntimeApiClientImpl(String hostnameAndPort) {
        Objects.requireNonNull(hostnameAndPort, "hostnameAndPort cannot be null");
        this.baseUrl = "http://" + hostnameAndPort;
        this.invocationEndpoint = this.baseUrl + "/2018-06-01/runtime/invocation/";
        NativeClient.init(hostnameAndPort);
    }

    @Override
    public void reportInitError(LambdaError error) throws IOException {
        String endpoint = this.baseUrl + "/2018-06-01/runtime/init/error";
        reportLambdaError(endpoint, error, XRAY_ERROR_CAUSE_MAX_HEADER_SIZE);
    }

    @Override
    public InvocationRequest nextInvocation() {
        return NativeClient.next();
    }

    @Override
    public void reportInvocationSuccess(String requestId, byte[] response) {
        NativeClient.postInvocationResponse(requestId.getBytes(UTF_8), response);
    }

    @Override
    public void reportInvocationError(String requestId, LambdaError error) throws IOException {
        String endpoint = invocationEndpoint + requestId + "/error";
        reportLambdaError(endpoint, error, XRAY_ERROR_CAUSE_MAX_HEADER_SIZE);
    }

    @Override
    public void restoreNext() throws IOException {
        String endpoint = this.baseUrl + "/2018-06-01/runtime/restore/next";
        int responseCode = doGet(endpoint);
        if (responseCode != HTTP_OK) {
            throw new LambdaRuntimeClientException(endpoint, responseCode);
        }
    }

    @Override
    public void reportRestoreError(LambdaError error) throws IOException {
        String endpoint = this.baseUrl + "/2018-06-01/runtime/restore/error";
        reportLambdaError(endpoint, error, XRAY_ERROR_CAUSE_MAX_HEADER_SIZE);
    }

    void reportLambdaError(String endpoint, LambdaError error, int maxXrayHeaderSize) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put(ERROR_TYPE_HEADER, error.errorType.getRapidError());

        if (error.xRayErrorCause != null) {
            byte[] xRayErrorCauseJson = DtoSerializers.serialize(error.xRayErrorCause);
            if (xRayErrorCauseJson != null && xRayErrorCauseJson.length < maxXrayHeaderSize) {
                headers.put(XRAY_ERROR_CAUSE_HEADER, new String(xRayErrorCauseJson));
            }
        }

        byte[] payload = DtoSerializers.serialize(error.errorRequest);
        int responseCode = doPost(endpoint, headers, payload);
        if (responseCode != HTTP_ACCEPTED) {
            throw new LambdaRuntimeClientException(endpoint, responseCode);
        }
    }

    private int doPost(String endpoint,
                       Map<String, String> headers,
                       byte[] payload) throws IOException {
        URL url = createUrl(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", DEFAULT_CONTENT_TYPE);
        conn.setRequestProperty("User-Agent", USER_AGENT);

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

    private int doGet(String endpoint) throws IOException {
        URL url = createUrl(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = conn.getResponseCode();
        closeInputStreamQuietly(conn);

        return responseCode;
    }

    private URL createUrl(String endpoint) {
        try {
            return new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
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
