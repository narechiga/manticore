
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
LineTerminator = \r|\n|\r\n;
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
	" "			{	//System.out.println("Detected space");
					/* ignore */}
	{Comment}		{ /* ignore */ 					}
	
	// Administrative
	"\\external"		{ return EXTERNAL;				}
	"\\functions"		{ return FUNCTIONS;				}
	"\\problem"		{ return PROBLEM;				}
	
	"\\schemaVariables"	{ yybegin( SCHEMAS ); return SCHEMAVARIABLES;	}
	"\\rules"		{ yybegin( SCHEMAS ); return RULES;		}
	
	"R"			{ return REALDECLARATION;			}
	
	// Hybrid programs
	":="			{ return ASSIGN;				}
	"?"			{ return TEST;					}
	"++"			{ return CUP;					}
	"\{"			{ return OPENBRACE;				}
	"\}"			{ return CLOSEBRACE;				}
	"'"			{ return PRIME;					}
	"\\["			{ return OPENBOX;				}
	"\\]"			{ return CLOSEBOX;				}
	"\\<"			{ return OPENDIAMOND;				}
	"\\>"			{ return CLOSEDIAMOND;				}
	"***"			{ return KLEENESTAR;				}
	"**"			{ return RANDOM;				}
	
	
	// Punctuation
	"("			{ return LPAREN;				}
	","			{ return COMMA;					}
	";"			{ return SEMICOLON;				}
	")"			{ return RPAREN;				}
	"\n"			{ /*return NEWLINE;*/				}
	
	// Basic arithmetic
	"+"			{ return PLUS;					}
	"*"			{ return MULTIPLY;				}
	"-"			{ return MINUS;					}
	"/"			{ return DIVIDE;				}
	"^"			{ return POWER;					}
	"="			{ return EQUALS;				}
	{DecIntegerLiteral}	{ return NUMBER;				}
	{InequalityLiteral}	{ return INEQUALITY;				}
	
	
	// First order logic symbols
	"true"			{ return TRUE;					}
	"false"			{ return FALSE;					}	
	"\&"			{ return AND;					}
	"\|"			{ return OR;					}
	"\!"			{ return NOT;					}
	"<->"			{ return IFF;					}
	"->"			{ return IMPLIES;				}
	"\\forall R"		{ return FORALL;				}
	"\\exists R"		{ return EXISTS;				}
	{IdentifierName}	{ return IDENTIFIER;				}
	
	[^]			{ System.out.println("Lexer: I'm confused, throwing error");
					return YYParser.YYERROR;}
}

<SCHEMAS> {
	"\{"			{ //System.out.println("(in schema state)");
				//System.out.println("opened a new brace: " + openBraceCount);
				if ( openBraceCount == 0 ) {
					//System.out.println("found initial open brace...");
					openBraceCount = openBraceCount + 1;
					return OPENBRACE;
				} else {
					//System.out.println("found new brace, treating it as schema text...");
					openBraceCount = openBraceCount + 1;
					return SCHEMATEXT;
				}						
				
										}

	"\}"			{ //System.out.println("(in schema state)");
				openBraceCount = openBraceCount - 1; 
				//System.out.println("closed a brace: " + openBraceCount);
				if ( openBraceCount == 0 ) {
					//System.out.println("exiting schema environement");
					yybegin( YYINITIAL );
					return CLOSEBRACE;			
				} else {
					//System.out.println("found a closing brace, treating it as schema text");
					return SCHEMATEXT;
				}						}

	{SchemaText}		{ return SCHEMATEXT;				}

}




