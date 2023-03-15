#!/bin/bash
# Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

set -euo pipefail

SRC_DIR=$(dirname "$0")
DST_DIR=${1}
MULTI_ARCH=${2}
CURL_VERSION=7.86.0

# Not using associative arrays to maintain bash 3 compatibility with building on MacOS
# MacOS ships with bash 3 and associative arrays require bash 4+
# Declaring a map as an array with the column character as a separator :
declare -a ARCHITECTURES_TO_PLATFORM=(
    "x86_64:linux/amd64"
    "aarch64:linux/arm64/v8"
)

declare -a TARGETS=("glibc" "musl")

for pair in "${ARCHITECTURES_TO_PLATFORM[@]}"; do
    arch=${pair%%:*}
    platform=${pair#*:}

    if [[ "${MULTI_ARCH}" != "true" ]] && [[ "$(arch)" != "${arch}" ]]; then
        echo "multi arch build not requested and host arch is $(arch), so skipping ${arch}:${platform} ..."
        continue
    fi

    mkdir -p "${DST_DIR}/classes/${arch}"

    for target in "${TARGETS[@]}"; do
        echo "Compiling the native library for target ${target} on architecture ${arch} using Docker platform ${platform}"
        artifact="${DST_DIR}/classes/${arch}/aws-lambda-runtime-interface-client.${target}.so"

        if [[ "${MULTI_ARCH}" == "true" ]]; then
            docker build --platform="${platform}" -f "${SRC_DIR}/Dockerfile.${target}" --build-arg CURL_VERSION=${CURL_VERSION} "${SRC_DIR}" -o - | tar -xOf - src/aws-lambda-runtime-interface-client.so > "${artifact}"
        else
            echo "multi-arch not requestsed, assuming this is a workaround to goofyness when docker buildx is enabled on Linux CI environments."
            echo "enabling docker buildx often updates the docker api version, so assuming that docker cli is also too old to use --output type=tar, so doing alternative build-tag-run approach"
            docker build --platform="${platform}" -t "lambda-java-jni-lib-${target}-${arch}" -f "${SRC_DIR}/Dockerfile.${target}" --build-arg CURL_VERSION=${CURL_VERSION} "${SRC_DIR}"
            docker run --rm --entrypoint /bin/cat "lambda-java-jni-lib-${target}-${arch}" /src/aws-lambda-runtime-interface-client.so > "${artifact}"
        fi

        [ -f "${artifact}" ]
        if ! file -b "${artifact}" | tr '-' '_' | tee /dev/stderr | grep -q "${arch}"; then
            echo "${artifact} did not appear to be the correct architecture, check that Docker buildx is enabled"
            exit 1
        fi
    done
done
