package com.amazonaws.services.lambda.runtime.log4j2;

import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import com.amazonaws.services.lambda.runtime.LambdaRuntimeInternal;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;

import java.io.Serializable;

/**
 * Class to append log4j2 logs from AWS Lambda function to CloudWatch
 * Created by adsuresh on 6/9/17.
 */
@Plugin(name = LambdaAppender.PLUGIN_NAME, category = LambdaAppender.PLUGIN_CATEGORY,
        elementType = LambdaAppender.PLUGIN_TYPE, printObject = true)
public class LambdaAppender extends AbstractAppender {

    public static final String PLUGIN_NAME = "Lambda";
    public static final String PLUGIN_CATEGORY = "Core";
    public static final String PLUGIN_TYPE = "appender";

    private LambdaLogger logger = LambdaRuntime.getLogger();

    /**
     * Builder class that follows log4j2 plugin convention
     * @param <B> Generic Builder class
     */
    public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B>
            implements org.apache.logging.log4j.core.util.Builder<LambdaAppender> {

        /**
         * creates a new LambdaAppender
         * @return a new LambdaAppender
         */
        public LambdaAppender build() {
            return new LambdaAppender(super.getName(), super.getFilter(), super.getOrCreateLayout(),
                    super.isIgnoreExceptions());
        }
    }

    /**
     * Method used by log4j2 to access this appender
     * @param <B> Generic Builder class
     * @return LambdaAppender Builder
     */
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }

    /**
     * Constructor method following AbstractAppender convention
     * @param name name of appender
     * @param filter filter specified in xml
     * @param layout layout specified in xml
     * @param ignoreExceptions whether to show exceptions or not specified in xml
     */
    private LambdaAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        LambdaRuntimeInternal.setUseLog4jAppender(true);
    }

    /**
     * Append log event to System.out
     * @param event log4j event
     */
    public void append(LogEvent event) {
        logger.log(super.getLayout().toByteArray(event));
    }
}
