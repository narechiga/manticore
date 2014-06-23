
%{
	import java.util.*;
	import manticore.dl.*;

	@SuppressWarnings({"unchecked"})
%}

%language "Java"
%define extends {dLParser}

%token EXTERNAL
%token FUNCTIONS
%token RULES
%token SCHEMAVARIABLES
%token SCHEMATEXT
%token PROBLEM
%token ANNOTATION

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

%type <String> input fullblock problemblock schemavarsblock rulesblock schematext varblock funblock functiondeclaration argumentdeclaration annotationblock
%type <ArrayList<String>> vardeclaration varlist varinitlist
%type <dLStructure> dLformula assignment test ode comparison
%type <Term> term
%type <HybridProgram> hybridprogram
%type <ContinuousProgram> odesystem
%type <ArrayList<dLStructure>> odelist annotationlist
%type <ArrayList<Term>> argumentlist

%type <String> EXTERNAL FUNCTIONS RULES SCHEMAVARIABLES SCHEMATEXT PROBLEM ASSIGN PRIME OPENBRACE CLOSEBRACE EQUALS TEST CUP RANDOM REALDECLARATION OPENBOX CLOSEBOX OPENDIAMOND CLOSEDIAMOND NUMBER IDENTIFIER PLUS MINUS MULTIPLY DIVIDE POWER NEWLINE INEQUALITY LPAREN RPAREN SEMICOLON COMMA AND OR NOT IMPLIES IFF FORALL EXISTS TRUE FALSE 

%%
input: 
	fullblock { 
		try {
			$$ = (String)$1; System.out.println("full block"); //System.out.println($$); 
		} catch ( Exception e ) {
			System.err.println("Exception at location input:fullblock");
			System.err.println( e );
		}
	}
	| funblock { 
		try {
			$$ = (String)$1; System.out.println("function block"); //System.out.println($$); 
		} catch ( Exception e ) {
			System.err.println("Exception at location input:funblock");
			System.err.println( e );
		}
	}
	| varblock { 
		try {
			$$ = (String)$1; System.out.println("variable declaration block"); //System.out.println($$); 
		} catch ( Exception e ) {
			System.err.println("Exception at location input:varblock");
			System.err.println( e );
		}
	}
	| schemavarsblock { 
		try {
			$$ = (String)$1; System.out.println("schema variables block"); //System.out.println($$); 
		} catch ( Exception e ) {
			System.err.println("Exception at location input:schemavarsblock");
			System.err.println( e );
		}
			
	}
	| rulesblock { 
		try {
			$$ = (String)$1; System.out.println("rules block"); //System.out.println($$); 
		} catch ( Exception e ) {
			System.err.println("Exception at location input:rulesblock");
			System.err.println( e );
		}
	}
	| dLformula { 
		try {
			System.out.println("Found: dLformula"); 
			parsedStructure = (dLStructure)$1;	
		} catch ( Exception e ) {
			System.err.println("Exception at location input:dLformula");
			System.err.println( e );
		}
	}
	| hybridprogram {
		try {
			HybridProgram hp = (HybridProgram)$1;
			$$ = "hybrid program"; 
			parsedStructure = (HybridProgram)$1;	
		} catch ( Exception e ) {
			System.err.println("Exception at location input:hybridprogram");
			System.err.println( e );
		}
	}
	| annotationblock {
		try {
			$$ = (String)$1; System.out.println("annotation block"); //System.out.println($$); 
		} catch ( Exception e ) {
			System.err.println("Exception at location input:annotationblock");
			System.err.println( e );
		}
	} 
	| error {
		System.err.println("Parser: I'm confused, throwing error");
	}
;

fullblock:
	problemblock { 
		try{
			$$ = (String)$1;
		} catch ( Exception e ) {
			System.err.println("Exception at location fullblock:problemblock");
			System.err.println( e );
		}
	}
	| funblock problemblock { 
		try{
			$$ = (String)$1 + (String)$2;
		} catch ( Exception e ) {
			System.err.println("Exception at location fullblock:funblock problemblock");
			System.err.println( e );
		}
	}
	| funblock schemavarsblock rulesblock problemblock { 
		try{
			$$ = (String)$1 + (String)$2 + (String)$3 + (String)$4;
		} catch ( Exception e ) {
			System.err.println("Exception at location fullblock:funblock schemavarsblock rulesblock problemblock");
			System.err.println( e );
		}
	}
	| annotationblock fullblock {
		try{
			$$ = (String)$1 + (String)$2;
		} catch ( Exception e ) {
			System.err.println("Exception at location annotationblock:fullblock");
			System.err.println( e );
		}
	}
