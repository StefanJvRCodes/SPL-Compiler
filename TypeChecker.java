//A added this file for type checking (SPL_Types spec)

import java.util.*;

public class TypeChecker {
    private SymbolTable symbolTable;
    private List<String> errors;
    private List<String> warnings;
    
    public TypeChecker(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        this.errors = new ArrayList<>();
        this.warnings = new ArrayList<>();
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public List<String> getWarnings() {
        return warnings;
    }
    
    public boolean check(ASTNode root) {
        errors.clear();
        warnings.clear();
        
        System.out.println("\n[3] TYPE CHECKING PHASE...");
        boolean success = checkNode(root);
        
        if (success && errors.isEmpty()) {
            System.out.println("✓ Type checking completed successfully!");
        } else {
            System.out.println("✗ Type checking failed!");
        }
        
        return success && errors.isEmpty();
    }
    
    private boolean checkNode(ASTNode node) {
        if (node == null) return true;
        
        String nodeType = node.getNodeType();
        boolean success = true;
        
        switch (nodeType) {
            case "SPL_PROG":
                success = checkSplProg(node);
                break;
            case "VARIABLES":
                success = checkVariables(node);
                break;
            case "PROCDEFS":
                success = checkProcdefs(node);
                break;
            case "FUNCDEFS":
                success = checkFuncdefs(node);
                break;
            case "PDEF":
                success = checkPdef(node);
                break;
            case "FDEF":
                success = checkFdef(node);
                break;
            case "MAINPROG":
                success = checkMainprog(node);
                break;
            case "MAXTHREE":
                success = checkMaxthree(node);
                break;
            case "PARAM":
                success = checkParam(node);
                break;
            case "BODY":
                success = checkBody(node);
                break;
            case "ALGO":
                success = checkAlgo(node);
                break;
            case "INSTR":
                success = checkInstr(node);
                break;
            case "ASSIGN":
                success = checkAssign(node);
                break;
            case "LOOP":
                success = checkLoop(node);
                break;
            case "BRANCH":
                success = checkBranch(node);
                break;
            case "TERM":
                success = checkTerm(node);
                break;
            case "ATOM":
                success = checkAtom(node);
                break;
            case "UNOP":
                success = checkUnop(node);
                break;
            case "BINOP":
                success = checkBinop(node);
                break;
            case "INPUT":
                success = checkInput(node);
                break;
            case "OUTPUT":
                success = checkOutput(node);
                break;
            case "HALT":
            case "PRINT":
            case "WHILE":
            case "DO":
            case "IF":
                // These nodes don't have specific type rules beyond their children
                success = true;
                break;
            default:
                // For other node types, just check children
                break;
        }
        
        // Always check children
        for (ASTNode child : node.getChildren()) {
            success = checkNode(child) && success;
        }
        
        return success;
    }
    
    private boolean checkSplProg(ASTNode node) {
        // SPL_PROG is correctly typed if all its components are correctly typed
        return checkAllChildren(node);
    }
    
    private boolean checkVariables(ASTNode node) {
        // VARIABLES is correctly typed if all VAR are numeric
        boolean success = true;
        for (ASTNode child : node.getChildren()) {
            if ("VAR".equals(child.getNodeType())) {
                // VAR is always numeric (fact from spec)
                child.setType("numeric");
                // Update symbol table if this VAR is a declaration
                SymbolTableEntry entry = symbolTable.lookup(child.getValue());
                if (entry != null) {
                    entry.setType("numeric");
                }
            }
            success = checkNode(child) && success;
        }
        return success;
    }
    
    private boolean checkProcdefs(ASTNode node) {
        // PROCDEFS is correctly typed if all PDEF are correctly typed
        return checkAllChildren(node);
    }
    
    private boolean checkFuncdefs(ASTNode node) {
        // FUNCDEFS is correctly typed if all FDEF are correctly typed
        return checkAllChildren(node);
    }
    
    private boolean checkPdef(ASTNode node) {
        // PDEF is correctly typed if:
        // - NAME is typeless
        // - PARAM is correctly typed  
        // - BODY is correctly typed
        boolean success = true;
        
        // Check NAME is typeless
        ASTNode nameNode = findFirstChild(node, "NAME");
        if (nameNode != null && nameNode.getValue() != null) {
            SymbolTableEntry entry = symbolTable.lookup(nameNode.getValue());
            if (entry != null && !entry.getKind().isTypeless()) {
                errors.add("Type error: Procedure name '" + nameNode.getValue() + "' must be typeless");
                success = false;
            }
        }
        
        return success;
    }
    
    private boolean checkFdef(ASTNode node) {
        // FDEF is correctly typed if:
        // - NAME is typeless
        // - PARAM is correctly typed
        // - BODY is correctly typed
        // - return ATOM is numeric
        boolean success = true;
        
        // Check NAME is typeless
        ASTNode nameNode = findFirstChild(node, "NAME");
        if (nameNode != null && nameNode.getValue() != null) {
            SymbolTableEntry entry = symbolTable.lookup(nameNode.getValue());
            if (entry != null && !entry.getKind().isTypeless()) {
                errors.add("Type error: Function name '" + nameNode.getValue() + "' must be typeless");
                success = false;
            }
        }
        
        // Check return type is numeric
        // Look for return ATOM in FDEF children
        for (ASTNode child : node.getChildren()) {
            if ("ATOM".equals(child.getNodeType())) {
                checkNode(child); // Ensure ATOM is checked
                String atomType = child.getType();
                if (atomType != null && !"numeric".equals(atomType)) {
                    errors.add("Type error: Function return value must be numeric");
                    success = false;
                }
            }
        }
        
        return success;
    }
    
    private boolean checkMainprog(ASTNode node) {
        // MAINPROG is correctly typed if VARIABLES and ALGO are correctly typed
        return checkAllChildren(node);
    }
    
    private boolean checkMaxthree(ASTNode node) {
        // MAXTHREE is correctly typed if all VAR are numeric
        boolean success = true;
        for (ASTNode child : node.getChildren()) {
            if ("VAR".equals(child.getNodeType())) {
                child.setType("numeric");
                SymbolTableEntry entry = symbolTable.lookup(child.getValue());
                if (entry != null) {
                    entry.setType("numeric");
                }
            }
            success = checkNode(child) && success;
        }
        return success;
    }
    
    private boolean checkParam(ASTNode node) {
        // PARAM is correctly typed if MAXTHREE is correctly typed
        return checkAllChildren(node);
    }
    
    private boolean checkBody(ASTNode node) {
        // BODY is correctly typed if MAXTHREE and ALGO are correctly typed
        return checkAllChildren(node);
    }
    
    private boolean checkAlgo(ASTNode node) {
        // ALGO is correctly typed if all INSTR are correctly typed
        return checkAllChildren(node);
    }
    
    private boolean checkInstr(ASTNode node) {
        // INSTR dispatch to specific instruction types
        if (node.getChildren().isEmpty()) return true;
        
        ASTNode firstChild = node.getChildren().get(0);
        String instrType = firstChild.getNodeType();
        
        switch (instrType) {
            case "HALT":
                return true; // halt is always correctly typed
            case "PRINT":
                return checkPrint(firstChild);
            case "ASSIGN":
                return checkAssign(firstChild);
            case "WHILE":
            case "DO":
                return checkLoop(firstChild);
            case "IF":
                return checkBranch(firstChild);
            default:
                // Check if it's a procedure call (NAME with INPUT)
                if ("NAME".equals(instrType)) {
                    return checkProcedureCall(node);
                }
                return true;
        }
    }
    
    private boolean checkPrint(ASTNode node) {
        // PRINT is correctly typed if OUTPUT is correctly typed
        if (node.getChildren().size() > 0) {
            ASTNode output = node.getChildren().get(0);
            checkNode(output);
            // OUTPUT can be ATOM (numeric) or string (always correct)
            String outputType = output.getType();
            if (outputType != null && "ATOM".equals(output.getNodeType()) && !"numeric".equals(outputType)) {
                errors.add("Type error: PRINT statement requires numeric ATOM or string");
                return false;
            }
        }
        return true;
    }
    
    private boolean checkProcedureCall(ASTNode node) {
        // NAME ( INPUT ) - NAME must be typeless and INPUT correctly typed
        if (node.getChildren().size() < 2) return true;
        
        ASTNode nameNode = node.getChildren().get(0);
        ASTNode inputNode = node.getChildren().get(1);
        
        // Check NAME is typeless (procedure)
        if (nameNode.getValue() != null) {
            SymbolTableEntry entry = symbolTable.lookup(nameNode.getValue());
            if (entry != null && !entry.getKind().isTypeless()) {
                errors.add("Type error: Procedure call '" + nameNode.getValue() + "' must be typeless");
                return false;
            }
        }
        
        // Check INPUT
        return checkNode(inputNode);
    }
    
    private boolean checkAssign(ASTNode node) {
        // ASSIGN has two forms:
        // 1. VAR = NAME ( INPUT ) - function call
        // 2. VAR = TERM - expression
        
        if (node.getChildren().size() < 2) return true;
        
        ASTNode varNode = node.getChildren().get(0);
        ASTNode rhsNode = node.getChildren().get(1);
        
        // VAR must be numeric
        varNode.setType("numeric");
        if (varNode.getValue() != null) {
            SymbolTableEntry varEntry = symbolTable.lookup(varNode.getValue());
            if (varEntry != null) {
                varEntry.setType("numeric");
            }
        }
        
        checkNode(rhsNode);
        
        // Check RHS type
        if ("NAME".equals(rhsNode.getNodeType())) {
            // Function call: VAR = NAME ( INPUT )
            // NAME must be typeless and INPUT correctly typed
            if (rhsNode.getValue() != null) {
                SymbolTableEntry funcEntry = symbolTable.lookup(rhsNode.getValue());
                if (funcEntry != null && !funcEntry.getKind().isTypeless()) {
                    errors.add("Type error: Function call '" + rhsNode.getValue() + "' in assignment must be typeless");
                    return false;
                }
            }
            
            // Check INPUT if present
            if (node.getChildren().size() > 2) {
                ASTNode inputNode = node.getChildren().get(2);
                checkNode(inputNode);
            }
        } else {
            // VAR = TERM - TERM must be numeric
            String rhsType = rhsNode.getType();
            if (rhsType != null && !"numeric".equals(rhsType)) {
                errors.add("Type error: Assignment requires numeric expression, found: " + rhsType);
                return false;
            }
        }
        
        return true;
    }
    
    private boolean checkLoop(ASTNode node) {
        // LOOP is correctly typed if TERM is boolean and ALGO is correctly typed
        boolean success = true;
        
        // Find TERM and ALGO children
        for (ASTNode child : node.getChildren()) {
            if ("TERM".equals(child.getNodeType())) {
                checkNode(child);
                String termType = child.getType();
                if (termType != null && !"boolean".equals(termType)) {
                    errors.add("Type error: Loop condition must be boolean, found: " + termType);
                    success = false;
                }
            } else {
                success = checkNode(child) && success;
            }
        }
        
        return success;
    }
    
    private boolean checkBranch(ASTNode node) {
        // BRANCH is correctly typed if TERM is boolean and ALGO(s) are correctly typed
        boolean success = true;
        
        // Find TERM child
        for (ASTNode child : node.getChildren()) {
            if ("TERM".equals(child.getNodeType())) {
                checkNode(child);
                String termType = child.getType();
                if (termType != null && !"boolean".equals(termType)) {
                    errors.add("Type error: Branch condition must be boolean, found: " + termType);
                    success = false;
                }
                break; // Only first TERM is the condition
            }
        }
        
        // Check all ALGO children
        for (ASTNode child : node.getChildren()) {
            if ("ALGO".equals(child.getNodeType())) {
                success = checkNode(child) && success;
            }
        }
        
        return success;
    }
    
    private boolean checkTerm(ASTNode node) {
        // TERM can be:
        // 1. ATOM -> numeric if ATOM is numeric
        // 2. ( UNOP TERM ) -> numeric if UNOP is numeric and TERM is numeric, OR boolean if UNOP is boolean and TERM is boolean
        // 3. ( TERM BINOP TERM ) -> similar rules
        
        if (node.getChildren().isEmpty()) return true;
        
        ASTNode firstChild = node.getChildren().get(0);
        
        if ("ATOM".equals(firstChild.getNodeType())) {
            // TERM ::= ATOM
            checkNode(firstChild);
            String atomType = firstChild.getType();
            if (atomType != null) {
                node.setType(atomType);
            }
            return true;
        } else if ("UNOP".equals(firstChild.getNodeType())) {
            // TERM ::= ( UNOP TERM )
            return checkUnopTerm(node);
        } else if ("TERM".equals(firstChild.getNodeType())) {
            // TERM ::= ( TERM BINOP TERM )
            return checkBinopTerm(node);
        } else if ("BINOP_EXPR".equals(firstChild.getNodeType())) {
            // This is our parser's representation of binary operations
            return checkBinopExpr(firstChild);
        }
        
        return true;
    }
    
    private boolean checkUnopTerm(ASTNode node) {
        // TERM ::= ( UNOP TERM )
        if (node.getChildren().size() < 2) return true;
        
        ASTNode unopNode = node.getChildren().get(0);
        ASTNode termNode = node.getChildren().get(1);
        
        checkNode(unopNode);
        checkNode(termNode);
        
        String unopType = unopNode.getType();
        String termType = termNode.getType();
        
        if (unopType == null || termType == null) {
            return true; // Can't check if types are not set
        }
        
        if ("neg".equals(unopNode.getValue())) {
            // neg requires numeric operand and produces numeric
            if (!"numeric".equals(termType)) {
                errors.add("Type error: 'neg' operator requires numeric operand, found: " + termType);
                return false;
            }
            node.setType("numeric");
        } else if ("not".equals(unopNode.getValue())) {
            // not requires boolean operand and produces boolean
            if (!"boolean".equals(termType)) {
                errors.add("Type error: 'not' operator requires boolean operand, found: " + termType);
                return false;
            }
            node.setType("boolean");
        }
        
        return true;
    }
    
    private boolean checkBinopExpr(ASTNode node) {
        // BINOP_EXPR ::= TERM BINOP TERM
        if (node.getChildren().size() < 3) return true;
        
        ASTNode term1 = node.getChildren().get(0);
        ASTNode binop = node.getChildren().get(1);
        ASTNode term2 = node.getChildren().get(2);
        
        checkNode(term1);
        checkNode(term2);
        checkNode(binop);
        
        String term1Type = term1.getType();
        String term2Type = term2.getType();
        String binopType = binop.getType();
        
        if (term1Type == null || term2Type == null || binopType == null) {
            return true; // Can't check if types are not set
        }
        
        if (!term1Type.equals(term2Type)) {
            errors.add("Type error: Binary operator requires operands of same type, found: " + 
                      term1Type + " and " + term2Type);
            return false;
        }
        
        if ("numeric".equals(binopType)) {
            // plus, minus, mult, div
            if (!"numeric".equals(term1Type)) {
                errors.add("Type error: Arithmetic operator requires numeric operands, found: " + term1Type);
                return false;
            }
            node.setType("numeric");
        } else if ("boolean".equals(binopType)) {
            // and, or
            if (!"boolean".equals(term1Type)) {
                errors.add("Type error: Boolean operator requires boolean operands, found: " + term1Type);
                return false;
            }
            node.setType("boolean");
        } else if ("comparison".equals(binopType)) {
            // eq, >
            if (!"numeric".equals(term1Type)) {
                errors.add("Type error: Comparison operator requires numeric operands, found: " + term1Type);
                return false;
            }
            node.setType("boolean");
        }
        
        return true;
    }
    
    private boolean checkBinopTerm(ASTNode node) {
        // To handles the case where we have direct TERM BINOP TERM structure
        return checkBinopExpr(node);
    }
    
    private boolean checkAtom(ASTNode node) {
        // ATOM can be VAR (numeric) or number (numeric)
        if (node.getValue() != null && isNumeric(node.getValue())) {
            node.setType("numeric");
            return true;
        }
        
        if (!node.getChildren().isEmpty()) {
            ASTNode child = node.getChildren().get(0);
            if ("VAR".equals(child.getNodeType())) {
                // VAR is always numeric
                child.setType("numeric");
                node.setType("numeric");
                
                // Update symbol table
                if (child.getValue() != null) {
                    SymbolTableEntry entry = symbolTable.lookup(child.getValue());
                    if (entry != null) {
                        entry.setType("numeric");
                    }
                }
                return true;
            }
        }
        
        return true;
    }
    
    private boolean checkUnop(ASTNode node) {
        // UNOP is either "neg" (numeric) or "not" (boolean)
        if ("neg".equals(node.getValue())) {
            node.setType("numeric");
        } else if ("not".equals(node.getValue())) {
            node.setType("boolean");
        }
        return true;
    }
    
    private boolean checkBinop(ASTNode node) {
        // Set BINOP type based on operator
        String op = node.getValue();
        if (op == null) return true;
        
        switch (op) {
            case "plus":
            case "minus":
            case "mult":
            case "div":
                node.setType("numeric");
                break;
            case "or":
            case "and":
                node.setType("boolean");
                break;
            case "eq":
            case ">":
                node.setType("comparison");
                break;
        }
        return true;
    }
    
    private boolean checkInput(ASTNode node) {
        // INPUT is correctly typed if all ATOM are numeric
        boolean success = true;
        for (ASTNode child : node.getChildren()) {
            if ("ATOM".equals(child.getNodeType())) {
                checkNode(child);
                String atomType = child.getType();
                if (atomType != null && !"numeric".equals(atomType)) {
                    errors.add("Type error: INPUT requires numeric ATOM, found: " + atomType);
                    success = false;
                }
            }
        }
        return success;
    }
    
    private boolean checkOutput(ASTNode node) {
        // OUTPUT can be ATOM (must be numeric) or string (always correct)
        if (!node.getChildren().isEmpty()) {
            ASTNode child = node.getChildren().get(0);
            if ("ATOM".equals(child.getNodeType())) {
                checkNode(child);
                String atomType = child.getType();
                if (atomType != null && !"numeric".equals(atomType)) {
                    errors.add("Type error: OUTPUT requires numeric ATOM or string, found: " + atomType);
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean checkAllChildren(ASTNode node) {
        boolean success = true;
        for (ASTNode child : node.getChildren()) {
            success = checkNode(child) && success;
        }
        return success;
    }
    
    private ASTNode findFirstChild(ASTNode node, String nodeType) {
        for (ASTNode child : node.getChildren()) {
            if (nodeType.equals(child.getNodeType())) {
                return child;
            }
        }
        return null;
    }
    
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public void printResults() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           TYPE CHECKING RESULTS");
        System.out.println("=".repeat(50));
        
        if (errors.isEmpty()) {
            System.out.println("✓ No type errors found!");
        } else {
            System.out.println("✗ Found " + errors.size() + " type error(s):");
            System.out.println("-".repeat(50));
            for (int i = 0; i < errors.size(); i++) {
                System.out.println((i + 1) + ". " + errors.get(i));
            }
        }
        
        if (!warnings.isEmpty()) {
            System.out.println("\nFound " + warnings.size() + " warning(s):");
            System.out.println("-".repeat(50));
            for (int i = 0; i < warnings.size(); i++) {
                System.out.println((i + 1) + ". " + warnings.get(i));
            }
        }
        
        System.out.println("=".repeat(50));
    }
}
