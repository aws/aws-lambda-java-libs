#!/bin/bash
# Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

set -euo pipefail

SRC_DIR=$(dirname "$0")
DST_DIR=${1}
CURL_VERSION=7.77.0

for i in x86,linux/amd64 arm,linux/arm64/v8; do
  arch=${i%,*};
  platform=${i#*,};

  mkdir "${DST_DIR}"/classes/$arch
  docker build --platform=$platform -f "${SRC_DIR}/Dockerfile.glibc" --build-arg CURL_VERSION=${CURL_VERSION} -t lambda-java-jni-lib-glibc-$arch "${SRC_DIR}"
  docker run --rm --entrypoint /bin/cat lambda-java-jni-lib-glibc-$arch /src/aws-lambda-runtime-interface-client.so > "${DST_DIR}"/classes/$arch/aws-lambda-runtime-interface-client.glibc.so

  docker build --platform=$platform -f "${SRC_DIR}/Dockerfile.musl" --build-arg CURL_VERSION=${CURL_VERSION} -t lambda-java-jni-lib-musl-$arch "${SRC_DIR}"
  docker run --rm --entrypoint /bin/cat lambda-java-jni-lib-musl-$arch /src/aws-lambda-runtime-interface-client.so > "${DST_DIR}"/classes/$arch/aws-lambda-runtime-interface-client.musl.so
done
