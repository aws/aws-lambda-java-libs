#!/bin/bash
# Copyright 2022 Amazon.com, Inc. or its affiliates. All Rights Reserved.

set -euo pipefail

echo "Setting up multi-arch build environment"
ARCHITECTURE=$(arch)
if [[ "$ARCHITECTURE" == "x86_64" ]]; then
	ARCHITECTURE_ALIAS="amd64"
	TARGET_EMULATOR="arm64"
elif [[ "$ARCHITECTURE" == "aarch64" ]]; then
	ARCHITECTURE_ALIAS="arm64"
	TARGET_EMULATOR="amd64"
else
	echo "Architecture $ARCHITECTURE is not currently supported."
	exit 1
fi
echo "Installing ${TARGET_EMULATOR} emulator"
docker pull public.ecr.aws/eks-distro-build-tooling/binfmt-misc:qemu-v6.1.0
docker run --rm --privileged public.ecr.aws/eks-distro-build-tooling/binfmt-misc:qemu-v6.1.0 --install ${TARGET_EMULATOR}
# Install buildx plugin only if not already present (i.e. it's installed for the local-agent)
if [[ ! -f "${DOCKER_CLI_PLUGIN_DIR}/docker-buildx" ]]; then
	echo "docker-buildx not found, installing now"
	mkdir -p "${DOCKER_CLI_PLUGIN_DIR}"
	BUILDX_URL=$(curl https://api.github.com/repos/docker/buildx/releases/latest | grep browser_download_url | grep "linux-${ARCHITECTURE_ALIAS}" | cut -d '"' -f 4)
	wget "${BUILDX_URL}" -O "${DOCKER_CLI_PLUGIN_DIR}"/docker-buildx
	chmod +x "${DOCKER_CLI_PLUGIN_DIR}"/docker-buildx
fi
echo "Setting docker build command to default to buildx"
docker buildx install
