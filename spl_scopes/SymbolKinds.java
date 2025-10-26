//A updated this file for type information (SPL_Types)

public enum SymbolKinds {
    GLOBAL_VAR("numeric"),     // Variables are always numeric
    LOCAL_VAR("numeric"),      
    PARAMETER("numeric"),      
    MAIN_VAR("numeric"),       
    PROCEDURE("typeless"),     // Procedures are typeless
    FUNCTION("numeric");       // Functions return numeric values

    private final String defaultType;

    SymbolKinds(String defaultType) {
        this.defaultType = defaultType;
    }

    public String getDefaultType() {
        return defaultType;
    }

    public boolean isTypeless() {
        return "typeless".equals(defaultType);
    }
}


////==========SPL_Scopes Version of SymbolKinds.java============
// public enum SymbolKinds {
//     GLOBAL_VAR,     // Variables declared in global scope
//     LOCAL_VAR,      // Variables declared in local scope (function/procedure body)
//     PARAMETER,      // Function/procedure parameters
//     MAIN_VAR,       // Variables declared in main scope
//     PROCEDURE,      // Procedure names
//     FUNCTION        // Function names
// }


