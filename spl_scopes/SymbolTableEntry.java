//A updated this file to include type information (SPL_Types)
public class SymbolTableEntry {
    private final String name;
    private final SymbolKinds kind;
    private final String scopeName;
    private final int nodeId;
    private String type; // Can be "numeric", "boolean", or "typeless"
    
    public SymbolTableEntry(String name, SymbolKinds kind, String scopeName, int nodeId) {
        this.name = name;
        this.kind = kind;
        this.scopeName = scopeName;
        this.nodeId = nodeId;
        this.type = kind.getDefaultType();
    }
    
    public SymbolTableEntry(String name, SymbolKinds kind, String scopeName, int nodeId, String type) {
        this.name = name;
        this.kind = kind;
        this.scopeName = scopeName;
        this.nodeId = nodeId;
        this.type = type;
    }
    
    public String getName() { 
        return name; 
    }
    
    public SymbolKinds getKind() { 
        return kind; 
    }
    
    public String getScopeName() { 
        return scopeName; 
    }
    
    public int getNodeId() { 
        return nodeId; 
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return String.format("%-15s | %-12s | %-20s | %-10s | Node: %d", 
            name, kind, scopeName, type, nodeId);
    }
}



////===========SPL_Scopes Version of SymbolTableEntry.java==========
// public class SymbolTableEntry {
//     private final String name;
//     private final SymbolKinds kind;
//     private final String scopeName;
//     private final int nodeId;
    
//     public SymbolTableEntry(String name, SymbolKinds kind, String scopeName, int nodeId) {
//         this.name = name;
//         this.kind = kind;
//         this.scopeName = scopeName;
//         this.nodeId = nodeId;
//     }
    
//     public String getName() { 
//         return name; 
//     }
    
//     public SymbolKinds getKind() { 
//         return kind; 
//     }
    
//     public String getScopeName() { 
//         return scopeName; 
//     }
    
//     public int getNodeId() { 
//         return nodeId; 
//     }
    
//     @Override
//     public String toString() {
//         return String.format("%-15s | %-12s | %-20s | Node: %d", 
//             name, kind, scopeName, nodeId);
//     }
// }
