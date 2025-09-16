import java.util.HashMap;
import java.util.Map;

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

    
    


    public static void SPL_PROG(){}
    public static void VARIABLES() {}
    public static void VAR() {}
    public static void NAME() {}
    public static void PROCDEFS() {}
    public static void PDEF() {}
    public static void FDEF() {}
    public static void FUNCDEFS() {}
    public static void BODY() {}
    public static void PARAM() {}
    public static void MAXTHREE() {}
    public static void MAINPROG() {}
    public static void ATOM() {}
    public static void ALGO() {}
    public static void INSTR() {}
    public static void ASSIGN() {}
    public static void LOOP() {}
    public static void BRANCH() {}
    public static void OUTPUT() {}
    public static void INPUT() {}
    public static void TERM() {}
    public static void UNOP() {}
    public static void BINOP() {}
}
