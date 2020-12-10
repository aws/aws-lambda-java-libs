/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests.annotations;

import java.lang.annotation.*;

/**
 * This annotation must be used in conjunction with {@link HandlerParams}.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Response {

    /**
     * Path and file name of the json response
     * @return the file name (including the path)
     */
    String value();

    /**
     * Type of the response
     * @return the type of the response
     */
    Class<?> type() default Void.class;
}
