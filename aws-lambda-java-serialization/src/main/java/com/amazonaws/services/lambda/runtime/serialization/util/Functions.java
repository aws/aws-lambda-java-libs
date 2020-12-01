/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.util;

/**
 * Interfaces for reflective function calls
 * R functions return a type R with n number of arguments
 * V functions are void
 * A generics represent arguments for a function handle
 */
public final class Functions {

    private Functions() {}

    public interface R0<R> {
        public R call();
    }

    public interface R1<R, A1> {
        public R call(A1 arg1);
    }

    public interface R2<R, A1, A2> {
        public R call(A1 arg1, A2 arg2);
    }

    public interface R3<R, A1, A2, A3> {
        public R call(A1 arg1, A2 arg2, A3 arg3);
    }

    public interface R4<R, A1, A2, A3, A4> {
        public R call(A1 arg1, A2 arg2, A3 arg3, A4 arg4);
    }

    public interface R5<R, A1, A2, A3, A4, A5> {
        public R call(A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5);
    }

    public interface R9<R, A1, A2, A3, A4, A5, A6, A7, A8, A9> {
        public R call(A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5, A6 arg6, A7 arg7, A8 arg8, A9 arg9);
    }

    public interface V0 {
        public void call();
    }

    public interface V1<A1> {
        public void call(A1 arg1);
    }

    public interface V2<A1, A2> {
        public void call(A1 arg1, A2 arg2);
    }
}
