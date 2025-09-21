/**
 * AbstractNodeVisitor.java
 * Abstract base class for NodeVisitor implementations
 */

/**
 * Abstract base class for NodeVisitor implementations
 * Provides sensible defaults for all visitor methods
 */
abstract class AbstractNodeVisitor implements NodeVisitor {
    
    /**
     * Default implementation for entering scope - can be overridden
     * @param node The scope node being entered
     */
    @Override
    public void enterScope(ASTNode node) {
        // Default implementation - subclasses can override
    }
    
    /**
     * Default implementation for exiting scope - can be overridden
     * @param node The scope node being exited
     */
    @Override
    public void exitScope(ASTNode node) {
        // Default implementation - subclasses can override
    }
    
    /**
     * Default implementation for traversal start - can be overridden
     * @param root The root node of the traversal
     */
    @Override
    public void onTraversalStart(ASTNode root) {
        // Default implementation - subclasses can override
    }
    
    /**
     * Default implementation for traversal end - can be overridden
     * @param root The root node of the traversal
     */
    @Override
    public void onTraversalEnd(ASTNode root) {
        // Default implementation - subclasses can override
    }
    
    /**
     * Utility method to check if a node defines a scope
     * @param node The node to check
     * @return true if the node defines a scope, false otherwise
     */
    protected boolean isScopeDefiningNode(ASTNode node) {
        if (node == null) return false;
        
        String type = node.getNodeType().toLowerCase();
        return type.contains("function") || 
               type.contains("procedure") || 
               type.contains("block") || 
               type.contains("main") || 
               type.contains("glob") ||
               type.equals("program");
    }
    
    /**
     * Utility method to check if a node is a declaration
     * @param node The node to check
     * @return true if the node is a declaration, false otherwise
     */
    protected boolean isDeclarationNode(ASTNode node) {
        if (node == null) return false;
        
        String type = node.getNodeType().toLowerCase();
        return type.contains("decl") || 
               type.contains("declaration") ||
               type.equals("vardecl") ||
               type.equals("funcdecl") ||
               type.equals("procdecl");
    }
    
    /**
     * Utility method to get indentation string based on depth
     * @param depth The current depth level
     * @return String with appropriate indentation
     */
    protected String getIndent(int depth) {
        return "  ".repeat(Math.max(0, depth));
    }
}