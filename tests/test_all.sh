#!/bin/bash

echo "======================================================"
echo "          SPL COMPILER - TUTOR TEST SCRIPT"
echo "======================================================"
echo

echo "Testing valid programs:"
echo "----------------------"
for file in simple_program.txt variable_test.txt print_test.txt example.txt; do
    if [ -f "$file" ]; then
        echo "Testing $file..."
        java SPLInterface "$file"
        echo
        echo "------------------------------------------------------"
        echo
    fi
done

echo "Testing error cases:"
echo "-------------------"
for file in lexical_error.txt syntax_error.txt naming_error.txt; do
    if [ -f "$file" ]; then
        echo "Testing $file (expecting errors)..."
        java SPLInterface "$file"
        echo
        echo "------------------------------------------------------"
        echo
    fi
done

echo "======================================================"
echo "                 ALL TESTS COMPLETED"
echo "======================================================"