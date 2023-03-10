/*
 *  Copyright 2023 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 */

package com.amazonaws.services.lambda.crac;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;


/**
 * Spec reference: https://crac.github.io/openjdk-builds/javadoc/api/java.base/jdk/crac/package-summary.html
 */

public class ContextImpl extends Context<Resource> {

    private volatile long order = -1L;
    private final WeakHashMap<Resource, Long> checkpointQueue = new WeakHashMap<>();

    @Override
    public synchronized void beforeCheckpoint(Context<? extends Resource> context) throws CheckpointException {

        List<Throwable> exceptionsThrown = new ArrayList<>();
        for (Resource resource : getCheckpointQueueReverseOrderOfRegistration()) {
            try {
                resource.beforeCheckpoint(this);
            } catch (CheckpointException e) {
                Collections.addAll(exceptionsThrown, e.getSuppressed());
            } catch (Exception e) {
                exceptionsThrown.add(e);
            }
        }

        if (!exceptionsThrown.isEmpty()) {
            CheckpointException checkpointException = new CheckpointException();
            for (Throwable t : exceptionsThrown) {
                checkpointException.addSuppressed(t);
            }
            throw checkpointException;
        }
    }

    @Override
    public synchronized void afterRestore(Context<? extends Resource> context) throws RestoreException {

        List<Throwable> exceptionsThrown = new ArrayList<>();
        for (Resource resource : getCheckpointQueueForwardOrderOfRegistration()) {
            try {
                resource.afterRestore(this);
            } catch (RestoreException e) {
                Collections.addAll(exceptionsThrown, e.getSuppressed());
            } catch (Exception e) {
                exceptionsThrown.add(e);
            }
        }

        if (!exceptionsThrown.isEmpty()) {
            RestoreException restoreException = new RestoreException();
            for (Throwable t : exceptionsThrown) {
                restoreException.addSuppressed(t);
            }
            throw restoreException;
        }
    }

    @Override
    public synchronized void register(Resource resource) {
        checkpointQueue.put(resource, ++order);
    }

    private List<Resource> getCheckpointQueueReverseOrderOfRegistration() {
        return checkpointQueue.entrySet()
                .stream()
                .sorted((r1, r2) -> (int) (r2.getValue() - r1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private List<Resource> getCheckpointQueueForwardOrderOfRegistration() {
        return checkpointQueue.entrySet()
                .stream()
                .sorted((r1, r2) -> (int) (r1.getValue() - r2.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