;

problemblock:
	PROBLEM OPENBRACE dLformula CLOSEBRACE { 
		try {
			parsedStructure = (dLStructure)$3;
			$$ = "{\n" + ((dLStructure)$3).toString() + "\n}"; System.out.println( $$ );
		} catch ( Exception e ) {
			System.err.println("Exception at location problemblock:PROBLEM OPENBRACE dLformula CLOSEBRACE");
			System.err.println( e );
		}
	}
	| PROBLEM OPENBRACE varblock dLformula CLOSEBRACE { 
		try {
			parsedStructure = (dLStructure)$4;
			$$ = "{\n" + (String)$3 + "\n" + ((dLStructure)$4).toString() + "\n}"; System.out.println($$);
		} catch ( Exception e ) {
			System.err.println("Exception at location problemblock:PROBLEM OPENBRACE varblock dLformula CLOSEBRACE");
			System.err.println( e );
		}
	}
;


/*==================== Annotations ====================*/
annotationblock:
	ANNOTATION OPENBRACE annotationlist CLOSEBRACE {
		try {
			this.annotations = (ArrayList<dLStructure>)$3;
		} catch ( Exception e ) {
			System.err.println("Exception at location annotationblock:ANNOTATION OPENBRACE annotationlist CLOSEBRACE");
			System.err.println( e );
		}
	}
;

annotationlist:
	dLformula SEMICOLON {
		try {
			ArrayList<dLStructure> annot = new ArrayList<dLStructure>();
			annot.add( (dLStructure)$1 );
			$$ = annot;
		} catch ( Exception e ) {
			System.err.println("Exception at location annotationlist:dLformula SEMICOLON");
			System.err.println( e );
		}
	}
	| annotationlist dLformula SEMICOLON {
		try {
			ArrayList<dLStructure> annot = new ArrayList<dLStructure>();
			annot.addAll( (ArrayList<dLStructure>)$1 );
			annot.add( (dLStructure)$2 );
			$$ = annot;
		} catch ( Exception e ) {
			System.err.println("Exception at location annotationlist:annotationlist dLformula SEMICOLON");
			System.err.println( e );
		}

	}
;


/*============================================================*/

/*==================== Schema rules and variables ====================*/
schemavarsblock:
	SCHEMAVARIABLES OPENBRACE schematext CLOSEBRACE { 
		try {
			if ( parsedStructure == null ) {
				parsedStructure = new dLStructure();
			}
			$$ = "(declare-schema-vars: \n" + (String)$3 + "\n)"; System.out.println( $$ );
			parsedStructure.declaredSchemaVariables = (String)$3;
		} catch ( Exception e ) {
			System.err.println("Exception at location schemavarsblock:SCHEMAVARIABLES OPENBRACE schematext CLOSEBRACE");
			System.err.println( e );
		}
	}
;

rulesblock:
	RULES OPENBRACE schematext CLOSEBRACE { 
		try {
			if ( parsedStructure == null ) {
				parsedStructure = new dLStructure();
			}
			$$ = "(declare-rules: \n" + (String)$3 + "\n)"; System.out.println( $$ );
			assert( parsedStructure != null );
			parsedStructure.declaredRules = (String)$3;
		} catch ( Exception e ) {
			System.err.println("Exception at location rulesblock:RULES OPENBRACE schematext CLOSEBRACE");
			System.err.println( e );
		}
	}
;

schematext:
	SCHEMATEXT { 
		try {
			$$ = (String)$1; 
		} catch ( Exception e ) {
			System.err.println("Exception at location schematext:SCHEMATEXT");
			System.err.println( e );
		}
	}
	| SCHEMATEXT schematext { 
		try {
			$$ = (String)$1 + (String)$2; 
		} catch ( Exception e ) {
			System.err.println("Exception at location schematext:SCHEMATEXT schematext");
			System.err.println( e );
		}
	}
;

/*============================================================*/

