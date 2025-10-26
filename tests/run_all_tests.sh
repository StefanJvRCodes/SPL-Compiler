#!/bin/bash

echo "======================================================"
echo "           SPL COMPILER - AUTOMATED TESTING"
echo "======================================================"

# Compile all Java files
echo "[1] Compiling Java files..."
javac *.java

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo
    
    echo "[2] Running all tests..."
    echo
    java TestRunner
else
    echo "❌ Java compilation failed!"
    exit 1
fi