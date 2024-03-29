/* Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime;

import com.amazonaws.services.lambda.runtime.logging.LogLevel;

/**
 * A low level Lambda runtime logger
 *
 */
public interface LambdaLogger {

    /**
    * Logs a string to AWS CloudWatch Logs
    *
    * <p>
    * Logging will not be done:
    * <ul>
    * <li>
    * If the container is not configured to log to CloudWatch.
    * </li>
    * <li>
    * If the role provided to the function does not have sufficient permissions.
    * </li>
    * </ul>
    * </p>
    *
    * @param message A string containing the event to log.
    */
    void log(String message);

    /**
     * Logs a byte array to AWS CloudWatch Logs
     * @param message byte array containing logs
     */
    void log(byte[] message);

    /**
     * LogLevel aware logging backend function.
     *
     * @param message in String format
     * @param logLevel
     */
    default void log(String message, LogLevel logLevel) {
        log(message);
    }

    /**
     * LogLevel aware logging backend function.
     *
     * @param message in byte[] format
     * @param logLevel
     */
    default void log(byte[] message, LogLevel logLevel) {
        log(message);
    }


}

