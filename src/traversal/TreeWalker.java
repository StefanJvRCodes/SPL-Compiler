/**
 * TreeWalker.java
 * TreeWalker interface for traversing AST nodes
 */

/**
 * TreeWalker interface for traversing AST nodes
 */
interface TreeWalker {
    /**
     * Traverse the AST starting from the root node using the specified visitor
     * @param root The root node to start traversal from
     * @param visitor The visitor to apply to each node
     * @throws IllegalArgumentException if root or visitor is null
     */
    void traverse(ASTNode root, NodeVisitor visitor);
    
    /**
     * Set the traversal order for the walker
     * @param order The traversal order to use
     */
    void setTraversalOrder(TraversalOrder order);
    
    /**
     * Get the current traversal order
     * @return The current traversal order
     */
    TraversalOrder getTraversalOrder();
}