/*
 *  Copyright 2023 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 */

package com.amazonaws.services.lambda.crac;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;

public class ContextImplTest {

    private Resource throwsWithSuppressedException, noop, noop2, throwsException, throwCustomException;

    @BeforeEach
    public void setup() throws Exception {

        throwsWithSuppressedException = Mockito.mock(Resource.class);
        CheckpointException checkpointException = new CheckpointException();
        checkpointException.addSuppressed(new NumberFormatException());

        RestoreException restoreException = new RestoreException();
        restoreException.addSuppressed(new NumberFormatException());

        doThrow(checkpointException).when(throwsWithSuppressedException).beforeCheckpoint(ArgumentMatchers.any());
        doThrow(restoreException).when(throwsWithSuppressedException).afterRestore(ArgumentMatchers.any());

        noop = Mockito.mock(Resource.class);
        Mockito.doNothing().when(noop).beforeCheckpoint(ArgumentMatchers.any());
        Mockito.doNothing().when(noop).afterRestore(ArgumentMatchers.any());

        noop2 = Mockito.mock(Resource.class);
        Mockito.doNothing().when(noop2).beforeCheckpoint(ArgumentMatchers.any());
        Mockito.doNothing().when(noop2).afterRestore(ArgumentMatchers.any());

        throwsException = Mockito.mock(Resource.class);
        doThrow(CheckpointException.class).when(throwsException).beforeCheckpoint(ArgumentMatchers.any());
        doThrow(RestoreException.class).when(throwsException).afterRestore(ArgumentMatchers.any());

        throwCustomException = Mockito.mock(Resource.class);
        doThrow(IndexOutOfBoundsException.class).when(throwCustomException).beforeCheckpoint(ArgumentMatchers.any());
        doThrow(UnsupportedOperationException.class).when(throwCustomException).afterRestore(ArgumentMatchers.any());

        Core.resetGlobalContext();
    }

    static class StatefulResource implements Resource {

        int state = 0;

        @Override
        public void afterRestore(Context<? extends Resource> context) {
            state += 1;
        }

        @Override
        public void beforeCheckpoint(Context<? extends Resource> context) {
            state += 2;
        }

        int getValue() {
            return state;
        }
    }

    static int GLOBAL_STATE;

    static class ChangeGlobalStateResource implements Resource {

        ChangeGlobalStateResource() {
            GLOBAL_STATE = 0;
        }

        @Override
        public void afterRestore(Context<? extends Resource> context) {
            GLOBAL_STATE += 1;
        }

        @Override
        public void beforeCheckpoint(Context<? extends Resource> context) {
            GLOBAL_STATE += 2;
        }
    }

    /**
     * Happy path test with real / not mocked resource
     */
    @Test
    public void verifyHooksWereExecuted() throws CheckpointException, RestoreException {
        StatefulResource resource = new StatefulResource();
        Core.getGlobalContext().register(resource);

        Core.getGlobalContext().beforeCheckpoint(null);
        Core.getGlobalContext().afterRestore(null);

        assertEquals(3, resource.getValue());
    }

    /**
     * This test is to validate GC intervention
     */
    @Test
    public void verifyHooksWereExecutedWithGC() throws CheckpointException, RestoreException {
        StatefulResource resource = new StatefulResource();
        Core.getGlobalContext().register(resource);
        gcAndSleep();

        Core.getGlobalContext().beforeCheckpoint(null);
        Core.getGlobalContext().afterRestore(null);

        assertEquals(3, resource.getValue());
    }

    @Test
    public void verifyHooksAreNotExecutedForGarbageCollectedResources() throws CheckpointException, RestoreException {
        Core.getGlobalContext().register(new ChangeGlobalStateResource());
        gcAndSleep();

        Core.getGlobalContext().beforeCheckpoint(null);
        Core.getGlobalContext().afterRestore(null);


        assertEquals(0, GLOBAL_STATE);
    }

    private static void gcAndSleep() {
        for (int i = 0; i < 10; i++) {
            System.gc();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("thread was interrupted");
            throw new RuntimeException(e);
        }
    }

    @Test
    public void Should_NotifyResourcesInReverseOrderOfRegistration_When_CheckpointNotification() throws Exception {
        // Given
        InOrder checkpointNotificationOrder = Mockito.inOrder(noop, noop2);
        Core.getGlobalContext().register(noop);
        Core.getGlobalContext().register(noop2);

        // When
        Core.getGlobalContext().beforeCheckpoint(null);

        // Then
        checkpointNotificationOrder.verify(noop2).beforeCheckpoint(ArgumentMatchers.any());
        checkpointNotificationOrder.verify(noop).beforeCheckpoint(ArgumentMatchers.any());
    }

    @Test
    public void Should_NotifyResourcesInOrderOfRegistration_When_RestoreNotification() throws Exception {
        // Given
        InOrder restoreNotificationOrder = Mockito.inOrder(noop, noop2);
        Core.getGlobalContext().register(noop);
        Core.getGlobalContext().register(noop2);

        // When
        Core.getGlobalContext().afterRestore(null);

        // Then
        restoreNotificationOrder.verify(noop).afterRestore(ArgumentMatchers.any());
        restoreNotificationOrder.verify(noop2).afterRestore(ArgumentMatchers.any());
    }

