#pragma once
/*
 * Copyright 2018-present Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

#include <cstdarg>

namespace aws {
namespace logging {

enum class verbosity {
    error,
    info,
    debug,
};

void log(verbosity v, char const* tag, char const* msg, va_list args);

[[gnu::format(printf, 2, 3)]] inline void log_error(char const* tag, char const* msg, ...)
{
    va_list args;
    va_start(args, msg);
    log(verbosity::error, tag, msg, args);
    va_end(args);
    (void)tag;
    (void)msg;
}

[[gnu::format(printf, 2, 3)]] inline void log_info(char const* tag, char const* msg, ...)
{
#if AWS_LAMBDA_LOG >= 1
    va_list args;
    va_start(args, msg);
    log(verbosity::info, tag, msg, args);
    va_end(args);
#else
    (void)tag;
    (void)msg;
#endif
}

[[gnu::format(printf, 2, 3)]] inline void log_debug(char const* tag, char const* msg, ...)
{
#if AWS_LAMBDA_LOG >= 2
    va_list args;
    va_start(args, msg);
    log(verbosity::debug, tag, msg, args);
    va_end(args);
#else
    (void)tag;
    (void)msg;
#endif
}

} // namespace logging
} // namespace aws
