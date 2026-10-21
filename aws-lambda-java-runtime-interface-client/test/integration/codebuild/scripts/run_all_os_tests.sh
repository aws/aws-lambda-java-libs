#!/bin/bash
# Copyright 2026 Amazon.com, Inc. or its affiliates. All Rights Reserved.

# Runs every combination in test/integration/os-test-matrix.json for the host's
# platform through run_os_test.sh. Needs jq.

set -euo pipefail

SCRIPTS_DIR="$(cd "$(dirname "$0")" && pwd)"
MATRIX="${SCRIPTS_DIR}/../../os-test-matrix.json"

case "$(arch)" in
    x86_64) HOST_PLATFORM="linux/amd64" ;;
    aarch64 | arm64) HOST_PLATFORM="linux/arm64/v8" ;;
    *)
        echo "Architecture $(arch) is not currently supported."
        exit 1
        ;;
esac

failed=()
while IFS=$'\t' read -r os distro runtime; do
    echo "=== ${os} ${distro} ${runtime} ${HOST_PLATFORM}"
    if ! OS_DISTRIBUTION="${os}" DISTRO_VERSION="${distro}" RUNTIME_VERSION="${runtime}" PLATFORM="${HOST_PLATFORM}" \
        "${SCRIPTS_DIR}/run_os_test.sh"; then
        failed+=("${os} ${distro} ${runtime}")
    fi
done < <(jq -r --arg p "${HOST_PLATFORM}" '.[] | select(.platforms | index($p)) | [.os, .distro, .runtime] | @tsv' "${MATRIX}")

if (( ${#failed[@]} > 0 )); then
    printf 'FAILED: %s\n' "${failed[@]}"
    exit 1
fi
