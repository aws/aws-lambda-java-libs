#!/bin/bash
# Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

set -euo pipefail

SRC_DIR=$(dirname "$0")
DST_DIR=${1}
CURL_VERSION=7.77.0

# compile the native library for x86
mkdir "${DST_DIR}"/classes/x86
docker build --platform=linux/amd64 -f "${SRC_DIR}/Dockerfile.glibc" --build-arg CURL_VERSION=${CURL_VERSION} -t lambda-java-jni-lib-glibc-x86 "${SRC_DIR}"
docker run --rm --entrypoint /bin/cat lambda-java-jni-lib-glibc-x86 /src/aws-lambda-runtime-interface-client.so > "${DST_DIR}"/classes/x86/aws-lambda-runtime-interface-client.glibc.so

docker build --platform=linux/amd64 -f "${SRC_DIR}/Dockerfile.musl" --build-arg CURL_VERSION=${CURL_VERSION} -t lambda-java-jni-lib-musl-x86 "${SRC_DIR}"
docker run --rm --entrypoint /bin/cat lambda-java-jni-lib-musl-x86 /src/aws-lambda-runtime-interface-client.so > "${DST_DIR}"/classes/x86/aws-lambda-runtime-interface-client.musl.so

# compile the native library for ARM
mkdir "${DST_DIR}"/classes/arm
docker build --platform=linux/arm64/v8 -f "${SRC_DIR}/Dockerfile.glibc" --build-arg CURL_VERSION=${CURL_VERSION} -t lambda-java-jni-lib-glibc-arm "${SRC_DIR}"
docker run --rm --entrypoint /bin/cat lambda-java-jni-lib-glibc-arm /src/aws-lambda-runtime-interface-client.so > "${DST_DIR}"/classes/arm/aws-lambda-runtime-interface-client.glibc.so

docker build --platform=linux/arm64/v8 -f "${SRC_DIR}/Dockerfile.musl" --build-arg CURL_VERSION=${CURL_VERSION} -t lambda-java-jni-lib-musl-arm "${SRC_DIR}"
docker run --rm --entrypoint /bin/cat lambda-java-jni-lib-musl-arm /src/aws-lambda-runtime-interface-client.so > "${DST_DIR}"/classes/arm/aws-lambda-runtime-interface-client.musl.so
