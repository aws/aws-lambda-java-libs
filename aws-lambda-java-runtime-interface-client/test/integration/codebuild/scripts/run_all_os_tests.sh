#!/bin/bash
# Copyright 2026 Amazon.com, Inc. or its affiliates. All Rights Reserved.

# Runs every combination in test/integration/os-test-matrix.json through
# run_os_test.sh. Only the host's platform by default (emulation is slow);
# PLATFORMS=all or e.g. PLATFORMS=linux/amd64 to choose. Needs jq.

set -euo pipefail

SCRIPTS_DIR="$(cd "$(dirname "$0")" && pwd)"
MATRIX="${SCRIPTS_DIR}/../../os-test-matrix.json"

case "$(arch)" in
    x86_64) HOST_PLATFORM="linux/amd64" ;;
    aarch64 | arm64) HOST_PLATFORM="linux/arm64/v8" ;;
    *) HOST_PLATFORM="" ;;
esac
PLATFORMS="${PLATFORMS:-${HOST_PLATFORM}}"

failed=()
while IFS=$'\t' read -r os distro runtime platform; do
    if [[ "${PLATFORMS}" != "all" ]] && [[ "${platform}" != "${PLATFORMS}" ]]; then
        continue
    fi
    echo "=== ${os} ${distro} ${runtime} ${platform}"
    if ! OS_DISTRIBUTION="${os}" DISTRO_VERSION="${distro}" RUNTIME_VERSION="${runtime}" PLATFORM="${platform}" \
        "${SCRIPTS_DIR}/run_os_test.sh"; then
        failed+=("${os} ${distro} ${runtime} ${platform}")
    fi
done < <(jq -r '.[] | .platforms[] as $p | [.os, .distro, .runtime, $p] | @tsv' "${MATRIX}")

if (( ${#failed[@]} > 0 )); then
    printf 'FAILED: %s\n' "${failed[@]}"
    exit 1
fi
