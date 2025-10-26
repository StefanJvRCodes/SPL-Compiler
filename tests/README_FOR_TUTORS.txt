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


OUTPUT:
-------
- Required submission messages for each compilation phase
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
- Java 23 or higher
- All .class files must be in same directory as SPLInterface.java

SUBMISSION READY: All required files included, no additional setup needed.

Project type "A"