package com.amazonaws.services.lambda.runtime.log4j2;

import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import com.amazonaws.services.lambda.runtime.LambdaRuntimeInternal;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.logging.LogFormat;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to append log4j2 logs from AWS Lambda function to CloudWatch
 * Created by adsuresh on 6/9/17.
 */
@Plugin(name = LambdaAppender.PLUGIN_NAME, category = LambdaAppender.PLUGIN_CATEGORY,
        elementType = LambdaAppender.PLUGIN_TYPE, printObject = true)
public class LambdaAppender extends AbstractAppender {
    static {
        LambdaRuntimeInternal.setUseLog4jAppender(true);
    }

    public static final String PLUGIN_NAME = "Lambda";
    public static final String PLUGIN_CATEGORY = "Core";
    public static final String PLUGIN_TYPE = "appender";

    private LambdaLogger logger = LambdaRuntime.getLogger();

    private static LogFormat logFormat = LogFormat.TEXT;

    private static final Map<Level, LogLevel> logLevelMapper = new HashMap<Level, LogLevel>() {{
        put(Level.TRACE, LogLevel.TRACE);
        put(Level.DEBUG, LogLevel.DEBUG);
        put(Level.INFO, LogLevel.INFO);
        put(Level.WARN, LogLevel.WARN);
        put(Level.ERROR, LogLevel.ERROR);
        put(Level.FATAL, LogLevel.FATAL);
    }};

    /**
     * Builder class that follows log4j2 plugin convention
     * @param <B> Generic Builder class
     */
    public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B>
            implements org.apache.logging.log4j.core.util.Builder<LambdaAppender> {

        @PluginAttribute(value = "format", defaultString = "TEXT")
        LogFormat logFormat;
        @PluginElement("LambdaTextFormat")
        private LambdaTextFormat lambdaTextFormat;
        @PluginElement("LambdaJsonFormat")
        private LambdaJsonFormat lambdaJsonFormat;

        /**
         * creates a new LambdaAppender
         * @return a new LambdaAppender
         */
        public LambdaAppender build() {
            Layout<?> layout;
            if (logFormat == LogFormat.TEXT) {
                layout = lambdaTextFormat != null ? lambdaTextFormat.getLayout() : super.getOrCreateLayout();
            } else {
                layout = lambdaJsonFormat != null ? lambdaJsonFormat.getLayout() : super.getOrCreateLayout();
            }
            return new LambdaAppender(super.getName(), super.getFilter(), layout, super.isIgnoreExceptions());
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
    }

    /**
     * Converts log4j Level into Lambda LogLevel
     * @param level the log4j log level
     * @return Lambda log leve
     */
    private LogLevel toLambdaLogLevel(Level level) {
        return logLevelMapper.getOrDefault(level, LogLevel.UNDEFINED);
    }

    /**
     * Append log event to System.out
     * @param event log4j event
     */
    public void append(LogEvent event) {
        logger.log(getLayout().toByteArray(event), toLambdaLogLevel(event.getLevel()));
    }
}
