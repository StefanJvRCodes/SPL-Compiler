import java.util.HashMap;
import java.util.Map;
import java.lang.Integer;
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
            
            // Check for end of file - no more tokens should remain
            String unexpectedToken = tf.next();
            if (unexpectedToken != null) {
                throw new Exception("Unexpected token after main program: " + unexpectedToken);
            }
            
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
            
            // Parse function body inline (different from procedure body)
            ASTNode bodyNode = new ASTNode("BODY");
            
            currToken = tf.next();
            if (!"local".equals(currToken)) {
                throw new Exception("Expected 'local', found: " + currToken);
            }
            
            currToken = tf.next();
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }
            
            bodyNode.addChild(parseMAXTHREE(tf));
            
            currToken = tf.next();
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }
            
            bodyNode.addChild(parseALGO(tf));
            node.addChild(bodyNode);
            
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

            // Accept ATOM (variable or number) instead of just variable
            if (isLegal(currToken) || isNumeric(currToken)) {
                ASTNode atomNode;
                if (isNumeric(currToken)) {
                    atomNode = new ASTNode("ATOM", currToken);
                } else {
                    atomNode = new ASTNode("ATOM");
                    atomNode.addChild(new ASTNode("VAR", currToken));
                }
                node.addChild(atomNode);
                
                // Try to parse second atom
                currToken = tf.next();
                if (currToken != null && (isLegal(currToken) || isNumeric(currToken))) {
                    ASTNode atomNode2;
                    if (isNumeric(currToken)) {
                        atomNode2 = new ASTNode("ATOM", currToken);
                    } else {
                        atomNode2 = new ASTNode("ATOM");
                        atomNode2.addChild(new ASTNode("VAR", currToken));
                    }
                    node.addChild(atomNode2);
                    
                    // Try to parse third atom
                    currToken = tf.next();
                    if (currToken != null && (isLegal(currToken) || isNumeric(currToken))) {
                        ASTNode atomNode3;
                        if (isNumeric(currToken)) {
                            atomNode3 = new ASTNode("ATOM", currToken);
                        } else {
                            atomNode3 = new ASTNode("ATOM");
                            atomNode3.addChild(new ASTNode("VAR", currToken));
                        }
                        node.addChild(atomNode3);
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
    
    public static ASTNode parseOUTPUT(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            
            // Check if it's a string literal
            if (isStringLiteral(currToken)) {
                ASTNode node = new ASTNode("STRING", currToken);
                return node;
            }
            
            // Otherwise, put the token back and parse as ATOM
            tf.prepend(currToken);
            return parseATOM(tf);
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
                
                // Check what comes next
                String currToken = tf.next();
                
                // If it's a semicolon, we have INSTR ; ALGO
                if (currToken != null && ";".equals(currToken)) {
                    ASTNode nextAlgo = parseALGO(tf);
                    if (nextAlgo != null) {
                        node.addChild(nextAlgo);
                    }
                } else {
                    // If it's not a semicolon, check if it's a valid terminator
                    if (currToken != null && !"}".equals(currToken) && !"return".equals(currToken)) {
                        // This is where we should report the semicolon error
                        throw new Exception("Expected ';', found: " + currToken);
                    }
                    // Put the token back for the caller to handle
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
            
            // Check for return keyword (end of function algorithm)
            if ("return".equals(currToken)) {
                tf.prepend(currToken);
                return null;
            }
            
            if ("halt".equals(currToken)) {
                return new ASTNode("HALT");
            } else if ("print".equals(currToken)) {
                ASTNode node = new ASTNode("PRINT");
                node.addChild(parseOUTPUT(tf));
                return node;
            } else if ("while".equals(currToken) || "do".equals(currToken)) {
                tf.prepend(currToken);
                return parseLOOP(tf);
            } else if ("if".equals(currToken)) {
                tf.prepend(currToken);
                return parseBRANCH(tf);
            } else if (isLegal(currToken)) {
                // Could be assignment or procedure call
                String nextToken = tf.next();
                if ("(".equals(nextToken)) {
                    // This is a procedure call: NAME(INPUT)
                    ASTNode procCallNode = new ASTNode("PROC_CALL");
                    ASTNode procNameNode = new ASTNode("PNAME", currToken);
                    procCallNode.addChild(procNameNode);
                    procCallNode.addChild(parseINPUT(tf));
                    
                    String closeToken = tf.next();
                    if (!")".equals(closeToken)) {
                        throw new Exception("Expected ')', found: " + closeToken);
                    }
                    
                    return procCallNode;
                } else {
                    // It's an assignment, put tokens back
                    tf.prepend(nextToken);
                    tf.prepend(currToken);
                    return parseASSIGN(tf);
                }
            } else {
                // Check if it's a closing brace (end of algorithm block)
                if ("}".equals(currToken)) {
                    tf.prepend(currToken);
                    return null;
                }
                // For other non-legal tokens (like keywords), put back and end parsing
                tf.prepend(currToken);
                return null;
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
                
                // Look ahead to see if this is a function call or regular TERM
                String nextToken = tf.next();
                if (nextToken != null && isLegal(nextToken)) {
                    // Check if the next token after the function name is '('
                    String afterName = tf.next();
                    if ("(".equals(afterName)) {
                        // This is a function call: VAR = NAME(INPUT)
                        ASTNode funcCallNode = new ASTNode("FUNC_CALL");
                        ASTNode funcNameNode = new ASTNode("FNAME", nextToken);
                        funcCallNode.addChild(funcNameNode);
                        funcCallNode.addChild(parseINPUT(tf));
                        
                        currToken = tf.next();
                        if (!")".equals(currToken)) {
                            throw new Exception("Expected ')', found: " + currToken);
                        }
                        
                        node.addChild(funcCallNode);
                        return node;
                    } else {
                        // Not a function call, put tokens back and parse as TERM
                        tf.prepend(afterName);
                        tf.prepend(nextToken);
                        node.addChild(parseTERM(tf));
                        return node;
                    }
                } else {
                    // Put token back and parse as TERM
                    tf.prepend(nextToken);
                    node.addChild(parseTERM(tf));
                    return node;
                }
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
            
            if ("(".equals(currToken)) {
                // Could be ( UNOP TERM ) or ( TERM BINOP TERM )
                String nextToken = tf.next();
                
                if ("neg".equals(nextToken) || "not".equals(nextToken)) {
                    // ( UNOP TERM )
                    ASTNode node = new ASTNode("UNOP_EXPR");
                    ASTNode unopNode = new ASTNode("UNOP", nextToken);
                    node.addChild(unopNode);
                    node.addChild(parseTERM(tf));
                    
                    currToken = tf.next();
                    if (!")".equals(currToken)) {
                        throw new Exception("Expected ')', found: " + currToken);
                    }
                    return node;
                } else {
                    // ( TERM BINOP TERM )
                    tf.prepend(nextToken); // Put back the token
                    
                    ASTNode node = new ASTNode("BINOP_EXPR");
                    node.addChild(parseTERM(tf)); // First TERM
                    
                    currToken = tf.next();
                    if (!isBinaryOperator(currToken)) {
                        throw new Exception("Expected binary operator, found: " + currToken);
                    }
                    ASTNode binopNode = new ASTNode("BINOP", currToken);
                    node.addChild(binopNode);
                    
                    node.addChild(parseTERM(tf)); // Second TERM
                    
                    currToken = tf.next();
                    if (!")".equals(currToken)) {
                        throw new Exception("Expected ')', found: " + currToken);
                    }
                    return node;
                }
            } else {
                // TERM ::= ATOM
                tf.prepend(currToken);
                return parseATOM(tf);
            }
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            return null;
        }
    }
    
    public static ASTNode parseINPUT(TokenFeeder tf) {
        try {
            ASTNode node = new ASTNode("INPUT");
            node.addChild(parseMAXTHREE(tf));
            return node;
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
    
    private static boolean isStringLiteral(String str) {
        // String must start and end with quotes
        if (str == null || str.length() < 2) {
            return false;
        }
        
        if (!str.startsWith("\"") || !str.endsWith("\"")) {
            return false;
        }
        
        // Extract content without quotes
        String content = str.substring(1, str.length() - 1);
        
        // Check max length 15 (content only, not including quotes)
        if (content.length() > 15) {
            return false;
        }
        
        // Check content contains only letters and digits
        return content.matches("[a-zA-Z0-9]*");
    }
}