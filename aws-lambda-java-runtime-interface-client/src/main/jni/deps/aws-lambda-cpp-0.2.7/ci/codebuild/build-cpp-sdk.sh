#!/bin/bash

set -euo pipefail

# build the AWS C++ SDK
cd /aws-sdk-cpp
git pull
mkdir build
cd build
cmake .. -GNinja -DBUILD_ONLY="lambda" \
    -DCMAKE_BUILD_TYPE=Release \
    -DENABLE_UNITY_BUILD=ON \
    -DBUILD_SHARED_LIBS=ON \
    -DENABLE_TESTING=OFF \
    -DCMAKE_INSTALL_PREFIX=/install $@
ninja
ninja install
