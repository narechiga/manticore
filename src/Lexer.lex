
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

IdentifierName = [a-z0-9A-Z]*
ModeID = M[0-9]+
StateVar = x[0-9]+
StateVarPlus = x[0-9]+_plus
DecIntegerLiteral = 0 | [1-9][0-9]* 
InequalityLiteral = < | > | <= | >= | \!=

%%
" "			{	//System.out.println("Detected space");
				/* ignore */}

// Hybrid programs
":="			{ return YYParser.ASSIGN;		}
"?"			{ return YYParser.TEST;			}
"++"			{ return YYParser.CUP;			}
"\&\&"			{ return YYParser.RESTRICTDOMAIN;	}
"\{"			{ return YYParser.OPENBRACE;		}
"}"			{ return YYParser.CLOSEBRACE;		}
"'"			{ return YYParser.PRIME;		}
"\\["			{ return YYParser.OPENBOX;		}
"\\]"			{ return YYParser.CLOSEBOX;		}
"\\<"			{ return YYParser.OPENDIAMOND;		}
"\\>"			{ return YYParser.CLOSEDIAMOND;		}
{ModeID}		{ System.out.println("Lexer: Found Mode ID: " + yytext() );
				return YYParser.MODEID;		}
{StateVar}		{ System.out.println("Lexer: Found statevar: " + yytext() );
				return YYParser.STATEVAR;	}
{StateVarPlus}		{ System.out.println("Lexer: Found statevar_plus: " + yytext() );
				return YYParser.STATEVARPLUS;
			}
"M"			{ System.out.println("Lexer: Found Mode variable");
				return YYParser.MODEVAR;		}



// Punctuation
"("			{ return YYParser.LPAREN;	}
","			{ return YYParser.COMMA;	}
";"			{ return YYParser.SEMICOLON;	}
")"			{ return YYParser.RPAREN;	}
"\n"			{ /*return YYParser.NEWLINE;*/	}

// Basic arithmetic
"+"			{ return YYParser.PLUS;		}
"*"			{ return YYParser.ASTERISK;	}
"-"			{ return YYParser.MINUS;	}
"/"			{ return YYParser.DIVIDE;	}
"^"			{ return YYParser.POWER;	}
"="			{ System.out.println("Lexer: Found equals");
				return YYParser.EQUALS;	}
{DecIntegerLiteral}	{	/*System.out.println("Detected number");*/ return YYParser.NUMBER;}
{InequalityLiteral}	{ return YYParser.INEQUALITY;	}


// First order logic symbols
"true"			{ return YYParser.TRUE;		}
"false"			{ return YYParser.FALSE;	}	
"\&"			{ return YYParser.AND;		}
"\|"			{ return YYParser.OR;		}
"\!"			{ return YYParser.NOT;		}
"->"			{ return YYParser.IMPLIES;	}
"\\forall R"		{ return YYParser.FORALL;	}
"\\exists R"		{ return YYParser.EXISTS;	}
{IdentifierName}	{ return YYParser.IDENTIFIER;	}

[^]			{	System.out.println("Lexer: I'm confused, throwing error");
				return YYParser.YYERROR;}




