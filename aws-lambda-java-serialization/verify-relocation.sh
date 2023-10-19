#!/bin/bash
# Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

# This script runs after the aws-lambda-java-serialization package phase. It verifies that no unexpected transitive
# dependencies were missed from the relocation of third party classes.

set -euo pipefail

ARTIFACT_PATH=${1}
RELOCATION_PREFIX=${2}
SERIALIZATION_MODULE_PATTERN=${3}

echo 'Validating that serialization module classes were not relocated'
OUTPUT=$(zipinfo ${ARTIFACT_PATH} | grep '.class' | grep ${RELOCATION_PREFIX//.//} | grep ${SERIALIZATION_MODULE_PATTERN//.//} || true)
if [[ ! -z "$OUTPUT" ]]; then
    echo "Serialization module classes were unexpectedly relocated"
    echo ${OUTPUT}
    exit 1
fi

echo 'Validating that everything other than serialization module classes were relocated'
OUTPUT=$(zipinfo ${ARTIFACT_PATH} | grep '.class' | grep -v ${SERIALIZATION_MODULE_PATTERN//.//} | grep -v 'META-INF' | grep -v ${RELOCATION_PREFIX//.//} || true)
if [[ ! -z "$OUTPUT" ]]; then
    echo "Some classes were not relocated"
    echo ${OUTPUT}
    exit 1
fi

echo 'Validating that META-INF/services were relocated'
OUTPUT=$(zipinfo ${ARTIFACT_PATH} | grep 'META-INF/services/.\+' | grep -v ${RELOCATION_PREFIX} || true)
if [[ ! -z "$OUTPUT" ]]; then
    echo "Some meta-inf services were not relocated"
    echo ${OUTPUT}
    exit 1
fi
