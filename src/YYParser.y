%{
	import java.util.*;
%}

%language "Java"

%token EXTERNAL
%token FUNCTIONS
%token RULES
%token SCHEMAVARIABLES
%token SCHEMATEXT
%token PROBLEM

/* Hybrid programs */
%token ASSIGN
%token PRIME
%token OPENBRACE
%token CLOSEBRACE
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
%token IFF
%token FORALL
%token EXISTS
%token TRUE
%token FALSE

%right IMPLIES IFF
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
	fullblock
		{ $$ = (String)$1; System.out.println("full block"); System.out.println($$); }
	| funblock
		{ $$ = (String)$1; System.out.println("function block"); System.out.println($$); }
	| varblock
		{ $$ = (String)$1; System.out.println("variable declaration block"); System.out.println($$); }
	| schemavarsblock
		{ $$ = (String)$1; System.out.println("schema variables block"); System.out.println($$); }
	| rulesblock
		{ $$ = (String)$1; System.out.println("rules block"); System.out.println($$); }
	| dLformula 
		{ System.out.println("Found: dLformula"); System.out.println((String)$1); }
	| hybridprogram
		{$$ = (String)$1; System.out.println("Found hybrid program"); System.out.println( (String)$1 ); }
;

fullblock:
	problemblock
		{ $$ = (String)$1; }
	| funblock problemblock
		{ $$ = (String)$1 + (String)$2; }
	| funblock schemavarsblock rulesblock problemblock
		{ $$ = (String)$1 + (String)$2 + (String)$3 + (String)$4; }
;

problemblock:
	PROBLEM OPENBRACE dLformula CLOSEBRACE
		{ $$ = "{\n" + (String)$3 + "\n}"; System.out.println( $$ ); }
	| PROBLEM OPENBRACE varblock dLformula CLOSEBRACE
		{ $$ = "{\n" + (String)$3 + "\n" + (String)$4 + "\n}"; System.out.println($$); }
;


/*==================== Schema rules and variables ====================*/
schemavarsblock:
	SCHEMAVARIABLES OPENBRACE schematext CLOSEBRACE
		{ $$ = "(declare-schema-vars: \n" + (String)$3 + "\n)"; System.out.println( $$ ); }
;

rulesblock:
	RULES OPENBRACE schematext CLOSEBRACE
		{ $$ = "(declare-rules: \n" + (String)$3 + "\n)"; System.out.println( $$ ); }
;

schematext:
	SCHEMATEXT
		{ $$ = (String)$1; }
	| SCHEMATEXT schematext
		{ $$ = (String)$1 + (String)$2; }
;

/*============================================================*/

/*==================== Variable declarations ====================*/
varblock:
	OPENBOX vardeclaration CLOSEBOX
		{ $$ = "(declare-vars: \n" + (String)$2 + ")"; System.out.println( $$ ); }
	| OPENBOX vardeclaration varinitlist CLOSEBOX
		{ $$ = "(declare-vars: \n" + (String)$2 + ")" + (String)$3; System.out.println( $$ ); }
;

vardeclaration:
	REALDECLARATION varlist SEMICOLON
		{ $$ = (String)$2; }
	| REALDECLARATION varlist SEMICOLON vardeclaration
		{ $$ = "\t(declare-real " + (String)$2 + " )\n"  + (String)$4; }
;

varlist:
	IDENTIFIER
		{ $$ = "\t(declare-real " + (String)$1 + " )\n"; }
	| IDENTIFIER COMMA varlist
		{ $$ = "\t(declare-real " + (String)$1 + " )\n" + (String)$3; }
	

varinitlist:
	IDENTIFIER ASSIGN term SEMICOLON
		{ $$ = "\t(init: " + (String)$1 + ", " + (String)$3 + " )\n";}
	| IDENTIFIER ASSIGN term SEMICOLON varinitlist
		{ $$ = "\t(init: " + (String)$1 + ", " + (String)$3 + " )\n" + (String)$5; }
;
/*============================================================*/

