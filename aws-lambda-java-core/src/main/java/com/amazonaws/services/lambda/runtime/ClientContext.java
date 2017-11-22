/* Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime;

import java.util.Map;

/**
 * 
 * Provides information about client configuration and execution environment.
 *
 */
public interface ClientContext {
    /**
     * Gets the client information provided by the AWS Mobile SDK
     * 
     */
    public Client getClient();

    /**
     * Gets custom values set by the client application
     * <p>
     * This map is mutable (and not thread-safe if mutated)
     * </p>
     */
    public Map<String, String> getCustom();
 
    /**
     * Gets environment information provided by mobile SDK, immutable. 
     * 
     */
    public Map<String, String> getEnvironment();

}
