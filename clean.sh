#!/bin/bash

# Clean script for SPL Compiler
# Removes all compiled .class files and temporary files

echo "======================================================"
echo "           SPL COMPILER - CLEAN PROJECT"
echo "======================================================"

# Get script directory to ensure proper paths
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "[1] Finding compiled files to remove..."

# Count .class files before deletion
CLASS_COUNT=$(find . -name "*.class" -type f | wc -l)
OUTPUT_COUNT=$(find . -name "*_output.txt" -type f | wc -l)

if [ $CLASS_COUNT -eq 0 ] && [ $OUTPUT_COUNT -eq 0 ]; then
    echo "✅ Project is already clean - no files to remove."
    echo
    echo "To compile the project, run:"
    echo "  ./compile.sh"
    exit 0
fi

echo "Found files to clean:"
echo "  - $CLASS_COUNT .class files"
echo "  - $OUTPUT_COUNT output files"
echo

echo "[2] Removing compiled files..."

# Remove all .class files including copies in tests directory
find . -name "*.class" -type f -delete

# Remove generated output files
find . -name "*_output.txt" -type f -delete

# Remove any temporary files that might exist
find . -name "*.tmp" -type f -delete 2>/dev/null || true

echo "✅ Cleanup completed successfully!"
echo

# Verify cleanup
REMAINING_CLASS=$(find . -name "*.class" -type f | wc -l)
REMAINING_OUTPUT=$(find . -name "*_output.txt" -type f | wc -l)

if [ $REMAINING_CLASS -eq 0 ] && [ $REMAINING_OUTPUT -eq 0 ]; then
    echo "✅ Project is now clean."
    echo "  - Removed $CLASS_COUNT .class files"
    echo "  - Removed $OUTPUT_COUNT output files"
else
    echo "⚠️  Warning: Some files may not have been removed."
fi

echo
echo "To recompile the project, run:"
echo "  ./compile.sh"
echo
echo "To compile and run tests, use:"
echo "  ./compile_and_run.sh test"