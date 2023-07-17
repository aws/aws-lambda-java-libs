package com.amazonaws.services.lambda.runtime.api.client;

import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.InvocationRequest;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}