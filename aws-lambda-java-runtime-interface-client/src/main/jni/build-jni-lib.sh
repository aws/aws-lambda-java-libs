#!/bin/bash
# Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

set -euo pipefail

SRC_DIR=$(dirname "$0")
DST_DIR=${1}
CURL_VERSION=7.77.0

# compile the native library
docker build -f "${SRC_DIR}/Dockerfile.glibc" --build-arg CURL_VERSION=${CURL_VERSION} -t lambda-java-jni-lib-glibc "${SRC_DIR}"
docker run --rm --entrypoint /bin/cat lambda-java-jni-lib-glibc /src/aws-lambda-runtime-interface-client.so > "${DST_DIR}"/classes/aws-lambda-runtime-interface-client.glibc.so

docker build -f "${SRC_DIR}/Dockerfile.musl" --build-arg CURL_VERSION=${CURL_VERSION} -t lambda-java-jni-lib-musl "${SRC_DIR}"
docker run --rm --entrypoint /bin/cat lambda-java-jni-lib-musl /src/aws-lambda-runtime-interface-client.so > "${DST_DIR}"/classes/aws-lambda-runtime-interface-client.musl.so

