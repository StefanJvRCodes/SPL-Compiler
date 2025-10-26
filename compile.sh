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
    echo "SPL Compiler is ready to use."
    echo
    echo "Usage examples:"
    echo "  java -cp \".:core_compiler:tests:spl_types:spl_scopes:code_generation\" SPLInterface example.spl"
    echo "  java -cp \".:core_compiler:tests:spl_types:spl_scopes:code_generation\" SPLInterface tests/final_grammar_test.txt"
else
    echo "❌ Compilation failed!"
    exit 1
fi