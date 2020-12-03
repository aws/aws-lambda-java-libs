/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests.annotations;

import com.amazonaws.services.lambda.runtime.tests.EventArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

/**
 * This annotation must be used in conjunction with {@link org.junit.jupiter.params.ParameterizedTest}.<br/>
 * It enables to inject an event (loaded from a json file) of the desired type into the current test.<br/>
 * Example:<br/>
 * <pre>
 *     &#64;ParameterizedTest
 *     &#64;Event(value = "sqs_event.json", type = SQSEvent.class)
 *     public void testInjectEvent(SQSEvent event) {
 *         assertThat(event).isNotNull();
 *         assertThat(event.getRecords()).hasSize(1);
 *     }
 * </pre>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(EventArgumentsProvider.class)
public @interface Event {

    /**
     * Path and file name of the json event
     * @return the file name (including the path)
     */
    String value();

    /**
     * Type of the event (for example, one of the <a href="https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-events">aws-lambda-java-events</a>), or your own type
     * @return the type of the event
     */
    Class<?> type() default Void.class;
}
