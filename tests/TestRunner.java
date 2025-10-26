import java.io.*;
import java.util.*;

public class TestRunner {
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    private static List<String> failedTests = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("                    SPL COMPILER TEST RUNNER");
        System.out.println("=".repeat(70));
        
        // Find all .spl test files
        File currentDir = new File(".");
        File[] splFiles = currentDir.listFiles((dir, name) -> name.endsWith(".spl"));
        
        if (splFiles == null || splFiles.length == 0) {
            System.out.println("No .spl test files found in current directory");
            return;
        }
        
        Arrays.sort(splFiles, (a, b) -> a.getName().compareTo(b.getName()));
        
        System.out.println("Found " + splFiles.length + " test files:");
        for (File file : splFiles) {
            System.out.println("  • " + file.getName());
        }
        System.out.println();
        
        // Run tests on each file
        for (File splFile : splFiles) {
            runTest(splFile.getName());
        }
        
        // Print summary
        printSummary();
    }
    
    private static void runTest(String filename) {
        System.out.println("-".repeat(60));
        System.out.println("Testing: " + filename);
        System.out.println("-".repeat(60));
        
        try {
            // Redirect System.out to capture output
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(baos));
            
            // Run the compiler
            SPLCompiler.main(new String[]{filename});
            
            // Restore original output
            System.setOut(originalOut);
            String output = baos.toString();
            
            // Analyze output for success/failure
            boolean success = analyzeOutput(output, filename);
            
            if (success) {
                testsPassed++;
                System.out.println("✅ PASSED: " + filename);
            } else {
                testsFailed++;
                failedTests.add(filename);
                System.out.println("❌ FAILED: " + filename);
            }
            
            // Show relevant parts of output
            System.out.println(getRelevantOutput(output));
            
        } catch (Exception e) {
            testsFailed++;
            failedTests.add(filename);
            System.out.println("❌ EXCEPTION in " + filename + ": " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private static boolean analyzeOutput(String output, String filename) {
        // Check for compilation success indicators
        if (output.contains("COMPILATION SUCCESSFUL")) {
            return true;
        }
        
        // For error test files, failure might be expected
        if (filename.contains("error") && output.contains("COMPILATION FAILED")) {
            return true; // Expected failure for error tests
        }
        
        // Check for specific phase completions
        boolean parsingPassed = output.contains("✓ Parsing completed successfully!");
        boolean semanticPassed = output.contains("✓ Semantic analysis completed successfully!");
        boolean typePassed = output.contains("✓ Type checking completed successfully!");
        boolean codePassed = output.contains("✓ Code generation completed successfully!");
        
        // At minimum, parsing should pass for non-error files
        if (!filename.contains("error") && !parsingPassed) {
            return false;
        }
        
        return !output.contains("✗");
    }
    
    private static String getRelevantOutput(String output) {
        StringBuilder relevant = new StringBuilder();
        String[] lines = output.split("\n");
        
        for (String line : lines) {
            // Show success/failure indicators
            if (line.contains("✓") || line.contains("✗") || 
                line.contains("COMPILATION") || line.contains("Error:") ||
                line.contains("PHASE") || line.contains("=".repeat(20))) {
                relevant.append(line).append("\n");
            }
        }
        
        return relevant.toString();
    }
    
    private static void printSummary() {
        System.out.println("=".repeat(70));
        System.out.println("                    TEST SUMMARY");
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
}