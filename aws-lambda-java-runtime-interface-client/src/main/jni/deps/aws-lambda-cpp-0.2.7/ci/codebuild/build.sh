#!/bin/bash

set -euo pipefail

# build the lambda-runtime
cd $CODEBUILD_SRC_DIR
mkdir build
cd build
cmake .. -GNinja -DBUILD_SHARED_LIBS=ON -DCMAKE_BUILD_TYPE=Debug -DCMAKE_INSTALL_PREFIX=/install $@
ninja
ninja install
