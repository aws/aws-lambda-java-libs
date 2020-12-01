#!/bin/bash

set -euo pipefail

cd $CODEBUILD_SRC_DIR
cd build
rm -rf *.zip
ninja $1
aws s3 cp tests/resources/lambda-test-fun.zip s3://aws-lambda-cpp-tests/$2lambda-test-fun.zip
ctest --output-on-failure

