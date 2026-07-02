package integ;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * integration test handler that logs a marker string using Log4j2 with the LambdaAppender.
 * the test verifies that the marker appears in CloudWatch Logs, confirming end-to-end
 * log delivery through the aws-lambda-java-log4j2 library.
 */
public class Log4j2TestHandler implements RequestHandler<Map<String, String>, String> {

    private static final Logger logger = LogManager.getLogger(Log4j2TestHandler.class);

    @Override
    public String handleRequest(Map<String, String> event, Context context) {
        String marker = event.getOrDefault("marker", "NO_MARKER_PROVIDED");

        logger.info("INTEG_TEST_MARKER: {}", marker);
        logger.debug("Debug level message with marker: {}", marker);
        logger.warn("Warning level message with marker: {}", marker);
        logger.error("Error level message with marker: {}", marker);

        return "OK:" + marker;
    }
}
