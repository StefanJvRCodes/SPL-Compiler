import java.util.*;

public class SemanticAnalyzer {
    private SymbolTable symbolTable;
    private List<String> errors;
    private Set<String> globalNames; // Track all names in Everywhere scope for cross-scope checking
    
    public SemanticAnalyzer() {
        this.symbolTable = new SymbolTable();
        this.errors = new ArrayList<>();
        this.globalNames = new HashSet<>();
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
    
    public boolean analyze(ASTNode root) {
        errors.clear();
        
        // First pass: collect all names in Everywhere scope
        collectEverywhereNames(root);
        
        // Second pass: perform semantic analysis
        analyzeNode(root);
        
        return errors.isEmpty();
    }
    
    // Collect all global, procedure, and function names for cross-scope checking
    private void collectEverywhereNames(ASTNode node) {
        if (node == null) return;
        
        String nodeType = node.getNodeType();
        
        switch (nodeType) {
            case "SPL_PROG":
                symbolTable.enterScope("Everywhere");
                break;
                
            case "VARIABLES":
                if (node.getParent() != null && "SPL_PROG".equals(node.getParent().getNodeType())) {
                    // Global variables
                    for (ASTNode child : node.getChildren()) {
                        if ("VAR".equals(child.getNodeType()) && child.getValue() != null) {
                            globalNames.add(child.getValue());
                        }
                    }
                }
                break;
                
            case "PDEF":
                // Procedure names
                for (ASTNode child : node.getChildren()) {
                    if ("NAME".equals(child.getNodeType()) && child.getValue() != null) {
                        globalNames.add(child.getValue());
                    }
                }
                break;
                
            case "FDEF":
                // Function names
                for (ASTNode child : node.getChildren()) {
                    if ("NAME".equals(child.getNodeType()) && child.getValue() != null) {
                        globalNames.add(child.getValue());
                    }
                }
                break;
        }
        
        for (ASTNode child : node.getChildren()) {
            collectEverywhereNames(child);
        }
    }
    
    private void analyzeNode(ASTNode node) {
        if (node == null) return;
        
        String nodeType = node.getNodeType();
        
        switch (nodeType) {
            case "SPL_PROG":
                analyzeSplProg(node);
                break;
            case "VARIABLES":
                analyzeVariables(node);
                break;
            case "PROCDEFS":
                analyzeProcdefs(node);
                break;
            case "FUNCDEFS":
                analyzeFuncdefs(node);
                break;
            case "PDEF":
                analyzePdef(node);
                break;
            case "FDEF":
                analyzeFdef(node);
                break;
            case "MAINPROG":
                analyzeMainprog(node);
                break;
            case "MAXTHREE":
                analyzeMaxthree(node);
                break;
            case "VAR":
                analyzeVar(node);
                break;
            default:
                // For other node types, just continue traversal
                for (ASTNode child : node.getChildren()) {
                    analyzeNode(child);
                }
                break;
        }
    }
    
    private void analyzeSplProg(ASTNode node) {
        symbolTable.enterScope("Everywhere");
        
        for (ASTNode child : node.getChildren()) {
            analyzeNode(child);
        }
        
        // Check cross-scope name conflicts in Everywhere scope
        checkEverywhereScopeConflicts();
        
        symbolTable.exitScope();
    }
    
    private void analyzeVariables(ASTNode node) {
        String scope = node.findScope();
        symbolTable.enterScope(scope);
        
        Set<String> declaredNames = new HashSet<>();
        
        for (ASTNode child : node.getChildren()) {
            if ("VAR".equals(child.getNodeType()) && child.getValue() != null) {
                String varName = child.getValue();
                
                // Check for duplicate declaration in same scope
                if (declaredNames.contains(varName)) {
                    errors.add("Name-rule-violation: Variable '" + varName + 
                              "' declared multiple times in scope '" + scope + "'");
                } else {
                    declaredNames.add(varName);
                    
                    SymbolKinds kind = getVariableKind(scope);
                    if (!symbolTable.insert(varName, kind, child.getNodeId())) {
                        errors.add("Name-rule-violation: Failed to insert variable '" + varName + "' in scope '" + scope + "'");
                    }
                }
            }
            analyzeNode(child);
        }
        
        symbolTable.exitScope();
    }
    
    private void analyzeProcdefs(ASTNode node) {
        symbolTable.enterScope("Procedure");
        
        Set<String> declaredNames = new HashSet<>();
        
        for (ASTNode child : node.getChildren()) {
            if ("PDEF".equals(child.getNodeType())) {
                ASTNode nameNode = findFirstChild(child, "NAME");
                if (nameNode != null && nameNode.getValue() != null) {
                    String procName = nameNode.getValue();
                    
                    if (declaredNames.contains(procName)) {
                        errors.add("Name-rule-violation: Procedure '" + procName + 
                                  "' declared multiple times in Procedure scope");
                    } else {
                        declaredNames.add(procName);
                        symbolTable.insert(procName, SymbolKinds.PROCEDURE, nameNode.getNodeId());
                    }
                }
            }
            analyzeNode(child);
        }
        
        symbolTable.exitScope();
    }
    
    private void analyzeFuncdefs(ASTNode node) {
        symbolTable.enterScope("Function");
        
        Set<String> declaredNames = new HashSet<>();
        
        for (ASTNode child : node.getChildren()) {
            if ("FDEF".equals(child.getNodeType())) {
                ASTNode nameNode = findFirstChild(child, "NAME");
                if (nameNode != null && nameNode.getValue() != null) {
                    String funcName = nameNode.getValue();
                    
                    if (declaredNames.contains(funcName)) {
                        errors.add("Name-rule-violation: Function '" + funcName + 
                                  "' declared multiple times in Function scope");
                    } else {
                        declaredNames.add(funcName);
                        symbolTable.insert(funcName, SymbolKinds.FUNCTION, nameNode.getNodeId());
                    }
                }
            }
            analyzeNode(child);
        }
        
        symbolTable.exitScope();
    }
    
    private void analyzePdef(ASTNode node) {
        String localScope = "Local_" + node.getNodeId();
        symbolTable.enterScope(localScope);
        
        // Collect parameter names
        Set<String> paramNames = new HashSet<>();
        ASTNode paramNode = findFirstChild(node, "PARAM");
        if (paramNode != null) {
            collectParamNames(paramNode, paramNames);
        }
        
        // Analyze body and check for shadowing
        ASTNode bodyNode = findFirstChild(node, "BODY");
        if (bodyNode != null) {
            checkShadowing(bodyNode, paramNames, "procedure");
            analyzeNode(bodyNode);
        }
        
        // Analyze other children
        for (ASTNode child : node.getChildren()) {
            if (!"BODY".equals(child.getNodeType())) {
                analyzeNode(child);
            }
        }
        
        symbolTable.exitScope();
    }
    
    private void analyzeFdef(ASTNode node) {
        String localScope = "Local_" + node.getNodeId();
        symbolTable.enterScope(localScope);
        
        // Collect parameter names
        Set<String> paramNames = new HashSet<>();
        ASTNode paramNode = findFirstChild(node, "PARAM");
        if (paramNode != null) {
            collectParamNames(paramNode, paramNames);
        }
        
        // Analyze body and check for shadowing
        ASTNode bodyNode = findFirstChild(node, "BODY");
        if (bodyNode != null) {
            checkShadowing(bodyNode, paramNames, "function");
            analyzeNode(bodyNode);
        }
        
        // Analyze other children
        for (ASTNode child : node.getChildren()) {
            if (!"BODY".equals(child.getNodeType())) {
                analyzeNode(child);
            }
        }
        
        symbolTable.exitScope();
    }
    
    private void analyzeMainprog(ASTNode node) {
        symbolTable.enterScope("Main");
        
        for (ASTNode child : node.getChildren()) {
            analyzeNode(child);
        }
        
        symbolTable.exitScope();
    }
    
    private void analyzeMaxthree(ASTNode node) {
        Set<String> declaredNames = new HashSet<>();
        
        for (ASTNode child : node.getChildren()) {
            if ("VAR".equals(child.getNodeType()) && child.getValue() != null) {
                String varName = child.getValue();
                
                if (declaredNames.contains(varName)) {
                    errors.add("Name-rule-violation: Variable '" + varName + 
                              "' declared multiple times in MAXTHREE");
                } else {
                    declaredNames.add(varName);
                    SymbolKinds kind = getVariableKind(symbolTable.getCurrentScope());
                    symbolTable.insert(varName, kind, child.getNodeId());
                }
            }
            analyzeNode(child);
        }
    }
    
    private void analyzeVar(ASTNode node) {
        if (node.getValue() == null) return;
        
        String varName = node.getValue();
        
        // Check if this VAR is in an ALGO context (variable usage, not declaration)
        if (isInAlgo(node)) {
            checkVariableUsage(node, varName);
        }
        
        // Continue with children
        for (ASTNode child : node.getChildren()) {
            analyzeNode(child);
        }
    }
    
    private void checkVariableUsage(ASTNode varNode, String varName) {
        String currentScope = symbolTable.getCurrentScope();
        
        if (currentScope.startsWith("Local_")) {
            // In function or procedure local scope
            ASTNode enclosingFunc = varNode.findEnclosingFunction();
            if (enclosingFunc != null) {
                checkLocalScopeVariable(varNode, varName, enclosingFunc);
            }
        } else if ("Main".equals(currentScope)) {
            checkMainScopeVariable(varNode, varName);
        } else {
            // Global or other scope - just do normal lookup
            SymbolTableEntry entry = symbolTable.lookup(varName);
            if (entry == null) {
                errors.add("Undeclared variable: '" + varName + "' at node " + varNode.getNodeId());
            }
        }
    }
    
    private void checkLocalScopeVariable(ASTNode varNode, String varName, ASTNode enclosingFunc) {
        // Check parameters first
        ASTNode paramNode = findFirstChild(enclosingFunc, "PARAM");
        if (paramNode != null && containsVariable(paramNode, varName)) {
            // Found in parameters - update symbol table
            symbolTable.insert(varName, SymbolKinds.PARAMETER, varNode.getNodeId());
            return;
        }
        
        // Check local variables
        ASTNode bodyNode = findFirstChild(enclosingFunc, "BODY");
        if (bodyNode != null) {
            ASTNode maxthreeNode = findFirstChild(bodyNode, "MAXTHREE");
            if (maxthreeNode != null && containsVariable(maxthreeNode, varName)) {
                // Found in local variables
                symbolTable.insert(varName, SymbolKinds.LOCAL_VAR, varNode.getNodeId());
                return;
            }
        }
        
        // Check global variables  
        SymbolTableEntry globalEntry = symbolTable.lookupInScope(varName, "Everywhere");
        if (globalEntry != null) {
            // Found in global scope
            return;
        }
        
        errors.add("Undeclared variable: '" + varName + "' at node " + varNode.getNodeId());
    }
    
    private void checkMainScopeVariable(ASTNode varNode, String varName) {
        // Check main's declared variables first
        SymbolTableEntry mainEntry = symbolTable.lookupInScope(varName, "Main");
        if (mainEntry != null) {
            return;
        }
        
        // Check global variables
        SymbolTableEntry globalEntry = symbolTable.lookupInScope(varName, "Everywhere");
        if (globalEntry != null) {
            return;
        }
        
        errors.add("Undeclared variable: '" + varName + "' at node " + varNode.getNodeId());
    }
    
    private void checkEverywhereScopeConflicts() {
        Map<String, List<String>> nameToKinds = new HashMap<>();
        
        // Collect all names and their kinds from Everywhere, Procedure, and Function scopes
        collectNamesFromScope("Everywhere", nameToKinds, "variable");
        collectNamesFromScope("Procedure", nameToKinds, "procedure");
        collectNamesFromScope("Function", nameToKinds, "function");
        
        // Check for conflicts
        for (Map.Entry<String, List<String>> entry : nameToKinds.entrySet()) {
            String name = entry.getKey();
            List<String> kinds = entry.getValue();
            
            if (kinds.size() > 1) {
                errors.add("Name-rule-violation: '" + name + 
                          "' is declared as both " + String.join(" and ", kinds) + 
                          " in Everywhere scope");
            }
        }
    }
    
    private void collectNamesFromScope(String scopeName, Map<String, List<String>> nameToKinds, String kindName) {
        HashMap<String, SymbolTableEntry> scopeEntries = symbolTable.getScopeEntries(scopeName);
        for (SymbolTableEntry entry : scopeEntries.values()) {
            String name = entry.getName();
            nameToKinds.computeIfAbsent(name, k -> new ArrayList<>()).add(kindName);
        }
    }
    
    private void checkShadowing(ASTNode bodyNode, Set<String> paramNames, String funcType) {
        ASTNode maxthreeNode = findFirstChild(bodyNode, "MAXTHREE");
        if (maxthreeNode != null) {
            for (ASTNode child : maxthreeNode.getChildren()) {
                if ("VAR".equals(child.getNodeType()) && child.getValue() != null) {
                    String varName = child.getValue();
                    if (paramNames.contains(varName)) {
                        errors.add("Name-rule-violation: Local variable '" + varName + 
                                  "' shadows parameter in " + funcType);
                    }
                }
            }
        }
    }
    
    private void collectParamNames(ASTNode paramNode, Set<String> paramNames) {
        for (ASTNode child : paramNode.getChildren()) {
            if ("VAR".equals(child.getNodeType()) && child.getValue() != null) {
                String paramName = child.getValue();
                paramNames.add(paramName);
                symbolTable.insert(paramName, SymbolKinds.PARAMETER, child.getNodeId());
            } else if (child.getChildren().size() > 0) {
                collectParamNames(child, paramNames);
            }
        }
    }
    
    private boolean containsVariable(ASTNode node, String varName) {
        if ("VAR".equals(node.getNodeType()) && varName.equals(node.getValue())) {
            return true;
        }
        for (ASTNode child : node.getChildren()) {
            if (containsVariable(child, varName)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isInAlgo(ASTNode node) {
        ASTNode current = node.getParent();
        while (current != null) {
            if ("ALGO".equals(current.getNodeType())) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }
    
    private ASTNode findFirstChild(ASTNode node, String nodeType) {
        for (ASTNode child : node.getChildren()) {
            if (nodeType.equals(child.getNodeType())) {
                return child;
            }
        }
        return null;
    }
    
    private SymbolKinds getVariableKind(String scope) {
        switch (scope) {
            case "Global":
                return SymbolKinds.GLOBAL_VAR;
            case "Main":
                return SymbolKinds.MAIN_VAR;
            default:
                if (scope.startsWith("Local_")) {
                    return SymbolKinds.LOCAL_VAR;
                }
                return SymbolKinds.GLOBAL_VAR;
        }
    }
    
    public void printResults() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("         SEMANTIC ANALYSIS RESULTS");
        System.out.println("=".repeat(50));
        
        if (errors.isEmpty()) {
            System.out.println("✓ No semantic errors found!");
        } else {
            System.out.println("✗ Found " + errors.size() + " semantic error(s):");
            System.out.println("-".repeat(50));
            for (int i = 0; i < errors.size(); i++) {
                System.out.println((i + 1) + ". " + errors.get(i));
            }
        }
        
        System.out.println("=".repeat(50));
        symbolTable.print();
    }
}