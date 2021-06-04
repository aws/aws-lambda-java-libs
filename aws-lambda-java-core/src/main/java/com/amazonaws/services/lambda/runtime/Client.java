/* Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime;

/**
 * Contains information about the client application that invoked the Lambda function. 
 *
 */
public interface Client {

    /**
    * Gets the application's installation id
    */
    String getInstallationId();

    /**
     * Gets the application's title
     *  
     */
    String getAppTitle();

    /**
     * Gets the application's version
     *  
     */
    String getAppVersionName();

    /**
     * Gets the application's version code
     *  
     */
    String getAppVersionCode();

    /**
     * Gets the application's package name
     */
    String getAppPackageName();
}
