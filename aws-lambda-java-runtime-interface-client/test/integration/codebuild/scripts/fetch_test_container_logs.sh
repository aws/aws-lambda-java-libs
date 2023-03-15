#!/bin/bash
# Copyright 2022 Amazon.com, Inc. or its affiliates. All Rights Reserved.

set -euo pipefail

echo "---------Container Logs: ${OS_DISTRIBUTION}-app----------"
echo
docker logs "${OS_DISTRIBUTION}-app" || true
echo
echo "---------------------------------------------------"
echo "--------Container Logs: ${OS_DISTRIBUTION}-tester--------"
echo
docker logs "${OS_DISTRIBUTION}-tester" || true
echo
echo "---------------------------------------------------"
