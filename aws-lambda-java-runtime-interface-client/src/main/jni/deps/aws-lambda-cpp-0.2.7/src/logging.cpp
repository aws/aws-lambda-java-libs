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
#include "aws/logging/logging.h"
#include <array>
#include <cstdio>
#include <chrono>

#define LAMBDA_RUNTIME_API __attribute__((visibility("default")))

namespace aws {
namespace logging {

static inline char const* get_prefix(verbosity v)
{
    switch (v) {
        case verbosity::error:
            return "[ERROR]";
        case verbosity::info:
            return "[INFO]";
        case verbosity::debug:
            return "[DEBUG]";
        default:
            return "[UNKNOWN]";
    }
}

LAMBDA_RUNTIME_API
void log(verbosity v, char const* tag, char const* msg, va_list args)
{
    va_list copy;
    va_copy(copy, args);
    const int sz = vsnprintf(nullptr, 0, msg, args) + 1;
    if (sz < 0) {
        puts("error occurred during log formatting!\n");
        va_end(copy);
        return;
    }
    constexpr int max_stack_buffer_size = 512;
    std::array<char, max_stack_buffer_size> buf;
    char* out = buf.data();
    if (sz >= max_stack_buffer_size) {
        out = new char[sz];
    }

    vsnprintf(out, sz, msg, copy);
    va_end(copy);
    auto ms = std::chrono::duration_cast<std::chrono::milliseconds>(
        std::chrono::high_resolution_clock::now().time_since_epoch());
    printf("%s [%lld] %s %s\n", get_prefix(v), static_cast<long long>(ms.count()), tag, out);
    // stdout is not line-buffered when redirected (for example to a file or to another process) so we must flush it
    // manually.
    fflush(stdout);
    if (out != buf.data()) {
        delete[] out;
    }
}
} // namespace logging
} // namespace aws
