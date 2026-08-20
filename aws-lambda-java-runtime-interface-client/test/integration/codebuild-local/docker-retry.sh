#!/bin/bash
# Copyright 2026 Amazon.com, Inc. or its affiliates. All Rights Reserved.

set -uo pipefail

MAX_ATTEMPTS="${RETRY_MAX_ATTEMPTS:-5}"
BASE_DELAY="${RETRY_BASE_DELAY:-5}"
MAX_DELAY="${RETRY_MAX_DELAY:-60}"

if (( $# == 0 )); then
    >&2 echo "usage: docker-retry.sh <command> [args...]"
    exit 2
fi

attempt=1
while true; do
    "$@" && exit 0
    status=$?

    if (( attempt >= MAX_ATTEMPTS )); then
        >&2 echo "docker-retry: '$*' failed after ${attempt} attempt(s) (exit ${status}); giving up."
        exit "$status"
    fi

    # Exponential backoff: BASE_DELAY * 2^(attempt-1), capped at MAX_DELAY.
    backoff=$(( BASE_DELAY * (2 ** (attempt - 1)) ))
    (( backoff > MAX_DELAY )) && backoff=$MAX_DELAY
    # Full jitter: wait a random duration in [0, backoff] so concurrent jobs
    # spread out instead of retrying at the same moment.
    delay=$(( RANDOM % (backoff + 1) ))

    >&2 echo "docker-retry: '$*' failed (exit ${status}); attempt ${attempt}/${MAX_ATTEMPTS}, retrying in ${delay}s."
    sleep "$delay"
    attempt=$(( attempt + 1 ))
done
