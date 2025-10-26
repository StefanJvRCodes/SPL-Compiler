import java.util.*;

public class CodeGenerator {
    private SymbolTable symbolTable;
    private StringBuilder targetCode;
    private Map<String, ASTNode> procedureDefinitions;
    private Map<String, ASTNode> functionDefinitions;
    private int lineNumber;
    
    public CodeGenerator(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        this.targetCode = new StringBuilder();
        this.procedureDefinitions = new HashMap<>();
        this.functionDefinitions = new HashMap<>();
    }
    
    public String generateCode(ASTNode ast) {
        targetCode.setLength(0);
        
        // Add modern BASIC program header
        targetCode.append("' SPL Compiler Generated Code\n");
        targetCode.append("' Modern BASIC Format\n\n");
        
        collectFunctionAndProcedureDefinitions(ast);
        
        ASTNode mainNode = findMainProgram(ast);
        if (mainNode != null) {
            generateMainProgram(mainNode);
        }
        
        return targetCode.toString();
    }
    
    private void addLine(String code) {
        targetCode.append(code).append("\n");
    }
    
    private void collectFunctionAndProcedureDefinitions(ASTNode node) {
        if (node == null) return;
        
        if ("PDEF".equals(node.getNodeType())) {
            ASTNode nameNode = node.getChildren().get(0);
            if (nameNode != null && "NAME".equals(nameNode.getNodeType())) {
                procedureDefinitions.put(nameNode.getValue(), node);
            }
        } else if ("FDEF".equals(node.getNodeType())) {
            ASTNode nameNode = node.getChildren().get(0);
            if (nameNode != null && "NAME".equals(nameNode.getNodeType())) {
                functionDefinitions.put(nameNode.getValue(), node);
            }
        }
        
        for (ASTNode child : node.getChildren()) {
            collectFunctionAndProcedureDefinitions(child);
        }
    }
    
    private ASTNode findMainProgram(ASTNode node) {
        if (node == null) return null;
        
        if ("MAINPROG".equals(node.getNodeType())) {
            return node;
        }
        
        for (ASTNode child : node.getChildren()) {
            ASTNode found = findMainProgram(child);
            if (found != null) return found;
        }
        
        return null;
    }
    
    private void generateMainProgram(ASTNode mainProg) {
        for (ASTNode child : mainProg.getChildren()) {
            if ("VARIABLES".equals(child.getNodeType())) {
                generateVariables(child);
            } else if ("ALGO".equals(child.getNodeType())) {
                generateAlgo(child);
            }
        }
    }
    
    private void generateVariables(ASTNode variables) {
        // Translation Advice: Variable-Declarations do not get translated to target code
        // They were only needed for filling the Symbol-Table
    }
    
    private void generateProcedure(ASTNode pdef) {
        // Translation Advice: The Sub-Tree "under" tree-node PDEF will later be used for "inlining"
        // For now, we just store the definition for potential inlining
    }
    
    private void generateFunction(ASTNode fdef) {
        // Translation Advice: The Sub-Tree "under" tree-node FDEF will later be used for "inlining"  
        // For now, we just store the definition for potential inlining
    }
    
    private void generateBody(ASTNode body) {
        for (ASTNode child : body.getChildren()) {
            if ("MAXTHREE".equals(child.getNodeType())) {
                generateMaxThree(child);
            } else if ("ALGO".equals(child.getNodeType())) {
                generateAlgo(child);
            }
        }
    }
    
    private void generateMaxThree(ASTNode maxThree) {
        // Translation Advice: Variable-Declarations do not get translated to target code
        // Only the ALGO will get translated
    }
    
    private void generateAlgo(ASTNode algo) {
        if (algo == null || algo.getChildren().isEmpty()) {
            return;
        }
        
        // Translation Advice: Similar to Trans(Stat → Stat1 ; Stat2) in Fig.6.5 of textbook
        for (ASTNode child : algo.getChildren()) {
            if ("HALT".equals(child.getNodeType()) || 
                "PRINT".equals(child.getNodeType()) || 
                "ASSIGN".equals(child.getNodeType()) ||
                "WHILE".equals(child.getNodeType()) ||
                "DO".equals(child.getNodeType()) ||
                "IF".equals(child.getNodeType())) {
                
                String instruction = generateInstructionCode(child);
                if (!instruction.isEmpty()) {
                    addLine(instruction);
                }
            } else if ("ALGO".equals(child.getNodeType())) {
                generateAlgo(child);
            }
        }
    }
    
