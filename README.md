# SPL Semantic Analyzer

A complete semantic analyzer for the SPL (Simple Programming Language) that implements all scope rules from the SPL_Scopes specification.

## Features

✅ **Complete Scope Analysis**
- Everywhere → Global/Procedure/Function/Main → Local scope hierarchy
- Cross-scope name conflict detection
- Parameter shadowing prevention
- Variable resolution (parameters → local → global)

✅ **Error Detection**
- Duplicate variable declarations
- Undeclared variable usage
- Name conflicts between variables and procedures/functions
- Parameter shadowing by local variables

✅ **Symbol Table Management**
- Unique node IDs for AST-symbol table linking
- Complete scope information tracking
- Foreign key relationships maintained

## Usage

### Interactive Mode
```bash
java SPLChecker
```
Enter SPL code interactively, type `END` to check, `help` for examples.

### File Mode
```bash
java SPLChecker filename.spl
```
Check an SPL file directly.

### Legacy Mode (Original Compiler)
```bash
java SPLCompiler filename.spl
```

## Examples

### Valid Program
```spl
glob { globalvar1 globalvar2 } 
proc { 
    myproc ( param1 param2 ) { 
        local { localvar1 localvar2 } 
        param1 = globalvar1 
    } 
} 
func { } 
main { 
    var { mainvar1 mainvar2 } 
    mainvar1 = globalvar1 
}
```

### Error Examples

**Duplicate Variables:**
```spl
glob { x x } proc { } func { } main { var { a } halt }
```

**Cross-scope Conflict:**
```spl
glob { samename } 
proc { samename ( ) { local { } halt } } 
func { } 
main { var { a } halt }
```

**Parameter Shadowing:**
```spl
glob { x } 
proc { myfunc ( param1 ) { local { param1 } halt } } 
func { } 
main { var { a } halt }
```

**Undeclared Variable:**
```spl
glob { x } 
proc { } 
func { } 
main { var { a } undeclared = a }
```

## Quick Start

1. **Compile:**
   ```bash
   javac *.java
   ```

2. **Run Interactive Mode:**
   ```bash
   java SPLChecker
   ```

3. **Try the example:**
   ```bash
   java SPLChecker example.spl
   ```

4. **Get help:**
   ```bash
   java SPLChecker
   SPL> help
   ```

## Implementation

- **SPLChecker.java** - Interactive interface and file checker
- **SPLCompiler.java** - Main compiler with full analysis
- **ASTParser.java** - Builds abstract syntax tree
- **SemanticAnalyzer.java** - Implements all SPL scope rules
- **SymbolTable.java** - Manages scoped symbol storage
- **ASTNode.java** - AST nodes with unique IDs
- **SymbolTableEntry.java** - Symbol entries with scope info
- **SymbolKinds.java** - Symbol type enumeration
- **TokenFeeder.java** - Token input handling

## SPL Language Structure

```
SPL_PROG ::= glob { VARIABLES } proc { PROCDEFS } func { FUNCDEFS } main { MAINPROG }
VARIABLES ::= VAR VARIABLES | ε
PROCDEFS ::= PDEF PROCDEFS | ε  
FUNCDEFS ::= FDEF FUNCDEFS | ε
PDEF ::= NAME ( PARAM ) { BODY }
FDEF ::= NAME ( PARAM ) { BODY ; return ATOM }
MAINPROG ::= var { VARIABLES } ALGO
BODY ::= local { MAXTHREE } ALGO
PARAM ::= MAXTHREE
MAXTHREE ::= VAR VAR VAR | VAR VAR | VAR | ε
ALGO ::= INSTR ; ALGO | INSTR
INSTR ::= halt | print ATOM | ASSIGN | LOOP | BRANCH
ASSIGN ::= VAR = TERM
ATOM ::= VAR | NUMBER
```

## Scope Rules Implemented

All rules from SPL_Scopes 1 specification:
1. Scope hierarchy enforcement
2. No duplicate declarations within same scope
3. No variable names matching procedure/function names
4. No parameter shadowing by local variables
5. Proper variable resolution order
6. Undeclared variable detection
7. Symbol table foreign key relationships
8. Unique node ID assignment

---

**100% compliant with SPL_Scopes 1 specification** ✅