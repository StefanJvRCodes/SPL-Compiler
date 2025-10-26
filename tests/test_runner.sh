#!/bin/bash

echo "======================================================"
echo "           SPL COMPILER CODE GENERATION TESTS"
echo "======================================================"
echo

# Compile the Java files
echo "[1] Compiling SPL Compiler..."
javac *.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed!"
    exit 1
fi
echo "✅ Compilation successful!"
echo

# Test 1: Original example
echo "[2] Testing original example.spl..."
echo "------------------------------------------------------"
java SPLCompiler example.spl
echo

# Test 2: Comprehensive test
echo "[3] Testing comprehensive_test.spl..."
echo "------------------------------------------------------"
java SPLCompiler comprehensive_test.spl
echo

# Test 3: Simple assignment test
echo "[4] Creating and testing simple assignment..."
cat > simple_test.spl << 'EOF'
glob { x y } 
proc { } 
func { } 
main { 
    var { a b } 
    a = 10 ; 
    b = x ; 
    print a ; 
    print b ; 
    halt 
}
EOF

echo "Content of simple_test.spl:"
echo "------------------------------------------------------"
cat simple_test.spl
echo
echo "Running simple_test.spl:"
echo "------------------------------------------------------"
java SPLCompiler simple_test.spl
echo

# Test 4: Multiple prints test
echo "[5] Creating and testing multiple prints..."
cat > print_test.spl << 'EOF'
glob { num } 
proc { } 
func { } 
main { 
    var { value } 
    value = 123 ; 
    print value ; 
    print 456 ; 
    print num ; 
    halt 
}
EOF

echo "Content of print_test.spl:"
echo "------------------------------------------------------"
cat print_test.spl
echo
echo "Running print_test.spl:"
echo "------------------------------------------------------"
java SPLCompiler print_test.spl
echo

echo "======================================================"
echo "                 ALL TESTS COMPLETED"
echo "======================================================"