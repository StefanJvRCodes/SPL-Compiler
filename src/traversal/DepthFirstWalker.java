/**
 * DepthFirstWalker.java
 * Depth-First implementation of the TreeWalker interface
 */

/**
 * Depth-First Walker implementation of TreeWalker
 * Supports pre-order, post-order, and in-order traversal strategies
 */
class DepthFirstWalker implements TreeWalker {
    private TraversalOrder traversalOrder;
    
    /**
     * Default constructor - uses pre-order traversal
     */
    public DepthFirstWalker() {
        this.traversalOrder = TraversalOrder.PRE_ORDER;
    }
    
    /**
     * Constructor with specified traversal order
     * @param order The traversal order to use
     */
    public DepthFirstWalker(TraversalOrder order) {
        this.traversalOrder = order;
    }
    
    @Override
    public void traverse(ASTNode root, NodeVisitor visitor) {
        if (root == null || visitor == null) {
            throw new IllegalArgumentException("Root node and visitor cannot be null");
        }
        
        // Notify visitor that traversal is starting
        visitor.onTraversalStart(root);
        
        try {
            boolean shouldContinue = traverseRecursive(root, visitor);
            if (!shouldContinue) {
                System.out.println("Traversal terminated early by visitor");
            }
        } catch (Exception e) {
            System.err.println("Error during traversal: " + e.getMessage());
            throw e;
        } finally {
            // Always notify visitor that traversal has ended
            visitor.onTraversalEnd(root);
        }
    }
    
    /**
     * Recursive traversal implementation
     * @param node Current node to visit
     * @param visitor Visitor to apply to nodes
     * @return true if traversal should continue, false if it should terminate
     */
    private boolean traverseRecursive(ASTNode node, NodeVisitor visitor) {
        if (node == null) return true;
        
        VisitResult result;
        
        switch (traversalOrder) {
            case PRE_ORDER:
                return handlePreOrderTraversal(node, visitor);
                
            case POST_ORDER:
                return handlePostOrderTraversal(node, visitor);
                
            case IN_ORDER:
                return handleInOrderTraversal(node, visitor);
                
            default:
                throw new IllegalStateException("Unknown traversal order: " + traversalOrder);
        }
    }
    
    /**
     * Handle pre-order traversal: visit node first, then children
     */
    private boolean handlePreOrderTraversal(ASTNode node, NodeVisitor visitor) {
        // Handle scope entry
        if (isScopeDefiningNode(node)) {
            visitor.enterScope(node);
        }
        
        // Visit the current node
        VisitResult result = visitor.visitNode(node);
        
        // Check if we should terminate or skip children
        if (result == VisitResult.TERMINATE) {
            handleScopeExit(node, visitor);
            return false;
        }
        
        if (result == VisitResult.SKIP_CHILDREN) {
            handleScopeExit(node, visitor);
            return true;
        }
        
        // Visit all children
        boolean continueTraversal = visitChildren(node, visitor);
        
        // Handle scope exit
        handleScopeExit(node, visitor);
        
        return continueTraversal;
    }
    
    /**
     * Handle post-order traversal: visit children first, then node
     */
    private boolean handlePostOrderTraversal(ASTNode node, NodeVisitor visitor) {
        // Handle scope entry
        if (isScopeDefiningNode(node)) {
            visitor.enterScope(node);
        }
        
        // Visit children first
        boolean continueTraversal = visitChildren(node, visitor);
        if (!continueTraversal) {
            handleScopeExit(node, visitor);
            return false;
        }
        
        // Then visit the current node
        VisitResult result = visitor.visitNode(node);
        
        // Handle scope exit
        handleScopeExit(node, visitor);
        
        return result != VisitResult.TERMINATE;
    }
    
    /**
     * Handle in-order traversal: for binary trees (left, node, right)
     */
    private boolean handleInOrderTraversal(ASTNode node, NodeVisitor visitor) {
        ASTNode[] children = node.getChildren();
        
        // Handle scope entry
        if (isScopeDefiningNode(node)) {
            visitor.enterScope(node);
        }
        
        // Visit left child first (if exists)
        if (children.length > 0 && children[0] != null) {
            if (!traverseRecursive(children[0], visitor)) {
                handleScopeExit(node, visitor);
                return false;
            }
        }
        
        // Visit current node
        VisitResult result = visitor.visitNode(node);
        if (result == VisitResult.TERMINATE) {
            handleScopeExit(node, visitor);
            return false;
        }
        
        // Visit remaining children (right subtree and others)
        for (int i = 1; i < children.length; i++) {
            if (children[i] != null) {
                if (!traverseRecursive(children[i], visitor)) {
                    handleScopeExit(node, visitor);
                    return false;
                }
            }
        }
        
        // Handle scope exit
        handleScopeExit(node, visitor);
        return true;
    }
    
    /**
     * Visit all children of a node
     */
    private boolean visitChildren(ASTNode node, NodeVisitor visitor) {
        for (ASTNode child : node.getChildren()) {
            if (child != null) {
                if (!traverseRecursive(child, visitor)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Handle scope exit if the node defines a scope
     */
    private void handleScopeExit(ASTNode node, NodeVisitor visitor) {
        if (isScopeDefiningNode(node)) {
            visitor.exitScope(node);
        }
    }
    
    /**
     * Determine if a node defines a scope based on its type
     */
    private boolean isScopeDefiningNode(ASTNode node) {
        String type = node.getNodeType().toLowerCase();
        return type.contains("function") || 
               type.contains("procedure") || 
               type.contains("block") || 
               type.contains("main") || 
               type.contains("glob") ||
               type.equals("program");
    }
    
    @Override
    public void setTraversalOrder(TraversalOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Traversal order cannot be null");
        }
        this.traversalOrder = order;
    }
    
    @Override
    public TraversalOrder getTraversalOrder() {
        return traversalOrder;
    }
    
    @Override
    public String toString() {
        return String.format("DepthFirstWalker{order=%s}", traversalOrder);
    }
}