/*==================== Variable declarations ====================*/
varblock:
	OPENBOX vardeclaration CLOSEBOX { 
		try {
			if ( parsedStructure == null ) {
				parsedStructure = new dLStructure();
			}
			parsedStructure.declaredProgramVariables = new ArrayList<String>();
			//parsedStructure.declaredProgramVariables.addAll( (ArrayList<String>)$2 );
			parsedStructure.declaredProgramVariables.addAll( $2 );
			ArrayList<String> vars = (ArrayList<String>)parsedStructure.declaredProgramVariables;
			$$ = vars.toString();
		} catch ( Exception e ) {
			System.err.println("Exception at location varblock:OPENBOX vardeclaration CLOSEBOX");
			System.err.println( e );
		}
		
	}
	| OPENBOX vardeclaration varinitlist CLOSEBOX { 
		//$$ = "(declare-vars: \n" + (String)$2 + ")" + (String)$3; System.out.println( $$ );
		try {
			if ( parsedStructure == null ) {
				parsedStructure = new dLStructure();
			}
			parsedStructure.declaredProgramVariables = (ArrayList<String>)$2;
			parsedStructure.variableInitializations = (ArrayList<String>)$3;

			ArrayList<String> result = new ArrayList<String>();
			result.addAll( parsedStructure.declaredProgramVariables );
			result.addAll( parsedStructure.variableInitializations );
			$$ = result.toString();
		} catch ( Exception e ) {
			System.err.println("Exception at location varblock:OPENBOX vardeclaration varinitlist CLOSEBOX");
			System.err.println( e );
		}

	}
;

vardeclaration:
	REALDECLARATION varlist SEMICOLON { 
		try {
			$$ = (ArrayList<String>)$2;
		} catch ( Exception e ) {
			System.err.println("Exception at location vardeclaration:REALDECLARATION varlist SEMICOLON");
			System.err.println( e );
		}
	}
	| vardeclaration REALDECLARATION varlist SEMICOLON { 
		//$$ = "\t(declare-real " + (String)$2 + " )\n"  + (String)$4;
		try {
			ArrayList<String> vars = $1;
			vars.add( (String)$4 );
			$$ = vars;
		} catch ( Exception e ) {
			System.err.println("Exception at location vardeclaration:vardeclaration REALDECLARATION varlist SEMICOLON");
			System.err.println( e );
		}
	}
;

varlist:
	IDENTIFIER { 
		//$$ = "\t(declare-real " + (String)$1 + " )\n";
		try {
			ArrayList<String> vars = new ArrayList<String>();
			vars.add( (String)$1 );
			$$ = vars;
		} catch ( Exception e ) {
			System.err.println("Exception at location varlist:IDENTIFIER");
			System.err.println( e );
		}
	}
	| varlist COMMA IDENTIFIER { 
		//$$ = "\t(declare-real " + (String)$1 + " )\n" + (String)$3;
		try {
			ArrayList<String> vars = new ArrayList<String>();
			vars.addAll( (ArrayList<String>)$1 );
			vars.add( (String)$3 );
			$$ = vars;
		} catch ( Exception e ) {
			System.err.println("Exception at location varlist:varlist COMMA IDENTIFIER");
			System.err.println( e );
		}
	}
;
	
varinitlist:
	IDENTIFIER ASSIGN term SEMICOLON { 
		//$$ = "\t(init: " + (String)$1 + ", " + (String)$3 + " )\n";
		try {
			ArrayList<String> init = new ArrayList<String>();
			dLStructure myTerm = (dLStructure)$3;
			init.add( (String)$1 + " := " + myTerm.toString() );
			$$ = init;
		} catch ( Exception e ) {
			System.err.println("Exception at location varinitlist:IDENTIFIER ASSIGN term SEMICOLON");
			System.err.println( e );
		}

	}
	| varinitlist IDENTIFIER ASSIGN term SEMICOLON { 
		//$$ = "\t(init: " + (String)$1 + ", " + (String)$3 + " )\n" + (String)$5;
		try {
			ArrayList<String> init = (ArrayList<String>)$1;
			dLStructure myTerm = (dLStructure)$4;
			init.add( (String)$2 + " := " + myTerm.toString() );
			$$ = init;
		} catch ( Exception e ) {
			System.err.println("Exception at location varinitlist:varinitlist IDENTIFIER ASSIGN term SEMICOLON");
			System.err.println( e );
		}
	}
;
/*============================================================*/

/*==================== Function declarations ====================*/
funblock:
	FUNCTIONS OPENBRACE functiondeclaration CLOSEBRACE { 
		try {
			//$$ = "(declare-funs: \n" + (String)$3 + ")"; System.out.println( $$ ); 
			$$ = (String)$3;
		} catch ( Exception e ) {
			System.err.println("Exception at location funblock:FUNCTIONS OPENBRACE functiondeclaration CLOSEBRACE");
			System.err.println( e );
		}
	}
;

