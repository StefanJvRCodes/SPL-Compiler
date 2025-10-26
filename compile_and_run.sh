#!/bin/bash

# Master compilation and execution script for SPL Compiler
# Can be run from project root

if [ "$1" == "" ]; then
    echo "======================================================"
    echo "           SPL COMPILER - COMPILE ONLY"
    echo "======================================================"
else
    echo "======================================================"
    echo "           SPL COMPILER - COMPILE & RUN"
    echo "======================================================"
fi

# Get script directory to ensure proper paths
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "[1] Compiling all Java files..."
javac -cp "core_compiler:tests:spl_types:spl_scopes:code_generation" \
    core_compiler/*.java \
    tests/*.java \
    spl_types/*.java \
    spl_scopes/*.java \
    code_generation/*.java

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo
    
    if [ "$1" == "" ]; then
        echo "All Java files compiled successfully."
        echo "Ready to use SPL compiler with:"
        echo "  java -cp \".:core_compiler:tests:spl_types:spl_scopes:code_generation\" SPLInterface <file.spl>"
    elif [ "$1" == "test" ]; then
        echo "[2] Running tests..."
        java -cp ".:core_compiler:tests:spl_types:spl_scopes:code_generation" CodeTestRunner
    elif [ "$1" == "compile" ] && [ -n "$2" ]; then
        echo "[2] Compiling $2..."
        java -cp ".:core_compiler:tests:spl_types:spl_scopes:code_generation" SPLInterface "$2"
    else
        echo "Usage:"
        echo "  ./compile_and_run.sh                   # Compile all Java files only"
        echo "  ./compile_and_run.sh test              # Compile and run all tests"
        echo "  ./compile_and_run.sh compile <file>    # Compile and run SPL file"
    fi
else
    echo "❌ Compilation failed!"
    exit 1
fi