#!/bin/bash

set -e

projectVersion="2.4.1"

# test uber jar
mvn -B -X \
    dependency:get \
    -Dartifact=com.amazonaws:aws-lambda-java-runtime-interface-client:${projectVersion} \
    -Dtransitive=false


PLATFORM_ARRAY=("linux-x86_64" "linux_musl-x86_64" "linux-aarch_64" "linux_musl-aarch_64")

for classifier in "${PLATFORM_ARRAY[@]}"; do
  # Test platform specific jar
  mvn -B \
      dependency:get \
      -Dartifact=com.amazonaws:aws-lambda-java-runtime-interface-client:${projectVersion}:jar:${classifier} \
      -Dtransitive=false
done