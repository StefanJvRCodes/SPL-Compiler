
public class SymbolTableEntry {
    private final String name;
    private final SymbolKinds kind;
    private final String scopeName;
    private final int nodeId;
    
    public SymbolTableEntry(String name, SymbolKinds kind, String scopeName, int nodeId) {
        this.name = name;
        this.kind = kind;
        this.scopeName = scopeName;
        this.nodeId = nodeId;
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
    
    @Override
    public String toString() {
        return String.format("%-15s | %-12s | %-20s | Node: %d", 
            name, kind, scopeName, nodeId);
    }
}