    @Test
    public void Should_ResourcesAreAlwaysNotified_When_AnyNotificationThrowsException() throws Exception {

        // Given
        Core.getGlobalContext().register(throwsWithSuppressedException);
        Core.getGlobalContext().register(noop);
        Core.getGlobalContext().register(noop2);
        Core.getGlobalContext().register(throwsException);
        Core.getGlobalContext().register(throwCustomException);

        // When
        try {
            Core.getGlobalContext().beforeCheckpoint(null);
        } catch (Exception ignored) {
        }

        try {
            Core.getGlobalContext().afterRestore(null);
        } catch (Exception ignored) {
        }

        // Then
        Mockito.verify(throwsWithSuppressedException, Mockito.times(1)).beforeCheckpoint(ArgumentMatchers.any());
        Mockito.verify(noop, Mockito.times(1)).beforeCheckpoint(ArgumentMatchers.any());
        Mockito.verify(noop2, Mockito.times(1)).beforeCheckpoint(ArgumentMatchers.any());
        Mockito.verify(throwsException, Mockito.times(1)).beforeCheckpoint(ArgumentMatchers.any());
        Mockito.verify(throwCustomException, Mockito.times(1)).beforeCheckpoint(ArgumentMatchers.any());

        Mockito.verify(throwsWithSuppressedException, Mockito.times(1)).afterRestore(ArgumentMatchers.any());
        Mockito.verify(noop, Mockito.times(1)).afterRestore(ArgumentMatchers.any());
        Mockito.verify(noop2, Mockito.times(1)).afterRestore(ArgumentMatchers.any());
        Mockito.verify(throwsException, Mockito.times(1)).afterRestore(ArgumentMatchers.any());
        Mockito.verify(throwCustomException, Mockito.times(1)).afterRestore(ArgumentMatchers.any());
    }

    @Test
    public void Should_CatchAndSuppressAnyExceptionsAsCheckpointException_When_CheckpointNotification() {
        // Given
        Core.getGlobalContext().register(throwsWithSuppressedException);
        Core.getGlobalContext().register(throwCustomException);

        // When
        try {
            Core.getGlobalContext().beforeCheckpoint(null);
        } catch (CheckpointException e1) {
            // Then
            assertEquals(2, e1.getSuppressed().length);
        } catch (Throwable e2) {
            fail("All exceptions thrown during checkpoint notification should be reported as CheckpointException");
        }
    }

    @Test
    public void Should_CatchAndSuppressAnyExceptionsAsRestoreException_When_RestoreNotification() {
        // Given
        Core.getGlobalContext().register(throwsWithSuppressedException);
        Core.getGlobalContext().register(throwCustomException);

        // When
        try {
            Core.getGlobalContext().afterRestore(null);
        } catch (RestoreException e1) {
            // Then
            assertEquals(2, e1.getSuppressed().length);
        } catch (Exception e2) {
            fail("All exceptions thrown during restore notification should be reported as RestoreException");
        }
    }

    @Test
    public void Should_SuppressOriginalCheckpointExceptionUnderAnotherCheckpointException_When_ResourceIsAContext() throws Exception {
        // Given
        Context<Resource> c0 = Mockito.mock(Context.class);
        doThrow(CheckpointException.class).when(c0).beforeCheckpoint(ArgumentMatchers.any());

        Core.getGlobalContext().register(c0);

        // When
        try {
            Core.getGlobalContext().beforeCheckpoint(null);
        } catch (CheckpointException e1) {
            // Then
            assertEquals(1, e1.getSuppressed().length);
            assertTrue(e1.getSuppressed()[0] instanceof CheckpointException,
                    "When the Resource is a Context and it throws CheckpointException it should be suppressed under another CheckpointException");

        } catch (Exception e2) {
            fail("All exceptions thrown during checkpoint notification should be reported as CheckpointException");
        }
    }

    @Test
    public void Should_SuppressOriginalRestoreExceptionUnderAnotherRestoreException_When_ResourceIsAContext() throws Exception {
        // Given
        Context<Resource> c0 = Mockito.mock(Context.class);
        doThrow(RestoreException.class).when(c0).afterRestore(ArgumentMatchers.any());

        Core.getGlobalContext().register(c0);

        // When
        try {
            Core.getGlobalContext().afterRestore(null);
        } catch (RestoreException e1) {
            // Then
            assertEquals(1, e1.getSuppressed().length);
            assertTrue(e1.getSuppressed()[0] instanceof RestoreException,
                    "When the Resource is a Context and it throws RestoreException it should be suppressed under another RestoreException");
        } catch (Exception e2) {
            fail("All exceptions thrown during restore notification should be reported as RestoreException");
        }
    }

    @Test
    public void Should_NotifyOnlyOnce_When_ResourceRegistersMultipleTimes() throws Exception {
        // Given
        Core.getGlobalContext().register(noop);
        Core.getGlobalContext().register(noop);

        // When
        Core.getGlobalContext().beforeCheckpoint(null);
        Core.getGlobalContext().afterRestore(null);

        // Then
        Mockito.verify(noop, Mockito.times(1)).beforeCheckpoint(ArgumentMatchers.any());
        Mockito.verify(noop, Mockito.times(1)).afterRestore(ArgumentMatchers.any());
    }
}
