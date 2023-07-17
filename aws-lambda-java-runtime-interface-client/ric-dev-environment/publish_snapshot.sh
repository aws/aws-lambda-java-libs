#!/bin/bash -x

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

CLASSIFIERS_ARRAY=("linux-x86_64" "linux_musl-x86_64" "linux-aarch_64" "linux_musl-aarch_64")

for str in "${CLASSIFIERS_ARRAY[@]}"; do
  FILES="${FILES}target/aws-lambda-java-runtime-interface-client-$projectVersion-$str.jar,"
  CLASSIFIERS="${CLASSIFIERS}${str},"
  TYPES="${TYPES}jar,"
done

# remove the last ","
FILES=${FILES%?}
CLASSIFIERS=${CLASSIFIERS%?}
TYPES=${TYPES%?}

mvn -B -X -P ci-repo \
    deploy:deploy-file \
    -DgroupId=com.amazonaws \
    -DartifactId=aws-lambda-java-runtime-interface-client \
    -Dpackaging=jar \
    -Dversion=$projectVersion \
    -Dfile=./target/aws-lambda-java-runtime-interface-client-$projectVersion.jar \
    -Dfiles=$FILES \
    -Dclassifiers=$CLASSIFIERS \
    -Dtypes=$TYPES \
    -DpomFile=pom.xml \
    -DrepositoryId=ci-repo -Durl=$MAVEN_REPO_URL \
    --settings ric-dev-environment/settings.xml

if [ -f pom.xml.versionsBackup ]; then
  mv pom.xml.versionsBackup pom.xml
fi
