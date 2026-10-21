#!/bin/bash
# Copyright 2026 Amazon.com, Inc. or its affiliates. All Rights Reserved.

# Runs one OS integration test: builds the RIC, builds a function image with the
# Runtime Interface Emulator for one OS / version / runtime / platform, invokes
# it and checks the response. Needs docker, maven, and JDK 8 as JAVA_HOME and
# as a maven toolchain.
#
# Required environment:
#   OS_DISTRIBUTION  alpine | amazoncorretto | amazonlinux | debian | ubuntu
#   DISTRO_VERSION   e.g. 3.15, 2, bookworm, 24.04 (amazoncorretto for amazoncorretto)
#   RUNTIME_VERSION  e.g. corretto11, openjdk8, 8, 11
#   PLATFORM         linux/amd64 | linux/arm64/v8
#
# Optional environment:
#   BASE_REGISTRY    registry for base images (default public.ecr.aws); also read
#                    by the JNI build.

set -euo pipefail

: "${OS_DISTRIBUTION:?}" "${DISTRO_VERSION:?}" "${RUNTIME_VERSION:?}" "${PLATFORM:?}"

SCRIPTS_DIR="$(cd "$(dirname "$0")" && pwd)"
REPO_ROOT="$(cd "${SCRIPTS_DIR}/../../../../.." && pwd)"
RIC_DIR="aws-lambda-java-runtime-interface-client"
cd "${REPO_ROOT}"
export BASE_REGISTRY="${BASE_REGISTRY:-public.ecr.aws}"

# Per-OS differences: where the image's JDK lives, and how to add curl (the
# tester container uses it to invoke the function).
case "${OS_DISTRIBUTION}" in
    alpine)
        DEFAULT_JAVA_BINARY_LOCATION="/usr/bin/java"
        CURL_INSTALL="RUN apk add curl"
        ;;
    debian | ubuntu)
        DEFAULT_JAVA_BINARY_LOCATION="/usr/lib/jvm/java-11-amazon-corretto/bin/java"
        CURL_INSTALL="RUN apt-get update && apt-get install -y curl"
        ;;
    amazoncorretto | amazonlinux)
        DEFAULT_JAVA_BINARY_LOCATION="/usr/bin/java"
        CURL_INSTALL=""
        ;;
    *)
        echo "OS_DISTRIBUTION ${OS_DISTRIBUTION} is not currently supported."
        exit 1
        ;;
esac
export JAVA_BINARY_LOCATION="${JAVA_BINARY_LOCATION:-${DEFAULT_JAVA_BINARY_LOCATION}}"

case "${PLATFORM}" in
    linux/amd64)
        RIE="aws-lambda-rie"
        QEMU_TARGET="x86_64"
        EMULATOR="amd64"
        ;;
    linux/arm64/v8)
        RIE="aws-lambda-rie-arm64"
        QEMU_TARGET="aarch64"
        EMULATOR="arm64"
        ;;
    *)
        echo "Platform ${PLATFORM} is not currently supported."
        exit 1
        ;;
esac
export RIE

# A platform other than the host's needs a qemu emulator registered with binfmt_misc.
if [[ "$(arch)" != "${QEMU_TARGET}" ]] && [[ ! -e "/proc/sys/fs/binfmt_misc/qemu-${QEMU_TARGET}" ]]; then
    echo "Installing ${EMULATOR} emulator"
    "${SCRIPTS_DIR}/docker-retry.sh" docker pull public.ecr.aws/eks-distro-build-tooling/binfmt-misc:qemu-v6.1.0
    docker run --rm --privileged public.ecr.aws/eks-distro-build-tooling/binfmt-misc:qemu-v6.1.0 --install "${EMULATOR}"
fi

mvn -v
# events is a dependency of serialization
(cd aws-lambda-java-events && mvn install)
(cd aws-lambda-java-core && mvn install)
(cd aws-lambda-java-serialization && mvn install)
(cd "${RIC_DIR}" && mvn install -DmultiArch=false -DargLineForReflectionTestOnly="")
(cd "${RIC_DIR}/test/integration/test-handler" && mvn install)

export IMAGE_TAG="java-${OS_DISTRIBUTION}-${DISTRO_VERSION}:${RUNTIME_VERSION}"
echo "Extracting and including Runtime Interface Emulator"
SCRATCH_DIR=".scratch"
rm -rf "${SCRATCH_DIR}"
mkdir "${SCRATCH_DIR}"
tar -xvf "${RIC_DIR}/test/integration/resources/${RIE}.tar.gz" --directory "${SCRATCH_DIR}"

DOCKERFILE="${SCRATCH_DIR}/Dockerfile.function.${OS_DISTRIBUTION}.tmp"
cp "${RIC_DIR}/test/integration/docker/Dockerfile.function.${OS_DISTRIBUTION}" "${DOCKERFILE}"
echo "COPY ${SCRATCH_DIR}/${RIE} /usr/bin/${RIE}" >> "${DOCKERFILE}"
if [[ -n "${CURL_INSTALL}" ]]; then
    echo "${CURL_INSTALL}" >> "${DOCKERFILE}"
fi

echo "Building image ${IMAGE_TAG}"
"${SCRIPTS_DIR}/docker-retry.sh" docker build . \
    -f "${DOCKERFILE}" \
    -t "${IMAGE_TAG}" \
    --platform="${PLATFORM}" \
    --build-arg BASE_REGISTRY="${BASE_REGISTRY}" \
    --build-arg RUNTIME_VERSION="${RUNTIME_VERSION}" \
    --build-arg DISTRO_VERSION="${DISTRO_VERSION}"

trap '"${SCRIPTS_DIR}/fetch_test_container_logs.sh"; "${SCRIPTS_DIR}/clean_up.sh"' EXIT
"${SCRIPTS_DIR}/run_invocation_test.sh"
