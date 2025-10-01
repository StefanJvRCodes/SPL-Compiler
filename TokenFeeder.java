//The lexer is going to initialize this class. It will pass an array of strings to the constructor.
//The parser is going to use this class to get the tokens
//The parser will expect the tokens in the exact way specified in the grammar



import java.util.Vector;

public class TokenFeeder {
    private Vector<String> tokens;

    public TokenFeeder(String[] initTokens) {
        tokens = new Vector<String>();
        for (String token : initTokens) {
            tokens.add(token);
        }
    }

    public String next(){
        if(tokens.isEmpty()){
            return null;
        }
        return tokens.remove(0);
    }

    
}
