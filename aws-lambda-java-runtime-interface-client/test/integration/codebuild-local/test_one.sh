#!/bin/bash
# Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

set -euo pipefail

CODEBUILD_IMAGE_TAG="${CODEBUILD_IMAGE_TAG:-al2/x86_64/standard/3.0}"

function usage {
    >&2 echo "usage: test_one.sh buildspec_yml os_distribution distro_version runtime_version [env]"
    >&2 echo "Runs one buildspec version combination from a build-matrix buildspec."
    >&2 echo "Required:"
    >&2 echo "  buildspec_yml          Used to specify the CodeBuild buildspec template file."
    >&2 echo "  os_distribution        Used to specify the OS distribution to build."
    >&2 echo "  distro_version         Used to specify the distro version of <os_distribution>."
    >&2 echo "  runtime_version        Used to specify the runtime version to test on the selected <distro_version>."
    >&2 echo "  platform               Used to specify the architecture platform to test on the selected <distro_version>."
    >&2 echo "Optional:"
    >&2 echo "  env                    Additional environment variables file."
}

# codebuild/local-builds images are not multi-architectural
function get_local_agent_image() {
    if [[ "$(arch)" == "aarch64" ]]; then
        echo "public.ecr.aws/codebuild/local-builds:aarch64"
    else
        echo "public.ecr.aws/codebuild/local-builds:latest"
    fi
}

main() {
    if (( $# != 5 && $# != 6)); then
        >&2 echo "Invalid number of parameters."
        usage
        exit 1
    fi

    BUILDSPEC_YML="$1"
    OS_DISTRIBUTION="$2"
    DISTRO_VERSION="$3"
    RUNTIME_VERSION="$4"
    PLATFORM="$5"
    PLATFORM_SANITIZED=$(echo "$PLATFORM" | tr "/" ".")
    EXTRA_ENV="${6-}"

    CODEBUILD_TEMP_DIR=$(mktemp -d codebuild."$OS_DISTRIBUTION"-"$DISTRO_VERSION"-"$RUNTIME_VERSION"-"$PLATFORM_SANITIZED".XXXXXXXXXX)
    trap 'rm -rf $CODEBUILD_TEMP_DIR' EXIT

    # Create an env file for codebuild_build.
    ENVFILE="$CODEBUILD_TEMP_DIR/.env"
    if [ -f "$EXTRA_ENV" ]; then
        cat "$EXTRA_ENV" > "$ENVFILE"
    fi
    {
        echo ""
        echo "OS_DISTRIBUTION=$OS_DISTRIBUTION"
        echo "DISTRO_VERSION=$DISTRO_VERSION"
        echo "RUNTIME_VERSION=$RUNTIME_VERSION"
        echo "PLATFORM=$PLATFORM"
    }  >> "$ENVFILE"

    ARTIFACTS_DIR="$CODEBUILD_TEMP_DIR/artifacts"
    mkdir -p "$ARTIFACTS_DIR"
    # Run CodeBuild local agent.
    "$(dirname "$0")"/codebuild_build.sh \
        -i "$CODEBUILD_IMAGE_TAG" \
        -a "$ARTIFACTS_DIR" \
        -e "$ENVFILE" \
        -b "$BUILDSPEC_YML" \
        -s "$(dirname $PWD)" \
        -l "$(get_local_agent_image)"
}

main "$@"
