#!/bin/bash

# Simple runner for SPL files from tests directory
# Usage: ./run.sh filename.txt

if [ $# -eq 0 ]; then
    echo "Usage: ./run.sh <file.txt>"
    echo "Example: ./run.sh while_test.txt"
    exit 1
fi

# Go to project root and run with proper classpath
cd ..
java -cp ".:core_compiler:tests:spl_types:spl_scopes:code_generation" SPLInterface "tests/$1"