#!/bin/bash
# Copyright 2026 Amazon.com, Inc. or its affiliates. All Rights Reserved.

set -uo pipefail

# Ladder of base wait times (in seconds) between successive attempts.
# Default: 1 min, 2 min, 5 min — 4 attempts total (initial + 3 retries).
# public.ecr.aws / Docker Hub rate-limit windows recover on a minute-scale
# timer, so short exponential backoff (5s..60s) tends to exhaust its budget
# before the rate-limit slot reopens.
IFS=' ' read -r -a DELAYS <<< "${RETRY_DELAYS:-60 120 300}"
JITTER_MAX="${RETRY_JITTER_MAX:-30}"
MAX_ATTEMPTS=$(( ${#DELAYS[@]} + 1 ))

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

    # Base delay from the ladder + additive jitter in [0, JITTER_MAX] so
    # concurrent shards don't retry in the exact same second.
    base=${DELAYS[$((attempt - 1))]}
    jitter=$(( RANDOM % (JITTER_MAX + 1) ))
    delay=$(( base + jitter ))

    >&2 echo "docker-retry: '$*' failed (exit ${status}); attempt ${attempt}/${MAX_ATTEMPTS}, retrying in ${delay}s (${base}s + ${jitter}s jitter)."
    sleep "$delay"
    attempt=$(( attempt + 1 ))
done
