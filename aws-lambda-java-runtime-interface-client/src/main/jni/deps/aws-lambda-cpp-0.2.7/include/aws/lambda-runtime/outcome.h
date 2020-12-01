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

#include <cassert>
#include <utility>

namespace aws {
namespace lambda_runtime {

template <typename TResult, typename TFailure>
class outcome {
public:
    outcome(TResult const& s) : m_s(s), m_success(true) {}
    outcome(TResult&& s) : m_s(std::move(s)), m_success(true) {}

    outcome(TFailure const& f) : m_f(f), m_success(false) {}
    outcome(TFailure&& f) : m_f(std::move(f)), m_success(false) {}

    outcome(outcome const& other) : m_success(other.m_success)
    {
        if (m_success) {
            new (&m_s) TResult(other.m_s);
        }
        else {
            new (&m_f) TFailure(other.m_f);
        }
    }

    outcome(outcome&& other) noexcept : m_success(other.m_success)
    {
        if (m_success) {
            new (&m_s) TResult(std::move(other.m_s));
        }
        else {
            new (&m_f) TFailure(std::move(other.m_f));
        }
    }

    ~outcome()
    {
        if (m_success) {
            m_s.~TResult();
        }
        else {
            m_f.~TFailure();
        }
    }

    TResult const& get_result() const&
    {
        assert(m_success);
        return m_s;
    }

    TResult&& get_result() &&
    {
        assert(m_success);
        return std::move(m_s);
    }

    TFailure const& get_failure() const&
    {
        assert(!m_success);
        return m_f;
    }

    TFailure&& get_failure() &&
    {
        assert(!m_success);
        return std::move(m_f);
    }

    bool is_success() const { return m_success; }

private:
    union {
        TResult m_s;
        TFailure m_f;
    };
    bool m_success;
};
} // namespace lambda_runtime
} // namespace aws
