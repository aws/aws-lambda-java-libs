#!/bin/bash

set -e

projectVersion=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)


# test uber jar
mvn -B -X -P ci-repo \
    dependency:get \
    -DremoteRepositories=ci-repo::::$MAVEN_REPO_URL \
    -Dartifact=com.amazonaws:aws-lambda-java-runtime-interface-client:${projectVersion}-SNAPSHOT \
    -Dtransitive=false \
    --settings ric-dev-environment/settings.xml


PLATFORM_ARRAY=("linux-x86_64" "linux_musl-x86_64" "linux-aarch_64" "linux_musl-aarch_64")

for classifier in "${PLATFORM_ARRAY[@]}"; do
  # Test platform specific jar
  mvn -B -P ci-repo \
      dependency:get \
      -DremoteRepositories=ci-repo::::$MAVEN_REPO_URL \
      -Dartifact=com.amazonaws:aws-lambda-java-runtime-interface-client:${projectVersion}-SNAPSHOT:jar:${classifier} \
      -Dtransitive=false \
      --settings ric-dev-environment/settings.xml
done