//Angie updated this file for types (SPL_Types)

// ASTNode.java
import java.util.*;

public class ASTNode {
    private static int nodeCounter = 0;
    
    private final int nodeId;
    private final String nodeType;
    private String value;
    private final List<ASTNode> children;
    private ASTNode parent;
    private String type; 
    
    public ASTNode(String nodeType) {
        this.nodeId = ++nodeCounter;
        this.nodeType = nodeType;
        this.children = new ArrayList<>();
        this.parent = null;
        this.value = null;
        this.type = null;  //added for SPL_Types 
    }
    
    public ASTNode(String nodeType, String value) {
        this.nodeId = ++nodeCounter;
        this.nodeType = nodeType;
        this.value = value;
        this.children = new ArrayList<>();
        this.parent = null;
        this.type = null;  //added for SPL_Types
    }
    
    public int getNodeId() {
        return nodeId;
    }
    
    public String getNodeType() {
        return nodeType;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    // Added getter & setter for type (SPL_Types)
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public List<ASTNode> getChildren() {
        return children;
    }
    
    public ASTNode getParent() {
        return parent;
    }
    
    public void addChild(ASTNode child) {
        children.add(child);
        child.parent = this;
    }
    
    public void addChildren(List<ASTNode> children) {
        for (ASTNode child : children) {
            addChild(child);
        }
    }
    
    // Find scope this node belongs to
    public String findScope() {
        ASTNode current = this;
        while (current != null) {
            String nodeType = current.getNodeType();
            switch (nodeType) {
                case "SPL_PROG":
                    return "Everywhere";
                case "VARIABLES":
                    if (current.parent != null && "SPL_PROG".equals(current.parent.getNodeType())) {
                        return "Global";
                    }
                    break;
                case "PROCDEFS":
                    return "Procedure";
                case "FUNCDEFS":
                    return "Function";
                case "MAINPROG":
                    return "Main";
                case "PDEF":
                case "FDEF":
                    return "Local_" + current.getNodeId(); // Each function/procedure has its own local scope
            }
            current = current.parent;
        }
        return "Everywhere"; // Default fallback
    }
    
    // Find the closest enclosing function or procedure definition
    public ASTNode findEnclosingFunction() {
        ASTNode current = this.parent;
        while (current != null) {
            if ("PDEF".equals(current.getNodeType()) || "FDEF".equals(current.getNodeType())) {
                return current;
            }
            current = current.parent;
        }
        return null;
    }
    
    @Override
    public String toString() {
        if (value != null) {
            if (type != null) {
                return String.format("%s[%d]: %s (%s)", nodeType, nodeId, value, type);
            } else {
                return String.format("%s[%d]: %s", nodeType, nodeId, value);
            }
        } else {
            if (type != null) {
                return String.format("%s[%d] (%s)", nodeType, nodeId, type);
            } else {
                return String.format("%s[%d]", nodeType, nodeId);
            }
        }
    }
    
    // Print the tree structure for debugging
    public void printTree(int indent) {
        System.out.println(" ".repeat(indent) + this.toString());
        for (ASTNode child : children) {
            child.printTree(indent + 2);
        }
    }
}

