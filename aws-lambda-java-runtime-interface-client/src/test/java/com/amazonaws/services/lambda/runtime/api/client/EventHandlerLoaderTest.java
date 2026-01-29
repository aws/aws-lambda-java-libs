package com.amazonaws.services.lambda.runtime.api.client;

import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.InvocationRequest;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventHandlerLoaderTest {

    @Test
    void RequestHandlerTest() throws Exception {
        String handler = "test.lambda.handlers.RequestHandlerImpl";
        LambdaRequestHandler lambdaRequestHandler = getLambdaRequestHandler(handler);
        assertSuccessfulInvocation(lambdaRequestHandler);
    }

    @Test
    void RequestStreamHandlerTest() throws Exception {
        String handler = "test.lambda.handlers.RequestStreamHandlerImpl";
        LambdaRequestHandler lambdaRequestHandler = getLambdaRequestHandler(handler);
        assertSuccessfulInvocation(lambdaRequestHandler);
    }

    @Test
    void PojoHandlerTest_noParams() throws Exception {
        String handler = "test.lambda.handlers.POJOHanlderImpl::noParamsHandler";
        LambdaRequestHandler lambdaRequestHandler = getLambdaRequestHandler(handler);
        assertSuccessfulInvocation(lambdaRequestHandler);
    }

    @Test
    void PojoHandlerTest_oneParamEvent() throws Exception {
        String handler = "test.lambda.handlers.POJOHanlderImpl::oneParamHandler_event";
        LambdaRequestHandler lambdaRequestHandler = getLambdaRequestHandler(handler);
        assertSuccessfulInvocation(lambdaRequestHandler);
    }

    @Test
    void PojoHandlerTest_oneParamContext() throws Exception {
        String handler = "test.lambda.handlers.POJOHanlderImpl::oneParamHandler_context";
        LambdaRequestHandler lambdaRequestHandler = getLambdaRequestHandler(handler);
        assertSuccessfulInvocation(lambdaRequestHandler);
    }

    @Test
    void PojoHandlerTest_twoParams() throws Exception {
        String handler = "test.lambda.handlers.POJOHanlderImpl::twoParamsHandler";
        LambdaRequestHandler lambdaRequestHandler = getLambdaRequestHandler(handler);
        assertSuccessfulInvocation(lambdaRequestHandler);
    }

    private LambdaRequestHandler getLambdaRequestHandler(String handler) throws ClassNotFoundException {
        ClassLoader cl = this.getClass().getClassLoader();
        HandlerInfo handlerInfo = HandlerInfo.fromString(handler, cl);
        return EventHandlerLoader.loadEventHandler(handlerInfo);
    }

    private static void assertSuccessfulInvocation(LambdaRequestHandler lambdaRequestHandler) throws Exception {
        InvocationRequest invocationRequest = getTestInvocationRequest();

        ByteArrayOutputStream resultBytes = lambdaRequestHandler.call(invocationRequest);
        String result = resultBytes.toString();

        assertEquals("\"success\"", result);
    }

    private static InvocationRequest getTestInvocationRequest() {
        InvocationRequest invocationRequest = new InvocationRequest();
        invocationRequest.setContent("\"Hello\"".getBytes());
        invocationRequest.setId("id");
        invocationRequest.setXrayTraceId("traceId");
        return invocationRequest;
    }

    // Multithreaded test methods

    @Test
    void RequestHandlerTest_Multithreaded() throws Exception {
        testHandlerConcurrency("test.lambda.handlers.RequestHandlerImpl");
    }

    @Test
    void RequestStreamHandlerTest_Multithreaded() throws Exception {
        testHandlerConcurrency("test.lambda.handlers.RequestStreamHandlerImpl");
    }

    @Test
    void PojoHandlerTest_noParams_Multithreaded() throws Exception {
        testHandlerConcurrency("test.lambda.handlers.POJOHanlderImpl::noParamsHandler");
    }

    @Test
    void PojoHandlerTest_oneParamEvent_Multithreaded() throws Exception {
        testHandlerConcurrency("test.lambda.handlers.POJOHanlderImpl::oneParamHandler_event");
    }

    @Test
    void PojoHandlerTest_oneParamContext_Multithreaded() throws Exception {
        testHandlerConcurrency("test.lambda.handlers.POJOHanlderImpl::oneParamHandler_context");
    }

    @Test
    void PojoHandlerTest_twoParams_Multithreaded() throws Exception {
        testHandlerConcurrency("test.lambda.handlers.POJOHanlderImpl::twoParamsHandler");
    }

    private void testHandlerConcurrency(String handlerName) throws Exception {
        // Create one handler instance
        LambdaRequestHandler handler = getLambdaRequestHandler(handlerName);
        
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<String>> futures = new ArrayList<>();
        CountDownLatch startLatch = new CountDownLatch(1);
        
        try {
            for (int i = 0; i < threadCount; i++) {
                futures.add(executor.submit(() -> {
                    try {
                        InvocationRequest request = getTestInvocationRequest();
                        startLatch.await();
                        ByteArrayOutputStream result = handler.call(request);
                        return result.toString();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }));
            }
            
            // Release all threads simultaneously and Verify all invocations return the expected result
            startLatch.countDown();
            
            for (Future<String> future : futures) {
                String result = future.get(5, TimeUnit.SECONDS);
                assertEquals("\"success\"", result);
            }
        } finally {
            executor.shutdown();
            assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
        }
    }
}