functiondeclaration:
	REALDECLARATION IDENTIFIER LPAREN argumentdeclaration RPAREN SEMICOLON {
		try {
			//$$ = "(R fun " + (String)$2 + " " + (String)$4 + " )\n";
			$$ = (String)$1 + (String)$2 + (String)$3 + (String)$4 + (String)$5 + (String)$6 + "\n";
		} catch ( Exception e ) {
			System.err.println("Exception at location functiondeclaration:REALDECLARATION IDENTIFIER LPAREN argumentdeclaration RPAREN SEMICOLON");
			System.err.println( e );
		}
	}
	| EXTERNAL REALDECLARATION IDENTIFIER LPAREN argumentdeclaration RPAREN SEMICOLON {
		try {
			//$$ = "(R fun " + (String)$3 + " " + (String)$5 + " )\n"; 
			$$ = (String)$1 + (String)$2 + (String)$3 + (String)$4 + (String)$5 + (String)$6 + (String)$7 + "\n";
		} catch ( Exception e ) {
			System.err.println("Exception at location functiondeclaration:REALDECLARATION IDENTIFIER LPAREN argumentdeclaration RPAREN SEMICOLON");
			System.err.println( e );
		}
	}
	| functiondeclaration REALDECLARATION IDENTIFIER LPAREN argumentdeclaration RPAREN SEMICOLON {
		try {
			//$$ = "(R fun " + (String)$2 + " " + (String)$4 + " )\n" + (String)$7; 
			$$ = (String)$1 + (String)$2 + (String)$3 + (String)$4 + (String)$5 + (String)$6 + (String)$7 + "\n";
		} catch ( Exception e ) {
			System.err.println("Exception at location functiondeclaration:REALDECLARATION IDENTIFIER LPAREN argumentdeclaration RPAREN SEMICOLON");
			System.err.println( e );
		}
	}
	| functiondeclaration EXTERNAL REALDECLARATION IDENTIFIER LPAREN argumentdeclaration RPAREN SEMICOLON {
		try {
			//$$ = "(R fun " + (String)$3 + " " + (String)$5 + " )\n" + (String)$8; 
			$$ = (String)$1 + (String)$2 + (String)$3 + (String)$4 + (String)$5 + (String)$6 + (String)$7 + (String)$8 + "\n";
		} catch ( Exception e ) {
			System.err.println("Exception at location functiondeclaration:functiondeclaration EXTERNAL REALDECLARATION IDENTIFIER LPAREN argumentdeclaration RPAREN SEMICOLON");
			System.err.println( e );
		}
	}
;

argumentdeclaration:
	%empty {
		$$ = "";
	}
	| REALDECLARATION { 
		try {
			$$ = (String)$1;
		} catch ( Exception e ) {
			System.err.println("Exception at location argumentdeclaration:REALDECLARATION");
			System.err.println( e );
		}
	}
	| argumentdeclaration COMMA REALDECLARATION { 
		try {
			$$ = (String)$1 + (String)$2 + (String)$3; 
		} catch ( Exception e ) {
			System.err.println("Exception at location argumentdeclaration:argumentdeclaration COMMA REALDECLARATION");
			System.err.println( e );
		}
	}
;
/*============================================================*/

