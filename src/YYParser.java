/* A Bison parser, made by GNU Bison 2.5.  */

/* Skeleton implementation for Bison LALR(1) parsers in Java
   
      Copyright (C) 2007-2011 Free Software Foundation, Inc.
   
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.
   
   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* First part of user declarations.  */

/* Line 32 of lalr1.java  */
/* Line 1 of "YYParser.y"  */

	import java.util.*;


/**
 * A Bison parser, automatically generated from <tt>YYParser.y</tt>.
 *
 * @author LALR (1) parser skeleton written by Paolo Bonzini.
 */
class YYParser
{
    /** Version number for the Bison executable that generated this parser.  */
  public static final String bisonVersion = "2.5";

  /** Name of the skeleton that generated this parser.  */
  public static final String bisonSkeleton = "lalr1.java";


  /** True if verbose error messages are enabled.  */
  public boolean errorVerbose = false;



  /** Token returned by the scanner to signal the end of its input.  */
  public static final int EOF = 0;

/* Tokens.  */
  /** Token number, to be returned by the scanner.  */
  public static final int ASSIGN = 258;
  /** Token number, to be returned by the scanner.  */
  public static final int PRIME = 259;
  /** Token number, to be returned by the scanner.  */
  public static final int OPENBRACE = 260;
  /** Token number, to be returned by the scanner.  */
  public static final int CLOSEBRACE = 261;
  /** Token number, to be returned by the scanner.  */
  public static final int RESTRICTDOMAIN = 262;
  /** Token number, to be returned by the scanner.  */
  public static final int EQUALS = 263;
  /** Token number, to be returned by the scanner.  */
  public static final int TEST = 264;
  /** Token number, to be returned by the scanner.  */
  public static final int CUP = 265;
  /** Token number, to be returned by the scanner.  */
  public static final int RANDOM = 266;
  /** Token number, to be returned by the scanner.  */
  public static final int OPENBOX = 267;
  /** Token number, to be returned by the scanner.  */
  public static final int CLOSEBOX = 268;
  /** Token number, to be returned by the scanner.  */
  public static final int OPENDIAMOND = 269;
  /** Token number, to be returned by the scanner.  */
  public static final int CLOSEDIAMOND = 270;
  /** Token number, to be returned by the scanner.  */
  public static final int MODEVAR = 271;
  /** Token number, to be returned by the scanner.  */
  public static final int MODEID = 272;
  /** Token number, to be returned by the scanner.  */
  public static final int STATEVAR = 273;
  /** Token number, to be returned by the scanner.  */
  public static final int STATEVARPLUS = 274;
  /** Token number, to be returned by the scanner.  */
  public static final int NUMBER = 275;
  /** Token number, to be returned by the scanner.  */
  public static final int ASTERISK = 276;
  /** Token number, to be returned by the scanner.  */
  public static final int IDENTIFIER = 277;
  /** Token number, to be returned by the scanner.  */
  public static final int PLUS = 278;
  /** Token number, to be returned by the scanner.  */
  public static final int MINUS = 279;
  /** Token number, to be returned by the scanner.  */
  public static final int DIVIDE = 280;
  /** Token number, to be returned by the scanner.  */
  public static final int POWER = 281;
  /** Token number, to be returned by the scanner.  */
  public static final int NEWLINE = 282;
  /** Token number, to be returned by the scanner.  */
  public static final int INEQUALITY = 283;
  /** Token number, to be returned by the scanner.  */
  public static final int LPAREN = 284;
  /** Token number, to be returned by the scanner.  */
  public static final int RPAREN = 285;
  /** Token number, to be returned by the scanner.  */
  public static final int SEMICOLON = 286;
  /** Token number, to be returned by the scanner.  */
  public static final int COMMA = 287;
  /** Token number, to be returned by the scanner.  */
  public static final int AND = 288;
  /** Token number, to be returned by the scanner.  */
  public static final int OR = 289;
  /** Token number, to be returned by the scanner.  */
  public static final int NOT = 290;
  /** Token number, to be returned by the scanner.  */
  public static final int IMPLIES = 291;
  /** Token number, to be returned by the scanner.  */
  public static final int FORALL = 292;
  /** Token number, to be returned by the scanner.  */
  public static final int EXISTS = 293;
  /** Token number, to be returned by the scanner.  */
  public static final int TRUE = 294;
  /** Token number, to be returned by the scanner.  */
  public static final int FALSE = 295;
  /** Token number, to be returned by the scanner.  */
  public static final int QUANTIFIER = 296;
  /** Token number, to be returned by the scanner.  */
  public static final int NEGATIVE = 297;



  

  /**
   * Communication interface between the scanner and the Bison-generated
   * parser <tt>YYParser</tt>.
   */
  public interface Lexer {
    

    /**
     * Method to retrieve the semantic value of the last scanned token.
     * @return the semantic value of the last scanned token.  */
    Object getLVal ();

    /**
     * Entry point for the scanner.  Returns the token identifier corresponding
     * to the next token and prepares to return the semantic value
     * of the token.
     * @return the token identifier corresponding to the next token. */
    int yylex () throws java.io.IOException;

    /**
     * Entry point for error reporting.  Emits an error
     * in a user-defined way.
     *
     * 
     * @param s The string for the error message.  */
     void yyerror (String s);
  }

  /** The object doing lexical analysis for us.  */
  private Lexer yylexer;
  
  



  /**
   * Instantiates the Bison-generated parser.
   * @param yylexer The scanner that will supply tokens to the parser.
   */
  public YYParser (Lexer yylexer) {
    this.yylexer = yylexer;
    
  }

  private java.io.PrintStream yyDebugStream = System.err;

  /**
   * Return the <tt>PrintStream</tt> on which the debugging output is
   * printed.
   */
  public final java.io.PrintStream getDebugStream () { return yyDebugStream; }

  /**
   * Set the <tt>PrintStream</tt> on which the debug output is printed.
   * @param s The stream that is used for debugging output.
   */
  public final void setDebugStream(java.io.PrintStream s) { yyDebugStream = s; }

  private int yydebug = 0;

  /**
   * Answer the verbosity of the debugging output; 0 means that all kinds of
   * output from the parser are suppressed.
   */
  public final int getDebugLevel() { return yydebug; }

