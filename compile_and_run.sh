#!/bin/bash

# Master compilation and execution script for SPL Compiler
# Can be run from project root

echo "======================================================"
echo "           SPL COMPILER - COMPILE & RUN"
echo "======================================================"

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
    
    if [ "$1" == "test" ]; then
        echo "[2] Running tests..."
        java -cp "core_compiler:tests:spl_types:spl_scopes:code_generation" CodeTestRunner
    elif [ "$1" == "compile" ] && [ -n "$2" ]; then
        echo "[2] Compiling $2..."
        java -cp "core_compiler:tests:spl_types:spl_scopes:code_generation" SPLCompiler "$2"
    else
        echo "Usage:"
        echo "  ./compile_and_run.sh test              # Run all tests"
        echo "  ./compile_and_run.sh compile <file>    # Compile SPL file"
        echo "  ./compile_and_run.sh                   # Just compile all Java files"
    fi
else
    echo "❌ Compilation failed!"
    exit 1
fi