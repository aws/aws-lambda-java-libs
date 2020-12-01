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

namespace aws {
namespace lambda_runtime {

/**
 * Returns the major component of the library version.
 */
unsigned get_version_major();

/**
 * Returns the minor component of the library version.
 */
unsigned get_version_minor();

/**
 * Returns the patch component of the library version.
 */
unsigned get_version_patch();

/**
 * Returns the semantic version of the library in the form Major.Minor.Patch
 */
char const* get_version();

} // namespace lambda_runtime
} // namespace aws
