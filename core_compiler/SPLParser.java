import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SPLParser {
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
                return node; // Empty variables list (nullable)
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
                return node; // Empty procdefs (nullable)
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
                return node; // Empty funcdefs (nullable)
            }
            
            tf.prepend(currToken);
            ASTNode fdef = parseFDEF(tf);
            if (fdef != null) {
                node.addChild(fdef);
                node.addChild(parseFUNCDEFS(tf)); // Recursive call - FIXED ORDER
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
                return node; // Empty maxthree (nullable)
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
            
            if (isValidNumber(currToken)) {
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
            
            // Check if we've reached the end of ALGO (return, }, etc.)
            String lookAhead = tf.next();
            if (lookAhead == null || "}".equals(lookAhead) || "return".equals(lookAhead)) {
                tf.prepend(lookAhead);
                return node; // Empty ALGO
            }
            tf.prepend(lookAhead);
            
            ASTNode instr = parseINSTR(tf);
            if (instr != null) {
                node.addChild(instr);
                
                String currToken = tf.next();
                if (currToken != null && ";".equals(currToken)) {
                    // Check if next part is return or end
                    String nextLookAhead = tf.next();
                    if (nextLookAhead != null && !"return".equals(nextLookAhead) && !"}".equals(nextLookAhead)) {
                        tf.prepend(nextLookAhead);
                        node.addChild(parseALGO(tf)); // Recursive call
                    } else {
                        tf.prepend(nextLookAhead);
                    }
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
                node.addChild(parseOUTPUT(tf));
                return node;
            } else if (isLegal(currToken)) {
                // Look ahead to determine if it's procedure call or assignment
                String nextToken = tf.next();
                tf.prepend(nextToken);
                tf.prepend(currToken);
                
                if ("(".equals(nextToken)) {
                    // Procedure call
                    return parseProcedureCall(tf);
                } else {
                    // Assignment
                    return parseASSIGN(tf);
                }
            } else if ("while".equals(currToken) || "do".equals(currToken)) {
                tf.prepend(currToken);
                return parseLOOP(tf);
            } else if ("if".equals(currToken)) {
                tf.prepend(currToken);
                return parseBRANCH(tf);
            } else {
                throw new Exception("Unexpected instruction: " + currToken);
            }
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseProcedureCall(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("PROC_CALL");
            
            String currToken = tf.next();
            if (!isLegal(currToken)) {
                throw new Exception("Expected procedure name, found: " + currToken);
            }
            
            ASTNode nameNode = new ASTNode("NAME", currToken);
            node.addChild(nameNode);
            
            currToken = tf.next();
            if (!"(".equals(currToken)) {
                throw new Exception("Expected '(', found: " + currToken);
            }
            
            node.addChild(parseINPUT(tf));
            
            currToken = tf.next();
            if (!")".equals(currToken)) {
                throw new Exception("Expected ')', found: " + currToken);
            }
            
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseASSIGN(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("ASSIGN");
            
            String currToken = tf.next();
            if (!isLegal(currToken)) {
                throw new Exception("Expected variable name, found: " + currToken);
            }
            
            ASTNode varNode = new ASTNode("VAR", currToken);
            node.addChild(varNode);
            
            currToken = tf.next();
            if (!"=".equals(currToken)) {
                throw new Exception("Expected '=', found: " + currToken);
            }
            
            // Check if it's function call assignment or term assignment
            String nextToken = tf.next();
            String afterNext = tf.next();
            tf.prepend(afterNext);
            tf.prepend(nextToken);
            
            if (isLegal(nextToken) && "(".equals(afterNext)) {
                // Function call assignment: VAR = NAME ( INPUT )
                ASTNode funcCall = new ASTNode("FUNC_CALL");
                
                currToken = tf.next(); // function name
                ASTNode funcNameNode = new ASTNode("NAME", currToken);
                funcCall.addChild(funcNameNode);
                
                currToken = tf.next(); // (
                if (!"(".equals(currToken)) {
                    throw new Exception("Expected '(', found: " + currToken);
                }
                
                funcCall.addChild(parseINPUT(tf));
                
                currToken = tf.next(); // )
                if (!")".equals(currToken)) {
                    throw new Exception("Expected ')', found: " + currToken);
                }
                
                node.addChild(funcCall);
            } else {
                // Term assignment: VAR = TERM
                node.addChild(parseTERM(tf));
            }
            
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseOUTPUT(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("OUTPUT");
            
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Expected output value");
            }
            
            if (isString(currToken)) {
                ASTNode stringNode = new ASTNode("STRING", currToken);
                node.addChild(stringNode);
            } else {
                tf.prepend(currToken);
                node.addChild(parseATOM(tf));
            }
            
            return node;
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseINPUT(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("INPUT");
            
            String currToken = tf.next();
            if (currToken == null || ")".equals(currToken)) {
                tf.prepend(currToken);
                return node; // Empty input (nullable)
            }
            
            // Parse first ATOM
            tf.prepend(currToken);
            node.addChild(parseATOM(tf));
            
            // Try to parse second ATOM
            currToken = tf.next();
            if (currToken != null && !")".equals(currToken) && 
                (isValidNumber(currToken) || isLegal(currToken))) {
                tf.prepend(currToken);
                node.addChild(parseATOM(tf));
                
                // Try to parse third ATOM
                currToken = tf.next();
                if (currToken != null && !")".equals(currToken) && 
                    (isValidNumber(currToken) || isLegal(currToken))) {
                    tf.prepend(currToken);
                    node.addChild(parseATOM(tf));
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
    
    public static ASTNode parseTERM(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            
            if ("(".equals(currToken)) {
                String nextToken = tf.next();
                tf.prepend(nextToken);
                
                if (isUnaryOperator(nextToken)) {
                    // ( UNOP TERM )
                    ASTNode node = new ASTNode("UNOP_EXPR");
                    
                    currToken = tf.next(); // unary operator
                    ASTNode unopNode = new ASTNode("UNOP", currToken);
                    node.addChild(unopNode);
                    
                    node.addChild(parseTERM(tf));
                    
                    currToken = tf.next();
                    if (!")".equals(currToken)) {
                        throw new Exception("Expected ')', found: " + currToken);
                    }
                    
                    return node;
                } else {
                    // ( TERM BINOP TERM )
                    ASTNode node = new ASTNode("BINOP_EXPR");
                    node.addChild(parseTERM(tf));
                    
                    currToken = tf.next();
                    if (!isBinaryOperator(currToken)) {
                        throw new Exception("Expected binary operator, found: " + currToken);
                    }
                    ASTNode binopNode = new ASTNode("BINOP", currToken);
                    node.addChild(binopNode);
                    
                    node.addChild(parseTERM(tf));
                    
                    currToken = tf.next();
                    if (!")".equals(currToken)) {
                        throw new Exception("Expected ')', found: " + currToken);
                    }
                    
                    return node;
                }
            } else {
                // ATOM
                tf.prepend(currToken);
                return parseATOM(tf);
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
    
    // Validation methods
    private static boolean isValidNumber(String str) {
        // Regular expression: ( 0 | [1...9][0...9]* )
        Pattern pattern = Pattern.compile("^(0|[1-9][0-9]*)$");
        return pattern.matcher(str).matches();
    }
    
    private static boolean isString(String str) {
        // String: any sequence of digits or letters between quotation marks, max length 15
        if (str.length() < 2 || !str.startsWith("\"") || !str.endsWith("\"")) {
            return false;
        }
        String content = str.substring(1, str.length() - 1);
        if (content.length() > 15) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]*$");
        return pattern.matcher(content).matches();
    }
    
    private static boolean isKeyword(String str) {
        return keywords.containsKey(str);
    }
    
    private static boolean isLegal(String str) {
        // Regular expression: [a...z]{a...z}*{0...9}*
        Pattern pattern = Pattern.compile("^[a-z][a-z]*[0-9]*$");
        
        if (!pattern.matcher(str).matches()) {
            return false;
        }
        
        if (isKeyword(str)) {
            return false;
        }
        
        return true;
    }
    
    private static boolean isUnaryOperator(String str) {
        return "neg".equals(str) || "not".equals(str);
    }
    
    private static boolean isBinaryOperator(String str) {
        return "eq".equals(str) || ">".equals(str) || "or".equals(str) || 
               "and".equals(str) || "plus".equals(str) || "minus".equals(str) || 
               "mult".equals(str) || "div".equals(str);
    }
    
    public static void ASSIGN(TokenFeeder tf) {
        try {
            VAR(tf);
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"=".equals(currToken)) {
                throw new Exception("Expected '=', found: " + currToken);
            }
            try {
                TERM(tf);
            } catch (Exception e) {
                NAME(tf);
                currToken = tf.next();
                if (currToken == null) {
                    throw new Exception("Unexpected end of input");
                }
                if (!"(".equals(currToken)) {
                    throw new Exception("Expected '(', found: " + currToken);
                }
                INPUT(tf);
                currToken = tf.next();
                if (currToken == null) {
                    throw new Exception("Unexpected end of input");
                }
                if (!")".equals(currToken)) {
                    throw new Exception("Expected ')', found: " + currToken);
                }
            }
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }
    
    public static void TERM(TokenFeeder tf) {
        String currToken = tf.next();
        try {
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if ("(".equals(currToken)) {
                try {
                    UNOP(tf);
                    TERM(tf);
                } catch (Exception e) {
                    TERM(tf);
                    BINOP(tf);
                    TERM(tf);
                }
            } else {
                tf.prepend(currToken);
                ATOM(tf);
            }
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            tf.prepend(currToken);
        }
    }
    
    public static void VAR(TokenFeeder tf) {
        String currToken = tf.next();
        if (currToken == null || !isLegal(currToken)) {
            throw new RuntimeException("Expected variable, found: " + currToken);
        }
    }
    
    public static void NAME(TokenFeeder tf) {
        String currToken = tf.next();
        if (currToken == null || !isLegal(currToken)) {
            throw new RuntimeException("Expected name, found: " + currToken);
        }
    }
    
    public static void INPUT(TokenFeeder tf) {
        // Parse up to 3 ATOM elements for INPUT
        for (int i = 0; i < 3; i++) {
            String currToken = tf.next();
            if (currToken == null || ")".equals(currToken)) {
                tf.prepend(currToken);
                break;
            }
            if (isValidNumber(currToken) || isLegal(currToken)) {
                // Valid ATOM, continue
            } else {
                tf.prepend(currToken);
                break;
            }
        }
    }
    
    public static void ATOM(TokenFeeder tf) {
        String currToken = tf.next();
        if (currToken == null || (!isValidNumber(currToken) && !isLegal(currToken))) {
            throw new RuntimeException("Expected atom (number or variable), found: " + currToken);
        }
    }
    
    public static void UNOP(TokenFeeder tf) {
        String currToken = tf.next();
        if (currToken == null || !isUnaryOperator(currToken)) {
            throw new RuntimeException("Expected unary operator, found: " + currToken);
        }
    }
    
    public static void BINOP(TokenFeeder tf) {
        String currToken = tf.next();
        if (currToken == null || !isBinaryOperator(currToken)) {
            throw new RuntimeException("Expected binary operator, found: " + currToken);
        }
    }
}