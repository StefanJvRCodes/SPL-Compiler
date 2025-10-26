#!/bin/bash

# Compile SPLCompilerSubmission with proper classpath
echo "Compiling SPLCompilerSubmission..."

# First compile all dependencies
cd ..
javac -cp "core_compiler:spl_types:spl_scopes:code_generation" \
    core_compiler/*.java \
    spl_types/*.java \
    spl_scopes/*.java \
    code_generation/*.java

# Then compile SPLCompilerSubmission
cd tests
javac -cp "../core_compiler:../spl_types:../spl_scopes:../code_generation" SPLCompilerSubmission.java

if [ $? -eq 0 ]; then
    echo "✅ SPLCompilerSubmission compiled successfully!"
    echo "Usage: java -cp \"../core_compiler:../spl_types:../spl_scopes:../code_generation:.\" SPLCompilerSubmission <input_file.txt>"
else
    echo "❌ Compilation failed!"
    exit 1
fi