    private String generateInstructionCode(ASTNode instr) {
        String nodeType = instr.getNodeType();
        
        switch (nodeType) {
            case "HALT":
                return "END";
            case "PRINT":
                return generatePrintCode(instr);
            case "ASSIGN":
                return generateAssignCode(instr);
            case "WHILE":
                return generateWhileCode(instr);
            case "DO":
                return generateDoCode(instr);
            case "IF":
                return generateIfCode(instr);
            case "PROC_CALL":
                return generateProcedureCallCode(instr);
            default:
                return "";
        }
    }
    
    private String generatePrintCode(ASTNode printNode) {
        if (printNode.getChildren().isEmpty()) {
            return "";
        }
        
        ASTNode outputNode = printNode.getChildren().get(0);
        String code = "";
        
        if ("STRING".equals(outputNode.getNodeType())) {
            // Handle string literals directly
            String value = outputNode.getValue();
            code = value;
        } else if ("ATOM".equals(outputNode.getNodeType())) {
            if (outputNode.getValue() != null) {
                // Check if it's a string (starts and ends with quotes) or number
                String value = outputNode.getValue();
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    // It's a string literal - use as is
                    code = value;
                } else if (isNumeric(value)) {
                    // It's a number
                    code = value;
                } else {
                    // It's some other literal value
                    code = value;
                }
            } else if (!outputNode.getChildren().isEmpty() && 
                       "VAR".equals(outputNode.getChildren().get(0).getNodeType())) {
                // It's a variable - lookup in symbol table
                String varName = outputNode.getChildren().get(0).getValue();
                SymbolTableEntry entry = symbolTable.lookup(varName);
                if (entry != null) {
                    code = entry.getName();
                } else {
                    code = varName;
                }
            }
        }
        
