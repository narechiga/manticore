
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
":="			{ return ASSIGN;		}
"?"			{ return TEST;			}
"++"			{ return CUP;			}
"\&\&"			{ return RESTRICTDOMAIN;	}
"\{"			{ return OPENBRACE;		}
"}"			{ return CLOSEBRACE;		}
"'"			{ return PRIME;		}
"\\["			{ return OPENBOX;		}
"\\]"			{ return CLOSEBOX;		}
"\\<"			{ return OPENDIAMOND;		}
"\\>"			{ return CLOSEDIAMOND;		}
{ModeID}		{ System.out.println("Lexer: Found Mode ID: " + yytext() );
				return MODEID;		}
{StateVar}		{ System.out.println("Lexer: Found statevar: " + yytext() );
				return STATEVAR;	}
{StateVarPlus}		{ System.out.println("Lexer: Found statevar_plus: " + yytext() );
				return STATEVARPLUS;
			}
"M"			{ System.out.println("Lexer: Found Mode variable");
				return MODEVAR;		}



// Punctuation
"("			{ return LPAREN;	}
","			{ return COMMA;	}
";"			{ return SEMICOLON;	}
")"			{ return RPAREN;	}
"\n"			{ /*return NEWLINE;*/	}

// Basic arithmetic
"+"			{ return PLUS;		}
"*"			{ return ASTERISK;	}
"-"			{ return MINUS;	}
"/"			{ return DIVIDE;	}
"^"			{ return POWER;	}
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
				return YYERROR;}




