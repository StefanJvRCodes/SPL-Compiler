import java.util.*;

class SymbolTable  {

     private HashMap<String, HashMap<String, SymbolTableEntry>> scopes;
    private Stack<String> scopeStack;
    
    public SymbolTable() {
        scopes = new HashMap<>();
        scopeStack = new Stack<>();
        scopes.put("Everywhere", new HashMap<>());
        scopeStack.push("Everywhere");
    }



     // Enter a new scope.
     // Call this when entering Global, Procedure, Function, Main, or Local scopes.
      public void enterScope(String scopeName) {
        if (!scopes.containsKey(scopeName)) {
            scopes.put(scopeName, new HashMap<>());
        }
        scopeStack.push(scopeName);
    }

    //exit scope
     public void exitScope() {
        if (scopeStack.size() > 1) {
            scopeStack.pop();
        }
    }

    //get current scope name
     public String getCurrentScope() {
        return scopeStack.peek();
    }


public boolean insert(String name, SymbolKinds kind, int nodeId) {
        String currentScope = getCurrentScope();
        HashMap<String, SymbolTableEntry> currentScopeTable = scopes.get(currentScope);
        
        // Check for duplicate declaration in current scope
        if (currentScopeTable.containsKey(name)) {
            return false; // Duplicate - emit name-rule-violation
        }
        
        SymbolTableEntry entry = new SymbolTableEntry(name, kind, currentScope, nodeId);
        currentScopeTable.put(name, entry);
        return true;
    }

    public SymbolTableEntry lookup(String name) {
        // Search from innermost scope to outermost
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            String scopeName = scopeStack.get(i);
            HashMap<String, SymbolTableEntry> scopeTable = scopes.get(scopeName);
            
            if (scopeTable.containsKey(name)) {
                return scopeTable.get(name);
            }
        }
        
        // Not found 
        return null;
    }
       
 public SymbolTableEntry lookupInScope(String name, String scopeName) {
        HashMap<String, SymbolTableEntry> scopeTable = scopes.get(scopeName);
        if (scopeTable == null) {
            return null;
        }
        return scopeTable.get(name);
    }

  public HashMap<String, SymbolTableEntry> getScopeEntries(String scopeName) {
        return scopes.getOrDefault(scopeName, new HashMap<>());
    }

 public boolean existsInCurrentScope(String name) {
        String currentScope = getCurrentScope();
        HashMap<String, SymbolTableEntry> scopeTable = scopes.get(currentScope);
        return scopeTable.containsKey(name);
    }

      public void print() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                    SYMBOL TABLE");
        System.out.println("=".repeat(70));
        
        for (String scopeName : scopes.keySet()) {
            HashMap<String, SymbolTableEntry> scopeTable = scopes.get(scopeName);
            
            if (!scopeTable.isEmpty()) {
                System.out.println("\nScope: " + scopeName);
                System.out.println("-".repeat(70));
                System.out.printf("%-15s | %-12s | %-20s | %s%n", 
                    "Name", "Kind", "Scope", "Node ID");
                System.out.println("-".repeat(70));
                
                for (SymbolTableEntry entry : scopeTable.values()) {
                    System.out.println(entry);
                }
            }
        }
        System.out.println("=".repeat(70) + "\n");
    }



}