  /**
   * Set the verbosity of the debugging output; 0 means that all kinds of
   * output from the parser are suppressed.
   * @param level The verbosity level for debugging output.
   */
  public final void setDebugLevel(int level) { yydebug = level; }

  private final int yylex () throws java.io.IOException {
    return yylexer.yylex ();
  }
  protected final void yyerror (String s) {
    yylexer.yyerror (s);
  }

  

  protected final void yycdebug (String s) {
    if (yydebug > 0)
      yyDebugStream.println (s);
  }

  private final class YYStack {
    private int[] stateStack = new int[16];
    
    private Object[] valueStack = new Object[16];

    public int size = 16;
    public int height = -1;

    public final void push (int state, Object value			    ) {
      height++;
      if (size == height)
        {
	  int[] newStateStack = new int[size * 2];
	  System.arraycopy (stateStack, 0, newStateStack, 0, height);
	  stateStack = newStateStack;
	  

	  Object[] newValueStack = new Object[size * 2];
	  System.arraycopy (valueStack, 0, newValueStack, 0, height);
	  valueStack = newValueStack;

	  size *= 2;
	}

      stateStack[height] = state;
      
      valueStack[height] = value;
    }

    public final void pop () {
      pop (1);
    }

    public final void pop (int num) {
      // Avoid memory leaks... garbage collection is a white lie!
      if (num > 0) {
	java.util.Arrays.fill (valueStack, height - num + 1, height + 1, null);
        
      }
      height -= num;
    }

    public final int stateAt (int i) {
      return stateStack[height - i];
    }

    public final Object valueAt (int i) {
      return valueStack[height - i];
    }

    // Print the state stack on the debug stream.
    public void print (java.io.PrintStream out)
    {
      out.print ("Stack now");

      for (int i = 0; i <= height; i++)
        {
	  out.print (' ');
	  out.print (stateStack[i]);
        }
      out.println ();
    }
  }

  /**
   * Returned by a Bison action in order to stop the parsing process and
   * return success (<tt>true</tt>).  */
  public static final int YYACCEPT = 0;

  /**
   * Returned by a Bison action in order to stop the parsing process and
   * return failure (<tt>false</tt>).  */
  public static final int YYABORT = 1;

  /**
   * Returned by a Bison action in order to start error recovery without
   * printing an error message.  */
  public static final int YYERROR = 2;

  // Internal return codes that are not supported for user semantic
  // actions.
  private static final int YYERRLAB = 3;
  private static final int YYNEWSTATE = 4;
  private static final int YYDEFAULT = 5;
  private static final int YYREDUCE = 6;
  private static final int YYERRLAB1 = 7;
  private static final int YYRETURN = 8;

  private int yyerrstatus_ = 0;

  /**
   * Return whether error recovery is being done.  In this state, the parser
   * reads token until it reaches a known state, and then restarts normal
   * operation.  */
  public final boolean recovering ()
  {
    return yyerrstatus_ == 0;
  }

