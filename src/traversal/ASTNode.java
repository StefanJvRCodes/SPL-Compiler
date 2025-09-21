/**
 * ASTNode.java
 * Core interfaces and enums for the Tree Traversal Engine
 */

/**
 * Represents a node in the Abstract Syntax Tree (AST)
 */
interface ASTNode {
    String getNodeType();
    Object getValue();
    ASTNode[] getChildren();
    String getUniqueIdentifier();
    void setUniqueIdentifier(String id);
    ASTNode getParent();
    void setParent(ASTNode parent);
}

/**
 * Enumeration for different traversal orders
 */
enum TraversalOrder {
    PRE_ORDER,    // Visit node before children
    POST_ORDER,   // Visit node after children
    IN_ORDER      // Visit node between children (for binary trees)
}

/**
 * Enumeration for visit results to control traversal flow
 */
enum VisitResult {
    CONTINUE,     // Continue normal traversal
    SKIP_CHILDREN, // Skip visiting children of current node
    TERMINATE     // Stop traversal completely
}