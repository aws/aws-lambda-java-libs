// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.services.lambda.extension;

import one.profiler.AsyncProfiler;

public class ShutdownHook implements Runnable {

    private String stopCommand;

    public ShutdownHook(String stopCommand) {
        this.stopCommand = stopCommand;
    }

    @Override
    public void run() {
        Logger.debug("running ShutdownHook");
        try {
            final String fileName = "/tmp/profiling-data-shutdown.html";
            Logger.debug("stopping the profiler");
            AsyncProfiler.getInstance().execute(String.format(this.stopCommand, fileName));
        } catch (Exception e) {
            Logger.error("could not stop the profiler");
        }
    }
}