  private int yyaction (int yyn, YYStack yystack, int yylen) 
  {
    Object yyval;
    

    /* If YYLEN is nonzero, implement the default value of the action:
       `$$ = $1'.  Otherwise, use the top of the stack.

       Otherwise, the following line sets YYVAL to garbage.
       This behavior is undocumented and Bison
       users should not rely upon it.  */
    if (yylen > 0)
      yyval = yystack.valueAt (yylen - 1);
    else
      yyval = yystack.valueAt (0);

    yy_reduce_print (yyn, yystack);

    switch (yyn)
      {
	  case 3:
  if (yyn == 3)
    
/* Line 351 of lalr1.java  */
/* Line 78 of "YYParser.y"  */
    { 
			System.out.println( ((yystack.valueAt (7-(1)))) );
		};
  break;
    

  case 4:
  if (yyn == 4)
    
/* Line 351 of lalr1.java  */
/* Line 81 of "YYParser.y"  */
    { System.out.println("Found: automaton"); };
  break;
    

  case 5:
  if (yyn == 5)
    
/* Line 351 of lalr1.java  */
/* Line 82 of "YYParser.y"  */
    { System.out.println("Found: modecheck");};
  break;
    

  case 6:
  if (yyn == 6)
    
/* Line 351 of lalr1.java  */
/* Line 83 of "YYParser.y"  */
    { System.out.println("Found: assignrandomplusvars");	};
  break;
    

  case 7:
  if (yyn == 7)
    
/* Line 351 of lalr1.java  */
/* Line 84 of "YYParser.y"  */
    { System.out.println("Found: updatevars");	};
  break;
    

  case 8:
  if (yyn == 8)
    
/* Line 351 of lalr1.java  */
/* Line 85 of "YYParser.y"  */
    { System.out.println("Found: modeswitch");};
  break;
    

  case 9:
  if (yyn == 9)
    
/* Line 351 of lalr1.java  */
/* Line 86 of "YYParser.y"  */
    { System.out.println("Found: odesystem");};
  break;
    

  case 10:
  if (yyn == 10)
    
/* Line 351 of lalr1.java  */
/* Line 87 of "YYParser.y"  */
    { System.out.println("Found: jumpcondition");};
  break;
    

  case 29:
  if (yyn == 29)
    
/* Line 351 of lalr1.java  */
/* Line 134 of "YYParser.y"  */
    { yyval = "(test " + (String)((yystack.valueAt (2-(2)))) + " )";				};
  break;
    

  case 30:
  if (yyn == 30)
    
/* Line 351 of lalr1.java  */
/* Line 135 of "YYParser.y"  */
    { yyval = (String)((yystack.valueAt (3-(2))));						};
  break;
    

  case 31:
  if (yyn == 31)
    
/* Line 351 of lalr1.java  */
/* Line 140 of "YYParser.y"  */
    { yyval = "(continuous " + "(odelist: " + (String)((yystack.valueAt (3-(2)))) + " )" + ", (domain: true )"; };
  break;
    

  case 32:
  if (yyn == 32)
    
/* Line 351 of lalr1.java  */
/* Line 142 of "YYParser.y"  */
    { yyval = "(continuous " + "(odelist: " + (String)((yystack.valueAt (5-(2)))) + " )" + ", (domain: " + (String)((yystack.valueAt (5-(4)))) + " )"; };
  break;
    

  case 33:
  if (yyn == 33)
    
/* Line 351 of lalr1.java  */
/* Line 145 of "YYParser.y"  */
    { yyval = (String)((yystack.valueAt (1-(1))));						};
  break;
    

  case 34:
  if (yyn == 34)
    
/* Line 351 of lalr1.java  */
/* Line 146 of "YYParser.y"  */
    { yyval = (String)((yystack.valueAt (3-(1)))) + ", " + (String)((yystack.valueAt (3-(3))));				};
  break;
    

  case 35:
  if (yyn == 35)
    
/* Line 351 of lalr1.java  */
/* Line 149 of "YYParser.y"  */
    { yyval = (String)((yystack.valueAt (4-(1)))) + "' = " + (String)((yystack.valueAt (4-(4))));			};
  break;
    

  case 37:
  if (yyn == 37)
    
/* Line 351 of lalr1.java  */
/* Line 154 of "YYParser.y"  */
    { 
			yyval = "true";
		};
  break;
    

  case 38:
  if (yyn == 38)
    
/* Line 351 of lalr1.java  */
/* Line 157 of "YYParser.y"  */
    { 
			yyval = "false";
		};
  break;
    

  case 39:
  if (yyn == 39)
    
/* Line 351 of lalr1.java  */
/* Line 160 of "YYParser.y"  */
    { 
		yyval = ((yystack.valueAt (1-(1)))); 							
		};
  break;
    

  case 40:
  if (yyn == 40)
    
/* Line 351 of lalr1.java  */
/* Line 163 of "YYParser.y"  */
    { yyval = "(and " + (String)((yystack.valueAt (3-(1)))) + ", " + (String)((yystack.valueAt (3-(3)))) + " )"; 	};
  break;
    

  case 41:
  if (yyn == 41)
    
/* Line 351 of lalr1.java  */
/* Line 164 of "YYParser.y"  */
    { yyval = "(or " + (String)((yystack.valueAt (3-(1)))) + ", " + (String)((yystack.valueAt (3-(3)))) + " )"; 		};
  break;
    

  case 42:
  if (yyn == 42)
    
/* Line 351 of lalr1.java  */
/* Line 165 of "YYParser.y"  */
    { yyval = "(not " + (String)((yystack.valueAt (2-(2)))) + " )"; 				};
  break;
    

  case 43:
  if (yyn == 43)
    
/* Line 351 of lalr1.java  */
/* Line 166 of "YYParser.y"  */
    { yyval = "( " + (String)((yystack.valueAt (3-(2)))) + ")"; 				};
  break;
    

  case 44:
  if (yyn == 44)
    
/* Line 351 of lalr1.java  */
/* Line 168 of "YYParser.y"  */
    { yyval = "(implies " + (String)((yystack.valueAt (3-(1)))) + ", " + (String)((yystack.valueAt (3-(3)))) + " )"; 	};
  break;
    

  case 45:
  if (yyn == 45)
    
/* Line 351 of lalr1.java  */
/* Line 170 of "YYParser.y"  */
    { yyval = "(forall " + (String)((yystack.valueAt (4-(2)))) + "; " + (String)((yystack.valueAt (4-(4)))) + " )";	};
  break;
    

  case 46:
  if (yyn == 46)
    
/* Line 351 of lalr1.java  */
/* Line 172 of "YYParser.y"  */
    { yyval = "(exists " + (String)((yystack.valueAt (4-(2)))) + "; " + (String)((yystack.valueAt (4-(4)))) + " )";	};
  break;
    

  case 47:
  if (yyn == 47)
    
/* Line 351 of lalr1.java  */
/* Line 175 of "YYParser.y"  */
    { 
		//this.parsedFormula = new dLFormula( (String)$2, );
		yyval = "(" + (String)((yystack.valueAt (3-(2)))) +" "+ (String)((yystack.valueAt (3-(1)))) + ", " + (String)((yystack.valueAt (3-(3)))) + ")"; 
	};
  break;
    

  case 48:
  if (yyn == 48)
    
/* Line 351 of lalr1.java  */
/* Line 183 of "YYParser.y"  */
    { 

		yyval = (String)((yystack.valueAt (1-(1))));
	};
  break;
    

  case 49:
  if (yyn == 49)
    
/* Line 351 of lalr1.java  */
/* Line 187 of "YYParser.y"  */
    {
		yyval = "(" + (String)((yystack.valueAt (4-(1)))) + " " + (String)((yystack.valueAt (4-(3)))) + ")";
	};
  break;
    

  case 50:
  if (yyn == 50)
    
/* Line 351 of lalr1.java  */
/* Line 190 of "YYParser.y"  */
    { 
		yyval = (String)((yystack.valueAt (1-(1))));
	};
  break;
    

  case 51:
  if (yyn == 51)
    
/* Line 351 of lalr1.java  */
/* Line 193 of "YYParser.y"  */
    { 
		yyval = (String)((yystack.valueAt (1-(1))));
	};
  break;
    

  case 52:
  if (yyn == 52)
    
/* Line 351 of lalr1.java  */
/* Line 196 of "YYParser.y"  */
    { 
		yyval = (String)((yystack.valueAt (1-(1))));
	};
  break;
    

  case 53:
  if (yyn == 53)
    
/* Line 351 of lalr1.java  */
/* Line 199 of "YYParser.y"  */
    { 
		yyval = "("+ (String)((yystack.valueAt (3-(2)))) +")"; 					
	};
  break;
    

  case 54:
  if (yyn == 54)
    
/* Line 351 of lalr1.java  */
/* Line 202 of "YYParser.y"  */
    { 
		yyval = "(+ " + (String)((yystack.valueAt (3-(1)))) + ", " + (String)((yystack.valueAt (3-(3))))+ " )";		
	};
  break;
    

  case 55:
  if (yyn == 55)
    
/* Line 351 of lalr1.java  */
/* Line 205 of "YYParser.y"  */
    { yyval = "(- " + (String)((yystack.valueAt (3-(1)))) + ", " + (String)((yystack.valueAt (3-(3)))) + ")";		};
  break;
    

  case 56:
  if (yyn == 56)
    
/* Line 351 of lalr1.java  */
/* Line 206 of "YYParser.y"  */
    { yyval = "(* " + (String)((yystack.valueAt (3-(1)))) + ", " + (String)((yystack.valueAt (3-(3)))) +")";		};
  break;
    

  case 57:
  if (yyn == 57)
    
/* Line 351 of lalr1.java  */
/* Line 207 of "YYParser.y"  */
    { yyval = "(/ " + (String)((yystack.valueAt (3-(1)))) + ", " + (String)((yystack.valueAt (3-(3)))) + ")";		};
  break;
    

  case 58:
  if (yyn == 58)
    
/* Line 351 of lalr1.java  */
/* Line 208 of "YYParser.y"  */
    { yyval = "(^ " + (String)((yystack.valueAt (3-(1)))) + ", " + (String)((yystack.valueAt (3-(3)))) + ")";		};
  break;
    

  case 59:
  if (yyn == 59)
    
/* Line 351 of lalr1.java  */
/* Line 209 of "YYParser.y"  */
    { yyval = "(- 0, " + (String)((yystack.valueAt (2-(2))));					};
  break;
    

  case 60:
  if (yyn == 60)
    
/* Line 351 of lalr1.java  */
/* Line 213 of "YYParser.y"  */
    { yyval = (String)((yystack.valueAt (1-(1)))); System.out.println(" found arglist");					};
  break;
    

  case 61:
  if (yyn == 61)
    
/* Line 351 of lalr1.java  */
/* Line 214 of "YYParser.y"  */
    { yyval = (String)((yystack.valueAt (1-(1)))); System.out.println(" found arglist");					};
  break;
    

  case 62:
  if (yyn == 62)
    
/* Line 351 of lalr1.java  */
/* Line 215 of "YYParser.y"  */
    { yyval = (String)((yystack.valueAt (1-(1)))); System.out.println(" found arglist");					};
  break;
    

  case 63:
  if (yyn == 63)
    
/* Line 351 of lalr1.java  */
/* Line 216 of "YYParser.y"  */
    { yyval = (String)((yystack.valueAt (3-(1)))) + ", " + (String)((yystack.valueAt (3-(3)))); System.out.println("found arglist, lots");		};
  break;
    



/* Line 351 of lalr1.java  */
/* Line 763 of "YYParser.java"  */
	default: break;
      }

    yy_symbol_print ("-> $$ =", yyr1_[yyn], yyval);

    yystack.pop (yylen);
    yylen = 0;

    /* Shift the result of the reduction.  */
    yyn = yyr1_[yyn];
    int yystate = yypgoto_[yyn - yyntokens_] + yystack.stateAt (0);
    if (0 <= yystate && yystate <= yylast_
	&& yycheck_[yystate] == yystack.stateAt (0))
      yystate = yytable_[yystate];
    else
      yystate = yydefgoto_[yyn - yyntokens_];

    yystack.push (yystate, yyval);
    return YYNEWSTATE;
  }

