import java.util.Scanner;
import java.io.*;

public class SPLChecker {
    
    public static void main(String[] args) {
        if (args.length == 0) {
            // Interactive mode
            runInteractiveMode();
        } else if (args.length == 1) {
            // File mode
            checkFile(args[0]);
        } else {
            printUsage();
        }
    }
    
    private static void runInteractiveMode() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=".repeat(60));
        System.out.println("              SPL SEMANTIC CHECKER");
        System.out.println("=".repeat(60));
        System.out.println("Enter SPL code (type 'END' on a new line to finish):");
        System.out.println("Or type 'help' for examples, 'quit' to exit");
        System.out.println("-".repeat(60));
        
        while (true) {
            System.out.print("SPL> ");
            String input = scanner.nextLine().trim();
            
            if ("quit".equalsIgnoreCase(input) || "exit".equalsIgnoreCase(input)) {
                System.out.println("Goodbye!");
                break;
            }
            
            if ("help".equalsIgnoreCase(input)) {
                showExamples();
                continue;
            }
            
            if ("clear".equalsIgnoreCase(input)) {
                clearScreen();
                continue;
            }
            
            // Multi-line input mode
            StringBuilder code = new StringBuilder();
            if (!"END".equalsIgnoreCase(input)) {
                code.append(input).append(" ");
                
                while (true) {
                    System.out.print("  -> ");
                    String line = scanner.nextLine().trim();
                    if ("END".equalsIgnoreCase(line)) {
                        break;
                    }
                    code.append(line).append(" ");
                }
            }
            
            if (code.length() > 0) {
                checkCode(code.toString());
            }
            
            System.out.println();
        }
        
        scanner.close();
    }
    
    private static void checkFile(String filename) {
        System.out.println("=".repeat(60));
        System.out.println("              SPL SEMANTIC CHECKER");
        System.out.println("=".repeat(60));
        System.out.println("Checking file: " + filename);
        System.out.println("=".repeat(60));
        
        try {
            // Use existing SPLCompiler for file checking
            SPLCompiler.main(new String[]{filename});
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    
    private static void checkCode(String code) {
        try {
            // Create temporary file
            File tempFile = File.createTempFile("spl_temp", ".spl");
            tempFile.deleteOnExit();
            
            // Write code to temporary file
            try (PrintWriter writer = new PrintWriter(tempFile)) {
                writer.println(code);
            }
            
            System.out.println("-".repeat(40));
            System.out.println("Checking SPL code...");
            System.out.println("-".repeat(40));
            
            // Use existing SPLCompiler
            SPLCompiler.main(new String[]{tempFile.getAbsolutePath()});
            
        } catch (Exception e) {
            System.err.println("Error processing code: " + e.getMessage());
        }
    }
    
    private static void showExamples() {
        System.out.println();
        System.out.println("=".repeat(50));
        System.out.println("                SPL EXAMPLES");
        System.out.println("=".repeat(50));
        
        System.out.println();
        System.out.println("1. VALID PROGRAM:");
        System.out.println("   glob { x y } proc { } func { } main { var { a } halt }");
        
        System.out.println();
        System.out.println("2. DUPLICATE VARIABLE ERROR:");
        System.out.println("   glob { x x } proc { } func { } main { var { a } halt }");
        
        System.out.println();
        System.out.println("3. CROSS-SCOPE NAME CONFLICT:");
        System.out.println("   glob { samename } proc { samename ( ) { local { } halt } }");
        System.out.println("   func { } main { var { a } halt }");
        
        System.out.println();
        System.out.println("4. PARAMETER SHADOWING:");
        System.out.println("   glob { x } proc { myfunc ( param1 ) { local { param1 } halt } }");
        System.out.println("   func { } main { var { a } halt }");
        
        System.out.println();
        System.out.println("5. UNDECLARED VARIABLE:");
        System.out.println("   glob { x } proc { } func { } main { var { a } undeclared = a }");
        
        System.out.println();
        System.out.println("6. VARIABLE RESOLUTION:");
        System.out.println("   glob { global1 } proc { myproc ( param1 ) { local { local1 }");
        System.out.println("   param1 = global1 } } func { } main { var { main1 } main1 = global1 }");
        
        System.out.println();
        System.out.println("=".repeat(50));
        System.out.println("Commands: 'help', 'clear', 'quit'");
        System.out.println("Enter your code and type 'END' to check it");
        System.out.println("=".repeat(50));
        System.out.println();
    }
    
    private static void clearScreen() {
        // Clear screen for better readability
        System.out.print("\033[2J\033[H");
        System.out.flush();
    }
    
    private static void printUsage() {
        System.out.println("SPL Semantic Checker");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java SPLChecker                    # Interactive mode");
        System.out.println("  java SPLChecker <filename.spl>     # Check file");
        System.out.println();
        System.out.println("Interactive mode commands:");
        System.out.println("  help  - Show examples");
        System.out.println("  clear - Clear screen");
        System.out.println("  quit  - Exit");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java SPLChecker");
        System.out.println("  java SPLChecker myprogram.spl");
    }
}