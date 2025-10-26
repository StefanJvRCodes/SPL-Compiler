# SPL COMPILER - USER MANUAL

## Project Type Declaration
**PROJECT TYPE: A** - Front-End, Semantic Analysis, and Generation of Executable BASIC Code  
**Maximum Marking Value: 10 Points**  
**Status: COMPLETE**

## System Requirements
- Java Runtime Environment (JRE) 8 or higher
- Input files must have .spl or .txt extension containing valid SPL programs

## Installation & Setup
1. Ensure all Java class files are in the same directory
2. Main executable class: `SPLCompilerSubmission.class`

## Usage Instructions

### Command Line Execution
```bash
java SPLCompilerSubmission <input_file>
```

### Example Usage
```bash
java SPLCompilerSubmission program.spl
java SPLCompilerSubmission test.txt
```

## Expected Output Messages

### Successful Compilation (Type A Project)
For valid SPL programs, the compiler outputs:
1. `Tokens accepted` (Lexical analysis passed)
2. `Syntax accepted` (Parsing passed)  
3. `Variable Naming and Function Naming accepted` (Semantic analysis passed)
4. `Types accepted` (Type checking passed)
5. `Code generation completed successfully`
6. `Executable BASIC code written to: <filename>_output.txt`

### Error Output Messages
- **Lexical Error:** `Lexical error: Invalid token found`
- **Syntax Error:** `Syntax error: Invalid syntax structure`  
- **Naming Error:** `Naming error: Variable or function naming violation detected`
- **Type Error:** `Type error: Type compatibility violation detected`

## Output Files
- **Input:** `program.spl` → **Output:** `program_output.txt`
- **Input:** `test.txt` → **Output:** `test_output.txt`

The generated `.txt` files contain executable BASIC code that can be run with a BASIC emulator.

## Sample Input/Output

### Input (test.spl):
```
glob { } 
proc { } 
func { } 
main { 
    var { num } 
    num = 123 ; 
    print num ; 
    halt 
}
```

### Output Messages:
```
Tokens accepted
Syntax accepted
Variable Naming and Function Naming accepted
Types accepted
Code generation completed successfully
Executable BASIC code written to: test_output.txt
```

### Generated BASIC Code (test_output.txt):
```
num = 123
PRINT num 
STOP
```

## Troubleshooting
- Ensure input file exists and is readable
- Verify Java classpath includes all required .class files
- Input files must contain valid SPL syntax according to language specification

---
**Compiler Version:** Final Submission 2025  
**Project Group:** [Your Group Information]  
**Submission Date:** October 27, 2025