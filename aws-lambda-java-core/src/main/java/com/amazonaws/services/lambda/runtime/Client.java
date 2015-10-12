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
    public String getInstallationId();

    /**
     * Gets the application's title
     *  
     */
    public String getAppTitle();

    /**
     * Gets the application's version
     *  
     */
    public String getAppVersionName();

    /**
     * Gets the application's version code
     *  
     */
    public String getAppVersionCode();

    /**
     * Gets the application's package name
     */
    public String getAppPackageName();
}
