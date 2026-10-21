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

case "${PLATFORM}" in
    linux/amd64) RIE="aws-lambda-rie" ;;
    linux/arm64/v8) RIE="aws-lambda-rie-arm64" ;;
    *)
        echo "Platform ${PLATFORM} is not currently supported."
        exit 1
        ;;
esac

# Unit tests run in the build job; only the artifacts are needed here.
mvn -v
(cd aws-lambda-java-events && mvn install -DskipTests)
(cd aws-lambda-java-core && mvn install -DskipTests)
(cd aws-lambda-java-serialization && mvn install -DskipTests)
(cd "${RIC_DIR}" && mvn install -DskipTests -DmultiArch=false)
(cd "${RIC_DIR}/test/integration/test-handler" && mvn install -DskipTests)

# Unique per combination, so different combinations can run side by side.
NAME="${OS_DISTRIBUTION}-${DISTRO_VERSION//./-}-${RUNTIME_VERSION//./-}"
IMAGE_TAG="java-integ-test:${NAME}"

echo "Building image ${IMAGE_TAG}"
docker build . \
    -f "${RIC_DIR}/test/integration/docker/Dockerfile.function.${OS_DISTRIBUTION}" \
    -t "${IMAGE_TAG}" \
    --platform="${PLATFORM}" \
    --build-arg BASE_REGISTRY="${BASE_REGISTRY}" \
    --build-arg RIE="${RIE}" \
    --build-arg RUNTIME_VERSION="${RUNTIME_VERSION}" \
    --build-arg DISTRO_VERSION="${DISTRO_VERSION}"

cleanup() {
    for container in "${NAME}-app" "${NAME}-tester"; do
        echo "--------- Container logs: ${container} ---------"
        docker logs "${container}" || true
        docker rm --force "${container}" >/dev/null 2>&1 || true
    done
    docker network rm "${NAME}-network" >/dev/null 2>&1 || true
}
trap cleanup EXIT

docker network create "${NAME}-network"
docker run \
    --detach \
    --name "${NAME}-app" \
    --network "${NAME}-network" \
    --entrypoint="" \
    --platform="${PLATFORM}" \
    "${IMAGE_TAG}" \
    sh -c "/usr/bin/${RIE} \$(command -v java) -jar ./HelloWorld-1.0.jar helloworld.App"
sleep 2

# An emulated platform (one that isn't the host's) takes significantly longer.
# `arch` prints aarch64/x86_64 on Linux, arm64/i386 on macOS.
case "$(arch)" in
    aarch64 | arm64) host_platform="linux/arm64/v8" ;;
    x86_64 | i386) host_platform="linux/amd64" ;;
    *) host_platform="" ;;
esac
if [[ "${PLATFORM}" != "${host_platform}" ]]; then
    max_time=150
else
    max_time=10
fi

docker run \
    --name "${NAME}-tester" \
    --network "${NAME}-network" \
    --entrypoint="" \
    --platform="${PLATFORM}" \
    "${IMAGE_TAG}" \
    curl -sS -X POST "http://${NAME}-app:8080/2015-03-31/functions/function/invocations" -d "{}" --max-time "${max_time}"
actual="$(docker logs --tail 1 "${NAME}-tester" | xargs)"
echo "Response: ${actual}"
if [[ "${actual}" != "success" ]]; then
    echo "fail! ${OS_DISTRIBUTION} ${DISTRO_VERSION} ${RUNTIME_VERSION} ${PLATFORM} - expected output success - got ${actual}"
    exit 1
fi
