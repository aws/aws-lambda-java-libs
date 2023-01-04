#!/bin/bash
# Copyright 2022 Amazon.com, Inc. or its affiliates. All Rights Reserved.

set -euo pipefail

echo "Cleaning up..."
docker stop "${OS_DISTRIBUTION}-app" || true
docker rm --force "${OS_DISTRIBUTION}-app" || true
docker stop "${OS_DISTRIBUTION}-tester" || true
docker rm --force "${OS_DISTRIBUTION}-tester" || true
docker network rm "${OS_DISTRIBUTION}-network" || true
