#!/bin/bash -x
# Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

set -euo pipefail

SRC_DIR=$(dirname "$0")
DST_DIR=${1}
MULTI_ARCH=${2}
BUILD_OS=${3}
BUILD_ARCH=${4}
CURL_VERSION=7.83.1

BASE_REGISTRY="${BASE_REGISTRY:-public.ecr.aws}"
AWS_REGION="${AWS_REGION:-${AWS_DEFAULT_REGION:-}}"

# aws-lambda-cpp is consumed as the prebuilt static library published on the
# upstream GitHub release rather than being compiled from a vendored source
# tree. We fetch and GPG-verify it
ALC_VERSION="1.0.1"
ALC_TAG="v${ALC_VERSION}"
ALC_REPO_URL="https://github.com/awslabs/aws-lambda-cpp"
ALC_RELEASE_URL="${ALC_REPO_URL}/releases/download/${ALC_TAG}"
ALC_SIGNING_KEY_URL="https://raw.githubusercontent.com/awslabs/aws-lambda-cpp/${ALC_TAG}/signing-public-key.asc"
ALC_STAGE_DIR="${SRC_DIR}/deps/aws-lambda-cpp"

function fetch_aws_lambda_cpp() {
  arch=$1

  release_arch="${arch/aarch_64/aarch64}"

  if [ -f "${ALC_STAGE_DIR}/.staged-arch" ] && \
     [ "$(cat "${ALC_STAGE_DIR}/.staged-arch")" == "${release_arch}" ]; then
    echo "aws-lambda-cpp ${ALC_VERSION} (${release_arch}) already staged, skipping fetch"
    return
  fi

  echo "Fetching prebuilt aws-lambda-cpp ${ALC_VERSION} for ${release_arch}"
  rm -rf "${ALC_STAGE_DIR}"
  mkdir -p "${ALC_STAGE_DIR}/lib" "${ALC_STAGE_DIR}/include"

  local workdir
  workdir=$(mktemp -d)
  local lib_asset="libaws-lambda-runtime-${release_arch}.a"

  curl -fsSL -o "${workdir}/${lib_asset}"     "${ALC_RELEASE_URL}/${lib_asset}"
  curl -fsSL -o "${workdir}/${lib_asset}.asc" "${ALC_RELEASE_URL}/${lib_asset}.asc"
  curl -fsSL -o "${workdir}/SHA256SUMS"       "${ALC_RELEASE_URL}/SHA256SUMS"
  curl -fsSL -o "${workdir}/SHA256SUMS.asc"   "${ALC_RELEASE_URL}/SHA256SUMS.asc"
  curl -fsSL -o "${workdir}/signing-key.asc"  "${ALC_SIGNING_KEY_URL}"

  local keyring
  keyring=$(mktemp)
  gpg --dearmor < "${workdir}/signing-key.asc" > "${keyring}"
  gpgv --keyring "${keyring}" "${workdir}/${lib_asset}.asc" "${workdir}/${lib_asset}"
  gpgv --keyring "${keyring}" "${workdir}/SHA256SUMS.asc"   "${workdir}/SHA256SUMS"
  rm -f "${keyring}"

  # Cross-check the checksum too (defence in depth; SHA256SUMS is itself signed).
  ( cd "${workdir}" && grep "${lib_asset}\$" SHA256SUMS | sha256sum -c - )

  cp "${workdir}/${lib_asset}" "${ALC_STAGE_DIR}/lib/libaws-lambda-runtime.a"

  # Headers aren't a release asset, so take them from the source at the same
  # tag. They are declarations only -- every symbol lives in the prebuilt lib.
  curl -fsSL -o "${workdir}/src.tar.gz" "${ALC_REPO_URL}/archive/refs/tags/${ALC_TAG}.tar.gz"
  tar -xzf "${workdir}/src.tar.gz" -C "${workdir}" "aws-lambda-cpp-${ALC_VERSION}/include"
  cp -R "${workdir}/aws-lambda-cpp-${ALC_VERSION}/include/." "${ALC_STAGE_DIR}/include/"

  echo "${release_arch}" > "${ALC_STAGE_DIR}/.staged-arch"
  rm -rf "${workdir}"
}

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

  fetch_aws_lambda_cpp "${arch}"

  docker_platform=$(get_docker_platform ${arch})

  echo "Compiling the native library with libc implementation \`${libc_impl}\` on architecture \`${arch}\` using Docker platform \`${docker_platform}\`"

  if [[ "${MULTI_ARCH}" == "true" ]]; then
      docker build --platform="${docker_platform}" -f "${SRC_DIR}/Dockerfile.${libc_impl}" \
            --build-arg CURL_VERSION=${CURL_VERSION} --build-arg BASE_REGISTRY=${BASE_REGISTRY} --build-arg AWS_REGION=${AWS_REGION} "${SRC_DIR}" -o - \
      | tar -xOf - src/aws-lambda-runtime-interface-client.so > "${artifact}"
  else
      echo "multi-arch not requested, assuming this is a workaround to goofyness when docker buildx is enabled on Linux CI environments."
      echo "enabling docker buildx often updates the docker api version, so assuming that docker cli is also too old to use --output type=tar, so doing alternative build-tag-run approach"
      image_name="lambda-java-jni-lib-${libc_impl}-${arch}"

      # GitHub actions is using dockerx build under the hood. We need to pass --load option to be able to run the image
      # This args is NOT part of the classic docker build command, so we need to check against a GitHub Action env var not to make local build crash.
      if [[ "${GITHUB_RUN_ID:+isset}" == "isset" ]]; then
        EXTRA_LOAD_ARG="--load"
      else
        EXTRA_LOAD_ARG=""
      fi

      docker build --platform="${docker_platform}" \
            -t "${image_name}" \
            -f "${SRC_DIR}/Dockerfile.${libc_impl}" \
            --build-arg CURL_VERSION=${CURL_VERSION} --build-arg BASE_REGISTRY=${BASE_REGISTRY} --build-arg AWS_REGION=${AWS_REGION} "${SRC_DIR}" ${EXTRA_LOAD_ARG}

      echo "Docker image has been successfully built"

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