        return "PRINT " + code;
    }
    
    private String generateAssignCode(ASTNode assignNode) {
        if (assignNode.getChildren().size() >= 2) {
            ASTNode varNode = assignNode.getChildren().get(0);
            ASTNode rightSide = assignNode.getChildren().get(1);
            
            if ("VAR".equals(varNode.getNodeType())) {
                String varName = varNode.getValue();
                SymbolTableEntry entry = symbolTable.lookup(varName);
                String targetVar = (entry != null) ? entry.getName() : varName;
                
                StringBuilder assignment = new StringBuilder();
                assignment.append(targetVar).append(" = ");
                
                if ("FUNC_CALL".equals(rightSide.getNodeType())) {
                    assignment.append(generateFunctionCallCode(rightSide));
                } else {
                    assignment.append(generateTermCode(rightSide));
                }
                
                return assignment.toString();
            }
        }
        return "";
    }
    
    private String generateTermCode(ASTNode term) {
        if (term == null) return "";
        
        String nodeType = term.getNodeType();
        
        switch (nodeType) {
            case "ATOM":
                return generateAtomCode(term);
            case "UNOP_EXPR":
                return generateUnaryExpressionCode(term);
            case "BINOP_EXPR":
                return generateBinaryExpressionCode(term);
            default:
                return "";
        }
    }
    
    private String generateAtomCode(ASTNode atom) {
        if (atom.getValue() != null) {
            // It's a number or literal
            if (isNumeric(atom.getValue())) {
                return atom.getValue();
            } else {
                return atom.getValue();
            }
        } else if (!atom.getChildren().isEmpty() && 
                   "VAR".equals(atom.getChildren().get(0).getNodeType())) {
            // It's a variable
            String varName = atom.getChildren().get(0).getValue();
            SymbolTableEntry entry = symbolTable.lookup(varName);
            if (entry != null) {
                return entry.getName();
            } else {
                return varName;
            }
        }
        return "";
    }
    
    private String generateUnaryExpressionCode(ASTNode unopExpr) {
        if (unopExpr.getChildren().size() < 2) return "";
        
        ASTNode unopNode = unopExpr.getChildren().get(0);
        ASTNode termNode = unopExpr.getChildren().get(1);
        
        String operator = unopNode.getValue();
        String basicOp = translateUnaryOperator(operator);
        
        return basicOp + " " + generateTermCode(termNode);
    }
    
    private String generateBinaryExpressionCode(ASTNode binopExpr) {
        if (binopExpr.getChildren().size() < 3) return "";
        
        ASTNode leftTerm = binopExpr.getChildren().get(0);
        ASTNode binopNode = binopExpr.getChildren().get(1);
        ASTNode rightTerm = binopExpr.getChildren().get(2);
        
        String leftCode = generateTermCode(leftTerm);
        String operator = binopNode.getValue();
        String rightCode = generateTermCode(rightTerm);
        
        return leftCode + " " + translateBinaryOperator(operator) + " " + rightCode;
    }
    
    private String generateFunctionCallCode(ASTNode funcCall) {
        return "REM Function call placeholder";
    }
    
    private String generateWhileCode(ASTNode whileNode) {
        if (whileNode.getChildren().size() >= 2) {
            StringBuilder result = new StringBuilder();
            result.append("WHILE ").append(generateTermCode(whileNode.getChildren().get(0)));
            addLine(result.toString());
            generateAlgo(whileNode.getChildren().get(1));
            addLine("WEND");
            return "";
        }
        return "";
    }
    
    private String generateDoCode(ASTNode doNode) {
        if (doNode.getChildren().size() >= 2) {
            addLine("DO");
            generateAlgo(doNode.getChildren().get(0));
            StringBuilder result = new StringBuilder();
            result.append("LOOP UNTIL ").append(generateTermCode(doNode.getChildren().get(1)));
            addLine(result.toString());
            return "";
        }
        return "";
    }
    
    private String generateIfCode(ASTNode ifNode) {
        if (ifNode.getChildren().size() >= 2) {
            StringBuilder result = new StringBuilder();
            result.append("IF ").append(generateTermCode(ifNode.getChildren().get(0))).append(" THEN");
            addLine(result.toString());
            generateAlgo(ifNode.getChildren().get(1));
            
            if (ifNode.getChildren().size() >= 3) {
                addLine("ELSE");
                generateAlgo(ifNode.getChildren().get(2));
            }
            addLine("END IF");
            return "";
        }
        return "";
    }
    
    private String generateProcedureCallCode(ASTNode procCall) {
        return "REM Procedure call placeholder";
    }
    
    private void generatePrint(ASTNode printNode) {
        // Translation Advice: Trans(print OUTPUT)
        if (printNode.getChildren().isEmpty()) {
            return;
        }
        
        ASTNode outputNode = printNode.getChildren().get(0);
        String code = "";
        
        if ("STRING".equals(outputNode.getNodeType())) {
            // Handle string literals directly
            String value = outputNode.getValue();
            code = " " + value + " ";
        } else if ("ATOM".equals(outputNode.getNodeType())) {
            if (outputNode.getValue() != null) {
                // Check if it's a string (starts and ends with quotes) or number
                String value = outputNode.getValue();
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    // It's a string literal - use as is
                    code = " " + value + " ";
                } else if (isNumeric(value)) {
                    // It's a number
                    code = " " + value + " ";
                } else {
                    // It's some other literal value
                    code = " " + value + " ";
                }
            } else if (!outputNode.getChildren().isEmpty() && 
                       "VAR".equals(outputNode.getChildren().get(0).getNodeType())) {
                // It's a variable - lookup in symbol table
                String varName = outputNode.getChildren().get(0).getValue();
                SymbolTableEntry entry = symbolTable.lookup(varName);
                if (entry != null) {
                    code = " " + entry.getName() + " ";
                } else {
                    code = " " + varName + " ";
                }
            }
        }
        
        targetCode.append("PRINT").append(code);
    }
    
    private void generateAssign(ASTNode assignNode) {
        // Translation Advice: Similar to Trans(Stat → id := Exp) in Fig.6.5 of textbook
        // however with a normal = (instead of the textbook's :=) in generated target code
        
        if (assignNode.getChildren().size() >= 2) {
            ASTNode varNode = assignNode.getChildren().get(0);
            ASTNode rightSide = assignNode.getChildren().get(1);
            
            if ("VAR".equals(varNode.getNodeType())) {
                String varName = varNode.getValue();
                SymbolTableEntry entry = symbolTable.lookup(varName);
                String targetVar = (entry != null) ? entry.getName() : varName;
                
                targetCode.append(targetVar).append(" = ");
                
                if ("FUNC_CALL".equals(rightSide.getNodeType())) {
                    generateFunctionCall(rightSide);
                } else {
                    generateTerm(rightSide);
                }
            }
        }
    }
    
    private void generateAtom(ASTNode atom) {
        // Translation Advice: Similar to Trans(Exp → id) and Trans(Exp → num) in Fig.6.3
        // however with normal = (instead of textbook's :=) in generated target code
        
        if (atom.getValue() != null) {
            // It's a number
            if (isNumeric(atom.getValue())) {
                targetCode.append(atom.getValue());
            } else {
                // It's a string or other literal
                targetCode.append(atom.getValue());
            }
        } else if (!atom.getChildren().isEmpty() && 
                   "VAR".equals(atom.getChildren().get(0).getNodeType())) {
            // It's a variable
            String varName = atom.getChildren().get(0).getValue();
            SymbolTableEntry entry = symbolTable.lookup(varName);
            if (entry != null) {
                targetCode.append(entry.getName());
            } else {
                targetCode.append(varName);
            }
        }
    }
    
    private void generateTerm(ASTNode term) {
        if (term == null) return;
        
        String nodeType = term.getNodeType();
        
        switch (nodeType) {
            case "ATOM":
                generateAtom(term);
                break;
            case "UNOP":
                // Legacy UNOP handling - should use UNOP_EXPR instead
                if (term.getChildren().size() > 0) {
                    String operator = term.getValue();
                    String basicOp = translateUnaryOperator(operator);
                    targetCode.append(basicOp).append(" ");
                    generateTerm(term.getChildren().get(0));
                }
                break;
            case "UNOP_EXPR":
                generateUnaryExpression(term);
                break;
            case "BINOP_EXPR":
                generateBinaryExpression(term);
                break;
            default:
                // Fallback for other term types
                for (ASTNode child : term.getChildren()) {
                    generateTerm(child);
                }
                break;
        }
    }
    
    
    private void generateWhile(ASTNode whileNode) {
        if (whileNode.getChildren().size() >= 2) {
            targetCode.append(" WHILE ");
            generateTerm(whileNode.getChildren().get(0));
            targetCode.append(" DO { ");
            generateAlgo(whileNode.getChildren().get(1));
            targetCode.append(" } ");
        }
    }
    
    private void generateDo(ASTNode doNode) {
        if (doNode.getChildren().size() >= 2) {
            targetCode.append(" DO { ");
            generateAlgo(doNode.getChildren().get(0));
            targetCode.append(" } UNTIL ");
            generateTerm(doNode.getChildren().get(1));
            targetCode.append(" ");
        }
    }
    
    private void generateIf(ASTNode ifNode) {
        if (ifNode.getChildren().size() >= 2) {
            targetCode.append(" IF ");
            generateTerm(ifNode.getChildren().get(0));
            targetCode.append(" THEN { ");
            generateAlgo(ifNode.getChildren().get(1));
            targetCode.append(" } ");
            
            if (ifNode.getChildren().size() >= 3) {
                targetCode.append(" ELSE { ");
                generateAlgo(ifNode.getChildren().get(2));
                targetCode.append(" } ");
            }
        }
    }
    
    private void generateUnaryExpression(ASTNode unopExpr) {
        // Handle ( UNOP TERM ) structure
        if (unopExpr.getChildren().size() < 2) return;
        
        ASTNode unopNode = unopExpr.getChildren().get(0);
        ASTNode termNode = unopExpr.getChildren().get(1);
        
        String operator = unopNode.getValue();
        String basicOp = translateUnaryOperator(operator);
        
        targetCode.append(basicOp).append(" ");
        generateTerm(termNode);
    }
    
    private void generateBinaryExpression(ASTNode binopExpr) {
        // Handle ( TERM BINOP TERM ) structure
        if (binopExpr.getChildren().size() < 3) return;
        
        ASTNode leftTerm = binopExpr.getChildren().get(0);
        ASTNode binopNode = binopExpr.getChildren().get(1);
        ASTNode rightTerm = binopExpr.getChildren().get(2);
        
        generateTerm(leftTerm);
        String operator = binopNode.getValue();
        targetCode.append(" ").append(translateBinaryOperator(operator)).append(" ");
        generateTerm(rightTerm);
    }
    
    
    private String translateUnaryOperator(String spOperator) {
        switch (spOperator) {
            case "neg": return "-";
            case "not": return "NOT";
            default: return spOperator.toUpperCase();
        }
    }
    
    private String translateBinaryOperator(String spOperator) {
        switch (spOperator) {
            case "plus": return "+";
            case "minus": return "-";
            case "mult": return "*";
            case "div": return "/";
            case "eq": return "=";
            case ">": return ">";
            case "and": return "AND";
            case "or": return "OR";
            default: return spOperator.toUpperCase();
        }
    }

    private void generateFunctionCall(ASTNode funcCall) {
        // Translation Advice: Function calls are inlined
        // For now, generate a placeholder comment
        if (funcCall.getChildren().size() >= 1) {
            ASTNode nameNode = funcCall.getChildren().get(0);
            if ("FNAME".equals(nameNode.getNodeType())) {
                String funcName = nameNode.getValue();
                targetCode.append("/* CALL ").append(funcName).append("() */");
                // TODO: Implement function inlining
            }
        }
    }
    
    private void generateProcedureCall(ASTNode procCall) {
        // Translation Advice: Procedure calls are inlined
        // For now, generate a placeholder comment
        if (procCall.getChildren().size() >= 1) {
            ASTNode nameNode = procCall.getChildren().get(0);
            if ("PNAME".equals(nameNode.getNodeType())) {
                String procName = nameNode.getValue();
                targetCode.append("/* CALL ").append(procName).append("() */");
                // TODO: Implement procedure inlining
            }
        }
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
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                CODE GENERATION");
        System.out.println("=".repeat(60));
        System.out.println("Generated Target Code:");
        System.out.println("-".repeat(60));
        System.out.println(targetCode.toString());
        System.out.println("=".repeat(60));
    }
}