  /* Return YYSTR after stripping away unnecessary quotes and
     backslashes, so that it's suitable for yyerror.  The heuristic is
     that double-quoting is unnecessary unless the string contains an
     apostrophe, a comma, or backslash (other than backslash-backslash).
     YYSTR is taken from yytname.  */
  private final String yytnamerr_ (String yystr)
  {
    if (yystr.charAt (0) == '"')
      {
        StringBuffer yyr = new StringBuffer ();
        strip_quotes: for (int i = 1; i < yystr.length (); i++)
          switch (yystr.charAt (i))
            {
            case '\'':
            case ',':
              break strip_quotes;

            case '\\':
	      if (yystr.charAt(++i) != '\\')
                break strip_quotes;
              /* Fall through.  */
            default:
              yyr.append (yystr.charAt (i));
              break;

            case '"':
              return yyr.toString ();
            }
      }
    else if (yystr.equals ("$end"))
      return "end of input";

    return yystr;
  }

  /*--------------------------------.
  | Print this symbol on YYOUTPUT.  |
  `--------------------------------*/

  private void yy_symbol_print (String s, int yytype,
			         Object yyvaluep				 )
  {
    if (yydebug > 0)
    yycdebug (s + (yytype < yyntokens_ ? " token " : " nterm ")
	      + yytname_[yytype] + " ("
	      + (yyvaluep == null ? "(null)" : yyvaluep.toString ()) + ")");
  }

