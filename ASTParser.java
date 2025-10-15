import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ASTParser {
    public static final Map<String, String> keywords = new HashMap<>();

    static {
        keywords.put("glob","");
        keywords.put("proc","");
        keywords.put("func","");
        keywords.put("main","");
        keywords.put("{","");
        keywords.put("}","");
        keywords.put("(","");
        keywords.put(")","");
        keywords.put(";","");
        keywords.put("return","");
        keywords.put("local","");
        keywords.put("var","");
        keywords.put("halt","");
        keywords.put("print","");
        keywords.put("=","");
        keywords.put("while","");
        keywords.put("do","");
        keywords.put("until","");
        keywords.put("if","");
        keywords.put("else","");
        keywords.put("neg","");
        keywords.put("not","");
        keywords.put("eq","");
        keywords.put(">","");
        keywords.put("or","");
        keywords.put("and","");
        keywords.put("plus","");
        keywords.put("minus","");
        keywords.put("mult","");
        keywords.put("div","");
    }
    
    public static ASTNode parseSPL_PROG(TokenFeeder tf) {
        try {
            ASTNode root = new ASTNode("SPL_PROG");
            
            String currToken = tf.next();
            if (!"glob".equals(currToken)) {
                throw new Exception("Expected 'glob', found: " + currToken);
            }
            
            root.addChild(parseGlob(tf));
            
            currToken = tf.next();
            if (!"proc".equals(currToken)) {
                throw new Exception("Expected 'proc', found: " + currToken);
            }
            
            root.addChild(parseProc(tf));
            
            currToken = tf.next();
            if (!"func".equals(currToken)) {
                throw new Exception("Expected 'func', found: " + currToken);
            }
            
            root.addChild(parseFunc(tf));
            
            currToken = tf.next();
            if (!"main".equals(currToken)) {
                throw new Exception("Expected 'main', found: " + currToken);
            }
            
            root.addChild(parseMainSPL(tf));
            
            return root;
            
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }
    
    public static ASTNode parseGlob(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("GLOB");
            
            String currToken = tf.next();
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }

            node.addChild(parseVARIABLES(tf));

            currToken = tf.next();
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }
            
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseProc(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("PROC");
            
            String currToken = tf.next();
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }

            node.addChild(parsePROCDEFS(tf));

            currToken = tf.next();
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }
            
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseFunc(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("FUNC");
            
            String currToken = tf.next();
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }

            node.addChild(parseFUNCDEFS(tf));

            currToken = tf.next();
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }
            
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseMainSPL(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("MAIN");
            
            String currToken = tf.next();
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }

            node.addChild(parseMAINPROG(tf));

            currToken = tf.next();
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }
            
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseVARIABLES(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("VARIABLES");
            
            String currToken = tf.next();
            if (currToken == null || "}".equals(currToken)) {
                tf.prepend(currToken);
                return node; // Empty variables list
            }
            
            if (isLegal(currToken)) {
                ASTNode varNode = new ASTNode("VAR", currToken);
                node.addChild(varNode);
                node.addChild(parseVARIABLES(tf)); // Recursive call
            } else {
                tf.prepend(currToken);
            }
            
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parsePROCDEFS(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("PROCDEFS");
            
            String currToken = tf.next();
            if (currToken == null || "}".equals(currToken)) {
                tf.prepend(currToken);
                return node; // Empty procdefs
            }
            
            tf.prepend(currToken);
            ASTNode pdef = parsePDEF(tf);
            if (pdef != null) {
                node.addChild(pdef);
                node.addChild(parsePROCDEFS(tf)); // Recursive call
            }
            
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseFUNCDEFS(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("FUNCDEFS");
            
            String currToken = tf.next();
            if (currToken == null || "}".equals(currToken)) {
                tf.prepend(currToken);
                return node; // Empty funcdefs
            }
            
            tf.prepend(currToken);
            ASTNode fdef = parseFDEF(tf);
            if (fdef != null) {
                node.addChild(fdef);
                node.addChild(parseFUNCDEFS(tf)); // Recursive call
            }
            
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parsePDEF(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("PDEF");
            
            String currToken = tf.next();
            if (currToken == null || !isLegal(currToken)) {
                tf.prepend(currToken);
                return null;
            }
            
            ASTNode nameNode = new ASTNode("NAME", currToken);
            node.addChild(nameNode);
            
            currToken = tf.next();
            if (!"(".equals(currToken)) {
                throw new Exception("Expected '(', found: " + currToken);
            }
            
            node.addChild(parsePARAM(tf));
            
            currToken = tf.next();
            if (!")".equals(currToken)) {
                throw new Exception("Expected ')', found: " + currToken);
            }
            
            currToken = tf.next();
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }
            
            node.addChild(parseBODY(tf));
            
            currToken = tf.next();
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }
            
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseFDEF(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("FDEF");
            
            String currToken = tf.next();
            if (currToken == null || !isLegal(currToken)) {
                tf.prepend(currToken);
                return null;
            }
            
            ASTNode nameNode = new ASTNode("NAME", currToken);
            node.addChild(nameNode);
            
            currToken = tf.next();
            if (!"(".equals(currToken)) {
                throw new Exception("Expected '(', found: " + currToken);
            }
            
            node.addChild(parsePARAM(tf));
            
            currToken = tf.next();
            if (!")".equals(currToken)) {
                throw new Exception("Expected ')', found: " + currToken);
            }
            
            currToken = tf.next();
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }
            
            node.addChild(parseBODY(tf));
            
            currToken = tf.next();
            if (!";".equals(currToken)) {
                throw new Exception("Expected ';', found: " + currToken);
            }
            
            currToken = tf.next();
            if (!"return".equals(currToken)) {
                throw new Exception("Expected 'return', found: " + currToken);
            }
            
            node.addChild(parseATOM(tf));
            
            currToken = tf.next();
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }
            
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseBODY(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("BODY");
            
            String currToken = tf.next();
            if (!"local".equals(currToken)) {
                throw new Exception("Expected 'local', found: " + currToken);
            }
            
            currToken = tf.next();
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }
            
            node.addChild(parseMAXTHREE(tf));
            
            currToken = tf.next();
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }
            
            node.addChild(parseALGO(tf));
            
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parsePARAM(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("PARAM");
            node.addChild(parseMAXTHREE(tf));
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseMAXTHREE(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("MAXTHREE");
            
            String currToken = tf.next();
            if (currToken == null || ")".equals(currToken) || "}".equals(currToken)) {
                tf.prepend(currToken);
                return node; // Empty maxthree
            }

            if (isLegal(currToken)) {
                ASTNode varNode = new ASTNode("VAR", currToken);
                node.addChild(varNode);
                
                // Try to parse second variable
                currToken = tf.next();
                if (currToken != null && isLegal(currToken)) {
                    ASTNode varNode2 = new ASTNode("VAR", currToken);
                    node.addChild(varNode2);
                    
                    // Try to parse third variable
                    currToken = tf.next();
                    if (currToken != null && isLegal(currToken)) {
                        ASTNode varNode3 = new ASTNode("VAR", currToken);
                        node.addChild(varNode3);
                    } else {
                        tf.prepend(currToken);
                    }
                } else {
                    tf.prepend(currToken);
                }
            } else {
                tf.prepend(currToken);
            }
            
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseMAINPROG(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("MAINPROG");
            
            String currToken = tf.next();
            if (!"var".equals(currToken)) {
                throw new Exception("Expected 'var', found: " + currToken);
            }
            
            currToken = tf.next();
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }
            
            node.addChild(parseVARIABLES(tf));
            
            currToken = tf.next();
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }
            
            node.addChild(parseALGO(tf));
            
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseATOM(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("ATOM");
            
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            
            if (isNumeric(currToken)) {
                node.setValue(currToken);
                return node;
            }
            
            if (isLegal(currToken)) {
                ASTNode varNode = new ASTNode("VAR", currToken);
                node.addChild(varNode);
                return node;
            }
            
            throw new Exception("Expected number or variable, found: " + currToken);
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseALGO(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("ALGO");
            
            ASTNode instr = parseINSTR(tf);
            if (instr != null) {
                node.addChild(instr);
                
                String currToken = tf.next();
                if (currToken != null && ";".equals(currToken)) {
                    node.addChild(parseALGO(tf)); // Recursive call
                } else {
                    tf.prepend(currToken);
                }
            }
            
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseINSTR(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                return null;
            }
            
            if ("halt".equals(currToken)) {
                return new ASTNode("HALT");
            } else if ("print".equals(currToken)) {
                ASTNode node = new ASTNode("PRINT");
                node.addChild(parseATOM(tf));
                return node;
            } else if ("while".equals(currToken) || "do".equals(currToken)) {
                tf.prepend(currToken);
                return parseLOOP(tf);
            } else if ("if".equals(currToken)) {
                tf.prepend(currToken);
                return parseBRANCH(tf);
            } else {
                tf.prepend(currToken);
                return parseASSIGN(tf);
            }
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseASSIGN(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("ASSIGN");
            
            String currToken = tf.next();
            if (isLegal(currToken)) {
                ASTNode varNode = new ASTNode("VAR", currToken);
                node.addChild(varNode);
                
                currToken = tf.next();
                if (!"=".equals(currToken)) {
                    throw new Exception("Expected '=', found: " + currToken);
                }
                
                node.addChild(parseTERM(tf));
                return node;
            } else {
                throw new Exception("Expected variable name, found: " + currToken);
            }
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseLOOP(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            
            if ("while".equals(currToken)) {
                ASTNode node = new ASTNode("WHILE");
                node.addChild(parseTERM(tf));
                
                currToken = tf.next();
                if (!"{".equals(currToken)) {
                    throw new Exception("Expected '{', found: " + currToken);
                }
                
                node.addChild(parseALGO(tf));
                
                currToken = tf.next();
                if (!"}".equals(currToken)) {
                    throw new Exception("Expected '}', found: " + currToken);
                }
                
                return node;
            } else if ("do".equals(currToken)) {
                ASTNode node = new ASTNode("DO");
                
                currToken = tf.next();
                if (!"{".equals(currToken)) {
                    throw new Exception("Expected '{', found: " + currToken);
                }
                
                node.addChild(parseALGO(tf));
                
                currToken = tf.next();
                if (!"}".equals(currToken)) {
                    throw new Exception("Expected '}', found: " + currToken);
                }
                
                currToken = tf.next();
                if (!"until".equals(currToken)) {
                    throw new Exception("Expected 'until', found: " + currToken);
                }
                
                node.addChild(parseTERM(tf));
                return node;
            } else {
                throw new Exception("Expected 'while' or 'do', found: " + currToken);
            }
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseBRANCH(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("IF");
            
            String currToken = tf.next();
            if (!"if".equals(currToken)) {
                throw new Exception("Expected 'if', found: " + currToken);
            }
            
            node.addChild(parseTERM(tf));
            
            currToken = tf.next();
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }
            
            node.addChild(parseALGO(tf));
            
            currToken = tf.next();
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }
            
            currToken = tf.next();
            if (currToken != null && "else".equals(currToken)) {
                currToken = tf.next();
                if (!"{".equals(currToken)) {
                    throw new Exception("Expected '{', found: " + currToken);
                }
                
                node.addChild(parseALGO(tf));
                
                currToken = tf.next();
                if (!"}".equals(currToken)) {
                    throw new Exception("Expected '}', found: " + currToken);
                }
            } else {
                tf.prepend(currToken);
            }
            
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseTERM(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            
            if ("neg".equals(currToken) || "not".equals(currToken)) {
                ASTNode node = new ASTNode("UNOP", currToken);
                node.addChild(parseATOM(tf));
                return node;
            } else if ("(".equals(currToken)) {
                ASTNode node = new ASTNode("BINOP_EXPR");
                node.addChild(parseTERM(tf));
                
                currToken = tf.next();
                if (isBinaryOperator(currToken)) {
                    ASTNode binopNode = new ASTNode("BINOP", currToken);
                    node.addChild(binopNode);
                } else {
                    throw new Exception("Expected binary operator, found: " + currToken);
                }
                
                node.addChild(parseTERM(tf));
                
                currToken = tf.next();
                if (!")".equals(currToken)) {
                    throw new Exception("Expected ')', found: " + currToken);
                }
                
                return node;
            } else {
                tf.prepend(currToken);
                return parseATOM(tf);
            }
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private static boolean isKeyword(String str) {
        return keywords.containsKey(str);
    }
    
    private static boolean isLegal(String str) {
        String regex = "^[a-z][a-z]*[0-9]*$";
        Pattern pattern = Pattern.compile(regex);
        
        if (!pattern.matcher(str).matches()) {
            return false;
        }
        
        if (isKeyword(str)) {
            return false;
        }
        
        return true;
    }
    
    private static boolean isBinaryOperator(String str) {
        return "eq".equals(str) || ">".equals(str) || "or".equals(str) || 
               "and".equals(str) || "plus".equals(str) || "minus".equals(str) || 
               "mult".equals(str) || "div".equals(str);
    }
}