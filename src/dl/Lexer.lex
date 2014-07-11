package manticore.dl;


%%

%public
%class Lexer
%implements YYParser.Lexer
%int
%unicode
%line
%column

%{
	int openBraceCount = 0;
	boolean debug = false;
	public Object getLVal() {
		//System.out.println("YYTEXT is: " + yytext() );
		return yytext();
	}

	public void yyerror ( String S ) {
		System.err.println( S );
	}
%}


IdentifierName = [a-zA-Z_]+[a-z0-9A-Z_]*
Number = [-]? [0-9]+ \.?[0-9]* | [-]? [0-9]+ \.?[0-9]* e [-]?[0-9]+
InequalityLiteral = < | > | <= | >= | \!=
SchemaText = [^\{\}]+


InputCharacter = [^\r\n]
LineTerminator = \r|\n|\r\n|\n;
WhiteSpace     = {LineTerminator} | [ \t\f]

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*
/**/
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}


%state SCHEMAS
%state COMMENTS

%%
<YYINITIAL> {
	{WhiteSpace} { 
		if ( debug ) {
			System.out.println("Lexer: space");
			System.out.println("Lexer @ " + yytext() );
		}
	}
	{Comment} {
		if ( debug ) {
			System.out.println("Lexer: comment");	
			System.out.println("Lexer @ " + yytext() );
		}
	}
	
	// Administrative
	"\\external" { 
		if ( debug ) {
			System.out.println("Lexer: EXTERNAL");
			System.out.println("Lexer @ " + yytext() );
		}
		return EXTERNAL;				
	}
	"\\functions" { 
		if ( debug ) {
			System.out.println("Lexer: FUNCTIONS");
			System.out.println("Lexer @ " + yytext() );
		}
		return FUNCTIONS;
	}
	"\\problem" {
		if ( debug ) {
			System.out.println("Lexer: PROBLEM");
			System.out.println("Lexer @ " + yytext() );
		}
		return PROBLEM;
	}
	"\\annotations" {
		if ( debug ) {
			System.out.println("Lexer: ANNOTATION");
			System.out.println("Lexer @ " + yytext() );
		}
		return ANNOTATION;
	}
		
	
	"\\schemaVariables" { 
		if ( debug ) {
			System.out.println("Lexer: SCHEMAVARIABLES");
			System.out.println("Lexer @ " + yytext() );
		}
		yybegin( SCHEMAS ); 
		return SCHEMAVARIABLES;
	}
	"\\rules" { 
		if ( debug ) {
			System.out.println("Lexer: RULES");
			System.out.println("Lexer @ " + yytext() );
		}
		yybegin( SCHEMAS ); return RULES;
	}
	// EITool files
	"\\statevariables" {
		if ( debug ) {
			System.out.println("Lexer: STATEVARIABLES");
			System.out.println("Lexer @ " + yytext() );
		}
		return STATEVARIABLES;
	}
	"\\eiparameters" {
		if ( debug ) {
			System.out.println("Lexer: EIPARAMETERS");
			System.out.println("Lexer @ " + yytext() );
		}
		return EIPARAMETERS;
	}
	"\\envelope" {
		if ( debug ) {
			System.out.println("Lexer: ENVELOPE");
			System.out.println("Lexer @ " + yytext() );
		}
		return ENVELOPE;
	}
	"\\invariant" {
		if ( debug ) {
			System.out.println("Lexer: INVARIANT");
			System.out.println("Lexer @ " + yytext() );
		}
		return INVARIANT;
	}
	"\\robustparameters" {
		if ( debug ) {
			System.out.println("Lexer: ROBUSTPARAMETERS");
			System.out.println("Lexer @ " + yytext() );
		}
		return ROBUSTPARAMETERS;
	}
	"\\controllaw" {
		if ( debug ) {
			System.out.println("Lexer: CONTROLLAW");
			System.out.println("Lexer @ " + yytext() );
		}
		return CONTROLLAW;
	}
	
	
	"R" { 
		if ( debug ) {
			System.out.println("Lexer: REALDECLARATION");
			System.out.println("Lexer @ " + yytext() );
		}
		return REALDECLARATION;
	}
	
	// Hybrid programs
	":=" { 
		if ( debug ) {
			System.out.println("Lexer: ASSIGN");
			System.out.println("Lexer @ " + yytext() );
		}
		return ASSIGN;
	}
	"?" { 
		if ( debug ) {
			System.out.println("Lexer: TEST");
			System.out.println("Lexer @ " + yytext() );
		}
		return TEST;
	}
	"++" { 
		if ( debug ) {
			System.out.println("Lexer: CUP");
			System.out.println("Lexer @ " + yytext() );
		}
		return CUP;
	}
	"\{" { 
		if ( debug ) {
			System.out.println("Lexer: OPENBRACE");
			System.out.println("Lexer @ " + yytext() );
		}
		return OPENBRACE;
	}
	"\}" { 
		if ( debug ) {
			System.out.println("Lexer: CLOSEBRACE");
			System.out.println("Lexer @ " + yytext() );
		}
		return CLOSEBRACE;
	}
	"'" { 
		if ( debug ) {
			System.out.println("Lexer: PRIME");
			System.out.println("Lexer @ " + yytext() );
		}
		return PRIME;
	}
	"\\[" { 
		if ( debug ) {
			System.out.println("Lexer: OPENBOX");
			System.out.println("Lexer @ " + yytext() );
		}
		return OPENBOX;
	}
	"\\]" { 
		if ( debug ) {
			System.out.println("Lexer: CLOSEBOX");
			System.out.println("Lexer @ " + yytext() );
		}
		return CLOSEBOX;
	}
	"\\<" { 
		if ( debug ) {
			System.out.println("Lexer: OPENDIAMOND");
			System.out.println("Lexer @ " + yytext() );
		}
		return OPENDIAMOND;
	}
	"\\>" { 
		if ( debug ) {
			System.out.println("Lexer: CLOSEDIAMOND");
			System.out.println("Lexer @ " + yytext() );
		}
		return CLOSEDIAMOND;
	}
	"***" { 
		if ( debug ) {
			System.out.println("Lexer: KLEENESTAR");
			System.out.println("Lexer @ " + yytext() );
		}
		return KLEENESTAR;
	}
	"**" { 
		if ( debug ) {
			System.out.println("Lexer: RANDOM");
			System.out.println("Lexer @ " + yytext() );
		}
		return RANDOM;
	}
	
	
	// Punctuation
	"(" { 
		if ( debug ) {
			System.out.println("Lexer: LPAREN");
			System.out.println("Lexer @ " + yytext() );
		}
		return LPAREN;
	}
	"," { 
		if ( debug ) {
			System.out.println("Lexer: COMMA");
			System.out.println("Lexer @ " + yytext() );
		}
		return COMMA;
	}
	";" { 
		if ( debug ) {
			System.out.println("Lexer: SEMICOLON");
			System.out.println("Lexer @ " + yytext() );
		}
		return SEMICOLON;
	}
	")" { 
		if ( debug ) {
			System.out.println("Lexer: RPAREN");
			System.out.println("Lexer @ " + yytext() );
		}
		return RPAREN;
	}
	
	// Basic arithmetic
	"+" { 
		if ( debug ) {
			System.out.println("Lexer: PLUS");
			System.out.println("Lexer @ " + yytext() );
		}
		return PLUS;
	}
	"*" { 
		if ( debug ) {
			System.out.println("Lexer: MULTIPLY");
			System.out.println("Lexer @ " + yytext() );
		}
		return MULTIPLY;
	}
	"-" { 
		if ( debug ) {
			System.out.println("Lexer: MINUS");
			System.out.println("Lexer @ " + yytext() );
		}
		return MINUS;
	}
	"/" { 
		if ( debug ) {
			System.out.println("Lexer: DIVIDE");
			System.out.println("Lexer @ " + yytext() );
		}
		return DIVIDE;
	}
	"^" { 		
		if ( debug ) {
			System.out.println("Lexer: POWER");
			System.out.println("Lexer @ " + yytext() );
		}
		return POWER;		
	}
	"=" { 
		if ( debug ) {
			System.out.println("Lexer: EQUALS");
			System.out.println("Lexer @ " + yytext() );
		}
		return EQUALS;
	}
	{Number} { 
		if ( debug ) {
			System.out.println("Lexer: NUMBER");
			System.out.println("Lexer @ " + yytext() );
		}
		return NUMBER;
	}
	{InequalityLiteral} { 
		if ( debug ) {
			System.out.println("Lexer: INEQUALITY");
			System.out.println("Lexer @ " + yytext() );
		}
		return INEQUALITY;
	}
	
	
	// First order logic symbols
	"true" { 
		if ( debug ) {
			System.out.println("Lexer: TRUE");
			System.out.println("Lexer @ " + yytext() );
		}
		return TRUE;
	}
	"false" { 
		if ( debug ) {
			System.out.println("Lexer: FALSE");
			System.out.println("Lexer @ " + yytext() );
		}
		return FALSE;
	}	
	"\&" { 
		if ( debug ) {
			System.out.println("Lexer: AND");
			System.out.println("Lexer @ " + yytext() );
		}
		return AND;
	}
	"\|" { 
		if ( debug ) {
			System.out.println("Lexer: OR");
			System.out.println("Lexer @ " + yytext() );
		}
		return OR;
	}
	"\!" { 
		if ( debug ) {
			System.out.println("Lexer: NOT");
			System.out.println("Lexer @ " + yytext() );
		}
		return NOT;
	}
	"<->" { 
		if ( debug ) {
			System.out.println("Lexer: IFF");
			System.out.println("Lexer @ " + yytext() );
		}
		return IFF;
	}
	"->" { 
		if ( debug ) {
			System.out.println("Lexer: IMPLIES");
			System.out.println("Lexer @ " + yytext() );
		}
		return IMPLIES;
	}
	"\\forall R" { 
		if ( debug ) {
			System.out.println("Lexer: FORALL");
			System.out.println("Lexer @ " + yytext() );
		}
		return FORALL;
	}
	"\\exists R" { 
		if ( debug ) {
			System.out.println("Lexer: EXISTS");
			System.out.println("Lexer @ " + yytext() );
		}
		return EXISTS;
	}
	{IdentifierName} { 
		if ( debug ) {
			System.out.println("Lexer: IDENTIFIER");
			System.out.println("Lexer @ " + yytext() );
		}
		return IDENTIFIER;
	}
	
	[^] { 
		System.out.println("Lexer: I'm confused, throwing error");
		System.out.println("Lexer @ " + yytext() );
		return YYParser.YYERROR;
	}
}

<SCHEMAS> {
	"\{" { 
		if ( openBraceCount == 0 ) {
			openBraceCount = openBraceCount + 1;
			if ( debug ) {
				System.out.println("Lexer: OPENBRACE");
			}
			return OPENBRACE;
		} else {
			openBraceCount = openBraceCount + 1;
			if ( debug ) {
				System.out.println("Lexer: SCHEMATEXT");
			}
			return SCHEMATEXT;
		}						

	}

	"\}" { 
		openBraceCount = openBraceCount - 1; 
		if ( openBraceCount == 0 ) {
			if ( debug ) {
				System.out.println("Lexer: CLOSEBRACE");
			}
			yybegin( YYINITIAL );
			return CLOSEBRACE;			
		} else {
			if ( debug ) {
				System.out.println("Lexer: SCHEMATEXT");
			}
			return SCHEMATEXT;
		}
	}

	{SchemaText} { 
		if ( debug ) {
			System.out.println("Lexer: SCHEMATEXT");
		}
		return SCHEMATEXT;
	}

}




