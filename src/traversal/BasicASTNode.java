/**
 * BasicASTNode.java
 * Basic implementation of the ASTNode interface
 */

/**
 * Basic AST Node implementation
 * Provides concrete implementation of the ASTNode interface
 */
class BasicASTNode implements ASTNode {
    private String nodeType;
    private Object value;
    private ASTNode[] children;
    private String uniqueIdentifier;
    private ASTNode parent;
    
    /**
     * Constructor for leaf nodes (no children)
     * @param nodeType The type of this node
     * @param value The value associated with this node
     */
    public BasicASTNode(String nodeType, Object value) {
        this.nodeType = nodeType;
        this.value = value;
        this.children = new ASTNode[0];
    }
    
    /**
     * Constructor for nodes with children
     * @param nodeType The type of this node
     * @param value The value associated with this node
     * @param children Array of child nodes
     */
    public BasicASTNode(String nodeType, Object value, ASTNode[] children) {
        this.nodeType = nodeType;
        this.value = value;
        this.children = children != null ? children : new ASTNode[0];
        
        // Set parent references for all children
        for (ASTNode child : this.children) {
            if (child != null) {
                child.setParent(this);
            }
        }
    }
    
    @Override
    public String getNodeType() { 
        return nodeType; 
    }
    
    @Override
    public Object getValue() { 
        return value; 
    }
    
    @Override
    public ASTNode[] getChildren() { 
        return children; 
    }
    
    @Override
    public String getUniqueIdentifier() { 
        return uniqueIdentifier; 
    }
    
    @Override
    public void setUniqueIdentifier(String id) { 
        this.uniqueIdentifier = id; 
    }
    
    @Override
    public ASTNode getParent() { 
        return parent; 
    }
    
    @Override
    public void setParent(ASTNode parent) { 
        this.parent = parent; 
    }
    
    /**
     * Add a child node to this node
     * @param child The child node to add
     */
    public void addChild(ASTNode child) {
        if (child == null) return;
        
        ASTNode[] newChildren = new ASTNode[children.length + 1];
        System.arraycopy(children, 0, newChildren, 0, children.length);
        newChildren[children.length] = child;
        
        this.children = newChildren;
        child.setParent(this);
    }
    
    /**
     * Check if this node has children
     * @return true if this node has children, false otherwise
     */
    public boolean hasChildren() {
        return children.length > 0;
    }
    
    /**
     * Get the number of children
     * @return the number of child nodes
     */
    public int getChildCount() {
        return children.length;
    }
    
    @Override
    public String toString() {
        return String.format("ASTNode{type='%s', value=%s, children=%d, id=%s}", 
                           nodeType, value, children.length, uniqueIdentifier);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        BasicASTNode that = (BasicASTNode) obj;
        return java.util.Objects.equals(uniqueIdentifier, that.uniqueIdentifier);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(uniqueIdentifier);
    }
}