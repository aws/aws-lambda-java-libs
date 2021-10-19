#!/bin/bash
# Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

set -euo pipefail

SRC_DIR=$(dirname "$0")
DST_DIR=${1}
CURL_VERSION=7.77.0

# compile the native library
docker build -f "${SRC_DIR}/Dockerfile.glibc" --build-arg CURL_VERSION="${CURL_VERSION}" --build-arg IMAGE_SOURCE=centos:7 -t lambda-java-jni-lib-glibc-x86_64 "${SRC_DIR}"
docker run --rm --entrypoint /bin/cat lambda-java-jni-lib-glibc /src/aws-lambda-runtime-interface-client.so > "${DST_DIR}"/classes/aws-lambda-runtime-interface-client.x86_64.glibc.so

docker build -f "${SRC_DIR}/Dockerfile.glibc" --build-arg CURL_VERSION="${CURL_VERSION}" --build-arg IMAGE_SOURCE=arm64v8/centos:7 -t lambda-java-jni-lib-glibc-arm64 "${SRC_DIR}"
docker run --rm --entrypoint /bin/cat lambda-java-jni-lib-glibc /src/aws-lambda-runtime-interface-client.so > "${DST_DIR}"/classes/aws-lambda-runtime-interface-client.arm64.glibc.so

docker build -f "${SRC_DIR}/Dockerfile.musl" --build-arg CURL_VERSION="${CURL_VERSION}" --build-arg IMAGE_SOURCE=alpine:3.12 -t lambda-java-jni-lib-musl-x86_64 "${SRC_DIR}"
docker run --rm --entrypoint /bin/cat lambda-java-jni-lib-musl /src/aws-lambda-runtime-interface-client.so > "${DST_DIR}"/classes/aws-lambda-runtime-interface-client.x86_64.musl.so

docker build -f "${SRC_DIR}/Dockerfile.musl" --build-arg CURL_VERSION="${CURL_VERSION}" --build-arg IMAGE_SOURCE=arm64v8/alpine:3 -t lambda-java-jni-lib-musl-arm64 "${SRC_DIR}"
docker run --rm --entrypoint /bin/cat lambda-java-jni-lib-musl /src/aws-lambda-runtime-interface-client.so > "${DST_DIR}"/classes/aws-lambda-runtime-interface-client.arm64.musl.so

