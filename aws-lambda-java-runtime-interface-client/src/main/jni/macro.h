/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

#ifndef _Included_macros
#define _Included_macros

#define CHECK_EXCEPTION(env, expr) \
    expr; \
    if ((env)->ExceptionOccurred()) \
        goto ERROR;

#endif
