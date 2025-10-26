#!/bin/bash

echo "======================================================"
echo "           SPL COMPILER - AUTOMATED TESTING"
echo "======================================================"

# Compile all Java files
echo "[1] Compiling Java files..."
cd ..
javac -cp "core_compiler:tests:spl_types:spl_scopes:code_generation" core_compiler/*.java tests/*.java spl_types/*.java spl_scopes/*.java code_generation/*.java
cd tests

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo
    
    echo "[2] Running all tests..."
    echo
    java -cp "../core_compiler:../tests:../spl_types:../spl_scopes:../code_generation" TestRunner
else
    echo "❌ Java compilation failed!"
    exit 1
fi