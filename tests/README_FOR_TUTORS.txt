SPL COMPILER SUBMISSION - TUTOR INSTRUCTIONS
===========================================

PROJECT TYPE: Type A - Complete compiler with executable BASIC code generation
SUBMISSION STATUS: COMPLETE

USAGE:
------
java SPLInterface <input_file.txt>

EXAMPLE:
--------
java SPLInterface simple_program.txt
java SPLInterface lexical_error.txt
java SPLInterface syntax_error.txt

PROVIDED TEST FILES:
-------------------
- simple_program.txt    (Valid SPL program)
- variable_test.txt     (Valid SPL program with multiple variables)
- print_test.txt        (Valid SPL program with print statements)
- example.txt           (Valid SPL program)
- lexical_error.txt     (Contains lexical errors)
- syntax_error.txt      (Contains syntax errors)
- naming_error.txt      (Contains naming/scope errors)

OUTPUT:
-------
- Required submission messages for each compilation phase
- Detailed compilation pipeline visualization
- Generated BASIC code in *_output.txt files (for successful compilations)

EXPECTED MESSAGES (Type A Project):
----------------------------------
1. "Tokens accepted" (or "Lexical error: ...")
2. "Syntax accepted" (or "Syntax error: ...")
3. "Variable Naming and Function Naming accepted" (or "Naming error: ...")
4. "Types accepted" (or "Type error: ...")
5. BASIC code generation with output file creation

SYSTEM REQUIREMENTS:
-------------------
- Java 8 or higher
- All .class files must be in same directory as SPLInterface.java

SUBMISSION READY: All required files included, no additional setup needed.