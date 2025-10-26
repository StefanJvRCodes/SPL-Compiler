SPL COMPILER - COMPLETE CLASSPATH SOLUTION
==========================================

PROBLEM SOLVED: No more "NoClassDefFoundError: TokenFeeder" errors!

SOLUTION: Use these scripts from the tests/ directory:

1. SIMPLE RUNNER (tests/run.sh):
   ./run.sh filename.txt
   
   Examples:
   ./run.sh while_test.txt
   ./run.sh print_test.txt
   ./run.sh syntax_error.txt

2. COMPILE AND TEST (tests/compile_and_test.sh):
   ./compile_and_test.sh filename.txt
   
   - Automatically compiles if needed
   - Then runs the test
   
   Examples:
   ./compile_and_test.sh while_test.txt
   ./compile_and_test.sh print_test.txt

HOW IT WORKS:
- Scripts automatically navigate to project root
- Set correct classpath: ".:core_compiler:tests:spl_types:spl_scopes:code_generation"
- Run SPLInterface with proper file path: "tests/filename.txt"

NO MORE MANUAL CLASSPATHS NEEDED!
=================================

Old way (ERROR):
  java SPLInterface while_test.txt

New way (WORKS):
  ./run.sh while_test.txt