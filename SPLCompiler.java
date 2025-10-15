public class SPLCompiler {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java SPLCompiler <input_file>");
            System.exit(1);
        }
        
        String inputFile = args[0];
        
        try {
            // Create token feeder from input file
            TokenFeeder tokenFeeder = new TokenFeeder(inputFile);
            
            System.out.println("=".repeat(60));
            System.out.println("                SPL COMPILER");
            System.out.println("=".repeat(60));
            System.out.println("Input file: " + inputFile);
            System.out.println("=".repeat(60));
            
            // Parse and build AST
            System.out.println("\n[1] PARSING PHASE...");
            ASTNode ast = SPLParser.parseSPL_PROG(tokenFeeder);
            
            if (ast == null) {
                System.out.println("✗ Parsing failed!");
                System.exit(1);
            }
            
            System.out.println("✓ Parsing completed successfully!");
            
            // Print AST for debugging
            if (args.length > 1 && "-debug".equals(args[1])) {
                System.out.println("\nAST Structure:");
                System.out.println("-".repeat(40));
                ast.printTree(0);
            }
            
            // Semantic analysis
            System.out.println("\n[2] SEMANTIC ANALYSIS PHASE...");
            SemanticAnalyzer analyzer = new SemanticAnalyzer();
            boolean semanticSuccess = analyzer.analyze(ast);
            
            if (semanticSuccess) {
                System.out.println("✓ Semantic analysis completed successfully!");
                System.out.println("✓ All SPL scope rules satisfied!");
            } else {
                System.out.println("✗ Semantic analysis failed!");
            }
            
            // Print results
            analyzer.printResults();
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("COMPILATION " + (semanticSuccess ? "SUCCESSFUL" : "FAILED"));
            System.out.println("=".repeat(60));
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}