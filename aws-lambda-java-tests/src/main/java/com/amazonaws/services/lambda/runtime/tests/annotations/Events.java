/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests.annotations;

import com.amazonaws.services.lambda.runtime.tests.EventsArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

/**
 * This annotation must be used in conjunction with {@link org.junit.jupiter.params.ParameterizedTest}.<br/>
 * It enables to inject multiple events (loaded from json files) of the desired type into the current test.<br/>
 * <p>
 * Several notations are possible according to what you want to do:
 * <ul>
 * <li>
 * Using the <code>folder</code> parameter is the more straightforward, and it will use all files in the folder<br/>
 * <pre>
 *     &#64;ParameterizedTest
 *     &#64;Events(folder = "sqs", type = SQSEvent.class)
 *     public void testInjectEventsFromFolder(SQSEvent event) {
 *         assertThat(event).isNotNull();
 *         assertThat(event.getRecords()).hasSize(1);
 *     }
 * </pre>
 * </li>
 * <li>
 * Or you can list all the {@link Event}s<br/>
 * <pre>
 * &#64;ParameterizedTest
 *     &#64;Events(
 *             events = {
 *                     &#64;Event("sqs/sqs_event.json"),
 *                     &#64;Event("sqs/sqs_event2.json"),
 *             },
 *             type = SQSEvent.class
 *     )
 *     public void testInjectEvents(SQSEvent event) {
 *         assertThat(event).isNotNull();
 *         assertThat(event.getRecords()).hasSize(1);
 *     }
 *
 *     &#64;ParameterizedTest
 *     &#64;Events(
 *             events = {
 *                     &#64;Event(value = "sqs/sqs_event.json", type = SQSEvent.class),
 *                     &#64;Event(value = "sqs/sqs_event2.json", type = SQSEvent.class),
 *             }
 *     )
 *     public void testInjectEvents2(SQSEvent event) {
 *         assertThat(event).isNotNull();
 *         assertThat(event.getRecords()).hasSize(1);
 *     }
 * </pre>
 * </li>
 * </ul>
 * </p>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(EventsArgumentsProvider.class)
public @interface Events {

    /**
     * Folder where to find json files containing events
     * @return the folder name
     */
    String folder() default "";

    /**
     * Type of the events (for example, one of the <a href="https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-events">aws-lambda-java-events</a>), or your own type
     * @return the type of the events
     */
    Class<?> type() default Void.class;

    /**
     * Mutually exclusive with folder
     * @return the array of events
     */
    Event[] events() default {};
}