/*==================== Differential dynamic logic ====================*/
dLformula:
	TRUE { 
		try {
			$$ = new TrueFormula();
		} catch ( Exception e ) {
			System.err.println("Exception at location dLformula:TRUE");
			System.err.println( e );
		}
	}
	| FALSE	{ 
		try {
			$$ = new FalseFormula();
		} catch ( Exception e ) {
			System.err.println("Exception at location dLformula:FALSE");
			System.err.println( e );
		}
	}
	| comparison { 
		//$$ = $1; 							
		try {
			$$ = (ComparisonFormula)$1;
		} catch ( Exception e ) {
			System.err.println("Exception at location dLformula:comparison");
			System.err.println( e );
		}
	}
	| dLformula AND dLformula { 
		//$$ = "(and " + (String)$1 + ", " + (String)$3 + " )"; 	
		try {
			//ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			//args.add( (dLStructure)$1 );
			//args.add( (dLStructure)$3 );
			//$$ = new dLStructure( "&", args );
			$$ = new AndFormula( (dLFormula)$1, (dLFormula)$3 );
		} catch ( Exception e ) {
			System.err.println("Exception at location dLformula:dLformula AND dLformula");
			System.err.println( e );
		}
	}
	| dLformula OR dLformula { 
		//$$ = "(or " + (String)$1 + ", " + (String)$3 + " )"; 
		try {
			//ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			//args.add( (dLStructure)$1 );
			//args.add( (dLStructure)$3 );
			//$$ = new dLStructure( "or", args );
			$$ = new OrFormula( (dLFormula)$1, (dLFormula)$3 );
		} catch ( Exception e ) {
			System.err.println("Exception at location dLformula:dLformula OR dLformula");
			System.err.println( e );
		}
	}
	| NOT dLformula	{ 
		//$$ = "(not " + (String)$2 + " )"; 
		try {
			//ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			//args.add( (dLStructure)$2 );
			//$$ = new dLStructure( "not", args );
			$$ = new NotFormula( (dLFormula)$2 );
		} catch ( Exception e ) {
			System.err.println("Exception at location dLformula:NOT dLformula");
			System.err.println( e );
		}
	}
	| LPAREN dLformula RPAREN { 
		//$$ = "( " + (String)$2 + ")";
		try {
			$$ = (dLFormula)$2;
		} catch ( Exception e ) {
			System.err.println("Exception at location dLformula:LPAREN dLformula RPAREN");
			System.err.println( e );
		}
	}
	| dLformula IMPLIES dLformula { 
		//$$ = "(implies " + (String)$1 + ", " + (String)$3 + " )";
		try {
			//ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			//args.add( (dLStructure)$1 );
			//args.add( (dLStructure)$3 );
			//$$ = new dLStructure( "implies", args );
			$$ = new ImpliesFormula( (dLFormula)$1, (dLFormula)$3 );
		} catch ( Exception e ) {
			System.err.println("Exception at location dLformula:dLformula IMPLIES dLformula");
			System.err.println( e );
		}
	}
	| dLformula IFF dLformula { 
		//$$ = "(iff " + (String)$1 + ", " + (String)$3 + " )"; 
		try {
		//	ArrayList<dLStructure> args = new ArrayList<dLStructure>();
		//	args.add( (dLStructure)$1 );
		//	args.add( (dLStructure)$3 );
		//	$$ = new dLStructure( "iff", args );
			$$ = new IffFormula( (dLFormula)$1, (dLFormula)$3 );
		} catch ( Exception e ) {
			System.err.println("Exception at location dLformula:dLformula IFF dLformula");
			System.err.println( e );
		}
	}
	| FORALL IDENTIFIER SEMICOLON dLformula %prec QUANTIFIER { 
		//$$ = "(forall " + (String)$2 + "; " + (String)$4 + " )";
		try {
			//ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			//args.add( new dLStructure( (String)$2 ) );
			//args.add( (dLStructure)$4 );
			//$$ = new dLStructure( "forall", args );
			$$ = new ForAllFormula( new RealVariable( (String)$2), (dLFormula)$4 );
		} catch ( Exception e ) {
			System.err.println("Exception at location dLformula:FORALL IDENTIFIER SEMICOLON dLformula");
			System.err.println( e );
		}
	}
	| EXISTS IDENTIFIER SEMICOLON dLformula %prec QUANTIFIER { 
		//$$ = "(exists " + (String)$2 + "; " + (String)$4 + " )";
		try {
			//ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			//args.add( new dLStructure( (String)$2 ) );
			//args.add( (dLStructure)$4 );
			//$$ = new dLStructure( "exists", args );
			$$ = new ExistsFormula( new RealVariable( (String)$2 ), (dLFormula)$4 );
		} catch ( Exception e ) {
			System.err.println("Exception at location dLformula:EXISTS IDENTIFIER SEMICOLON dLformula");
			System.err.println( e );
		}
	}
	| OPENBOX hybridprogram CLOSEBOX dLformula { 
		//$$ = "(box (hp: " + (String)$2 + " ), (post: " + (String)$4 + " ) )"; 
		try {
			ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			args.add( (HybridProgram)$2 );
			args.add( (dLStructure)$4 );
			$$ = new dLStructure( "[]", args );
			$$ = new BoxModalityFormula( (HybridProgram)$2, (dLFormula)$4 );
		} catch ( Exception e ) {
			System.err.println("Exception at location OPENBOX:hybridprogram CLOSEBOX dLformula");
			System.err.println( e );
		}
	}
	| OPENDIAMOND hybridprogram CLOSEDIAMOND dLformula { 
		//$$ = "(diamond (hp: " + (String)$2 + " ), (post: " + (String)$4 + " ) )";
		try {
			//ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			//args.add( (HybridProgram)$2 );
			//args.add( (dLStructure)$4 );
			//$$ = new dLStructure( "<>", args );
			$$ = new BoxModalityFormula( (HybridProgram)$2, (dLFormula)$4 );
		} catch ( Exception e ) {
			System.err.println("Exception at location OPENDIAMOND hybridprogram CLOSEDIAMOND dLformula");
			System.err.println( e );
		}
	}