  /**
   * Parse input from the scanner that was specified at object construction
   * time.  Return whether the end of the input was reached successfully.
   *
   * @return <tt>true</tt> if the parsing succeeds.  Note that this does not
   *          imply that there were no syntax errors.
   */
  public boolean parse () throws java.io.IOException
  {
    /// Lookahead and lookahead in internal form.
    int yychar = yyempty_;
    int yytoken = 0;

    /* State.  */
    int yyn = 0;
    int yylen = 0;
    int yystate = 0;

    YYStack yystack = new YYStack ();

    /* Error handling.  */
    int yynerrs_ = 0;
    

    /// Semantic value of the lookahead.
    Object yylval = null;

    int yyresult;

    yycdebug ("Starting parse\n");
    yyerrstatus_ = 0;


    /* Initialize the stack.  */
    yystack.push (yystate, yylval);

    int label = YYNEWSTATE;
    for (;;)
      switch (label)
      {
        /* New state.  Unlike in the C/C++ skeletons, the state is already
	   pushed when we come here.  */
      case YYNEWSTATE:
        yycdebug ("Entering state " + yystate + "\n");
        if (yydebug > 0)
          yystack.print (yyDebugStream);

        /* Accept?  */
        if (yystate == yyfinal_)
          return true;

        /* Take a decision.  First try without lookahead.  */
        yyn = yypact_[yystate];
        if (yy_pact_value_is_default_ (yyn))
          {
            label = YYDEFAULT;
	    break;
          }

        /* Read a lookahead token.  */
        if (yychar == yyempty_)
          {
	    yycdebug ("Reading a token: ");
	    yychar = yylex ();
            
            yylval = yylexer.getLVal ();
          }

        /* Convert token to internal form.  */
        if (yychar <= EOF)
          {
	    yychar = yytoken = EOF;
	    yycdebug ("Now at end of input.\n");
          }
        else
          {
	    yytoken = yytranslate_ (yychar);
	    yy_symbol_print ("Next token is", yytoken,
			     yylval);
          }

        /* If the proper action on seeing token YYTOKEN is to reduce or to
           detect an error, take that action.  */
        yyn += yytoken;
        if (yyn < 0 || yylast_ < yyn || yycheck_[yyn] != yytoken)
          label = YYDEFAULT;

        /* <= 0 means reduce or error.  */
        else if ((yyn = yytable_[yyn]) <= 0)
          {
	    if (yy_table_value_is_error_ (yyn))
	      label = YYERRLAB;
	    else
	      {
	        yyn = -yyn;
	        label = YYREDUCE;
	      }
          }

        else
          {
            /* Shift the lookahead token.  */
	    yy_symbol_print ("Shifting", yytoken,
			     yylval);

            /* Discard the token being shifted.  */
            yychar = yyempty_;

            /* Count tokens shifted since error; after three, turn off error
               status.  */
            if (yyerrstatus_ > 0)
              --yyerrstatus_;

            yystate = yyn;
            yystack.push (yystate, yylval);
            label = YYNEWSTATE;
          }
        break;

      /*-----------------------------------------------------------.
      | yydefault -- do the default action for the current state.  |
      `-----------------------------------------------------------*/
      case YYDEFAULT:
        yyn = yydefact_[yystate];
        if (yyn == 0)
          label = YYERRLAB;
        else
          label = YYREDUCE;
        break;

      /*-----------------------------.
      | yyreduce -- Do a reduction.  |
      `-----------------------------*/
      case YYREDUCE:
        yylen = yyr2_[yyn];
        label = yyaction (yyn, yystack, yylen);
	yystate = yystack.stateAt (0);
        break;

      /*------------------------------------.
      | yyerrlab -- here on detecting error |
      `------------------------------------*/
      case YYERRLAB:
        /* If not already recovering from an error, report this error.  */
        if (yyerrstatus_ == 0)
          {
            ++yynerrs_;
            if (yychar == yyempty_)
              yytoken = yyempty_;
            yyerror (yysyntax_error (yystate, yytoken));
          }

        
        if (yyerrstatus_ == 3)
          {
	    /* If just tried and failed to reuse lookahead token after an
	     error, discard it.  */

	    if (yychar <= EOF)
	      {
	      /* Return failure if at end of input.  */
	      if (yychar == EOF)
	        return false;
	      }
	    else
	      yychar = yyempty_;
          }

        /* Else will try to reuse lookahead token after shifting the error
           token.  */
        label = YYERRLAB1;
        break;

      /*---------------------------------------------------.
      | errorlab -- error raised explicitly by YYERROR.  |
      `---------------------------------------------------*/
      case YYERROR:

        
        /* Do not reclaim the symbols of the rule which action triggered
           this YYERROR.  */
        yystack.pop (yylen);
        yylen = 0;
        yystate = yystack.stateAt (0);
        label = YYERRLAB1;
        break;

      /*-------------------------------------------------------------.
      | yyerrlab1 -- common code for both syntax error and YYERROR.  |
      `-------------------------------------------------------------*/
      case YYERRLAB1:
        yyerrstatus_ = 3;	/* Each real token shifted decrements this.  */

        for (;;)
          {
	    yyn = yypact_[yystate];
	    if (!yy_pact_value_is_default_ (yyn))
	      {
	        yyn += yyterror_;
	        if (0 <= yyn && yyn <= yylast_ && yycheck_[yyn] == yyterror_)
	          {
	            yyn = yytable_[yyn];
	            if (0 < yyn)
		      break;
	          }
	      }

	    /* Pop the current state because it cannot handle the error token.  */
	    if (yystack.height == 1)
	      return false;

	    
	    yystack.pop ();
	    yystate = yystack.stateAt (0);
	    if (yydebug > 0)
	      yystack.print (yyDebugStream);
          }

	

        /* Shift the error token.  */
        yy_symbol_print ("Shifting", yystos_[yyn],
			 yylval);

        yystate = yyn;
	yystack.push (yyn, yylval);
        label = YYNEWSTATE;
        break;

        /* Accept.  */
      case YYACCEPT:
        return true;

        /* Abort.  */
      case YYABORT:
        return false;
      }
  }

  // Generate an error message.
  private String yysyntax_error (int yystate, int tok)
  {
    if (errorVerbose)
      {
        /* There are many possibilities here to consider:
           - Assume YYFAIL is not used.  It's too flawed to consider.
             See
             <http://lists.gnu.org/archive/html/bison-patches/2009-12/msg00024.html>
             for details.  YYERROR is fine as it does not invoke this
             function.
           - If this state is a consistent state with a default action,
             then the only way this function was invoked is if the
             default action is an error action.  In that case, don't
             check for expected tokens because there are none.
           - The only way there can be no lookahead present (in tok) is
             if this state is a consistent state with a default action.
             Thus, detecting the absence of a lookahead is sufficient to
             determine that there is no unexpected or expected token to
             report.  In that case, just report a simple "syntax error".
           - Don't assume there isn't a lookahead just because this
             state is a consistent state with a default action.  There
             might have been a previous inconsistent state, consistent
             state with a non-default action, or user semantic action
             that manipulated yychar.  (However, yychar is currently out
             of scope during semantic actions.)
           - Of course, the expected token list depends on states to
             have correct lookahead information, and it depends on the
             parser not to perform extra reductions after fetching a
             lookahead from the scanner and before detecting a syntax
             error.  Thus, state merging (from LALR or IELR) and default
             reductions corrupt the expected token list.  However, the
             list is correct for canonical LR with one exception: it
             will still contain any token that will not be accepted due
             to an error action in a later state.
        */
        if (tok != yyempty_)
          {
            // FIXME: This method of building the message is not compatible
            // with internationalization.
            StringBuffer res =
              new StringBuffer ("syntax error, unexpected ");
            res.append (yytnamerr_ (yytname_[tok]));
            int yyn = yypact_[yystate];
            if (!yy_pact_value_is_default_ (yyn))
              {
                /* Start YYX at -YYN if negative to avoid negative
                   indexes in YYCHECK.  In other words, skip the first
                   -YYN actions for this state because they are default
                   actions.  */
                int yyxbegin = yyn < 0 ? -yyn : 0;
                /* Stay within bounds of both yycheck and yytname.  */
                int yychecklim = yylast_ - yyn + 1;
                int yyxend = yychecklim < yyntokens_ ? yychecklim : yyntokens_;
                int count = 0;
                for (int x = yyxbegin; x < yyxend; ++x)
                  if (yycheck_[x + yyn] == x && x != yyterror_
                      && !yy_table_value_is_error_ (yytable_[x + yyn]))
                    ++count;
                if (count < 5)
                  {
                    count = 0;
                    for (int x = yyxbegin; x < yyxend; ++x)
                      if (yycheck_[x + yyn] == x && x != yyterror_
                          && !yy_table_value_is_error_ (yytable_[x + yyn]))
                        {
                          res.append (count++ == 0 ? ", expecting " : " or ");
                          res.append (yytnamerr_ (yytname_[x]));
                        }
                  }
              }
            return res.toString ();
          }
      }

    return "syntax error";
  }

