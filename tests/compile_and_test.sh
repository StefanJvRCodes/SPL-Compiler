#!/bin/bash

# Compile and test script for tests directory
# Usage: ./compile_and_test.sh filename.txt

if [ $# -eq 0 ]; then
    echo "Usage: ./compile_and_test.sh <file.txt>"
    echo "Example: ./compile_and_test.sh while_test.txt"
    exit 1
fi

# Go to project root
cd ..

# Compile if needed
if [ ! -f "tests/SPLInterface.class" ]; then
    echo "Compiling SPL Compiler..."
    ./compile.sh
    echo
fi

# Run the test
echo "Testing: $1"
echo "================================"
java -cp ".:core_compiler:tests:spl_types:spl_scopes:code_generation" SPLInterface "tests/$1"