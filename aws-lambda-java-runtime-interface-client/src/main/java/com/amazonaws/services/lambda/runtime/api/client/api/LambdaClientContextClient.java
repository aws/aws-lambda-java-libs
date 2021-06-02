/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.api;

import com.amazonaws.services.lambda.runtime.Client;

public class LambdaClientContextClient implements Client {

    private String installation_id;

    private String app_title;

    private String app_version_name;

    private String app_version_code;

    private String app_package_name;

    public String getInstallationId() {
        return installation_id;
    }

    public String getAppTitle() {
        return app_title;
    }

    public String getAppVersionName() {
        return app_version_name;
    }

    public String getAppVersionCode() {
        return app_version_code;
    }

    public String getAppPackageName() {
        return app_package_name;
    }
}