  /**
   * Whether the given <code>yypact_</code> value indicates a defaulted state.
   * @param yyvalue   the value to check
   */
  private static boolean yy_pact_value_is_default_ (int yyvalue)
  {
    return yyvalue == yypact_ninf_;
  }

  /**
   * Whether the given <code>yytable_</code> value indicates a syntax error.
   * @param yyvalue   the value to check
   */
  private static boolean yy_table_value_is_error_ (int yyvalue)
  {
    return yyvalue == yytable_ninf_;
  }

  /* YYPACT[STATE-NUM] -- Index in YYTABLE of the portion describing
     STATE-NUM.  */
  private static final short yypact_ninf_ = -19;
  private static final short yypact_[] =
  {
        18,     1,   135,    19,    28,    42,   -19,   -14,    75,    63,
     147,    26,    27,   -19,   -19,    64,    58,   -19,    40,    49,
     -19,    65,   -19,   -19,   -19,   -19,   144,   -19,   183,   101,
      22,   -19,   -19,   -19,   123,   179,   105,    99,   106,    88,
      75,    94,   114,   172,   186,   188,   126,   138,    78,   167,
     147,   -19,   100,   102,   -19,     4,     6,   160,   163,   147,
     147,    97,    75,    75,    75,    75,    75,    75,   181,   -19,
     147,     1,   202,   147,   -19,   -19,   -19,   -19,   -19,   -19,
     -18,   175,   -19,   -19,   -19,   -19,   -19,   -19,   -19,   -19,
     147,   147,   154,     4,   -19,    40,   -19,    42,   -12,   191,
     -19,   211,   220,   187,   187,     4,   179,    94,   103,   103,
      94,    94,   125,    75,    33,   -19,   177,   -19,    88,   -19,
     -19,   208,    -3,   204,   207,    20,   125,   -19,   197,   196,
     147,    15,   198,   -19,   -19,   217,   -19,    14,   147,    28,
      14,   200,   179,    68,   -19
  };

  /* YYDEFACT[S] -- default reduction number in state S.  Performed when
     YYTABLE doesn't specify something else to do.  Zero means the
     default is an error.  */
  private static final byte yydefact_[] =
  {
        36,     0,    36,     0,    51,    52,    48,    50,     0,    36,
      36,     0,     0,    37,    38,     0,     4,    12,     5,     6,
      10,     7,     8,    11,    22,     9,     0,    39,     0,     0,
       0,    33,    51,    52,    36,    29,     0,     0,     0,     0,
       0,    59,     0,     0,     0,     0,     0,     0,     0,     0,
      36,    42,     0,     0,     1,     0,     0,     0,     0,    36,
      36,    36,     0,     0,     0,     0,     0,     0,     0,    31,
      36,     0,     0,    36,    26,    23,    19,    61,    62,    60,
       0,     0,    16,    18,    21,    25,    28,    30,    43,    53,
      36,    36,     0,     0,    14,     0,    13,     0,     0,     0,
      27,     0,     0,    40,    41,     0,    44,    56,    54,    55,
      57,    58,    47,     0,     0,    34,     0,    49,     0,    45,
      46,     0,     0,     0,     0,     0,    35,    32,     0,    63,
      36,     0,     0,    20,    24,     0,    17,     0,    36,     0,
       0,     0,     3,     0,    15
  };

  /* YYPGOTO[NTERM-NUM].  */
  private static final short yypgoto_[] =
  {
       -19,   -19,   127,    -5,     8,     9,   111,     3,    91,    -4,
      -8,   180,   -19,   164,     0,   -19,    12,   119
  };

  /* YYDEFGOTO[NTERM-NUM].  */
  private static final byte
  yydefgoto_[] =
  {
        -1,    15,    16,    17,    43,    44,    20,    45,    22,    23,
      24,    25,    30,    31,    48,    27,    28,    80
  };

  /* YYTABLE[YYPACT[STATE-NUM]].  What to do in state STATE-NUM.  If
     positive, shift that token.  If negative, reduce the rule which
     number is the opposite.  If YYTABLE_NINF_, syntax error.  */
  private static final short yytable_ninf_ = -3;
  private static final short
  yytable_[] =
  {
        26,    47,    35,    21,    42,    46,   130,    97,    18,    19,
      51,     1,   117,    92,   118,    39,   101,    98,    -2,    29,
      41,    49,    36,     1,   130,    97,   131,     2,    69,    70,
      55,    37,   139,    93,     3,    98,     4,     5,     6,   127,
       7,   135,     8,   140,   131,    38,    49,     9,    52,    53,
      94,    96,    81,    10,    71,    11,    12,    13,    14,   103,
     104,   106,    49,    95,    54,    99,    59,    60,    55,    73,
     114,    56,     2,   106,   107,   108,   109,   110,   111,   112,
      57,     4,     5,     6,     3,     7,   102,     8,    42,    46,
     119,   120,     9,    32,    33,     6,    58,     7,    10,     8,
      11,    12,    13,    14,    40,    68,    77,    78,    88,   105,
      79,    59,    60,    95,    73,    32,    33,     6,    75,     7,
      66,     8,    74,    47,    62,   126,    50,    76,    65,    66,
      35,    90,    10,    91,    11,    12,    13,    14,   142,    72,
     141,    32,    33,     6,    82,     7,    62,     8,    63,    64,
      65,    66,    50,    32,    33,     6,    86,     7,    10,     8,
      11,    12,    13,    14,    34,    32,    33,     6,    87,     7,
      10,     8,    11,    12,    13,    14,    50,    59,    60,   101,
      61,   102,    10,   121,    11,    12,    13,    14,    62,   113,
      63,    64,    65,    66,   128,    67,    62,    89,    63,    64,
      65,    66,    83,    56,    62,    89,    63,    64,    65,    66,
     116,    67,    59,    60,   123,    73,    84,    57,    85,    58,
      59,    60,   122,   124,    72,   133,   134,   136,   118,   137,
     138,   143,   125,   132,   144,   115,   100,   129
  };

