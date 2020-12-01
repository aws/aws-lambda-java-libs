#!/bin/bash

set -euo pipefail

CLANG_FORMAT=clang-format

if NOT type $CLANG_FORMAT > /dev/null 2>&1; then
    echo "No appropriate clang-format found."
    exit 1
fi

FAIL=0
SOURCE_FILES=$(find src include tests -type f -name "*.h" -o -name "*.cpp")
for i in $SOURCE_FILES
do
    if [ $($CLANG_FORMAT -output-replacements-xml $i | grep -c "<replacement ") -ne 0 ]
    then
        echo "$i failed clang-format check."
        FAIL=1
    fi
done

exit $FAIL

