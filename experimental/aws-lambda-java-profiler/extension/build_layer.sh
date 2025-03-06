# Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
# SPDX-License-Identifier: MIT-0

./gradlew :shadowJar

chmod +x extensions/profiler-extension
archive="extension.zip"
if [ -f "$archive" ] ; then
    rm "$archive"
fi

zip "$archive" -j build/libs/profiler-extension.jar
zip "$archive" extensions/*