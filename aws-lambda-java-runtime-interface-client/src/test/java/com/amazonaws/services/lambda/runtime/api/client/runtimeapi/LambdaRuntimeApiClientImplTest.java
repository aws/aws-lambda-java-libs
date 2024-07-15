/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.ErrorRequest;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.StackElement;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.XRayErrorCause;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.XRayException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import okhttp3.HttpUrl;
import static java.net.HttpURLConnection.HTTP_ACCEPTED;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import okhttp3.mockwebserver.MockWebServer;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class LambdaRuntimeApiClientImplTest {

    MockWebServer mockWebServer;
    LambdaRuntimeApiClientImpl lambdaRuntimeApiClientImpl;

    String[] errorStackStrace = { "item0", "item1", "item2" };
    ErrorRequest errorRequest = new ErrorRequest("testErrorMessage", "testErrorType", errorStackStrace);

    String requestId = "1234";

    @BeforeEach
    void setUp() {
        mockWebServer = new MockWebServer();
        String hostnamePort = getHostnamePort();
        lambdaRuntimeApiClientImpl = new LambdaRuntimeApiClientImpl(hostnamePort);
    }

    @Test
    public void reportInitErrorTest() {
        try {
            RapidErrorType rapidErrorType = RapidErrorType.AfterRestoreError;

            MockResponse mockResponse = new MockResponse();
            mockResponse.setResponseCode(HTTP_ACCEPTED);
            mockWebServer.enqueue(mockResponse);

            LambdaError lambdaError = new LambdaError(errorRequest, rapidErrorType);
            lambdaRuntimeApiClientImpl.reportInitError(lambdaError);
            RecordedRequest recordedRequest = mockWebServer.takeRequest();
            HttpUrl actualUrl = recordedRequest.getRequestUrl();
            String expectedUrl = "http://" + getHostnamePort() + "/2018-06-01/runtime/init/error";
            assertEquals(expectedUrl, actualUrl.toString());

            String userAgentHeader = recordedRequest.getHeader("User-Agent");
            assertTrue(userAgentHeader.startsWith("aws-lambda-java/"));

            String lambdaRuntimeErrorTypeHeader = recordedRequest.getHeader("Lambda-Runtime-Function-Error-Type");
            assertEquals("Runtime.AfterRestoreError", lambdaRuntimeErrorTypeHeader);

            String actualBody = recordedRequest.getBody().readUtf8();
            assertEquals("{\"errorMessage\":\"testErrorMessage\",\"errorType\":\"testErrorType\",\"stackTrace\":[\"item0\",\"item1\",\"item2\"]}", actualBody);
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void reportInitErrorTestWrongStatusCode() {
        int errorStatusCode = HTTP_INTERNAL_ERROR;
        try {
            RapidErrorType rapidErrorType = RapidErrorType.AfterRestoreError;

            MockResponse mockResponse = new MockResponse();
            mockResponse.setResponseCode(errorStatusCode);
            mockWebServer.enqueue(mockResponse);

            LambdaError lambdaError = new LambdaError(errorRequest, rapidErrorType);
            lambdaRuntimeApiClientImpl.reportInitError(lambdaError);
            fail();
        } catch(LambdaRuntimeClientException e) {
            String expectedUrl = "http://" + getHostnamePort() + "/2018-06-01/runtime/init/error";
            String expectedMessage = expectedUrl + " Response code: '" + errorStatusCode + "'.";
            assertEquals(expectedMessage, e.getLocalizedMessage());
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void reportRestoreErrorTest() {
        try {
            RapidErrorType rapidErrorType = RapidErrorType.AfterRestoreError;

            MockResponse mockResponse = new MockResponse();
            mockResponse.setResponseCode(HTTP_ACCEPTED);
            mockWebServer.enqueue(mockResponse);

            LambdaError lambdaError = new LambdaError(errorRequest, rapidErrorType);
            lambdaRuntimeApiClientImpl.reportRestoreError(lambdaError);
            RecordedRequest recordedRequest = mockWebServer.takeRequest();
            HttpUrl actualUrl = recordedRequest.getRequestUrl();
            String expectedUrl = "http://" + getHostnamePort() + "/2018-06-01/runtime/restore/error";
            assertEquals(expectedUrl, actualUrl.toString());

            String userAgentHeader = recordedRequest.getHeader("User-Agent");
            assertTrue(userAgentHeader.startsWith("aws-lambda-java/"));

            String lambdaRuntimeErrorTypeHeader = recordedRequest.getHeader("Lambda-Runtime-Function-Error-Type");
            assertEquals("Runtime.AfterRestoreError", lambdaRuntimeErrorTypeHeader);

            String actualBody = recordedRequest.getBody().readUtf8();
            assertEquals("{\"errorMessage\":\"testErrorMessage\",\"errorType\":\"testErrorType\",\"stackTrace\":[\"item0\",\"item1\",\"item2\"]}", actualBody);
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void reportRestoreErrorTestWrongStatusCode() {
        int errorStatusCode = HTTP_INTERNAL_ERROR;
        try {
            RapidErrorType rapidErrorType = RapidErrorType.AfterRestoreError;

            MockResponse mockResponse = new MockResponse();
            mockResponse.setResponseCode(errorStatusCode);
            mockWebServer.enqueue(mockResponse);

            LambdaError lambdaError = new LambdaError(errorRequest, rapidErrorType);
            lambdaRuntimeApiClientImpl.reportRestoreError(lambdaError);
            fail();
        } catch(LambdaRuntimeClientException e) {
            String expectedUrl = "http://" + getHostnamePort() + "/2018-06-01/runtime/restore/error";
            String expectedMessage = expectedUrl + " Response code: '" + errorStatusCode + "'.";
            assertEquals(expectedMessage, e.getLocalizedMessage());
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void reportInvocationErrorTest() {
        try {
            RapidErrorType rapidErrorType = RapidErrorType.AfterRestoreError;

            MockResponse mockResponse = new MockResponse();
            mockResponse.setResponseCode(HTTP_ACCEPTED);
            mockWebServer.enqueue(mockResponse);

            LambdaError lambdaError = new LambdaError(errorRequest, rapidErrorType);
            lambdaRuntimeApiClientImpl.reportInvocationError(requestId, lambdaError);
            RecordedRequest recordedRequest = mockWebServer.takeRequest();
            HttpUrl actualUrl = recordedRequest.getRequestUrl();
            String expectedUrl = "http://" + getHostnamePort() + "/2018-06-01/runtime/invocation/1234/error";
            assertEquals(expectedUrl, actualUrl.toString());

            String userAgentHeader = recordedRequest.getHeader("User-Agent");
            assertTrue(userAgentHeader.startsWith("aws-lambda-java/"));

            String lambdaRuntimeErrorTypeHeader = recordedRequest.getHeader("Lambda-Runtime-Function-Error-Type");
            assertEquals("Runtime.AfterRestoreError", lambdaRuntimeErrorTypeHeader);

            String actualBody = recordedRequest.getBody().readUtf8();
            assertEquals("{\"errorMessage\":\"testErrorMessage\",\"errorType\":\"testErrorType\",\"stackTrace\":[\"item0\",\"item1\",\"item2\"]}", actualBody);
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void reportInvocationErrorTestWrongStatusCode() {
        int errorStatusCode = HTTP_INTERNAL_ERROR;
        try {
            RapidErrorType rapidErrorType = RapidErrorType.AfterRestoreError;

            MockResponse mockResponse = new MockResponse();
            mockResponse.setResponseCode(errorStatusCode);
            mockWebServer.enqueue(mockResponse);

            LambdaError lambdaError = new LambdaError(errorRequest, rapidErrorType);
            lambdaRuntimeApiClientImpl.reportInvocationError(requestId, lambdaError);
            fail();
        } catch(LambdaRuntimeClientException e) {
            String expectedUrl = "http://" + getHostnamePort() + "/2018-06-01/runtime/invocation/1234/error";
            String expectedMessage = expectedUrl + " Response code: '" + errorStatusCode + "'.";
            assertEquals(expectedMessage, e.getLocalizedMessage());
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void reportLambdaErrorWithXRayTest() {
        try {
            RapidErrorType rapidErrorType = RapidErrorType.AfterRestoreError;

            MockResponse mockResponse = new MockResponse();
            mockResponse.setResponseCode(HTTP_ACCEPTED);
            mockWebServer.enqueue(mockResponse);

            String workingDirectory = "my-test-directory";
            List<String> paths = new ArrayList<String>();
            paths.add("path-0");
            paths.add("path-1");
            paths.add("path-2");

            List<StackElement> stackElements0 = new ArrayList<>();
            stackElements0.add(new StackElement("label0", "path0", 0));
            stackElements0.add(new StackElement("label1", "path1", 1));
            stackElements0.add(new StackElement("label2", "path2", 2));
            XRayException xRayException0 = new XRayException("my-test-message0", "my-test-type0", stackElements0);

            List<StackElement> stackElements1 = new ArrayList<>();
            stackElements1.add(new StackElement("label10", "path10", 0));
            stackElements1.add(new StackElement("label11", "path11", 11));
            stackElements1.add(new StackElement("label12", "path12", 12));
            XRayException xRayException1 = new XRayException("my-test-message1", "my-test-type0", stackElements1);

            List<XRayException> exceptions = new ArrayList<>();
            exceptions.add(xRayException0);
            exceptions.add(xRayException1);

            XRayErrorCause xRayErrorCause = new XRayErrorCause(workingDirectory, exceptions, paths);
            LambdaError lambdaError = new LambdaError(errorRequest, xRayErrorCause, rapidErrorType);
            lambdaRuntimeApiClientImpl.reportInvocationError(requestId, lambdaError);
            RecordedRequest recordedRequest = mockWebServer.takeRequest();

            String xrayErrorCauseHeader = recordedRequest.getHeader("Lambda-Runtime-Function-XRay-Error-Cause");
            assertEquals("{\"working_directory\":\"my-test-directory\",\"exceptions\":[{\"message\":\"my-test-message0\",\"type\":\"my-test-type0\",\"stack\":[{\"label\":\"label0\"," +
                "\"path\":\"path0\",\"line\":0},{\"label\":\"label1\",\"path\":\"path1\",\"line\":1},{\"label\":\"label2\",\"path\":\"path2\",\"line\":2}]},{\"message\":\"my-test-message1\"," +
                "\"type\":\"my-test-type0\",\"stack\":[{\"label\":\"label10\",\"path\":\"path10\",\"line\":0},{\"label\":\"label11\",\"path\":\"path11\",\"line\":11},{\"label\":\"label12\"," +
                "\"path\":\"path12\",\"line\":12}]}],\"paths\":[\"path-0\",\"path-1\",\"path-2\"]}", xrayErrorCauseHeader);
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void reportInvocationSuccessTest() {
        try {
            MockResponse mockResponse = new MockResponse();
            mockResponse.setResponseCode(HTTP_ACCEPTED);
            mockWebServer.enqueue(mockResponse);

            String response = "{\"msg\":\"test\"}";
            lambdaRuntimeApiClientImpl.reportInvocationSuccess(requestId, response.getBytes());
            RecordedRequest recordedRequest = mockWebServer.takeRequest();
            HttpUrl actualUrl = recordedRequest.getRequestUrl();
            String expectedUrl = "http://" + getHostnamePort() + "/2018-06-01/runtime/invocation/1234/response";
            assertEquals(expectedUrl, actualUrl.toString());

            String actualBody = recordedRequest.getBody().readUtf8();
            assertEquals("{\"msg\":\"test\"}", actualBody);
        } catch(Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void restoreNextTest() {
        try {
            MockResponse mockResponse = new MockResponse();
            mockResponse.setResponseCode(HTTP_OK);
            mockWebServer.enqueue(mockResponse);

            lambdaRuntimeApiClientImpl.restoreNext();
            RecordedRequest recordedRequest = mockWebServer.takeRequest();
            HttpUrl actualUrl = recordedRequest.getRequestUrl();
            String expectedUrl = "http://" + getHostnamePort() + "/2018-06-01/runtime/restore/next";
            assertEquals(expectedUrl, actualUrl.toString());

            String actualBody = recordedRequest.getBody().readUtf8();
            assertEquals("", actualBody);
        } catch(Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void restoreNextWrongStatusCodeTest() {
        int errorStatusCode = HTTP_INTERNAL_ERROR;
        try {
            MockResponse mockResponse = new MockResponse();
            mockResponse.setResponseCode(errorStatusCode);
            mockWebServer.enqueue(mockResponse);

            lambdaRuntimeApiClientImpl.restoreNext();
            fail();
        } catch(LambdaRuntimeClientException e) {
            String expectedUrl = "http://" + getHostnamePort() + "/2018-06-01/runtime/restore/next";
            String expectedMessage = expectedUrl + " Response code: '" + errorStatusCode + "'.";
            assertEquals(expectedMessage, e.getLocalizedMessage());
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void nextTest() {
        try {
            MockResponse mockResponse = new MockResponse();
            mockResponse.setResponseCode(HTTP_ACCEPTED);
            mockResponse.setHeader("lambda-runtime-aws-request-id", "1234567890");
            mockResponse.setHeader("Content-Type", "application/json");
            mockWebServer.enqueue(mockResponse);

            lambdaRuntimeApiClientImpl.nextInvocation();
            RecordedRequest recordedRequest = mockWebServer.takeRequest();
            HttpUrl actualUrl = recordedRequest.getRequestUrl();
            String expectedUrl = "http://" + getHostnamePort() + "/2018-06-01/runtime/invocation/next";
            assertEquals(expectedUrl, actualUrl.toString());

            String actualBody = recordedRequest.getBody().readUtf8();
            assertEquals("", actualBody);
        } catch(Exception e) {
            fail();
        }
    }


    @Test
    public void createUrlMalformedTest() {
        RapidErrorType rapidErrorType = RapidErrorType.AfterRestoreError;
        LambdaError lambdaError = new LambdaError(errorRequest, rapidErrorType);
        RuntimeException thrown = assertThrows(RuntimeException.class, ()->{
            lambdaRuntimeApiClientImpl.reportLambdaError("invalidurl", lambdaError, 100);
        });
        assertTrue(thrown.getLocalizedMessage().contains("java.net.MalformedURLException"));
    }

    @Test
    public void lambdaReportErrorXRayHeaderTooLongTest() {
        try {
            RapidErrorType rapidErrorType = RapidErrorType.AfterRestoreError;

            MockResponse mockResponse = new MockResponse();
            mockResponse.setResponseCode(HTTP_ACCEPTED);
            mockWebServer.enqueue(mockResponse);

            String workingDirectory = "my-test-directory";
            List<String> paths = new ArrayList<String>();
            paths.add("path-0");

            List<StackElement> stackElements = new ArrayList<>();
            stackElements.add(new StackElement("label0", "path0", 0));
            XRayException xRayException = new XRayException("my-test-message0", "my-test-type0", stackElements);

            List<XRayException> exceptions = new ArrayList<>();
            exceptions.add(xRayException);

            XRayErrorCause xRayErrorCause = new XRayErrorCause(workingDirectory, exceptions, paths);
            LambdaError lambdaError = new LambdaError(errorRequest, xRayErrorCause, rapidErrorType);
            lambdaRuntimeApiClientImpl.reportLambdaError("http://" + getHostnamePort(), lambdaError, 10);
            RecordedRequest recordedRequest = mockWebServer.takeRequest();

            String xrayErrorCauseHeader = recordedRequest.getHeader("Lambda-Runtime-Function-XRay-Error-Cause");
            assertNull(xrayErrorCauseHeader);
        } catch(Exception e) {
            fail();
        }
    }

    private String getHostnamePort() {
        return mockWebServer.getHostName() + ":" + mockWebServer.getPort();
    }
}