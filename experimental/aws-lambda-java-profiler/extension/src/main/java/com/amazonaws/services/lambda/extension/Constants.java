package com.amazonaws.services.lambda.extension;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {

    private static final String DEFAULT_AWS_LAMBDA_PROFILER_START_COMMAND =
            "start,event=wall,interval=1us";
    private static final String DEFAULT_AWS_LAMBDA_PROFILER_STOP_COMMAND =
            "stop,file=%s,include=*AWSLambda.main,include=start_thread";
    public static final String PROFILER_START_COMMAND =
            System.getenv().getOrDefault(
                    "AWS_LAMBDA_PROFILER_START_COMMAND",
                    DEFAULT_AWS_LAMBDA_PROFILER_START_COMMAND
            );
    public static final String PROFILER_STOP_COMMAND =
            System.getenv().getOrDefault(
                    "AWS_LAMBDA_PROFILER_STOP_COMMAND",
                    DEFAULT_AWS_LAMBDA_PROFILER_STOP_COMMAND
            );

    public static String getFilePathFromEnv(){
        Pattern pattern = Pattern.compile("file=([^,]+)");
        Matcher matcher = pattern.matcher(PROFILER_START_COMMAND);

        return matcher.find() ? matcher.group(1) : "/tmp/profiling-data-%s.html";
    }
}
