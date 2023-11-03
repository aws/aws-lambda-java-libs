/* Copyright 2023 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.log4j2;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.io.Serializable;

@Plugin(name = "LambdaJsonFormat", category = "core", printObject = true)
public class LambdaJsonFormat {

    private Layout layout;

    @PluginFactory
    public static LambdaJsonFormat createNode(@PluginElement("Layout") Layout<? extends Serializable> layout) {
        return new LambdaJsonFormat(layout);
    }

    private LambdaJsonFormat(Layout layout) {
        this.layout = layout;
    }

    public Layout getLayout() {
        return layout;
    }
}
