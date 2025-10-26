//The lexer is going to initialize this class. It will pass an array of strings to the constructor.
//The parser is going to use this class to get the tokens
//The parser will expect the tokens in the exact way specified in the grammar
//Parallel names for symbols vector?



import java.util.Vector;
import java.io.*;
import java.util.*;

public class TokenFeeder {
    private Vector<String> tokens;

    public TokenFeeder(String[] initTokens) {
        tokens = new Vector<String>();
        for (String token : initTokens) {
            tokens.add(token);
        }
    }
    
    public TokenFeeder(String filename) {
        tokens = new Vector<String>();
        try {
            Scanner scanner = new Scanner(new File(filename));
            scanner.useDelimiter(""); // Read character by character
            
            StringBuilder currentToken = new StringBuilder();
            boolean inString = false;
            
            while (scanner.hasNext()) {
                String ch = scanner.next();
                
                if (ch.equals("\"")) {
                    if (inString) {
                        // End of string
                        currentToken.append(ch);
                        tokens.add(currentToken.toString());
                        currentToken = new StringBuilder();
                        inString = false;
                    } else {
                        // Start of string - save any existing token first
                        if (currentToken.length() > 0) {
                            tokens.add(currentToken.toString());
                            currentToken = new StringBuilder();
                        }
                        currentToken.append(ch);
                        inString = true;
                    }
                } else if (inString) {
                    // Inside string, add everything
                    currentToken.append(ch);
                } else if (ch.matches("\\s")) {
                    // Whitespace outside string - end current token
                    if (currentToken.length() > 0) {
                        tokens.add(currentToken.toString());
                        currentToken = new StringBuilder();
                    }
                } else if (ch.matches("[{}();=]")) {
                    // Special delimiter/operator - save current token first, then add delimiter
                    if (currentToken.length() > 0) {
                        tokens.add(currentToken.toString());
                        currentToken = new StringBuilder();
                    }
                    tokens.add(ch);
                } else {
                    // Regular character
                    currentToken.append(ch);
                }
            }
            
            // Add final token if any
            if (currentToken.length() > 0) {
                tokens.add(currentToken.toString());
            }
            
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: Cannot read file " + filename);
            System.exit(1);
        }
    }

    public String next(){
        if(tokens.isEmpty()){
            return null;
        }
        return tokens.remove(0);
    }

    public void prepend(String token){
        tokens.add(0, token);
    }

    
}
