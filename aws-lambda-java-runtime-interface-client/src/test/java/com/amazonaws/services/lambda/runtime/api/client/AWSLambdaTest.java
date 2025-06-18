/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.LambdaRuntimeApiClientImpl;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.LambdaRuntimeClientMaxRetriesExceededException;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.InvocationRequest;
import com.amazonaws.services.lambda.runtime.api.client.util.ConcurrencyConfig;
import com.amazonaws.services.lambda.runtime.api.client.util.EnvReader;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.api.client.logging.LambdaContextLogger;
import com.amazonaws.services.lambda.runtime.logging.LogFormat;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AWSLambdaTest {

    private static class SampleHandler implements RequestHandler<Map<String, String>, String> {
        public static final String ADD_ENTRY_TO_MAP_ID_OP_MODE = "ADD_ENTRY_TO_MAP_ID";
        public static final String FAIL_IMMEDIATELY_OP_MODE = "FAIL_IMMEDIATELY";

        public static final int nOfIterations = 10;
        public static final int perIterationDelayMS = 10;
        public static Map<String, Integer> hashMap = new ConcurrentHashMap<String, Integer>();
        public static AtomicInteger globalCounter = new AtomicInteger();

        public static void resetStaticFields() {
            hashMap.clear();
            globalCounter = new AtomicInteger();
        }

        private static void addEntryToMapImplementation(String name) {
            int i = 0;
            while (i++ < nOfIterations) {
                hashMap.put(name, hashMap.getOrDefault(name, 0) + 1);
                globalCounter.incrementAndGet();
                try {
                    Thread.sleep(perIterationDelayMS);
                } catch (InterruptedException e) {
                }
            }
        }

        @Override
        public String handleRequest(Map<String, String> event, Context context) {
            // Thread.currentThread().getId() instead of Thread.currentThread().getName() when upgrading JAVA
            String name = "Thread " + Thread.currentThread().getName();
            String opMode = event.get("id");

            switch (opMode) {
                case ADD_ENTRY_TO_MAP_ID_OP_MODE:
                    addEntryToMapImplementation(name);
                    break;
                case FAIL_IMMEDIATELY_OP_MODE:
                    String[] sArr = {};
                    return sArr[1];
                default:
                    break;
            }

            return name;
        }
    }

    @Mock
    private LambdaRuntimeApiClientImpl runtimeClient;

    @Mock
    private LambdaContextLogger lambdaLogger;
 
    @Mock
    private EnvReader envReader;
    
    @Mock
    private ConcurrencyConfig concurrencyConfig;

    private LambdaRequestHandler lambdaRequestHandler = new LambdaRequestHandler() {
        private SampleHandler sHandler = new SampleHandler();

        @Override
        public ByteArrayOutputStream call(InvocationRequest request) throws Error, Exception {
            HashMap<String, String> eventMap = new HashMap<String,String>();
            eventMap.put("id", request.getId());
            sHandler.handleRequest(eventMap, null);
            return new ByteArrayOutputStream();
        }
    };

    private static InvocationRequest getFakeInvocationRequest(String id) {
        InvocationRequest request = new InvocationRequest();
        request.setId(id);
        request.setDeadlineTimeInMs(Long.MAX_VALUE);
        request.setContent("".getBytes());
        return request;
    }

    private static final LambdaRuntimeClientMaxRetriesExceededException fakelambdaRuntimeClientMaxRetriesExceededException = new LambdaRuntimeClientMaxRetriesExceededException("Fake max retries happened");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SampleHandler.resetStaticFields();
    }

    /*
     * com.amazonaws.services.lambda.runtime.api.client.util.SampleHandler contains static fields. These fields are expected to be shared if initialization is behaving as expected.
     * After execution of the Runtime loops, we should see that the SampleHandler.globalCounter has been acted on by all the threads.
     * The concurrent hashmap in SampleHandler.hashMap should also have all the correct count of Threads that ran.
     * IMPORTANT: This test fails through only timeout.
     */
    @Test
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    void testConcurrentRunWithPlatformThreads() throws Throwable {
        when(concurrencyConfig.isMultiConcurrent()).thenReturn(true);
        when(concurrencyConfig.getNumberOfPlatformThreads()).thenReturn(4);

        InvocationRequest successfullInvocationRequest = getFakeInvocationRequest(SampleHandler.ADD_ENTRY_TO_MAP_ID_OP_MODE);

        when(runtimeClient.nextInvocationWithExponentialBackoff(lambdaLogger))
        .thenReturn(successfullInvocationRequest)
        .thenReturn(successfullInvocationRequest)
        .thenReturn(successfullInvocationRequest)
        .thenReturn(successfullInvocationRequest)
        .thenReturn(successfullInvocationRequest)
        .thenReturn(successfullInvocationRequest)
        .thenReturn(successfullInvocationRequest)
        .thenThrow(fakelambdaRuntimeClientMaxRetriesExceededException)
        .thenThrow(fakelambdaRuntimeClientMaxRetriesExceededException)
        .thenThrow(fakelambdaRuntimeClientMaxRetriesExceededException)
        .thenThrow(fakelambdaRuntimeClientMaxRetriesExceededException);

        AWSLambda.startRuntimeLoops(lambdaRequestHandler, lambdaLogger, concurrencyConfig, runtimeClient);

        // Success Reports Must Equal number of tasks that ran successfully.
        verify(runtimeClient, times(7)).reportInvocationSuccess(eq(SampleHandler.ADD_ENTRY_TO_MAP_ID_OP_MODE), any());
        // Hashmap keys should equal the number of threads (runtime loops).
        assertEquals(4, SampleHandler.hashMap.size());
        // Hashmap total count should equal all tasks that ran * number of iterations per task
        assertEquals(7 * SampleHandler.nOfIterations, SampleHandler.globalCounter.get());
    }
 
    @Test
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    void testConcurrentRunWithPlatformThreadsWithFailures() throws Throwable {
        when(lambdaLogger.getLogFormat()).thenReturn(LogFormat.JSON);
        when(concurrencyConfig.isMultiConcurrent()).thenReturn(true);
        when(concurrencyConfig.getNumberOfPlatformThreads()).thenReturn(4);

        InvocationRequest successfullInvocationRequest = getFakeInvocationRequest(SampleHandler.ADD_ENTRY_TO_MAP_ID_OP_MODE);
        InvocationRequest failImmediatelyRequest = getFakeInvocationRequest(SampleHandler.FAIL_IMMEDIATELY_OP_MODE);
        InvocationRequest userFaultRequest = mock(InvocationRequest.class);
        final String UserFaultID = "Injected Fault Request ID";
        when(userFaultRequest.getId()).thenThrow(UserFault.makeUserFault(new Exception("OH NO"), true)).thenReturn(UserFaultID);

        when(runtimeClient.nextInvocationWithExponentialBackoff(lambdaLogger))
        .thenReturn(failImmediatelyRequest)
        .thenReturn(userFaultRequest)
        .thenReturn(successfullInvocationRequest)
        .thenReturn(successfullInvocationRequest)
        .thenThrow(fakelambdaRuntimeClientMaxRetriesExceededException)
        .thenThrow(fakelambdaRuntimeClientMaxRetriesExceededException)
        .thenThrow(fakelambdaRuntimeClientMaxRetriesExceededException)
        .thenThrow(fakelambdaRuntimeClientMaxRetriesExceededException);

        AWSLambda.startRuntimeLoops(lambdaRequestHandler, lambdaLogger, concurrencyConfig, runtimeClient);

        // One for each of failImmediatelyRequest and userFaultRequest in finally block 
        // Four for crashing the Four runtime loops in the outermost catch of the runtime loop after the Null responses.
        // 2 + 4 = 6
        verify(lambdaLogger, times(6)).log(anyString(), eq(LogLevel.ERROR));

        // Failed invokes should be reported.
        verify(runtimeClient).reportInvocationError(eq(SampleHandler.FAIL_IMMEDIATELY_OP_MODE), any());
        verify(runtimeClient).reportInvocationError(eq(UserFaultID), any());
        
        // Success Reports Must Equal number of tasks that ran successfully.
        verify(runtimeClient, times(2)).reportInvocationSuccess(eq(SampleHandler.ADD_ENTRY_TO_MAP_ID_OP_MODE), any());
        
        // Hashmap keys should equal the minumum between(number of threads (runtime loops) AND number of tasks that ran successfully).
        assertEquals(2, SampleHandler.hashMap.size());
        
        // Hashmap total count should equal all tasks that ran * number of iterations per task
        assertEquals(2 * SampleHandler.nOfIterations, SampleHandler.globalCounter.get());
    }

    @Test
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    void testConcurrentModeLoopDoesNotExitExceptForLambdaRuntimeClientMaxRetriesExceededException() throws Throwable {
        when(lambdaLogger.getLogFormat()).thenReturn(LogFormat.JSON);
        when(concurrencyConfig.isMultiConcurrent()).thenReturn(true);
        when(concurrencyConfig.getNumberOfPlatformThreads()).thenReturn(1);

        InvocationRequest successfullInvocationRequest = getFakeInvocationRequest(SampleHandler.ADD_ENTRY_TO_MAP_ID_OP_MODE);
        InvocationRequest failImmediatelyRequest = getFakeInvocationRequest(SampleHandler.FAIL_IMMEDIATELY_OP_MODE);

        InvocationRequest userFaultRequest = mock(InvocationRequest.class); // unrecoverable in sequential but recoverable in multiconcurrent mode.
        final String UserFaultID = "Injected Fault Request ID";
        when(userFaultRequest.getId()).thenThrow(UserFault.makeUserFault(new Exception("OH NO"), true)).thenReturn(UserFaultID);
        
        InvocationRequest virtualMachineErrorRequest = mock(InvocationRequest.class); // unrecoverable in sequential but recoverable in multiconcurrent mode.
        final String IOErrorID = "ioerr1";
        when(virtualMachineErrorRequest.getId()).thenThrow(UserFault.makeUserFault(new IOError(new Throwable()), true)).thenReturn(IOErrorID);

        when(runtimeClient.nextInvocationWithExponentialBackoff(lambdaLogger))
        .thenReturn(failImmediatelyRequest)
        .thenReturn(userFaultRequest)
        .thenReturn(virtualMachineErrorRequest)
        .thenReturn(successfullInvocationRequest)
        .thenReturn(successfullInvocationRequest)
        .thenThrow(fakelambdaRuntimeClientMaxRetriesExceededException)
        .thenReturn(successfullInvocationRequest);

        AWSLambda.startRuntimeLoops(lambdaRequestHandler, lambdaLogger, concurrencyConfig, runtimeClient);
         
         // One for each of failImmediatelyRequest, userFaultRequest, and virtualMachineErrorRequest + One for the runtime loop thread crashing.
         verify(lambdaLogger, times(4)).log(anyString(), eq(LogLevel.ERROR));
         
         // Failed invokes should be reported.
         verify(runtimeClient).reportInvocationError(eq(SampleHandler.FAIL_IMMEDIATELY_OP_MODE), any());
         verify(runtimeClient).reportInvocationError(eq(UserFaultID), any());
         verify(runtimeClient).reportInvocationError(eq(IOErrorID), any());
         
         // Success Reports Must Equal number of tasks that ran successfully.
         verify(runtimeClient, times(2)).reportInvocationSuccess(eq(SampleHandler.ADD_ENTRY_TO_MAP_ID_OP_MODE), any());
         
         // Hashmap keys should equal the minumum between(number of threads (runtime loops) AND number of tasks that ran successfully).
         assertEquals(1, SampleHandler.hashMap.size());
         
         // Hashmap total count should equal all tasks that ran * number of iterations per task
         assertEquals(2 * SampleHandler.nOfIterations, SampleHandler.globalCounter.get());
    }
        

    /*
     * 
     * NON-CONCURRENT-MODE TESTS
     * 
     */
    
    @Test
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    void testSequentialWithFatalUserFaultErrorStopsLoop() throws Throwable {
        when(lambdaLogger.getLogFormat()).thenReturn(LogFormat.JSON);
        when(concurrencyConfig.isMultiConcurrent()).thenReturn(false);

        InvocationRequest successfullInvocationRequest = getFakeInvocationRequest(SampleHandler.ADD_ENTRY_TO_MAP_ID_OP_MODE);
        InvocationRequest failImmediatelyRequest = getFakeInvocationRequest(SampleHandler.FAIL_IMMEDIATELY_OP_MODE); // recoverable error in all modes.
        
        InvocationRequest userFaultRequest = mock(InvocationRequest.class); // unrecoverable in sequential but recoverable in multiconcurrent mode.
        final String UserFaultID = "Injected Fault Request ID";
        when(userFaultRequest.getId()).thenThrow(UserFault.makeUserFault(new Exception("OH NO"), true)).thenReturn(UserFaultID);
        
        InvocationRequest virtualMachineErrorRequest = mock(InvocationRequest.class); // unrecoverable in sequential but recoverable in multiconcurrent mode.
        final String IOErrorID = "ioerr1";
        when(virtualMachineErrorRequest.getId()).thenThrow(UserFault.makeUserFault(new IOError(new Throwable()), true)).thenReturn(IOErrorID);

        when(runtimeClient.nextInvocation())
        .thenReturn(successfullInvocationRequest)
        .thenReturn(successfullInvocationRequest)
        .thenReturn(failImmediatelyRequest)
        .thenReturn(userFaultRequest)
        // these two should not be even feltched since userFaultRequest is not recoverable.
        .thenReturn(successfullInvocationRequest)
        .thenReturn(virtualMachineErrorRequest);

        AWSLambda.startRuntimeLoops(lambdaRequestHandler, lambdaLogger, concurrencyConfig, runtimeClient);

        // One for failImmediatelyRequest and userFaultRequest in finally block.
        verify(lambdaLogger, times(2)).log(anyString(), eq(LogLevel.ERROR));
        
        // Failed invokes should be reported.
        verify(runtimeClient).reportInvocationError(eq(SampleHandler.FAIL_IMMEDIATELY_OP_MODE), any());
        verify(runtimeClient).reportInvocationError(eq(UserFaultID), any());
        
        // Success Reports Must Equal number of tasks that ran successfully. And only 2 Error reports for failImmediatelyRequest and userFaultRequest.
        verify(runtimeClient, times(2)).reportInvocationSuccess(eq(SampleHandler.ADD_ENTRY_TO_MAP_ID_OP_MODE), any());
        verify(runtimeClient, times(2)).reportInvocationError(any(), any());
        
        // Hashmap keys should equal one as it is not multithreaded.
        assertEquals(1, SampleHandler.hashMap.size());
        
        // Hashmap total count should equal all tasks that ran * number of iterations per task
        assertEquals(2 * SampleHandler.nOfIterations, SampleHandler.globalCounter.get());
    }

    @Test
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    void testSequentialWithVirtualMachineErrorStopsLoop() throws Throwable {
        when(lambdaLogger.getLogFormat()).thenReturn(LogFormat.JSON);
        when(concurrencyConfig.isMultiConcurrent()).thenReturn(false);

        InvocationRequest successfullInvocationRequest = getFakeInvocationRequest(SampleHandler.ADD_ENTRY_TO_MAP_ID_OP_MODE);
        InvocationRequest failImmediatelyRequest = getFakeInvocationRequest(SampleHandler.FAIL_IMMEDIATELY_OP_MODE); // recoverable error in all modes.
        
        InvocationRequest userFaultRequest = mock(InvocationRequest.class); // unrecoverable in sequential but recoverable in multiconcurrent mode.
        final String UserFaultID = "Injected Fault Request ID";
        when(userFaultRequest.getId()).thenThrow(UserFault.makeUserFault(new Exception("OH NO"), true)).thenReturn(UserFaultID);
        
        InvocationRequest virtualMachineErrorRequest = mock(InvocationRequest.class); // unrecoverable in sequential but recoverable in multiconcurrent mode.
        final String IOErrorID = "ioerr1";
        when(virtualMachineErrorRequest.getId()).thenThrow(UserFault.makeUserFault(new IOError(new Throwable()), true)).thenReturn(IOErrorID);

        when(runtimeClient.nextInvocation())
        .thenReturn(successfullInvocationRequest)
        .thenReturn(successfullInvocationRequest)
        .thenReturn(failImmediatelyRequest)
        .thenReturn(virtualMachineErrorRequest)
        // these two should not be even feltched since userFaultRequest is not recoverable.
        .thenReturn(successfullInvocationRequest)
        .thenReturn(userFaultRequest);

        AWSLambda.startRuntimeLoops(lambdaRequestHandler, lambdaLogger, concurrencyConfig, runtimeClient);

        // One for failImmediatelyRequest and userFaultRequest in finally block.
        verify(lambdaLogger, times(2)).log(anyString(), eq(LogLevel.ERROR));
        
        // Failed invokes should be reported.
        verify(runtimeClient).reportInvocationError(eq(SampleHandler.FAIL_IMMEDIATELY_OP_MODE), any());
        verify(runtimeClient).reportInvocationError(eq(IOErrorID), any());
        
        // Success Reports Must Equal number of tasks that ran successfully. And only 2 Error reports for failImmediatelyRequest and virtualMachineErrorRequest.
        verify(runtimeClient, times(2)).reportInvocationSuccess(eq(SampleHandler.ADD_ENTRY_TO_MAP_ID_OP_MODE), any());
        verify(runtimeClient, times(2)).reportInvocationError(any(), any());
        
        // Hashmap keys should equal one as it is not multithreaded.
        assertEquals(1, SampleHandler.hashMap.size());
        
        // Hashmap total count should equal all tasks that ran * number of iterations per task
        assertEquals(2 * SampleHandler.nOfIterations, SampleHandler.globalCounter.get());
    }
}