#!/bin/bash
# Copyright 2022 Amazon.com, Inc. or its affiliates. All Rights Reserved.

set -euxo pipefail

echo "Running Image ${IMAGE_TAG}"
docker network create "${OS_DISTRIBUTION}-network"

function_jar="./HelloWorld-1.0.jar"
function_handler="helloworld.App"
docker run \
  --detach \
  --name "${OS_DISTRIBUTION}-app" \
  --network "${OS_DISTRIBUTION}-network" \
  --entrypoint="" \
  --platform="${PLATFORM}" \
  "${IMAGE_TAG}" \
  sh -c "/usr/bin/${RIE} ${JAVA_BINARY_LOCATION} -jar ${function_jar} ${function_handler}"
sleep 2

# running on arm64 hosts with x86_64 being emulated takes significantly more time than any other combination
if [[ "$(arch)" == "aarch64" ]] && [[ "${PLATFORM}" == "linux/amd64" ]]; then
  declare -i time_out=150
else
  declare -i time_out=10
fi

docker run \
  --name "${OS_DISTRIBUTION}-tester" \
  --env "TARGET=${OS_DISTRIBUTION}-app" \
  --env "MAX_TIME=${time_out}" \
  --network "${OS_DISTRIBUTION}-network" \
  --entrypoint="" \
  --platform="${PLATFORM}" \
    "${IMAGE_TAG}" \
  sh -c 'curl -X POST "http://${TARGET}:8080/2015-03-31/functions/function/invocations" -d "{}" --max-time ${MAX_TIME}'
actual="$(docker logs --tail 1 "${OS_DISTRIBUTION}-tester" | xargs)"
expected='success'
echo "Response: ${actual}"
if [[ "${actual}" != "${expected}" ]]; then
  echo "fail! runtime: ${RUNTIME} - expected output ${expected} - got ${actual}"
  exit 1
fi