/*==================== Function declarations ====================*/
funblock:
	FUNCTIONS OPENBRACE functiondeclaration CLOSEBRACE
		{ $$ = "(declare-funs: \n" + (String)$3 + ")"; System.out.println( $$ ); }
;

functiondeclaration:
	REALDECLARATION IDENTIFIER LPAREN argumentdeclaration RPAREN SEMICOLON
		{$$ = "(R fun " + (String)$2 + " " + (String)$4 + " )\n"; }
	| EXTERNAL REALDECLARATION IDENTIFIER LPAREN argumentdeclaration RPAREN SEMICOLON
		{$$ = "(R fun " + (String)$3 + " " + (String)$5 + " )\n"; }
	| REALDECLARATION IDENTIFIER LPAREN argumentdeclaration RPAREN SEMICOLON functiondeclaration
		{$$ = "(R fun " + (String)$2 + " " + (String)$4 + " )\n" + (String)$7; }
	| EXTERNAL REALDECLARATION IDENTIFIER LPAREN argumentdeclaration RPAREN SEMICOLON functiondeclaration
		{$$ = "(R fun " + (String)$3 + " " + (String)$5 + " )\n" + (String)$8; }
;

argumentdeclaration:
	| REALDECLARATION
		{ $$ = (String)$1; }
	| REALDECLARATION COMMA argumentdeclaration
		{ $$ = (String)$1 + ", " + (String)$3; }
;
/*============================================================*/

/*==================== Differential dynamic logic ====================*/
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
	| dLformula IFF dLformula		
								{ $$ = "(iff " + (String)$1 + ", " + (String)$3 + " )"; 	}
	| FORALL IDENTIFIER SEMICOLON dLformula %prec QUANTIFIER
								{ $$ = "(forall " + (String)$2 + "; " + (String)$4 + " )";	}
	| EXISTS IDENTIFIER SEMICOLON dLformula %prec QUANTIFIER
								{ $$ = "(exists " + (String)$2 + "; " + (String)$4 + " )";	}
	| OPENBOX hybridprogram CLOSEBOX dLformula
								{ $$ = "(box (hp: " + (String)$2 + " ), (post: " + (String)$4 + " ) )"; }
	| OPENDIAMOND hybridprogram CLOSEDIAMOND dLformula
								{ $$ = "(diamond (hp: " + (String)$2 + " ), (post: " + (String)$4 + " ) )"; }
;

hybridprogram:
	odesystem
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
	| LPAREN hybridprogram RPAREN
		{ $$ = (String)$2; }
;

assignment:
	IDENTIFIER ASSIGN RANDOM
		{ $$ = "(assign " + (String)$1 + ", " + (String)$3 + " )"; }
	| IDENTIFIER ASSIGN term
		{ $$ = "(assign " + (String)$1 + ", " + (String)$3 + " )"; }
;


test:
	TEST dLformula						{ $$ = "(test " + (String)$2 + " )";				}
	;

odesystem:
	OPENBRACE odelist CLOSEBRACE
		{ $$ = "(continuous " + "(odelist: " + (String)$2 + " )" + ", (domain: true ) )"; }
	| OPENBRACE odelist AND dLformula CLOSEBRACE	
		{ $$ = "(continuous " + "(odelist: " + (String)$2 + " )" + ", (domain: " + (String)$4 + " ) )"; }
	;
odelist:
	ode							{ $$ = (String)$1;						}
	| odelist COMMA ode					{ $$ = (String)$1 + ", " + (String)$3;				}
	;
ode:
	IDENTIFIER PRIME EQUALS term				{ $$ = (String)$1 + "' = " + (String)$4;			}
	;



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
	| MINUS term %prec NEGATIVE				{ $$ = "(- 0, " + (String)$2 + " )";				}
	;

argumentlist:
	| term							{ $$ = (String)$1; System.out.println(" found arglist");					}
	| term COMMA argumentlist				{ $$ = (String)$1 + ", " + (String)$3; System.out.println("found arglist, multiple args");	}
	;

/*============================================================*/
%%



