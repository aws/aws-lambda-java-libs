package com.amazonaws.services.lambda.runtime.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.Layout;

import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import com.amazonaws.services.lambda.runtime.LambdaRuntimeInternal;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

/**
 * LambdaAppender is the custom log4j appender
 * to be used in the log4j.properties file.
 * You should not be required to use this class directly.
 */
public class LambdaAppender extends AppenderSkeleton {

    LambdaLogger logger = LambdaRuntime.getLogger();

    public LambdaAppender() {
        super();
        LambdaRuntimeInternal.setUseLog4jAppender(true);
    }

    @Override
    protected void append(LoggingEvent event) {
        if(this.layout == null) {
            logger.log(event.getLevel() + " " + event.getMessage());
            return;
        }
        logger.log(this.layout.format(event));
        //prints the Throwable from the log
        if(layout.ignoresThrowable()) {
            StringBuilder traceString = new StringBuilder();
            String[] s = event.getThrowableStrRep();
            if (s != null) {
                int len = s.length;
                for(int i = 0; i < len; i++) {
                    traceString.append(s[i]);
                    traceString.append(Layout.LINE_SEP);
                }
            }
            logger.log(traceString.toString());
        }
    }

    public void close() {
    }

    public boolean requiresLayout() {
        return true;
    }
}
