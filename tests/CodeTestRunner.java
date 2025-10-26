import java.io.*;
import java.util.*;

public class CodeTestRunner {
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    private static List<String> failedTests = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("                SPL COMPILER - CODE-BASED TESTS");
        System.out.println("=".repeat(70));
        
        // Run all test cases
        runBasicTests();
        runSemanticTests();
        runTypeTests();
        runCodeGenTests();
        runErrorTests();
        
        // Print final summary
        printSummary();
    }
    
    // ====================== BASIC PARSING TESTS ======================
    private static void runBasicTests() {
        System.out.println("\n[BASIC PARSING TESTS]");
        
        // Test 1: Minimal valid program
        runTest("Minimal Program", """
            glob { } 
            proc { } 
            func { } 
            main { 
                var { } 
                halt 
            }
            """, true);
        
        // Test 2: Simple variable assignment
        runTest("Simple Assignment", """
            glob { x } 
            proc { } 
            func { } 
            main { 
                var { a } 
                a = 10 ; 
                halt 
            }
            """, true);
        
        // Test 3: Print statement
        runTest("Print Statement", """
            glob { } 
            proc { } 
            func { } 
            main { 
                var { num } 
                num = 123 ; 
                print num ; 
                halt 
            }
            """, true);
    }
    
    // ====================== SEMANTIC ANALYSIS TESTS ======================
    private static void runSemanticTests() {
        System.out.println("\n[SEMANTIC ANALYSIS TESTS]");
        
        // Test 1: Variable scoping
        runTest("Variable Scoping", """
            glob { globalVar } 
            proc { 
                testProc ( param1 ) { 
                    local { localVar } 
                    localVar = param1 ; 
                    globalVar = localVar 
                } 
            } 
            func { } 
            main { 
                var { mainVar } 
                mainVar = globalVar ; 
                halt 
            }
            """, true);
        
        // Test 2: Procedure with parameters
        runTest("Procedure Parameters", """
            glob { result } 
            proc { 
                calculate ( x y ) { 
                    local { temp } 
                    temp = x ; 
                    result = y 
                } 
            } 
            func { } 
            main { 
                var { a b } 
                a = 10 ; 
                b = 20 ; 
                call calculate ( a b ) ; 
                halt 
            }
            """, true);
    }
    
    // ====================== TYPE CHECKING TESTS ======================
    private static void runTypeTests() {
        System.out.println("\n[TYPE CHECKING TESTS]");
        
        // Test 1: Integer operations
        runTest("Integer Operations", """
            glob { } 
            proc { } 
            func { } 
            main { 
                var { a b result } 
                a = 10 ; 
                b = 5 ; 
                result = a + b ; 
                print result ; 
                halt 
            }
            """, true);
        
        // Test 2: Boolean operations
        runTest("Boolean Operations", """
            glob { } 
            proc { } 
            func { } 
            main { 
                var { x flag } 
                x = 15 ; 
                flag = x > 10 ; 
                if flag then { 
                    print x 
                } else { 
                    print 0 
                } ; 
                halt 
            }
            """, true);
    }
    
    // ====================== CODE GENERATION TESTS ======================
    private static void runCodeGenTests() {
        System.out.println("\n[CODE GENERATION TESTS]");
        
        // Test 1: Complete program with all features
        runTest("Complete Program", """
            glob { counter total } 
            proc { 
                increment ( ) { 
                    local { } 
                    counter = counter + 1 
                } 
            } 
            func { } 
            main { 
                var { i } 
                counter = 0 ; 
                total = 0 ; 
                i = 1 ; 
                while i <= 5 do { 
                    call increment ( ) ; 
                    total = total + i ; 
                    i = i + 1 
                } ; 
                print total ; 
                halt 
            }
            """, true);
        
        // Test 2: Conditional statements
        runTest("Conditional Logic", """
            glob { } 
            proc { } 
            func { } 
            main { 
                var { num } 
                num = 25 ; 
                if num > 20 then { 
                    print 1 
                } else { 
                    print 0 
                } ; 
                halt 
            }
            """, true);
    }
    
    // ====================== ERROR TESTS ======================
    private static void runErrorTests() {
        System.out.println("\n[ERROR HANDLING TESTS]");
        
        // Test 1: Undefined variable
        runTest("Undefined Variable", """
            glob { } 
            proc { } 
            func { } 
            main { 
                var { a } 
                a = undefinedVar ; 
                halt 
            }
            """, false);
        
        // Test 2: Missing halt
        runTest("Missing Halt", """
            glob { } 
            proc { } 
            func { } 
            main { 
                var { a } 
                a = 10 
            }
            """, false);
    }
    
    // ====================== HELPER METHODS ======================
    private static void runTest(String testName, String splCode, boolean shouldPass) {
        System.out.println("\nTesting: " + testName);
        System.out.println("-".repeat(50));
        
        try {
            // Create temporary file
            File tempFile = File.createTempFile("test_", ".spl");
            tempFile.deleteOnExit();
            
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write(splCode);
            }
            
            // Capture output
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            PrintStream originalErr = System.err;
            
            System.setOut(new PrintStream(baos));
            System.setErr(new PrintStream(baos));
            
            boolean compilerSuccess = false;
            try {
                SPLCompiler.main(new String[]{tempFile.getAbsolutePath()});
                compilerSuccess = true;
            } catch (SystemExitException e) {
                compilerSuccess = false;
            } catch (Exception e) {
                compilerSuccess = false;
            }
            
            // Restore output
            System.setOut(originalOut);
            System.setErr(originalErr);
            
            String output = baos.toString();
            boolean actualSuccess = compilerSuccess || output.contains("COMPILATION SUCCESSFUL");
            
            // Check if result matches expectation
            boolean testPassed = (shouldPass && actualSuccess) || (!shouldPass && !actualSuccess);
            
            if (testPassed) {
                testsPassed++;
                System.out.println("✅ PASSED: " + testName);
            } else {
                testsFailed++;
                failedTests.add(testName);
                System.out.println("❌ FAILED: " + testName);
                System.out.println("   Expected: " + (shouldPass ? "Success" : "Failure"));
                System.out.println("   Got: " + (actualSuccess ? "Success" : "Failure"));
            }
            
            // Show key output lines
            showKeyOutput(output);
            
        } catch (Exception e) {
            testsFailed++;
            failedTests.add(testName);
            System.out.println("❌ EXCEPTION in " + testName + ": " + e.getMessage());
        }
    }
    
    private static void showKeyOutput(String output) {
        String[] lines = output.split("\n");
        for (String line : lines) {
            if (line.contains("✓") || line.contains("✗") || 
                line.contains("COMPILATION") || line.contains("Error:")) {
                System.out.println("   " + line.trim());
            }
        }
    }
    
    private static void printSummary() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                    FINAL TEST SUMMARY");
        System.out.println("=".repeat(70));
        System.out.println("Total tests: " + (testsPassed + testsFailed));
        System.out.println("Passed: " + testsPassed + " ✅");
        System.out.println("Failed: " + testsFailed + (testsFailed > 0 ? " ❌" : ""));
        
        if (!failedTests.isEmpty()) {
            System.out.println("\nFailed tests:");
            for (String test : failedTests) {
                System.out.println("  • " + test);
            }
        }
        
        double successRate = (testsPassed * 100.0) / (testsPassed + testsFailed);
        System.out.println("\nSuccess rate: " + String.format("%.1f", successRate) + "%");
        System.out.println("=".repeat(70));
    }
    
    // Custom exception to handle System.exit calls
    private static class SystemExitException extends SecurityException {
        public final int status;
        public SystemExitException(int status) {
            this.status = status;
        }
    }
}