;

hybridprogram:
	odesystem { 
		//$$ = (String)$1;
		try {
			$$ = (ContinuousProgram)$1;
		} catch ( Exception e ) {
			System.err.println("Exception at location hybridprogram:odesystem");
			System.err.println( e );
		}
	}
	| test { 
		//$$ = (String)$1;
		try {
			$$ = (TestProgram)$1;
		} catch ( Exception e ) {
			System.err.println("Exception at location hybridprogram:test");
			System.err.println( e );
		}
	}
	| assignment { 
		//$$ = (String)$1;
		try {
			$$ = (AssignmentProgram)$1;
		} catch ( Exception e ) {
			System.err.println("Exception at location hybridprogram:assignment");
			System.err.println( e );
		}
	}
	| hybridprogram SEMICOLON hybridprogram { 
		//$$ = "(sequence " + (String)$1 + ", " + (String)$3 + " )"; 
		try {
			//ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			//args.add( (dLStructure)$1 );
			//args.add( (dLStructure)$3 );
			//$$ = new dLStructure( "sequence", args );
			$$ = new SequenceProgram( (HybridProgram)$1, (HybridProgram)$3 );
		} catch ( Exception e ) {
			System.err.println("Exception at location hybridprogram:hybridprogram SEMICOLON hybridprogram");
			System.err.println( e );
		}
	}
	| hybridprogram CUP hybridprogram { 
		//$$ = "(choice " + (String)$1 + ", " + (String)$3 + " )"; 
		try {
		//	ArrayList<HybridProgram> args = new ArrayList<HybridProgram>();
		//	args.add( (HybridProgram)$1 );
		//	args.add( (HybridProgram)$3 );
		//	$$ = new HybridProgram( "choice", args );
			$$ = new ChoiceProgram( (HybridProgram)$1, (HybridProgram)$3 );
		} catch ( Exception e ) {
			System.err.println("Exception at location hybridprogram:hybridprogram CUP hybridprogram");
			System.err.println( e );
		}
	}
	| hybridprogram KLEENESTAR {
		//$$ = "(repeat " + (String)$1 + " )";
		try {
			//ArrayList<HybridProgram> args = new ArrayList<HybridProgram>();
			//args.add( (HybridProgram)$1 );
			//$$ = new HybridProgram( "repeat", args );
			$$ = new RepetitionProgram( (HybridProgram)$1 );
		} catch ( Exception e ) {
			System.err.println("Exception at location hybridprogram:hybridprogram KLEENESTAR");
			System.err.println( e );
		}
	}
	| LPAREN hybridprogram RPAREN { 
		//$$ = (String)$2;
		try {
			$$ = (HybridProgram)$2;
		} catch ( Exception e ) {
			System.err.println("Exception at location hybridprogram:LPAREN hybridprogram RPAREN");
			System.err.println( e );
		}
	}
;

assignment:
	IDENTIFIER ASSIGN RANDOM { 
		//$$ = "(assign " + (String)$1 + ", " + (String)$3 + " )";
		try {
			//ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			//args.add( new dLStructure( (String)$1 ) );
			//args.add( new dLStructure( "arbitrary" ) );
			//$$ = new HybridProgram( ":=", args );
			$$ = new AssignmentProgram( new RealVariable( (String)$1 ), new Term( "arbitrary" ) );
		} catch ( Exception e ) {
			System.err.println("Exception at location assignment:IDENTIFIER ASSIGN RANDOM");
			System.err.println( e );
		}
	}
	| IDENTIFIER ASSIGN term { 
		//$$ = "(assign " + (String)$1 + ", " + (String)$3 + " )";
		try {
			//ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			//args.add( new dLStructure( (String)$1 ));
			//args.add( (dLStructure)$3 );
			//$$ = new HybridProgram( ":=", args );
			$$ = new AssignmentProgram( new RealVariable( (String)$1 ), (Term)$3 );
		} catch ( Exception e ) {
			System.err.println("Exception at location IDENTIFIER ASSIGN term");
			System.err.println( e );
		}
	}
;


