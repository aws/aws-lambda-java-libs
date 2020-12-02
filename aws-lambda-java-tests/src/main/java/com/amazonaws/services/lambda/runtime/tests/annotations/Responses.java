/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests.annotations;

import java.lang.annotation.*;

/**
 * This annotation must be used in conjunction with {@link HandlerParams}.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Responses {

    /**
     * Folder where to find json files containing responses
     * @return the folder name
     */
    String folder() default "";

    /**
     * Type of the responses
     * @return the type of the responses
     */
    Class<?> type() default Void.class;

    /**
     * Mutually exclusive with folder
     * @return the array of responses
     */
    Response[] responses() default {};
}
