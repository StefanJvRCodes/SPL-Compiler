/**
 * TreeTraversalDemo.java
 * Demonstration of the Tree Traversal Engine
 */

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstration class for the Tree Traversal Engine
 */
class TreeTraversalDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Tree Traversal Engine Demo ===\n");
        
        // Create sample SPL program AST
        ASTNode program = createSampleSPLProgram();
        
        // Assign unique identifiers to all nodes
        assignUniqueIdentifiers(program);
        
        // Demonstrate different traversal orders
        demonstrateTraversalOrders(program);
        
        // Demonstrate node collection
        demonstrateNodeCollection(program);
    }
    
    /**
     * Demonstrate different traversal orders
     */
    private static void demonstrateTraversalOrders(ASTNode program) {
        TreeWalker walker = new DepthFirstWalker();
        
        System.out.println("=== PRE-ORDER TRAVERSAL ===");
        walker.setTraversalOrder(TraversalOrder.PRE_ORDER);
        walker.traverse(program, new PrintingVisitor(true, true));
        
        System.out.println("\n=== POST-ORDER TRAVERSAL ===");
        walker.setTraversalOrder(TraversalOrder.POST_ORDER);
        walker.traverse(program, new PrintingVisitor(true, false));
    }
    
    /**
     * Demonstrate node collection capabilities
     */
    private static void demonstrateNodeCollection(ASTNode program) {
        System.out.println("\n=== NODE COLLECTION DEMO ===");
        
        TreeWalker walker = new DepthFirstWalker(TraversalOrder.PRE_ORDER);
        
        // Collect variable declarations
        NodeCollectorVisitor varCollector = new NodeCollectorVisitor("VarDecl");
        walker.traverse(program, varCollector);
        
        System.out.printf("Found %d variable declarations:%n", varCollector.getCollectedCount());
        for (ASTNode node : varCollector.getCollectedNodes()) {
            System.out.printf("  - %s (ID: %s)%n", 
                            node.getValue(), 
                            node.getUniqueIdentifier());
        }
        
        // Collect all block nodes using pattern matching
        NodeCollectorVisitor blockCollector = new NodeCollectorVisitor("Block", true);
        walker.traverse(program, blockCollector);
        
        System.out.printf("\nFound %d block nodes:%n", blockCollector.getCollectedCount());
        blockCollector.printCollectionSummary();
    }
    
    /**
     * Creates a sample SPL AST structure
     */
    private static ASTNode createSampleSPLProgram() {
        // Create variable declarations
        ASTNode varX = new BasicASTNode("VarDecl", "x");
        ASTNode varY = new BasicASTNode("VarDecl", "y");
        ASTNode varZ = new BasicASTNode("VarDecl", "z");
        
        // Create scope blocks
        ASTNode globBlock = new BasicASTNode("GlobBlock", "global", new ASTNode[]{varX});
        ASTNode funcBlock = new BasicASTNode("FuncBlock", "function y", new ASTNode[]{varY});
        ASTNode procBlock = new BasicASTNode("ProcBlock", "procedure z", new ASTNode[]{varZ});
        ASTNode mainBlock = new BasicASTNode("MainBlock", "main", new ASTNode[]{});
        
        // Create root program node
        return new BasicASTNode("Program", "SPL_Program", 
                               new ASTNode[]{globBlock, funcBlock, procBlock, mainBlock});
    }
    
    /**
     * Assigns unique identifiers to all nodes in the tree
     */
    private static void assignUniqueIdentifiers(ASTNode root) {
        final AtomicInteger counter = new AtomicInteger(0);
        
        TreeWalker walker = new DepthFirstWalker(TraversalOrder.PRE_ORDER);
        walker.traverse(root, new AbstractNodeVisitor() {
            @Override
            public VisitResult visitNode(ASTNode node) {
                String id = String.format("node_%04d", counter.incrementAndGet());
                node.setUniqueIdentifier(id);
                return VisitResult.CONTINUE;
            }
        });
    }
}