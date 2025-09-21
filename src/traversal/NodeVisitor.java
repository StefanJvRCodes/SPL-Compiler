/**
 * NodeVisitor.java
 * NodeVisitor interface using the Visitor pattern
 */

/**
 * NodeVisitor interface using the Visitor pattern
 * Provides hooks for processing nodes during tree traversal
 */
interface NodeVisitor {
    /**
     * Visit a node during traversal
     * @param node The node being visited
     * @return VisitResult indicating how to proceed with traversal
     */
    VisitResult visitNode(ASTNode node);
    
    /**
     * Called when entering a scope-defining node
     * @param node The scope node being entered
     */
    void enterScope(ASTNode node);
    
    /**
     * Called when exiting a scope-defining node
     * @param node The scope node being exited
     */
    void exitScope(ASTNode node);
    
    /**
     * Called at the beginning of tree traversal
     * @param root The root node of the traversal
     */
    void onTraversalStart(ASTNode root);
    
    /**
     * Called at the end of tree traversal
     * @param root The root node of the traversal
     */
    void onTraversalEnd(ASTNode root);
}