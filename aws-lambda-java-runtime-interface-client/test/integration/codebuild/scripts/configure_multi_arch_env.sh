#!/bin/bash
# Copyright 2022 Amazon.com, Inc. or its affiliates. All Rights Reserved.

set -euo pipefail

echo "Setting up multi-arch build environment"
ARCHITECTURE=$(arch)
if [[ "$ARCHITECTURE" == "x86_64" ]]; then
	TARGET_EMULATOR="arm64"
elif [[ "$ARCHITECTURE" == "aarch64" ]]; then
	TARGET_EMULATOR="amd64"
else
	echo "Architecture $ARCHITECTURE is not currently supported."
	exit 1
fi

echo "Installing ${TARGET_EMULATOR} emulator"
# BASE_REGISTRY is the ECR pull-through cache when the buildspec logged in to it.
BINFMT_IMAGE="${BASE_REGISTRY:-public.ecr.aws}/eks-distro-build-tooling/binfmt-misc:qemu-v6.1.0"
"$(dirname "$0")"/../../codebuild-local/docker-retry.sh docker pull "$BINFMT_IMAGE"
docker run --rm --privileged "$BINFMT_IMAGE" --install ${TARGET_EMULATOR}
echo "Setting docker build command to default to buildx"
echo "Docker buildx version: $(docker buildx version)"
docker buildx install
