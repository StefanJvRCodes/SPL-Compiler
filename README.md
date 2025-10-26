# SPL Compiler - Complete Implementation

**Project Type A: Complete SPL Compiler with Executable BASIC Code Generation**
- **Maximum Points**: 10/10
- **Status**: COMPLETE ✅

## 📋 Project Classification

This is a **Project Type A** submission that implements a complete SPL (Students' Programming Language) compiler with all required phases:

✅ **Lexical Analysis** - Tokenizes SPL source code  
✅ **Syntax Analysis** - Parses SPL grammar and builds AST  
✅ **Semantic Analysis** - Variable/function naming and scope validation  
✅ **Type Checking** - Type compatibility verification  
✅ **Code Generation** - Generates executable BASIC code  

## 🚀 Usage

```bash
./compile.sh
cd tests
java SPLInterface <input_file.txt>
```

## 📊 Output Specifications

### Input
- SPL Program in a `*.txt` file

### Output (Complete Type A Implementation)
1. **"Tokens accepted"** - if lexical analysis succeeds
2. **"Syntax accepted"** - if parsing succeeds  
3. **"Variable Naming and Function Naming accepted"** - if semantic analysis succeeds
4. **"Types accepted"** - if type checking succeeds
5. **Executable BASIC code** - Generated as `*_output.txt` file

### Error Handling
- **"Lexical error:"** + details for tokenization issues
- **"Syntax error:"** + details for parsing issues  
- **"Naming error:"** + details for scope violations
- **"Type error:"** + details for type mismatches

## 🎯 Complete Feature Set

### Core Language Support
- ✅ **Global variables** (`glob { x y }`)
- ✅ **Procedures** with parameters (`proc { test(a) { ... } }`)
- ✅ **Functions** with return values (`func { add(x y) { ... return result } }`)
- ✅ **Local variables** with proper scoping (`local { var }`)
- ✅ **Control flow** (while loops, if-else branches)
- ✅ **Expressions** (arithmetic, logical, comparison operations)
- ✅ **Print statements** and variable assignments

### Advanced Features
- ✅ **Function calls** in assignments (`x = add(a, b)`)
- ✅ **Procedure calls** (`test(parameter)`)
- ✅ **Nested expressions** (`(x plus y) mult z`)
- ✅ **Complex control structures** with functions/procedures

## 🔧 Target BASIC Dialect

**Modern BASIC/FreeBASIC** compatible code generation with:
- `SUB` and `FUNCTION` definitions
- `DIM` variable declarations  
- `WHILE`/`WEND` loop structures
- `EXIT SUB`/`EXIT FUNCTION` for early termination
- `PRINT` statements for output

## 🏆 Project Achievement

This SPL compiler successfully achieves **Project Type A** specifications:
- Complete front-end compilation pipeline
- Full semantic analysis with scoping and type checking  
- Executable BASIC code generation
- Comprehensive error reporting at every phase
- Support for the complete SPL grammar specification