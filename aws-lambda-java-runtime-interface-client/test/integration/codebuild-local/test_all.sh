#!/bin/bash
# Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

set -euo pipefail

CODEBUILD_IMAGE_TAG="${CODEBUILD_IMAGE_TAG:-al2/x86_64/standard/3.0}"
DRYRUN="${DRYRUN-0}"

function usage {
    echo "usage: test_all.sh buildspec_yml_dir"
    echo "Runs all buildspec build-matrix combinations via test_one.sh."
    echo "Required:"
    echo "  buildspec_yml_dir      Used to specify the CodeBuild buildspec template file."
}

do_one_yaml() {
    local -r YML="$1"

    OS_DISTRIBUTION=$(grep -oE 'OS_DISTRIBUTION:\s*(\S+)' "$YML" | cut -d' ' -f2)
    DISTRO_VERSIONS=$(sed '1,/DISTRO_VERSION/d;/RUNTIME_VERSION/,$d' "$YML" | tr -d '\-" ')
    RUNTIME_VERSIONS=$(sed '1,/RUNTIME_VERSION/d;/PLATFORM/,$d' "$YML" | sed '/#.*$/d' |  tr -d '\-" ')
    PLATFORMS=$(sed '1,/PLATFORM/d;/phases/,$d' "$YML" |  tr -d '\-" ')

    for DISTRO_VERSION in $DISTRO_VERSIONS; do
      for RUNTIME_VERSION in $RUNTIME_VERSIONS; do
        for PLATFORM in $PLATFORMS; do
          if (( DRYRUN == 1 )); then
            echo DRYRUN test_one_combination "$YML" "$OS_DISTRIBUTION" "$DISTRO_VERSION" "$RUNTIME_VERSION" "$PLATFORM"
          else
            test_one_combination "$YML" "$OS_DISTRIBUTION" "$DISTRO_VERSION" "$RUNTIME_VERSION" "$PLATFORM"
          fi
        done
      done
    done
}

test_one_combination() {
    local -r YML="$1"
    local -r OS_DISTRIBUTION="$2"
    local -r DISTRO_VERSION="$3"
    local -r RUNTIME_VERSION="$4"
    local -r PLATFORM="$5"
    local -r PLATFORM_SANITIZED=$(echo "$PLATFORM" | tr "/" ".")

    echo Testing:
    echo "  BUILDSPEC" "$YML"
    echo "  with" "$OS_DISTRIBUTION"-"$DISTRO_VERSION" "$RUNTIME_VERSION" "$PLATFORM"

    "$(dirname "$0")"/test_one.sh "$YML" "$OS_DISTRIBUTION" "$DISTRO_VERSION" "$RUNTIME_VERSION" "$PLATFORM" \
        > >(sed "s/^/$OS_DISTRIBUTION$DISTRO_VERSION-$RUNTIME_VERSION-$PLATFORM_SANITIZED: /") 2> >(sed "s/^/$OS_DISTRIBUTION-$DISTRO_VERSION:$RUNTIME_VERSION:$PLATFORM_SANITIZED: /" >&2)
}

main() {
    if (( $# != 1 && $# != 2)); then
        >&2 echo "Invalid number of parameters."
        usage
        exit 1
    fi
    BUILDSPEC_YML_DIR="$1"
    HAS_YML=0
    for f in "$BUILDSPEC_YML_DIR"/*.yml ; do
        [ -f "$f" ] || continue;
        do_one_yaml "$f"
        HAS_YML=1
    done

    if (( HAS_YML == 0 )); then
        >&2 echo At least one buildspec is required.
        exit 2
    fi
}

main "$@"
