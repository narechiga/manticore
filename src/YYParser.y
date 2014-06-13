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
%token REALDECLARATION
/*%token KLEENESTAR*/


/* precedence for hybrid program operators */
%right KLEENESTAR
%right SEMICOLON CUP


/* Modalities */
%token OPENBOX
%token CLOSEBOX
%token OPENDIAMOND
%token CLOSEDIAMOND

/* Arithmetic */
%token NUMBER
%token IDENTIFIER
%token PLUS
%token MINUS
%token MULTIPLY
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

%right IMPLIES
%right OR AND
%left OPENBOX CLOSEBOX OPENDIAMOND CLOSEDIAMOND
%left NOT QUANTIFIER

%right INEQUALITY /* <, >, <=, >=, =, != */
%left MINUS PLUS
%left DIVIDE MULTIPLY
%right POWER
%left NEGATIVE
%left REALDECLARATION

%%
input: 
	dLformula { System.out.println("Found: dLformula"); System.out.println((String)$1); }
	| hybridprogram
		{$$ = (String)$1; System.out.println("Found hybrid program"); System.out.println( (String)$1 ); }
	| OPENBOX vardeclaration CLOSEBOX
		{ $$ = "(declare-vars: \n" + (String)$2 + ")"; System.out.println( $$ ); }
;

dLformula:
	TRUE { 
			$$ = "true";
		}
	| FALSE	{ 
			$$ = "false";
		}
	| comparison { 
		$$ = $1; 							
		}
	| dLformula AND dLformula				{ $$ = "(and " + (String)$1 + ", " + (String)$3 + " )"; 	}
	| dLformula OR dLformula				{ $$ = "(or " + (String)$1 + ", " + (String)$3 + " )"; 		}
	| NOT dLformula						{ $$ = "(not " + (String)$2 + " )"; 				}
	| LPAREN dLformula RPAREN				{ $$ = "( " + (String)$2 + ")"; 				}
	| dLformula IMPLIES dLformula		
								{ $$ = "(implies " + (String)$1 + ", " + (String)$3 + " )"; 	}
	| FORALL IDENTIFIER SEMICOLON dLformula %prec QUANTIFIER
								{ $$ = "(forall " + (String)$2 + "; " + (String)$4 + " )";	}
	| EXISTS IDENTIFIER SEMICOLON dLformula %prec QUANTIFIER
								{ $$ = "(exists " + (String)$2 + "; " + (String)$4 + " )";	}
	| OPENBOX hybridprogram CLOSEBOX dLformula
								{ $$ = "(box (hp: " + (String)$2 + " ), (post: " + (String)$4 + " )"; }
	| OPENDIAMOND hybridprogram CLOSEDIAMOND dLformula
								{ $$ = "(diamond (hp: " + (String)$2 + " ), (post: " + (String)$4 + " )"; }
;

hybridprogram:
	| odesystem
		{ $$ = (String)$1; }
	| test
		{ $$ = (String)$1; }
	| assignment
		{ $$ = (String)$1; }
	| hybridprogram SEMICOLON hybridprogram
		{ $$ = "(sequence " + (String)$1 + ", " + (String)$3 + " )"; }
	| hybridprogram CUP hybridprogram
		{ $$ = "(choice " + (String)$1 + ", " + (String)$3 + " )"; }
	| hybridprogram KLEENESTAR
		{ $$ = "(repeat " + (String)$1 + " )"; }
;

vardeclaration:
	REALDECLARATION IDENTIFIER
		{$$ = "\t(declare-real " + (String)$2 + " )\n"; }
	| REALDECLARATION IDENTIFIER SEMICOLON vardeclaration
		{$$ = "\t(declare-real " + (String)$2 + " )\n"  + (String)$4; }
;

assignment:
	IDENTIFIER ASSIGN RANDOM
		{ $$ = "(assign " + (String)$1 + ", " + (String)$3 + " )"; }
	| IDENTIFIER ASSIGN term
		{ $$ = "(assign " + (String)$1 + ", " + (String)$3 + " )"; }
	| LPAREN assignment RPAREN
		{ $$ = (String)$2; }
;


test:
	TEST dLformula						{ $$ = "(test " + (String)$2 + " )";				}
	| LPAREN test RPAREN					{ $$ = (String)$2;						}
	;

odesystem:
	LPAREN odesystem RPAREN
		{ $$ = (String)$2; }
	| OPENBRACE odelist CLOSEBRACE
		{ $$ = "(continuous " + "(odelist: " + (String)$2 + " )" + ", (domain: true )"; }
	| OPENBRACE odelist RESTRICTDOMAIN folformula CLOSEBRACE	
		{ $$ = "(continuous " + "(odelist: " + (String)$2 + " )" + ", (domain: " + (String)$4 + " )"; }
	;
odelist:
	ode							{ $$ = (String)$1;						}
	| odelist COMMA ode					{ $$ = (String)$1 + ", " + (String)$3;				}
	;
ode:
	IDENTIFIER PRIME EQUALS term				{ $$ = (String)$1 + "' = " + (String)$4;			}
	;


folformula:
	TRUE { 
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
	| NOT folformula					{ $$ = "(not " + (String)$2 + " )"; 				}
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
	| term EQUALS term {
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
	| LPAREN term RPAREN { 
		$$ = "("+ (String)$2 +")"; 					
	}
	| term PLUS term { 
		$$ = "(+ " + (String)$1 + ", " + (String)$3+ " )";		
	}
	| term MINUS term					{ $$ = "(- " + (String)$1 + ", " + (String)$3 + ")";		}
	| term MULTIPLY term
								{ $$ = "(* " + (String)$1 + ", " + (String)$3 +")";		}
	| term DIVIDE term					{ $$ = "(/ " + (String)$1 + ", " + (String)$3 + ")";		}
	| term POWER term					{ $$ = "(^ " + (String)$1 + ", " + (String)$3 + ")";		}
	| MINUS term %prec NEGATIVE				{ $$ = "(- 0, " + (String)$2;					}
	;

argumentlist:
	| term							{ $$ = (String)$1; System.out.println(" found arglist");					}
	| term COMMA argumentlist				{ $$ = (String)$1 + ", " + (String)$3; System.out.println("found arglist, multiple args");	}
	;
%%



