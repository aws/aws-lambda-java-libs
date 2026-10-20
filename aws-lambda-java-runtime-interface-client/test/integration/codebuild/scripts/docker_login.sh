# Copyright 2026 Amazon.com, Inc. or its affiliates. All Rights Reserved.
#
# Docker registry logins for the buildspec install phase. Source it, don't run
# it (". path/to/docker_login.sh"): when the ECR pull-through cache login fails
# it unsets BASE_REGISTRY in the caller's shell, so the JNI and function images
# fall back to public.ecr.aws.
# POSIX sh, no "exit" and no "set -e": it runs in the buildspec's shell.

# docker_login <registry> <username> <password>
# Skips the login when any argument is empty. A failed or skipped login only
# returns non-zero; the caller decides what to fall back to.
docker_login() {
    if [ -z "${1:-}" ] || [ -z "${2:-}" ] || [ -z "${3:-}" ]; then
        echo "No credentials for $1; continuing without docker login."
        return 1
    fi
    if printf '%s' "$3" | docker login --username "$2" --password-stdin "$1"; then
        echo "Logged in to $1."
    else
        echo "Docker login to $1 failed; continuing without it."
        return 1
    fi
}

# ECR pull-through cache. BASE_REGISTRY and ECR_PASSWORD come from test_one.sh
# when the runner is logged in to it (.github/actions/ecr-pull-through-login);
# fork PRs get neither.
if [ -z "${BASE_REGISTRY:-}" ] || ! docker_login "${BASE_REGISTRY%%/*}" AWS "${ECR_PASSWORD:-}"; then
    echo "No ECR pull-through cache login; base images come from public.ecr.aws."
    unset BASE_REGISTRY
fi
unset ECR_PASSWORD

# Docker Hub, when the CodeBuild project sets credentials.
docker_login docker.io "${DOCKERHUB_USERNAME:-}" "${DOCKERHUB_PASSWORD:-}" || true
