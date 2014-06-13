
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

IdentifierName = [a-z0-9A-Z]+
DecIntegerLiteral = 0 | [1-9][0-9]* 
InequalityLiteral = < | > | <= | >= | \!=

%%
" "			{	//System.out.println("Detected space");
				/* ignore */}

"R"			{return REALDECLARATION;	}

// Hybrid programs
":="			{ return ASSIGN;		}
"?"			{ return TEST;			}
"++"			{ return CUP;			}
"\&\&"			{ return RESTRICTDOMAIN;	}
"\{"			{ return OPENBRACE;		}
"}"			{ return CLOSEBRACE;		}
"'"			{ return PRIME;			}
"\\["			{ return OPENBOX;		}
"\\]"			{ return CLOSEBOX;		}
"\\<"			{ return OPENDIAMOND;		}
"\\>"			{ return CLOSEDIAMOND;		}
"***"			{ return KLEENESTAR;		}
"**"			{ return RANDOM;		}


// Punctuation
"("			{ return LPAREN;	}
","			{ return COMMA;	}
";"			{ return SEMICOLON;	}
")"			{ return RPAREN;	}
"\n"			{ /*return NEWLINE;*/	}

// Basic arithmetic
"+"			{ return PLUS;		}
"*"			{ return MULTIPLY;	}
"-"			{ return MINUS;		}
"/"			{ return DIVIDE;	}
"^"			{ return POWER;		}
"="			{ System.out.println("Lexer: Found equals");
				return EQUALS;	}
{DecIntegerLiteral}	{	/*System.out.println("Detected number");*/ return NUMBER;}
{InequalityLiteral}	{ return INEQUALITY;	}


// First order logic symbols
"true"			{ return TRUE;		}
"false"			{ return FALSE;	}	
"\&"			{ return AND;		}
"\|"			{ return OR;		}
"\!"			{ return NOT;		}
"->"			{ return IMPLIES;	}
"\\forall R"		{ return FORALL;	}
"\\exists R"		{ return EXISTS;	}
{IdentifierName}	{ return IDENTIFIER;	}

[^]			{	System.out.println("Lexer: I'm confused, throwing error");
				return YYParser.YYERROR;}




