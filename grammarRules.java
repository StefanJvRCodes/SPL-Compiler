import java.util.HashMap;
import java.util.Map;
import java.lang.Integer;

public class grammarRules {
    public static final Map<String, String> keywords = new HashMap<>();

    public grammarRules() {
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

    
    
    public static void glob(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }


            VARIABLES(tf);


            currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }


    public static void proc(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }


            PROCDEFS(tf);


            currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }


    public static void func(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }


            FUNCDEFS(tf);


            currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }


    public static void mainSPL(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }


            MAINPROG(tf);


            currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }


    public static void SPL_PROG(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"glob".equals(currToken)) {
                throw new Exception("Expected 'glob', found: " + currToken);
            }
            glob(tf);


            if (!"proc".equals(currToken)) {
                throw new Exception("Expected 'proc', found: " + currToken);
            }
            proc(tf);


            if (!"func".equals(currToken)) {
                throw new Exception("Expected 'func', found: " + currToken);
            }
            func(tf);


            if (!"main".equals(currToken)) {
                throw new Exception("Expected 'main', found: " + currToken);
            }
            mainSPL(tf);


        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            System.exit(1);
        }

    }


    public static void VARIABLES(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                return;
            }
            if ("VAR".equals(currToken)) {
                VAR(tf);
                VARIABLES(tf);
            } else {
                throw new Exception("Expected user defined name or end of variables, found: " + currToken);
            }
            
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }



    public static void VAR(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"VAR".equals(currToken)) {
                throw new Exception("Expected user defined name, found: " + currToken);
            }
            
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }



    public static void NAME(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"NAME".equals(currToken)) {
                throw new Exception("Expected user defined name, found: " + currToken);
            }
            
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }



    public static void PROCDEFS(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                return;
            }
            tf.prepend(currToken);
            PDEF(tf);
            PROCDEFS(tf);
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }


    public static void PDEF(TokenFeeder tf) {
        try {
            NAME(tf);
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"(".equals(currToken)) {
                throw new Exception("Expected '(', found: " + currToken);
            }
            PARAM(tf);
            currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!")".equals(currToken)) {
                throw new Exception("Expected ')', found: " + currToken);
            }
            currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }
            BODY(tf);
            currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }

            
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }



   
    public static void FDEF(TokenFeeder tf) {
        try {
            NAME(tf);
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"(".equals(currToken)) {
                throw new Exception("Expected '(', found: " + currToken);
            }
            PARAM(tf);
            currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!")".equals(currToken)) {
                throw new Exception("Expected ')', found: " + currToken);
            }
            currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }
            BODY(tf);
            currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!";".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }
            currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"return".equals(currToken)) {
                throw new Exception("Expected 'return', found: " + currToken);
            }
            ATOM(tf);
            currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }

        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }


    public static void FUNCDEFS(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                return;
            }
            tf.prepend(currToken);
            FDEF(tf);
            FUNCDEFS(tf);
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
            
        }
    }


    
    public static void BODY(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"local".equals(currToken)) {
                throw new Exception("Expected 'local', found: " + currToken);
            }
            currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }
            MAXTHREE(tf);
            currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }
            ALGO(tf);

        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }



    public static void PARAM(TokenFeeder tf) {
        try {
            MAXTHREE(tf);
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }


    //MAXTHREE := //nullable
    //MAXTHREE := VAR
    //MAXTHREE := VAR VAR
    //MAXTHREE := VAR VAR VAR
    public static void MAXTHREE(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                return;
            }
            tf.prepend(currToken);
            VAR(tf);

            //TODO, figure out the three vars thing
            
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }



    public static void MAINPROG(TokenFeeder tf) {
        try {
            //TODO
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }



    public static void ATOM(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (isNumeric(currToken)){
                return;
            }
            tf.prepend(currToken);
            VAR(tf);

            
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }


    public static void ALGO(TokenFeeder tf) {
        try {
            INSTR(tf);
            String currToken = tf.next();
            if (currToken == null) {
                tf.prepend(currToken);
                return;
            }
            if (!";".equals(currToken)) {
                ALGO(tf);
            }

            
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }



    public static void INSTR(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if ("halt".equals(currToken)) {
                return;
            } else if ("print".equals(currToken)) {
                OUTPUT(tf);
            }
            tf.prepend(currToken);
            //TODO figure out INSTR
            
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }



    public static void ASSIGN(TokenFeeder tf) {
        try {
            
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }


    public static void LOOP(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if ("while".equals(currToken)) {
                TERM(tf);
                currToken = tf.next();
                if (currToken == null) {
                    throw new Exception("Unexpected end of input");
                }
                if (!"{".equals(currToken)) {
                    throw new Exception("Expected '{', found: " + currToken);
                }
                ALGO(tf);
                currToken = tf.next();
                if (currToken == null) {
                    throw new Exception("Unexpected end of input");
                }
                if (!"}".equals(currToken)) {
                    throw new Exception("Expected '}', found: " + currToken);
                }
            } else if ("do".equals(currToken)) {
                currToken = tf.next();
                if (currToken == null) {
                    throw new Exception("Unexpected end of input");
                }
                if (!"{".equals(currToken)) {
                    throw new Exception("Expected '{', found: " + currToken);
                }
                ALGO(tf);
                currToken = tf.next();
                if (currToken == null) {
                    throw new Exception("Unexpected end of input");
                }
                if (!"}".equals(currToken)) {
                    throw new Exception("Expected '}', found: " + currToken);
                }
                currToken = tf.next();
                if (currToken == null) {
                    throw new Exception("Unexpected end of input");
                }
                if (!"until".equals(currToken)) {
                    throw new Exception("Expected 'until', found: " + currToken);
                }
                TERM(tf);
            } else {
                throw new Exception("Expected 'while' or 'do', found: " + currToken);
            }
            
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }


    public static void BRANCH(TokenFeeder tf) {
        try {
            String currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"if".equals(currToken)) {
                throw new Exception("Expected 'if', found: " + currToken);
            }
            TERM(tf);
            currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"{".equals(currToken)) {
                throw new Exception("Expected '{', found: " + currToken);
            }
            ALGO(tf);
            currToken = tf.next();
            if (currToken == null) {
                throw new Exception("Unexpected end of input");
            }
            if (!"}".equals(currToken)) {
                throw new Exception("Expected '}', found: " + currToken);
            }

            currToken = tf.next();
            if (currToken != null && "else".equals(currToken)) {
                currToken = tf.next();
                if (currToken == null) {
                    throw new Exception("Unexpected end of input");
                }
                if (!"{".equals(currToken)) {
                    throw new Exception("Expected '{', found: " + currToken);
                }
                ALGO(tf);
                currToken = tf.next();
                if (currToken == null) {
                    throw new Exception("Unexpected end of input");
                }
                if (!"}".equals(currToken)) {
                    throw new Exception("Expected '}', found: " + currToken);
                }
            } else {
                tf.prepend(currToken);
            }
            
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }



    public static void OUTPUT(TokenFeeder tf) {
        try {
            
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }



    public static void INPUT(TokenFeeder tf) {
        try {
            
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }



    public static void TERM(TokenFeeder tf) {
        try {
            
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }



    public static void UNOP(TokenFeeder tf) {
        String currToken = tf.next();
        if (currToken.equals("neg") || currToken.equals("not")) {
            // valid unary operator
        } else {
            throw new IllegalArgumentException("Invalid unary operator: " + str);
        }

        try {
            
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }



    public static void BINOP(TokenFeeder tf) {
        String currToken = tf.next();
        if (currToken.equals("eq") || currToken.equals(">") || currToken.equals("or") || currToken.equals("and") || currToken.equals("plus") || currToken.equals("minus") || currToken.equals("mult") || currToken.equals("div")) {
            // valid binary operator
        } else {
            throw new IllegalArgumentException("Invalid binary operator: " + currToken);
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
}
