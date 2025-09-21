/**
 * PrintingVisitor.java
 * Visitor implementation that prints node information during traversal
 */

/**
 * Simple visitor that prints node information during traversal
 * Provides visual representation of the tree structure with proper indentation
 */
class PrintingVisitor extends AbstractNodeVisitor {
    private int depth = 0;
    private boolean showIds = true;
    private boolean showScopeMessages = true;
    
    /**
     * Default constructor - shows IDs and scope messages
     */
    public PrintingVisitor() {
    }
    
    /**
     * Constructor with configuration options
     * @param showIds Whether to display node unique identifiers
     * @param showScopeMessages Whether to display scope entry/exit messages
     */
    public PrintingVisitor(boolean showIds, boolean showScopeMessages) {
        this.showIds = showIds;
        this.showScopeMessages = showScopeMessages;
    }
    
    @Override
    public VisitResult visitNode(ASTNode node) {
        String indent = getIndent(depth);
        String idInfo = showIds && node.getUniqueIdentifier() != null 
                       ? String.format(" (ID: %s)", node.getUniqueIdentifier())
                       : "";
        
        String valueInfo = node.getValue() != null 
                          ? String.format(": %s", node.getValue())
                          : "";
        
        System.out.printf("%s%s%s%s%n", 
                         indent, 
                         node.getNodeType(), 
                         valueInfo,
                         idInfo);
        
        // Increment depth for children (will be handled by scope methods or manually)
        if (!isScopeDefiningNode(node)) {
            depth++;
        }
        
        return VisitResult.CONTINUE;
    }
    
    @Override
    public void enterScope(ASTNode node) {
        if (isScopeDefiningNode(node) && showScopeMessages) {
            String indent = getIndent(depth);
            System.out.printf("%s>>> Entering scope: %s%n", indent, node.getNodeType());
        }
        
        // Increment depth when entering scope
        if (isScopeDefiningNode(node)) {
            depth++;
        }
    }
    
    @Override
    public void exitScope(ASTNode node) {
        // Decrement depth when exiting scope
        if (isScopeDefiningNode(node)) {
            depth = Math.max(0, depth - 1);
            
            if (showScopeMessages) {
                String indent = getIndent(depth);
                System.out.printf("%s<<< Exiting scope: %s%n", indent, node.getNodeType());
            }
        } else {
            // Decrement for non-scope nodes
            depth = Math.max(0, depth - 1);
        }
    }
    
    @Override
    public void onTraversalStart(ASTNode root) {
        System.out.println("=== Starting Tree Traversal ===");
        if (root != null) {
            System.out.printf("Root: %s%n", root.getNodeType());
        }
        depth = 0;
    }
    
    @Override
    public void onTraversalEnd(ASTNode root) {
        System.out.println("=== Tree Traversal Complete ===");
        depth = 0;
    }
    
    /**
     * Set whether to show unique identifiers
     * @param showIds true to show IDs, false to hide them
     */
    public void setShowIds(boolean showIds) {
        this.showIds = showIds;
    }
    
    /**
     * Set whether to show scope messages
     * @param showScopeMessages true to show scope entry/exit messages
     */
    public void setShowScopeMessages(boolean showScopeMessages) {
        this.showScopeMessages = showScopeMessages;
    }
    
    /**
     * Get current depth level
     * @return current depth level
     */
    public int getCurrentDepth() {
        return depth;
    }
    
    /**
     * Reset depth to zero (useful for multiple traversals)
     */
    public void resetDepth() {
        this.depth = 0;
    }
}