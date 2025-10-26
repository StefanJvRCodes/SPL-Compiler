#!/bin/bash

# Run SPLCompilerSubmission with proper classpath
# Usage: ./run_submission.sh <input_file.txt>

if [ $# -ne 1 ]; then
    echo "Usage: ./run_submission.sh <input_file.txt>"
    exit 1
fi

# Compile if not already compiled
if [ ! -f "SPLCompilerSubmission.class" ]; then
    echo "Compiling SPLCompilerSubmission first..."
    ./compile_submission.sh
    if [ $? -ne 0 ]; then
        echo "Compilation failed!"
        exit 1
    fi
fi

# Run SPLCompilerSubmission
java -cp "../core_compiler:../spl_types:../spl_scopes:../code_generation:." SPLCompilerSubmission "$1"