  /* YYCHECK.  */
  private static final short
  yycheck_[] =
  {
         0,     9,     2,     0,     9,     9,     9,    19,     0,     0,
      10,     5,    30,     9,    32,    29,    19,    29,     0,    18,
       8,     9,     3,     5,     9,    19,    29,     9,     6,     7,
      10,     3,    18,    29,    16,    29,    18,    19,    20,     6,
      22,    21,    24,    29,    29,     3,    34,    29,    22,    22,
      55,    55,    40,    35,    32,    37,    38,    39,    40,    59,
      60,    61,    50,    55,     0,    56,    33,    34,    10,    36,
      70,    31,     9,    73,    62,    63,    64,    65,    66,    67,
      31,    18,    19,    20,    16,    22,    18,    24,    93,    93,
      90,    91,    29,    18,    19,    20,    31,    22,    35,    24,
      37,    38,    39,    40,    29,     4,    18,    19,    30,    12,
      22,    33,    34,   105,    36,    18,    19,    20,    19,    22,
      26,    24,    17,   131,    21,   113,    29,    21,    25,    26,
     130,    31,    35,    31,    37,    38,    39,    40,   138,    16,
     137,    18,    19,    20,    30,    22,    21,    24,    23,    24,
      25,    26,    29,    18,    19,    20,    30,    22,    35,    24,
      37,    38,    39,    40,    29,    18,    19,    20,    30,    22,
      35,    24,    37,    38,    39,    40,    29,    33,    34,    19,
      36,    18,    35,    29,    37,    38,    39,    40,    21,     8,
      23,    24,    25,    26,    17,    28,    21,    30,    23,    24,
      25,    26,    30,    31,    21,    30,    23,    24,    25,    26,
       8,    28,    33,    34,     3,    36,    30,    31,    30,    31,
      33,    34,    31,     3,    16,    21,    19,    30,    32,    31,
      13,    31,   105,   122,   143,    71,    56,   118
  };

  /* STOS_[STATE-NUM] -- The (internal number of the) accessing
     symbol of state STATE-NUM.  */
  private static final byte
  yystos_[] =
  {
         0,     5,     9,    16,    18,    19,    20,    22,    24,    29,
      35,    37,    38,    39,    40,    44,    45,    46,    47,    48,
      49,    50,    51,    52,    53,    54,    57,    58,    59,    18,
      55,    56,    18,    19,    29,    57,     3,     3,     3,    29,
      29,    59,    46,    47,    48,    50,    52,    53,    57,    59,
      29,    57,    22,    22,     0,    10,    31,    31,    31,    33,
      34,    36,    21,    23,    24,    25,    26,    28,     4,     6,
       7,    32,    16,    36,    17,    19,    21,    18,    19,    22,
      60,    59,    30,    30,    30,    30,    30,    30,    30,    30,
      31,    31,     9,    29,    46,    47,    52,    19,    29,    48,
      54,    19,    18,    57,    57,    12,    57,    59,    59,    59,
      59,    59,    59,     8,    57,    56,     8,    30,    32,    57,
      57,    29,    31,     3,     3,    45,    59,     6,    17,    60,
       9,    29,    49,    21,    19,    21,    30,    31,    13,    18,
      29,    50,    57,    31,    51
  };

  /* TOKEN_NUMBER_[YYLEX-NUM] -- Internal symbol number corresponding
     to YYLEX-NUM.  */
  private static final short
  yytoken_number_[] =
  {
         0,   256,   257,   258,   259,   260,   261,   262,   263,   264,
     265,   266,   267,   268,   269,   270,   271,   272,   273,   274,
     275,   276,   277,   278,   279,   280,   281,   282,   283,   284,
     285,   286,   287,   288,   289,   290,   291,   292,   293,   294,
     295,   296,   297
  };

  /* YYR1[YYN] -- Symbol number of symbol that rule YYN derives.  */
  private static final byte
  yyr1_[] =
  {
         0,    43,    44,    44,    44,    44,    44,    44,    44,    44,
      44,    45,    45,    45,    45,    46,    46,    47,    47,    48,
      48,    48,    49,    50,    50,    50,    51,    52,    52,    53,
      53,    54,    54,    55,    55,    56,    57,    57,    57,    57,
      57,    57,    57,    57,    57,    57,    57,    58,    59,    59,
      59,    59,    59,    59,    59,    59,    59,    59,    59,    59,
      60,    60,    60,    60
  };

  /* YYR2[YYN] -- Number of symbols composing right hand side of rule YYN.  */
  private static final byte
  yyr2_[] =
  {
         0,     2,     0,     7,     1,     1,     1,     1,     1,     1,
       1,     1,     1,     3,     3,     9,     3,     6,     3,     3,
       5,     3,     1,     3,     5,     3,     3,     3,     3,     2,
       3,     3,     5,     1,     3,     4,     0,     1,     1,     1,
       3,     3,     2,     3,     3,     4,     4,     3,     1,     4,
       1,     1,     1,     3,     3,     3,     3,     3,     3,     2,
       1,     1,     1,     3
  };

