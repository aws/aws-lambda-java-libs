#!/bin/bash -x
# Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

set -euo pipefail

SRC_DIR=$(dirname "$0")
DST_DIR=${1}
MULTI_ARCH=${2}
BUILD_OS=${3}
BUILD_ARCH=${4}
CURL_VERSION=7.83.1

function get_docker_platform() {
  arch=$1

  if [ "${arch}" == "x86_64" ]; then
    echo "linux/amd64"
  elif [ "${arch}" == "aarch_64" ]; then
    echo "linux/arm64/v8"
  else
    echo "UNKNOWN_DOCKER_PLATFORM"
  fi
}

function get_target_os() {
  libc_impl=$1

  if [ "${libc_impl}" == "glibc" ]; then
    echo "linux"
  elif [ "${libc_impl}" == "musl" ]; then
    echo "linux_musl"
  else
    echo "UNKNOWN_OS"
  fi
}

function build_for_libc_arch() {
  libc_impl=$1
  arch=$2
  artifact=$3

  docker_platform=$(get_docker_platform ${arch})

  echo "Compiling the native library with libc implementation \`${libc_impl}\` on architecture \`${arch}\` using Docker platform \`${docker_platform}\`"

  if [[ "${MULTI_ARCH}" == "true" ]]; then
      docker build --platform="${docker_platform}" -f "${SRC_DIR}/Dockerfile.${libc_impl}" \
            --build-arg CURL_VERSION=${CURL_VERSION} "${SRC_DIR}" -o - \
      | tar -xOf - src/aws-lambda-runtime-interface-client.so > "${artifact}"
  else
      echo "multi-arch not requested, assuming this is a workaround to goofyness when docker buildx is enabled on Linux CI environments."
      echo "enabling docker buildx often updates the docker api version, so assuming that docker cli is also too old to use --output type=tar, so doing alternative build-tag-run approach"
      image_name="lambda-java-jni-lib-${libc_impl}-${arch}"
      docker build --platform="${docker_platform}" \
            -t "${image_name}" \
            -f "${SRC_DIR}/Dockerfile.${libc_impl}" \
            --build-arg CURL_VERSION=${CURL_VERSION} "${SRC_DIR}"

      docker run --rm --entrypoint /bin/cat "${image_name}" \
            /src/aws-lambda-runtime-interface-client.so > "${artifact}"
  fi

  [ -f "${artifact}" ]

  # file -b ${artifact} produces lines like this:
  #     x86_64:  ELF 64-bit LSB shared object, x86-64, version 1 (GNU/Linux), dynamically linked, BuildID[sha1]=582888b42da34895828e1281cbbae15d279175b7, not stripped
  #   aarch_64:  ELF 64-bit LSB shared object, ARM aarch64, version 1 (GNU/Linux), dynamically linked, BuildID[sha1]=fa54218974fb2c17772b6acf22467a2c67a87011, not stripped
  # we need to ensure it has the expected architecture in it
  #
  # cut -d "," -f2 will extract second field (' x86-64' or ' ARM aarch64')
  # tr -d '-' removes '-', so we'll have (' x8664' or ' ARM aarch64')
  # grep -q is for quiet mode, no output
  # ${arch//_} removes '_' chars from the `aarch` variable, (aarch_64 => aarch64, x86_64 => x8664)
  if ! file -b "${artifact}" | cut -d "," -f2 | tr -d '-' | grep -q "${arch//_}"; then
      echo "${artifact} did not appear to be the correct architecture, check that Docker buildx is enabled"
      exit 1
  fi
}

function get_target_artifact() {
  target_os=$1
  target_arch=$2

  target_file="${DST_DIR}/classes/jni/libaws-lambda-jni.${target_os}-${target_arch}.so"
  target_dir=$(dirname "$target_file")
  mkdir -p "$target_dir"
  echo "$target_file"
}



if [ -n "$BUILD_OS" ] && [ -n "$BUILD_ARCH" ]; then
  # build for the specified arch and libc implementation
  libc_impl="glibc"
  if [ "$BUILD_OS" == "linux_musl" ]; then
    libc_impl="musl"
  fi
  target_artifact=$(get_target_artifact "$BUILD_OS" "$BUILD_ARCH")
  build_for_libc_arch "$libc_impl" "$BUILD_ARCH" "$target_artifact"
else
  # build for all architectures and libc implementations
  declare -a ARCHITECTURES=("x86_64" "aarch_64")
  declare -a LIBC_IMPLS=("glibc" "musl")

  for arch in "${ARCHITECTURES[@]}"; do

      if [[ "${MULTI_ARCH}" != "true" ]] && [[ "$(arch)" != "${arch}" ]]; then
          echo "multi arch build not requested and host arch is $(arch), so skipping ${arch}..."
          continue
      fi

      for libc_impl in "${LIBC_IMPLS[@]}"; do
        target_os=$(get_target_os $libc_impl)
        target_artifact=$(get_target_artifact "$target_os" "$arch")
        build_for_libc_arch "$libc_impl" "$arch" "$target_artifact"
      done

  done
fi
