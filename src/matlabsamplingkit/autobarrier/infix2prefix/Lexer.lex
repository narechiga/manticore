
%%

%public
%class Lexer
%implements YYParser.Lexer
%int
%unicode
%line
%column

%{
	public Object getLVal() {
		//System.out.println("YYTEXT is: " + yytext() );
		return yytext();
	}

	public void yyerror ( String S ) {
		System.err.println( S );
	}
%}

Identifier = [a-z0-9A-Z]*
Number =  [0-9]+ \.?[0-9]* | [0-9]+ \.?[0-9]* e [-]?[0-9]+
ComparatorLiteral = < | > | <= | >= | = | \!=

%%
{Number}		{	//System.out.println("Detected number");
				return NUMBER;}
{ComparatorLiteral}	{return COMPARATOR;}
" "			{	//System.out.println("Detected space");
				/* ignore */}
"and"			{ return AND; }
"or"			{ return OR; }
"not"			{ return NOT; }
"implies"		{ return IMPLIES; }
{Identifier}		{ return IDENTIFIER;}
"+"			{ return PLUS;}
"*"			{ return TIMES;}
"-"			{ return MINUS;}
"/"			{ return DIVIDE;}
"^"			{ return POWER;}
"("			{ return LPAREN;}
")"			{ return RPAREN;}
","			{ return COMMA;}
"\n"			{ return NEWLINE;}
[^]			{	System.out.println("Lexer: I'm confused, throwing error");
				return YYParser.YYERROR;}




