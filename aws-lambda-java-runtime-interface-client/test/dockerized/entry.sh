#!/bin/sh
# Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
# SPDX-License-Identifier: Apache-2.0

set -e

exec /usr/local/bin/aws-lambda-rie java \
  -cp "/opt/ric/*:/var/task" \
  com.amazonaws.services.lambda.runtime.api.client.AWSLambda "$@"
