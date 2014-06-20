
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
	public Object getLVal() {
		//System.out.println("YYTEXT is: " + yytext() );
		return yytext();
	}

	public void yyerror ( String S ) {
		System.err.println( S );
	}
%}


IdentifierName = [a-zA-Z_]+[a-z0-9A-Z_]*
DecIntegerLiteral = 0 | [1-9][0-9]* 
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
		System.out.println("Lexer: space");
		System.out.println("Lexer @ " + yytext() );
	}
	{LineTerminator} {
		System.out.println("Lexer: newline");
		System.out.println("Lexer @ " + yytext() );
	}
	{Comment} {
		System.out.println("Lexer: comment");	
		System.out.println("Lexer @ " + yytext() );
	}
	
	// Administrative
	"\\external" { 
		System.out.println("Lexer: EXTERNAL");
		System.out.println("Lexer @ " + yytext() );
		return EXTERNAL;				
	}
	"\\functions" { 
		System.out.println("Lexer: FUNCTIONS");
		System.out.println("Lexer @ " + yytext() );
		return FUNCTIONS;
	}
	"\\problem" {
		System.out.println("Lexer: PROBLEM");
		System.out.println("Lexer @ " + yytext() );
		return PROBLEM;
	}
	"\\annotation" {
		System.out.println("Lexer: ANNOTATION");
		System.out.println("Lexer @ " + yytext() );
		return ANNOTATION;
	}
		
	
	"\\schemaVariables" { 
		System.out.println("Lexer: SCHEMAVARIABLES");
		System.out.println("Lexer @ " + yytext() );
		yybegin( SCHEMAS ); 
		return SCHEMAVARIABLES;
	}
	"\\rules" { 
		System.out.println("Lexer: RULES");
		System.out.println("Lexer @ " + yytext() );
		yybegin( SCHEMAS ); return RULES;
	}
	
	"R" { 
		System.out.println("Lexer: REALDECLARATION");
		System.out.println("Lexer @ " + yytext() );
		return REALDECLARATION;
	}
	
	// Hybrid programs
	":=" { 
		System.out.println("Lexer: ASSIGN");
		System.out.println("Lexer @ " + yytext() );
		return ASSIGN;
	}
	"?" { 
		System.out.println("Lexer: TEST");
		System.out.println("Lexer @ " + yytext() );
		return TEST;
	}
	"++" { 
		System.out.println("Lexer: CUP");
		System.out.println("Lexer @ " + yytext() );
		return CUP;
	}
	"\{" { 
		System.out.println("Lexer: OPENBRACE");
		System.out.println("Lexer @ " + yytext() );
		return OPENBRACE;
	}
	"\}" { 
		System.out.println("Lexer: CLOSEBRACE");
		System.out.println("Lexer @ " + yytext() );
		return CLOSEBRACE;
	}
	"'" { 
		System.out.println("Lexer: PRIME");
		System.out.println("Lexer @ " + yytext() );
		return PRIME;
	}
	"\\[" { 
		System.out.println("Lexer: OPENBOX");
		System.out.println("Lexer @ " + yytext() );
		return OPENBOX;
	}
	"\\]" { 
		System.out.println("Lexer: CLOSEBOX");
		System.out.println("Lexer @ " + yytext() );
		return CLOSEBOX;
	}
	"\\<" { 
		System.out.println("Lexer: OPENDIAMOND");
		System.out.println("Lexer @ " + yytext() );
		return OPENDIAMOND;
	}
	"\\>" { 
		System.out.println("Lexer: CLOSEDIAMOND");
		System.out.println("Lexer @ " + yytext() );
		return CLOSEDIAMOND;
	}
	"***" { 
		System.out.println("Lexer: KLEENESTAR");
		System.out.println("Lexer @ " + yytext() );
		return KLEENESTAR;
	}
	"**" { 
		System.out.println("Lexer: RANDOM");
		System.out.println("Lexer @ " + yytext() );
		return RANDOM;
	}
	
	
	// Punctuation
	"(" { 
		System.out.println("Lexer: LPAREN");
		System.out.println("Lexer @ " + yytext() );
		return LPAREN;
	}
	"," { 
		System.out.println("Lexer: COMMA");
		System.out.println("Lexer @ " + yytext() );
		return COMMA;
	}
	";" { 
		System.out.println("Lexer: SEMICOLON");
		System.out.println("Lexer @ " + yytext() );
		return SEMICOLON;
	}
	")" { 
		System.out.println("Lexer: RPAREN");
		System.out.println("Lexer @ " + yytext() );
		return RPAREN;
	}
	
	// Basic arithmetic
	"+" { 
		System.out.println("Lexer: PLUS");
		System.out.println("Lexer @ " + yytext() );
		return PLUS;
	}
	"*" { 
		System.out.println("Lexer: MULTIPLY");
		System.out.println("Lexer @ " + yytext() );
		return MULTIPLY;
	}
	"-" { 
		System.out.println("Lexer: MINUS");
		System.out.println("Lexer @ " + yytext() );
		return MINUS;
	}
	"/" { 
		System.out.println("Lexer: DIVIDE");
		System.out.println("Lexer @ " + yytext() );
		return DIVIDE;
	}
	"^" { 		
		System.out.println("Lexer: POWER");
		System.out.println("Lexer @ " + yytext() );
		return POWER;		
	}
	"=" { 
		System.out.println("Lexer: EQUALS");
		System.out.println("Lexer @ " + yytext() );
		return EQUALS;
	}
	{DecIntegerLiteral} { 
		System.out.println("Lexer: NUMBER");
		System.out.println("Lexer @ " + yytext() );
		return NUMBER;
	}
	{InequalityLiteral} { 
		System.out.println("Lexer: INEQUALITY");
		System.out.println("Lexer @ " + yytext() );
		return INEQUALITY;
	}
	
	
	// First order logic symbols
	"true" { 
		System.out.println("Lexer: TRUE");
		System.out.println("Lexer @ " + yytext() );
		return TRUE;
	}
	"false" { 
		System.out.println("Lexer: FALSE");
		System.out.println("Lexer @ " + yytext() );
		return FALSE;
	}	
	"\&" { 
		System.out.println("Lexer: AND");
		System.out.println("Lexer @ " + yytext() );
		return AND;
	}
	"\|" { 
		System.out.println("Lexer: OR");
		System.out.println("Lexer @ " + yytext() );
		return OR;
	}
	"\!" { 
		System.out.println("Lexer: NOT");
		System.out.println("Lexer @ " + yytext() );
		return NOT;
	}
	"<->" { 
		System.out.println("Lexer: IFF");
		System.out.println("Lexer @ " + yytext() );
		return IFF;
	}
	"->" { 
		System.out.println("Lexer: IMPLIES");
		System.out.println("Lexer @ " + yytext() );
		return IMPLIES;
	}
	"\\forall R" { 
		System.out.println("Lexer: FORALL");
		System.out.println("Lexer @ " + yytext() );
		return FORALL;
	}
	"\\exists R" { 
		System.out.println("Lexer: EXISTS");
		System.out.println("Lexer @ " + yytext() );
		return EXISTS;
	}
	{IdentifierName} { 
		System.out.println("Lexer: IDENTIFIER");
		System.out.println("Lexer @ " + yytext() );
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
		//System.out.println("(in schema state)");
		//System.out.println("opened a new brace: " + openBraceCount);
		if ( openBraceCount == 0 ) {
			//System.out.println("found initial open brace...");
			openBraceCount = openBraceCount + 1;
			System.out.println("Lexer: OPENBRACE");
			return OPENBRACE;
		} else {
			//System.out.println("found new brace, treating it as schema text...");
			openBraceCount = openBraceCount + 1;
			System.out.println("Lexer: SCHEMATEXT");
			return SCHEMATEXT;
		}						

	}

	"\}" { 
		//System.out.println("(in schema state)");
		openBraceCount = openBraceCount - 1; 
		//System.out.println("closed a brace: " + openBraceCount);
		if ( openBraceCount == 0 ) {
			//System.out.println("exiting schema environement");
			System.out.println("Lexer: CLOSEBRACE");
			yybegin( YYINITIAL );
			return CLOSEBRACE;			
		} else {
			//System.out.println("found a closing brace, treating it as schema text");
			System.out.println("Lexer: SCHEMATEXT");
			return SCHEMATEXT;
		}
	}

	{SchemaText} { 
		System.out.println("Lexer: SCHEMATEXT");
		return SCHEMATEXT;
	}

}