test:
	TEST dLformula { 
		//$$ = "(test " + (String)$2 + " )";
		try {
			//ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			//args.add( (dLStructure)$2 );
			//$$ = new dLStructure( "?", args );
			$$ = new TestProgram( (dLStructure)$2 );
		} catch ( Exception e ) {
			System.err.println("Exception at location test:TEST dLformula");
			System.err.println( e );
		}
	}
;

odesystem:
	OPENBRACE odelist CLOSEBRACE { 
		//$$ = "(continuous " + "(odelist: " + (String)$2 + " )" + ", (domain: true ) )"; 
		try {
			//ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			//args.addAll( (ArrayList<dLStructure>)$2 );
			//args.addAll( $2 );
			//args.add( new dLStructure("true") );
			//$$ = new dLStructure( "continuous", args );
			$$ = new ContinuousProgram( (ArrayList<dLStructure>)$2 ); // Constructor appends "true" doe automaticaly
		} catch ( Exception e ) {
			System.err.println("Exception at location odesystem:OPENBRACE odelist CLOSEBRACE");
			System.err.println( e );
		}
	}
	| OPENBRACE odelist AND dLformula CLOSEBRACE { 
		//$$ = "(continuous " + "(odelist: " + (String)$2 + " )" + ", (domain: " + (String)$4 + " ) )"; 
		try {
			//ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			//args.addAll( (ArrayList<dLStructure>)$2 );
			//args.addAll( $2 );
			//args.add( (dLStructure)$4 );
			//$$ = new dLStructure( "continuous", args );
			$$ = new ContinuousProgram( (ArrayList<dLStructure>)$2, (dLStructure)$4 );
		} catch ( Exception e ) {
			System.err.println("Exception at location odesystem:OPENBRACE odelist AND dLformula CLOSEBRACE");
			System.err.println( e );
		}
	}
;

odelist:
	ode { 
		//$$ = (String)$1;
		try {
			ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			args.add( (dLStructure)$1 );
			$$ = args;
		} catch ( Exception e ) {
			System.err.println("Exception at location odelist:ode");
			System.err.println( e );
		}

	}
	| odelist COMMA ode { 
		//$$ = (String)$1 + ", " + (String)$3;
		try {
			ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			args.addAll( (ArrayList<dLStructure>)$1 );
			args.add( (dLStructure)$3 );
			$$ = args;
		} catch ( Exception e ) {
			System.err.println("Exception at location odelist:odelist COMMA ode");
			System.err.println( e );
		}

	}
;
ode:
	IDENTIFIER PRIME EQUALS term { 
		//$$ = (String)$1 + "' = " + (String)$4;
		try {
			//ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			//args.add( new dLStructure( (String)$1) );
			//args.add( (dLStructure)$4 );
			//$$ = new dLStructure( "d/dt", args );
			$$ = new ExplicitODE( new RealVariable( (String)$1 ), (Term)$4 );
		} catch ( Exception e ) {
			System.err.println("Exception at location ode:IDENTIFIER PRIME EQUALS term");
			System.err.println( e );
		}

	}
;



comparison:
	term INEQUALITY term { 
		//$$ = "(" + (String)$2 +" "+ (String)$1 + ", " + (String)$3 + ")"; 
		try {
			//ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			//args.add( (dLStructure)$1 );
			//args.add( (dLStructure)$3 );
			//$$ = new dLStructure( (String)$2, args );
			$$ = new ComparisonFormula( new Operator( (String)$2 ), (Term)$1, (Term)$3 ) ;
		} catch ( Exception e ) {
			System.err.println("Exception at location comparison:term INEQUALITY term");
			System.err.println( e );
		}
	}
	| term EQUALS term {
		//$$ = "(" + (String)$2 +" "+ (String)$1 + ", " + (String)$3 + ")"; 
		try {
			//ArrayList<dLStructure> args = new ArrayList<dLStructure>();
			//args.add( (dLStructure)$1 );
			//args.add( (dLStructure)$3 );
			//$$ = new dLStructure( (String)$2, args );
			$$ = new ComparisonFormula( new Operator( (String)$2), (Term)$1, (Term)$3 ) ;
		} catch ( Exception e ) {
			System.err.println("Exception at location comparison:term EQUALS term");
			System.err.println( e );
		}
	}
;


