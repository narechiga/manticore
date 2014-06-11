%{
	import java.util.*;
%}

%language "Java"

/* Hybrid programs */
%token ASSIGN
%token PRIME
%token OPENBRACE
%token CLOSEBRACE
%token RESTRICTDOMAIN
%token EQUALS
%token TEST
%token CUP
%token RANDOM

%token OPENBOX
%token CLOSEBOX
%token OPENDIAMOND
%token CLOSEDIAMOND

%token MODEVAR
%token MODEID
%token STATEVAR
%token STATEVARPLUS

/* semicolon is already above */

/* Arithmetic */
%token NUMBER
%token ASTERISK
%token IDENTIFIER
%token PLUS
%token MINUS
%token DIVIDE
%token POWER
%token NEWLINE
%token INEQUALITY

/* Punctuation */
%token LPAREN
%token RPAREN
%token SEMICOLON
%token COMMA

/* First Order Logic */
%token AND
%token OR
%token NOT
%token IMPLIES
%token FORALL
%token EXISTS
%token TRUE
%token FALSE

%left SEMICOLON
%left CUP
//%left TEST

%right IMPLIES
%right OR AND
%left NOT QUANTIFIER

%right INEQUALITY /* <, >, <=, >=, =, != */
%left MINUS PLUS
%left DIVIDE ASTERISK
%left NEGATIVE
%right POWER




%%
input: // dLFormula
//	| folformula { System.out.println("Found: folformula");}
	| folformula IMPLIES OPENBOX automaton ASTERISK CLOSEBOX folformula
		{ 
			System.out.println( $1 );
		}
	| automaton { System.out.println("Found: automaton"); }
	| modecheck { System.out.println("Found: modecheck");}
	| assignrandomplusvars { System.out.println("Found: assignrandomplusvars");	}
	| updatevars { System.out.println("Found: updatevars");	}
	| modeswitch { System.out.println("Found: modeswitch");}
	| odesystem { System.out.println("Found: odesystem");}
	| jumpcondition { System.out.println("Found: jumpcondition");}

;

automaton:
	mode
	| edge
	| automaton CUP mode
	| automaton CUP edge
;

edge:
	modecheck SEMICOLON assignrandomplusvars SEMICOLON jumpcondition SEMICOLON updatevars SEMICOLON modeswitch
	| LPAREN edge RPAREN
;

modecheck:
	TEST LPAREN MODEVAR EQUALS MODEID RPAREN
	| LPAREN modecheck RPAREN
;

assignrandomplusvars:
	STATEVARPLUS ASSIGN ASTERISK
	| assignrandomplusvars SEMICOLON STATEVARPLUS ASSIGN ASTERISK
	| LPAREN assignrandomplusvars RPAREN
	;

jumpcondition:
	// I guess no details checking for now
	test
;
updatevars:
	STATEVAR ASSIGN STATEVARPLUS
	| updatevars SEMICOLON STATEVAR ASSIGN STATEVARPLUS
	| LPAREN updatevars RPAREN
	;

modeswitch:
	MODEVAR ASSIGN MODEID
;

mode:
	modecheck SEMICOLON odesystem
	| LPAREN mode RPAREN
;

test:
	TEST folformula						{ $$ = "(test " + (String)$2 + " )";				}
	| LPAREN test RPAREN					{ $$ = (String)$2;						}
	;

odesystem:
	OPENBRACE odelist CLOSEBRACE
		{ $$ = "(continuous " + "(odelist: " + (String)$2 + " )" + ", (domain: true )"; }
	| OPENBRACE odelist RESTRICTDOMAIN folformula CLOSEBRACE	
		{ $$ = "(continuous " + "(odelist: " + (String)$2 + " )" + ", (domain: " + (String)$4 + " )"; }
	;
odelist:
	ode							{ $$ = (String)$1;						}
	| odelist COMMA ode					{ $$ = (String)$1 + ", " + (String)$3;				}
	;
ode:
	STATEVAR PRIME EQUALS term				{ $$ = (String)$1 + "' = " + (String)$4;			}
	;


folformula:
	| TRUE { 
			$$ = "true";
		}
	| FALSE	{ 
			$$ = "false";
		}
	| comparison { 
		$$ = $1; 							
		}
	| folformula AND folformula				{ $$ = "(and " + (String)$1 + ", " + (String)$3 + " )"; 	}
	| folformula OR folformula				{ $$ = "(or " + (String)$1 + ", " + (String)$3 + " )"; 		}
	| NOT folformula						{ $$ = "(not " + (String)$2 + " )"; 				}
	| LPAREN folformula RPAREN				{ $$ = "( " + (String)$2 + ")"; 				}
	| folformula IMPLIES folformula		
								{ $$ = "(implies " + (String)$1 + ", " + (String)$3 + " )"; 	}
	| FORALL IDENTIFIER SEMICOLON folformula %prec QUANTIFIER
								{ $$ = "(forall " + (String)$2 + "; " + (String)$4 + " )";	}
	| EXISTS IDENTIFIER SEMICOLON folformula %prec QUANTIFIER
								{ $$ = "(exists " + (String)$2 + "; " + (String)$4 + " )";	}

comparison:
	term INEQUALITY term { 
		//this.parsedFormula = new dLFormula( (String)$2, );
		$$ = "(" + (String)$2 +" "+ (String)$1 + ", " + (String)$3 + ")"; 
	}
	;


term:
	NUMBER { 

		$$ = (String)$1;
	}
	| IDENTIFIER LPAREN argumentlist RPAREN {
		$$ = "(" + (String)$1 + " " + (String)$3 + ")";
	}
	| IDENTIFIER { 
		$$ = (String)$1;
	}
	| STATEVAR { 
		$$ = (String)$1;
	}
	| STATEVARPLUS { 
		$$ = (String)$1;
	}
	| LPAREN term RPAREN { 
		$$ = "("+ (String)$2 +")"; 					
	}
	| term PLUS term { 
		$$ = "(+ " + (String)$1 + ", " + (String)$3+ " )";		
	}
	| term MINUS term					{ $$ = "(- " + (String)$1 + ", " + (String)$3 + ")";		}
	| term ASTERISK term					{ $$ = "(* " + (String)$1 + ", " + (String)$3 +")";		}
	| term DIVIDE term					{ $$ = "(/ " + (String)$1 + ", " + (String)$3 + ")";		}
	| term POWER term					{ $$ = "(^ " + (String)$1 + ", " + (String)$3 + ")";		}
	| MINUS term %prec NEGATIVE				{ $$ = "(- 0, " + (String)$2;					}
	;

argumentlist:
	IDENTIFIER						{ $$ = (String)$1; System.out.println(" found arglist");					}
	| STATEVAR						{ $$ = (String)$1; System.out.println(" found arglist");					}
	| STATEVARPLUS						{ $$ = (String)$1; System.out.println(" found arglist");					}
	| argumentlist COMMA argumentlist				{ $$ = (String)$1 + ", " + (String)$3; System.out.println("found arglist, lots");		}
	;
%%



