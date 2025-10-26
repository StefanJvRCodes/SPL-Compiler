#!/bin/bash

# Simple compilation script for SPL Compiler
# Compiles all Java files without running anything

echo "======================================================"
echo "           SPL COMPILER - COMPILE ONLY"
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
    echo "✅ All Java files compiled successfully!"
    echo
    echo "[2] Copying .class files to tests directory for direct usage..."
    cp core_compiler/*.class tests/
    cp spl_types/*.class tests/
    cp spl_scopes/*.class tests/
    cp code_generation/*.class tests/
    echo "✅ Class files copied to tests directory!"
    echo
    echo "SPL Compiler is ready to use."
    echo
    echo "Usage from tests directory:"
    echo "  cd tests"
    echo "  java SPLInterface filename.txt"
    echo
    echo "Usage from project root:"
    echo "  java -cp \".:core_compiler:tests:spl_types:spl_scopes:code_generation\" SPLInterface tests/filename.txt"
else
    echo "❌ Compilation failed!"
    exit 1
fi