term:
	NUMBER { 
		try {
			$$ = new Real( (String)$1 );
			//System.out.println( ((Term)$$).toString() );
		} catch ( Exception e ) {
			System.err.println("Exception at location term:NUMBER");
			System.err.println( e );
		}
	}
	| IDENTIFIER LPAREN argumentlist RPAREN {
		//$$ = "(" + (String)$1 + " " + (String)$3 + ")";
		try {
			$$ = new Term( new Operator( (String)$1 ), (ArrayList<Term>)$3 );
		} catch ( Exception e ) {
			System.err.println("Exception at location term:IDENTIFIER LPAREN argumentlist RPAREN");
			System.err.println( e );
		}
	}
	| IDENTIFIER { 
		//$$ = (String)$1;
		try {
			$$ = new RealVariable( (String)$1 );
	//		System.out.println( $$.toString() );
		} catch ( Exception e ) {
			System.err.println("Exception at location term:IDENTIFIER");
			System.err.println( e );
		}
	}
	| LPAREN term RPAREN { 
		//$$ = "("+ (String)$2 +")"; 					
		try {
			$$ = (Term)$2;
		} catch ( Exception e ) {
			System.err.println("Exception at location term:LPAREN term RPAREN");
			System.err.println( e );
		}
	}
	| term PLUS term { 
		//$$ = "(+ " + (String)$1 + ", " + (String)$3+ " )";		
		try {
			ArrayList<Term> args = new ArrayList<Term>();
			args.add( (Term)$1 );
			args.add( (Term)$3 );
			$$ = new Term( new Operator("+", true), args );
		} catch ( Exception e ) {
			System.err.println("Exception at location term:term PLUS term");
			System.err.println( e );
		}
	}
	| term MINUS term { 
		//$$ = "(- " + (String)$1 + ", " + (String)$3 + ")";
		try {
			ArrayList<Term> args = new ArrayList<Term>();
			args.add( (Term)$1 );
			args.add( (Term)$3 );
			$$ = new Term( new Operator("-", true), args );
		} catch ( Exception e ) {
			System.err.println("Exception at location term:term MINUS term");
			System.err.println( e );
		}
	}
	| term MULTIPLY term { 
		//$$ = "(* " + (String)$1 + ", " + (String)$3 +")";
		try {
			ArrayList<Term> args = new ArrayList<Term>();
			args.add( (Term)$1 );
			args.add( (Term)$3 );
			$$ = new Term( new Operator("*", true), args );
		} catch ( Exception e ) {
			System.err.println("Exception at location term:term MULTIPLY term");
			System.err.println( e );
		}
	}
	| term DIVIDE term { 
		//$$ = "(/ " + (String)$1 + ", " + (String)$3 + ")";
		try {
			ArrayList<Term> args = new ArrayList<Term>();
			args.add( (Term)$1 );
			args.add( (Term)$3 );
			$$ = new Term( new Operator("/", true), args );
		} catch ( Exception e ) {
			System.err.println("Exception at location term:term DIVIDE term");
			System.err.println( e );
		}
	}
	| term POWER term { 
		//$$ = "(^ " + (String)$1 + ", " + (String)$3 + ")";		
		try {
			ArrayList<Term> args = new ArrayList<Term>();
			args.add( (Term)$1 );
			args.add( (Term)$3 );
			$$ = new Term( new Operator("^", true), args );
		} catch ( Exception e ) {
			System.err.println("Exception at location term:term POWER tterm:term POWER term");
			System.err.println( e );
		}
	}
	| MINUS term %prec NEGATIVE { 
		//$$ = "(- 0, " + (String)$2 + " )";
		try {
			ArrayList<Term> args = new ArrayList<Term>();
			args.add( new Real( "0" ) );
			args.add( (Term)$2 );
			$$ = new Term( new Operator("-", true), args );
		} catch ( Exception e ) {
			System.err.println("Exception at location term:MINUS term");
			System.err.println( e );
		}
	}
;

argumentlist:
	%empty {
		$$ = null;
	}
	| term	{ 
		//$$ = (String)$1; System.out.println(" found arglist");					
		try {
			ArrayList<Term> args = new ArrayList<Term>();
			args.add( (Term)$1 );
			$$ = args;
		} catch ( Exception e ) {
			System.err.println("Exception at location argumentlist:term");
			System.err.println( e );
		}
	}
	| argumentlist COMMA term { 
		//$$ = (String)$1 + ", " + (String)$3; System.out.println("found arglist, multiple args");
		try {
			ArrayList<Term> args = new ArrayList<Term>();
			args.addAll( (ArrayList<Term>)$1 );
			args.add( (Term)$3 );
			$$ = args;
		} catch ( Exception e ) {
			System.err.println("Exception at location argumentlist:argumentlist COMMA term");
			System.err.println( e );
		}
	}
;

/*============================================================*/
%%




