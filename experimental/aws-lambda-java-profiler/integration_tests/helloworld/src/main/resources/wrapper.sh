#!/bin/bash

# the path to the interpreter and all of the originally intended arguments
args=("$@")

# the extra options to pass to the interpreter
echo "${args[@]}"

# start the runtime with the extra options
exec "${args[@]}"