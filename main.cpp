#include <iostream>

using namespace std;

void WHILE();
void PROG();
void COND();
void OPENS();
void CLOSES();
void TERM();
void SEMICOLON();
void S();

void OPENS(){
    char symbol;
    cout << "Enter a symbol:" << endl;
    cin >> symbol;

    if ((symbol == '{')){

    }else{
        cout << "Syntax error";
        S();
    }
}

void CLOSES(){
    char symbol;
    cout << "Enter a symbol:" << endl;
    cin >> symbol;

    if ((symbol == '}')){

    }else{
        cout << "Syntax error";
        S();
    }
}

void CLOSING(){
    char symbol;
    cout << "Enter a symbol:" << endl;
    cin >> symbol;

    if ((symbol == ')')){

    }else{
        cout << "Syntax error";
        S();
    }
}

void SEMICOLON(){
    char symbol;
    cout << "Enter a symbol:" << endl;
    cin >> symbol;

    if ((symbol == ';')){

    }else{
        cout << "Syntax error";
        S();
    }
}

void DOLLAR(){
    char symbol;
    cout << "Enter a symbol:" << endl;
    cin >> symbol;

    if ((symbol == '$')){
        cout << "Success";

    }else{
        cout << "Syntax error";
        S();
    }
}



void OP(){
    char symbol;
    cout << "Enter a symbol:" << endl;
    cin >> symbol;

    if ((symbol == '+') || (symbol == '<')){

    }else{
        cout << "Syntax error";
        S();
    }
}

void TERM(){
    char symbol;
    cout << "Enter a symbol:" << endl;
    cin >> symbol;

    if ((symbol == 'c') || (symbol == 'b') || (symbol == 'a')){

    }else if ((symbol == '(')){
        TERM();
        OP();
        TERM();
        CLOSING();
    }else{
        cout << "Syntax error";
        S();
    }
}

void VAR(){
    char symbol;
    cout << "Enter a symbol:" << endl;
    cin >> symbol;

    if ((symbol == 'a') || (symbol == 'b') || (symbol == 'c') ){
        TERM();
        SEMICOLON();
    }else{
        cout << "Syntax error";
        S();
    }
}

void COND(){
    char symbol;
    cout << "Enter a symbol:" << endl;
    cin >> symbol;

if ((symbol == '(')){
        TERM();
        OP();
        TERM();
        CLOSING();
    }else{
        cout << "Syntax error";
        S();
    }
}




void ASSIGN(){
    char symbol;
    cout << "Enter a symbol:" << endl;
    cin >> symbol;

    if ((symbol == '=')){
        TERM();
        SEMICOLON();
    }else{
        cout << "Syntax error";
        S();
    }
}

void PROG(){
    char symbol;
    cout << "Enter a symbol:" << endl;
    cin >> symbol;

    if ((symbol == 'a') || (symbol == 'b') || (symbol == 'c')){
        ASSIGN();
    }else if ((symbol == 'w')){
        WHILE();
    }else{
        cout << "Syntax error";
        S();
    }
}

void WHILE(){
    COND();
    OPENS();
    PROG();
    CLOSES();
}

void S (){
cout << "\nFrom the beginning" << endl;
    PROG();
    DOLLAR();
}

//    char symbol;
//    cout << "Enter a symbol:" << endl;
//    cin >> symbol;
//
//    if ((symbol == 'S')){
//        PROG();
//        DOLLAR();
//    }else{
//        cout << "Syntax error";
//    }





int main() {
    std::cout << "Your parser program. Feed me symbols" << std::endl;
    S();
    return 0;
}
