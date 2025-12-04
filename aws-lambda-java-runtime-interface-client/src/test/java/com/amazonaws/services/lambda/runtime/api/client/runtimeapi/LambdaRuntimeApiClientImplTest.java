/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.amazonaws.services.lambda.runtime.api.client.logging.LambdaContextLogger;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.ErrorRequest;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.InvocationRequest;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.StackElement;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.XRayErrorCause;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.XRayException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import okhttp3.HttpUrl;
import static java.net.HttpURLConnection.HTTP_ACCEPTED;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import okhttp3.mockwebserver.MockWebServer;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

@DisabledOnOs(OS.MAC)
public class LambdaRuntimeApiClientImplTest {

    @SuppressWarnings("rawtypes")
    private final Supplier mockSupplier = mock(Supplier.class);
    @SuppressWarnings("rawtypes")
    private final Function mockExceptionMessageComposer = mock(Function.class);
    private final LambdaContextLogger mockLambdaContextLogger = mock(LambdaContextLogger.class);
    private final LambdaRuntimeClientMaxRetriesExceededException retriesExceededException = new LambdaRuntimeClientMaxRetriesExceededException("Testing Invocations");
    final String fakeExceptionMessage = "Something bad";

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

    @SuppressWarnings("unchecked")
    @Test
    public void testgetSupplierResultWithExponentialBackoffAllFailing() throws Exception {

        when(mockSupplier.get()).thenThrow(new RuntimeException(new Exception(fakeExceptionMessage)));
        when(mockExceptionMessageComposer.apply(any())).thenReturn(fakeExceptionMessage);

        try {
            LambdaRuntimeApiClientImpl.getSupplierResultWithExponentialBackoff(mockLambdaContextLogger, 5, 200, 5, mockSupplier, mockExceptionMessageComposer, retriesExceededException);
        } catch (LambdaRuntimeClientMaxRetriesExceededException e) { }

        verify(mockSupplier, times(5)).get();
        verify(mockLambdaContextLogger).log(eq(fakeExceptionMessage + "\nRetrying."), any());
        verify(mockLambdaContextLogger).log(eq(fakeExceptionMessage + "\nRetrying in 5 ms."), any());
        verify(mockLambdaContextLogger).log(eq(fakeExceptionMessage + "\nRetrying in 10 ms."), any());
        verify(mockLambdaContextLogger).log(eq(fakeExceptionMessage + "\nRetrying in 20 ms."), any());
        verify(mockLambdaContextLogger).log(eq(fakeExceptionMessage), any());
        verify(mockLambdaContextLogger, times(5)).log(anyString(), any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testgetSupplierResultWithExponentialBackoffTwoFailingThenSuccess() throws Exception {
        InvocationRequest fakeRequest = new InvocationRequest();
        
        when(mockExceptionMessageComposer.apply(any())).thenReturn(fakeExceptionMessage);
        
        when(mockSupplier.get())
        .thenThrow(new RuntimeException(new Exception(fakeExceptionMessage)))
        .thenThrow(new RuntimeException(new Exception(fakeExceptionMessage)))
        .thenReturn(fakeRequest);

        InvocationRequest invocationRequest = (InvocationRequest) LambdaRuntimeApiClientImpl.getSupplierResultWithExponentialBackoff(mockLambdaContextLogger, 5, 200, 5, mockSupplier, mockExceptionMessageComposer, retriesExceededException);

        assertEquals(fakeRequest, invocationRequest);
        verify(mockSupplier, times(3)).get();
        verify(mockLambdaContextLogger).log(eq(fakeExceptionMessage + "\nRetrying."), any());
        verify(mockLambdaContextLogger).log(eq(fakeExceptionMessage + "\nRetrying in 5 ms."), any());
        verify(mockLambdaContextLogger, times(2)).log(anyString(), any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testgetSupplierResultWithExponentialBackoffDoesntGoAboveMax() throws Exception {

        when(mockSupplier.get()).thenThrow(new RuntimeException(new Exception(fakeExceptionMessage)));

        when(mockExceptionMessageComposer.apply(any())).thenReturn(fakeExceptionMessage);

        try {
            LambdaRuntimeApiClientImpl.getSupplierResultWithExponentialBackoff(mockLambdaContextLogger, 100, 200, 5, mockSupplier, mockExceptionMessageComposer, retriesExceededException);
        } catch (LambdaRuntimeClientMaxRetriesExceededException e) { }

        verify(mockSupplier, times(5)).get();
        verify(mockLambdaContextLogger).log(eq(fakeExceptionMessage + "\nRetrying."), any());
        verify(mockLambdaContextLogger).log(eq(fakeExceptionMessage + "\nRetrying in 100 ms."), any());
        verify(mockLambdaContextLogger, times(2)).log(eq(fakeExceptionMessage + "\nRetrying in 200 ms."), any());
        verify(mockLambdaContextLogger).log(eq(fakeExceptionMessage), any());
        verify(mockLambdaContextLogger, times(5)).log(anyString(), any());
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
    public void nextWithoutTenantIdHeaderTest() {
        try {
            MockResponse mockResponse = buildMockResponseForNextInvocation();
            mockWebServer.enqueue(mockResponse);

            InvocationRequest invocationRequest = lambdaRuntimeApiClientImpl.nextInvocation();
            verifyNextInvocationRequest();
            assertNull(invocationRequest.getTenantId());
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void nextWithTenantIdHeaderTest() {
        try {
            MockResponse mockResponse = buildMockResponseForNextInvocation();
            String expectedTenantId = "my-tenant-id";
            mockResponse.setHeader("lambda-runtime-aws-tenant-id", expectedTenantId);
            mockWebServer.enqueue(mockResponse);

            InvocationRequest invocationRequest = lambdaRuntimeApiClientImpl.nextInvocation();
            verifyNextInvocationRequest();
            assertEquals(expectedTenantId, invocationRequest.getTenantId());

        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void nextWithEmptyTenantIdHeaderTest() {
        try {
            MockResponse mockResponse = buildMockResponseForNextInvocation();
            mockResponse.setHeader("lambda-runtime-aws-tenant-id", "");
            mockWebServer.enqueue(mockResponse);
    
            InvocationRequest invocationRequest = lambdaRuntimeApiClientImpl.nextInvocation();
            verifyNextInvocationRequest();
            assertNull(invocationRequest.getTenantId());
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void nextWithNullTenantIdHeaderTest() {
        try {
            MockResponse mockResponse = buildMockResponseForNextInvocation();
            assertThrows(NullPointerException.class, () -> {
                mockResponse.setHeader("lambda-runtime-aws-tenant-id", null);
            });
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

    private MockResponse buildMockResponseForNextInvocation() {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(HTTP_ACCEPTED);
        mockResponse.setHeader("lambda-runtime-aws-request-id", "1234567890");
        mockResponse.setHeader("Content-Type", "application/json");
        return mockResponse;
    }

    private void verifyNextInvocationRequest() throws Exception {
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        HttpUrl actualUrl = recordedRequest.getRequestUrl();
        String expectedUrl = "http://" + getHostnamePort() + "/2018-06-01/runtime/invocation/next";
        assertEquals(expectedUrl, actualUrl.toString());

        String actualBody = recordedRequest.getBody().readUtf8();
        assertEquals("", actualBody);
    }

    private String getHostnamePort() {
        return mockWebServer.getHostName() + ":" + mockWebServer.getPort();
    }
}