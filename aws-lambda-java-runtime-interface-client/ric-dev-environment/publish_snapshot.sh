#!/bin/bash

set -e

projectVersion=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
if [[ -z ${ENABLE_SNAPSHOT} ]]; then
  echo "Skipping SNAPSHOT deployment, as ENABLE_SNAPSHOT environment variable is not defined"
  exit
fi

echo "Deploying SNAPSHOT artifact"
if [[ ${projectVersion} != *"SNAPSHOT"* ]]; then
    snapshotProjectVersion="${projectVersion}-SNAPSHOT"
    echo "projectVersion: ${projectVersion}"
    echo "snapshotProjectVersion: ${snapshotProjectVersion}"
    mvn versions:set "-DnewVersion=${snapshotProjectVersion}"
else
    echo "Already -SNAPSHOT version"
fi

mvn -P ci-repo deploy --settings ric-dev-environment/settings.xml
mv pom.xml.versionsBackup pom.xml
