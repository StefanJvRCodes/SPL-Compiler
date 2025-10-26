# SPL Compiler Code Generation Tests

## How to Test

1. **Compile the SPL Compiler:**
   ```bash
   javac *.java
   ```

2. **Run Individual Tests:**

   **Test 1 - Original Example:**
   ```bash
   java SPLCompiler example.spl
   ```
   Expected: Single assignment `mainvar1 = globalvar1`

   **Test 2 - Working Test (Full Features):**
   ```bash
   java SPLCompiler working_test.spl
   ```
   Expected: Multiple assignments and prints

   **Test 3 - Assignment Test:**
   ```bash
   java SPLCompiler assignment_test.spl
   ```
   Expected: Variable-to-variable assignments

   **Test 4 - Minimal Test:**
   ```bash
   java SPLCompiler minimal_test.spl
   ```
   Expected: Simple assignment and print

## What to Look For

✅ **Variables** - No code generated (only used for symbol table)
✅ **PDEF/FDEF** - Stored for inlining (no immediate code)  
✅ **MAINPROG** - Only ALGO generates code
✅ **ATOM** - Variables lookup symbol table, numbers used directly
✅ **HALT** - Generates "STOP"
✅ **PRINT** - Generates "PRINT variable/number"
✅ **ASSIGN** - Generates "var = value" (uses = not :=)

## Expected Output Format

```
Generated Target Code:
------------------------------------------------------------
variable1 = value1
variable2 = value2
PRINT variable1 
PRINT 42 
STOP
------------------------------------------------------------
```