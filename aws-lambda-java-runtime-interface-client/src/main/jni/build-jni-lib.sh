#!/bin/bash
# Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

set -euo pipefail

SRC_DIR=$(dirname "$0")
DST_DIR=${1}
CURL_VERSION=7.77.0

# Not using associative arrays to maintain bash 3 compatibility with building on MacOS
# MacOS ships with bash 3 and associative arrays require bash 4+
# Declaring a map as an array with the column character as a separator :
declare -a ARCHITECTURES_TO_PLATFORM=(
    "x86_64:linux/amd64"
    "arm64:linux/arm64/v8"
)

declare -a TARGETS=("glibc" "musl")

for pair in "${ARCHITECTURES_TO_PLATFORM[@]}"; do
    arch=${pair%%:*}
    platform=${pair#*:}

    mkdir -p "${DST_DIR}"/classes/"${arch}"

    for target in "${TARGETS[@]}"; do
        echo "Compiling the native library for target ${target} on architecture ${arch} using Docker platform ${platform}"
        docker build --platform="${platform}" -f "${SRC_DIR}"/Dockerfile."${target}" --build-arg CURL_VERSION=${CURL_VERSION} -t lambda-java-jni-lib-"${target}"-"${arch}" "${SRC_DIR}"
        docker run --rm --entrypoint /bin/cat lambda-java-jni-lib-"${target}"-"${arch}" /src/aws-lambda-runtime-interface-client.so > "${DST_DIR}"/classes/"${arch}"/aws-lambda-runtime-interface-client."${target}".so
    done
done