  /* YYTNAME[SYMBOL-NUM] -- String name of the symbol SYMBOL-NUM.
     First, the terminals, then, starting at \a yyntokens_, nonterminals.  */
  private static final String yytname_[] =
  {
    "$end", "error", "$undefined", "ASSIGN", "PRIME", "OPENBRACE",
  "CLOSEBRACE", "RESTRICTDOMAIN", "EQUALS", "TEST", "CUP", "RANDOM",
  "OPENBOX", "CLOSEBOX", "OPENDIAMOND", "CLOSEDIAMOND", "MODEVAR",
  "MODEID", "STATEVAR", "STATEVARPLUS", "NUMBER", "ASTERISK", "IDENTIFIER",
  "PLUS", "MINUS", "DIVIDE", "POWER", "NEWLINE", "INEQUALITY", "LPAREN",
  "RPAREN", "SEMICOLON", "COMMA", "AND", "OR", "NOT", "IMPLIES", "FORALL",
  "EXISTS", "TRUE", "FALSE", "QUANTIFIER", "NEGATIVE", "$accept", "input",
  "automaton", "edge", "modecheck", "assignrandomplusvars",
  "jumpcondition", "updatevars", "modeswitch", "mode", "test", "odesystem",
  "odelist", "ode", "folformula", "comparison", "term", "argumentlist", null
  };

  /* YYRHS -- A `-1'-separated list of the rules' RHS.  */
  private static final byte yyrhs_[] =
  {
        44,     0,    -1,    -1,    57,    36,    12,    45,    21,    13,
      57,    -1,    45,    -1,    47,    -1,    48,    -1,    50,    -1,
      51,    -1,    54,    -1,    49,    -1,    52,    -1,    46,    -1,
      45,    10,    52,    -1,    45,    10,    46,    -1,    47,    31,
      48,    31,    49,    31,    50,    31,    51,    -1,    29,    46,
      30,    -1,     9,    29,    16,     8,    17,    30,    -1,    29,
      47,    30,    -1,    19,     3,    21,    -1,    48,    31,    19,
       3,    21,    -1,    29,    48,    30,    -1,    53,    -1,    18,
       3,    19,    -1,    50,    31,    18,     3,    19,    -1,    29,
      50,    30,    -1,    16,     3,    17,    -1,    47,    31,    54,
      -1,    29,    52,    30,    -1,     9,    57,    -1,    29,    53,
      30,    -1,     5,    55,     6,    -1,     5,    55,     7,    57,
       6,    -1,    56,    -1,    55,    32,    56,    -1,    18,     4,
       8,    59,    -1,    -1,    39,    -1,    40,    -1,    58,    -1,
      57,    33,    57,    -1,    57,    34,    57,    -1,    35,    57,
      -1,    29,    57,    30,    -1,    57,    36,    57,    -1,    37,
      22,    31,    57,    -1,    38,    22,    31,    57,    -1,    59,
      28,    59,    -1,    20,    -1,    22,    29,    60,    30,    -1,
      22,    -1,    18,    -1,    19,    -1,    29,    59,    30,    -1,
      59,    23,    59,    -1,    59,    24,    59,    -1,    59,    21,
      59,    -1,    59,    25,    59,    -1,    59,    26,    59,    -1,
      24,    59,    -1,    22,    -1,    18,    -1,    19,    -1,    60,
      32,    60,    -1
  };

  /* YYPRHS[YYN] -- Index of the first RHS symbol of rule number YYN in
     YYRHS.  */
  private static final short yyprhs_[] =
  {
         0,     0,     3,     4,    12,    14,    16,    18,    20,    22,
      24,    26,    28,    30,    34,    38,    48,    52,    59,    63,
      67,    73,    77,    79,    83,    89,    93,    97,   101,   105,
     108,   112,   116,   122,   124,   128,   133,   134,   136,   138,
     140,   144,   148,   151,   155,   159,   164,   169,   173,   175,
     180,   182,   184,   186,   190,   194,   198,   202,   206,   210,
     213,   215,   217,   219
  };

  /* YYRLINE[YYN] -- Source line where rule number YYN was defined.  */
  private static final short yyrline_[] =
  {
         0,    75,    75,    77,    81,    82,    83,    84,    85,    86,
      87,    92,    93,    94,    95,    99,   100,   104,   105,   109,
     110,   111,   116,   119,   120,   121,   125,   129,   130,   134,
     135,   139,   141,   145,   146,   149,   153,   154,   157,   160,
     163,   164,   165,   166,   167,   169,   171,   175,   183,   187,
     190,   193,   196,   199,   202,   205,   206,   207,   208,   209,
     213,   214,   215,   216
  };

  // Report on the debug stream that the rule yyrule is going to be reduced.
  private void yy_reduce_print (int yyrule, YYStack yystack)
  {
    if (yydebug == 0)
      return;

    int yylno = yyrline_[yyrule];
    int yynrhs = yyr2_[yyrule];
    /* Print the symbols being reduced, and their result.  */
    yycdebug ("Reducing stack by rule " + (yyrule - 1)
	      + " (line " + yylno + "), ");

    /* The symbols being reduced.  */
    for (int yyi = 0; yyi < yynrhs; yyi++)
      yy_symbol_print ("   $" + (yyi + 1) + " =",
		       yyrhs_[yyprhs_[yyrule] + yyi],
		       ((yystack.valueAt (yynrhs-(yyi + 1)))));
  }

  /* YYTRANSLATE(YYLEX) -- Bison symbol number corresponding to YYLEX.  */
  private static final byte yytranslate_table_[] =
  {
         0,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     1,     2,     3,     4,
       5,     6,     7,     8,     9,    10,    11,    12,    13,    14,
      15,    16,    17,    18,    19,    20,    21,    22,    23,    24,
      25,    26,    27,    28,    29,    30,    31,    32,    33,    34,
      35,    36,    37,    38,    39,    40,    41,    42
  };

  private static final byte yytranslate_ (int t)
  {
    if (t >= 0 && t <= yyuser_token_number_max_)
      return yytranslate_table_[t];
    else
      return yyundef_token_;
  }

  private static final int yylast_ = 237;
  private static final int yynnts_ = 18;
  private static final int yyempty_ = -2;
  private static final int yyfinal_ = 54;
  private static final int yyterror_ = 1;
  private static final int yyerrcode_ = 256;
  private static final int yyntokens_ = 43;

  private static final int yyuser_token_number_max_ = 297;
  private static final int yyundef_token_ = 2;

/* User implementation code.  */

}


/* Line 931 of lalr1.java  */
/* Line 218 of "YYParser.y"  */





