#!/bin/bash

# SPL Compiler wrapper script for tests directory
# Allows running SPL compiler from tests/ directory with proper classpath

if [ $# -eq 0 ]; then
    echo "Usage: ./spl.sh <file.spl>"
    echo "Example: ./spl.sh while_test.txt"
    exit 1
fi

# Get the tests directory and project root
TESTS_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$TESTS_DIR")"

# Change to project root for proper classpath resolution
cd "$PROJECT_ROOT"

# Check if classes are compiled
if [ ! -f "tests/SPLInterface.class" ]; then
    echo "Compiling SPL Compiler..."
    ./compile.sh
    echo
fi

# Run the SPL compiler with proper classpath
echo "Running SPL Compiler on: $1"
echo "========================================"
java -cp ".:core_compiler:tests:spl_types:spl_scopes:code_generation" SPLInterface "tests/$1"