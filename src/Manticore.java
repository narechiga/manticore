import java.io.*;
import java.util.*;

import manticore.dl.*;
import manticore.symbolicexecution.*;
import manticore.matlabsimulationkit.*;

class Manticore {

	static boolean debug = false;

	public static void main( String [] args ) {

		System.out.println("This is Manticore, a strategy engine for the theorem prover KeYmaera");

		if ( args.length < 1 ) {
			commandLine();
		} else if ( args.length > 1 ) {
			System.out.println("Too many arguments.");
			System.exit(1);
		} else {
			System.out.println("Input argument is: " + args[0] );
		}

		
		// Try to parse the input file
		try {
			FileReader inputReader = new FileReader( args[0] );
			Lexer fileLexer = new Lexer( inputReader );
			YYParser fileParser = new YYParser( fileLexer );

			fileParser.parse();
			System.out.println( "PARSED: " + fileParser.parsedStructure.toKeYmaeraString() );
			System.out.println("Continuous blocks================================================");
			ArrayList<ContinuousProgram> continuousblocks = fileParser.parsedStructure.extractContinuousBlocks();
			System.out.println("Continuous blocks found: " + continuousblocks.size() );

			Iterator<ContinuousProgram> cbit = continuousblocks.iterator();

			while ( cbit.hasNext() ) {
				System.out.println("_________________________________________________________________");
				System.out.println("Continuous block:");

				System.out.println( cbit.next().toKeYmaeraString() );
				System.out.println("_________________________________________________________________");
			}

			System.out.println("With annotations: " + fileParser.annotations);

			System.out.println("Writing to dynsys.m file...");
			MatlabSimulationKit.generateDynsysFile( fileParser.parsedStructure.extractFirstHybridProgram() );
			System.out.println("Writing to problemstatement.m file...");
			MatlabSimulationKit.generateProblemStatementFile( fileParser.declaredProgramVariables, 1, 1 );

			// Run devil run!
			ProcessBuilder builder = new ProcessBuilder("matlab", "-nodesktop", "-nosplash",
							"< manticore/matlabsimulationkit/run.m");
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));

			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println ("Matlab output: " + line);
			}

		} catch ( Exception e ) {
			System.err.println( e );
		}


		System.exit(1);


		
		///* * * Everything below is still in testing! * * */
		///* Parse the input file */	
		//Mode myMode = new Mode("M1");
		//try {
		//	KeYmaeraParser myParser = new KeYmaeraParser( args[0] );

		//	ArrayList<String> listOfModes = myParser.parseStableModes();
		//	System.out.println("Stable modes are: ");
		//	System.out.println(listOfModes);
		//	
		//	myParser.parseODEs( myMode );
		//	myParser.parseGuardList( myMode );

		//	myParser.die();
		//} catch ( Exception ex ) {
		//	ex.printStackTrace();
		//}

		///* Generate an SoS invariant */
		///* * * Artificially generate a mode, because mode parsing is not yet functional * * */
		////Mode myMode = new Mode("M1");
		////myMode.addVariable("x1"); myMode.addVariable("x2"); myMode.addVariable("x3");
		////myMode.addODE("(-x1^3 - x1*x3^2)*(x3^2 + 1)");
		////myMode.addODE("(-x2 - x1^2*x2)*(x3^2 + 1)");
		////myMode.addODE("(-x3 + 3*x1^2*x3)*(x3^2 + 1) - 3*x3");

		////myMode.addVariable("firstVar"); myMode.addVariable("secondVar");
		////myMode.addODE("secondVar"); myMode.addODE("-firstVar");

		//SoSLyapunovCandidateGenerator candidateGenerator = new SoSLyapunovCandidateGenerator( myMode );
		//MathematicaCandidateValidator candidateValidator = new MathematicaCandidateValidator( myMode );

		//String lyapCandidate = candidateGenerator.generateCandidate();
		//System.out.println("A candidate Lyapunov function is: " + lyapCandidate);

		///* Conditions can be checked separately */
		///*candidateValidator.checkLyapunovPositivity( lyapCandidate );
		//candidateValidator.checkLyapunovDerivativeNegativity( lyapCandidate );*/
		///* or all at once */

		//boolean isLyapunov = candidateValidator.checkLyapunov( lyapCandidate );
		//System.out.println("The result of checking the Lyapunov conditions is: " + isLyapunov);

		//System.out.println("I will now try to find a sublevel set that excludes the mode guard.");
		//String level = MathematicaSublevelSetFinder.findSublevelSet( myMode, lyapCandidate );

		//String hybridInvariant = "("+lyapCandidate+" - "+level+" < 0 ) & M = " + myMode.modeID;
		//System.out.println("Hybrid invariant is: " + hybridInvariant);

		//ProofGenerator myProofGenerator = new ProofGenerator();
		//myProofGenerator.applyFirstCut( hybridInvariant, args[0] );
		////myProofGenerator.writePartialProof( args[0] );
		////my

	} 

	/*==================== Command line interface ====================*/
	public static void commandLine() {
		Scanner commandScanner = new Scanner( System.in );
		while (true) {
			try {
			        System.out.print("\nmanticore:> ");
				String input = commandScanner.nextLine();
				input = input.trim();
				Scanner in = new Scanner( input );
				
				if ( in.hasNext("parse") ){
					in.skip("parse");
					runParser( in.nextLine() + "\n" );
				} else if ( in.hasNext("evaluate") ) {
					in.skip("evaluate");
					runEvaluate( in.nextLine() + "\n");
				} else if ( in.hasNext("simulate") ) {
					in.skip("simulate");
					runSimulate( in.nextLine() + "\n");
				//} else if ( in.hasNext("execute") ) {
				//	in.skip("execute");
				//	runExecute( in.nextLine() + "\n");
				} else if ( in.hasNext("version") ) {
					System.out.println("Manticore version 0");
					in.nextLine();
				} else {
					runParser( in.nextLine() + "\n" );
				}

			} catch ( Exception e ) { 
				System.out.println("Error running parser test loop.");
			        System.err.println( e );
				e.printStackTrace();
			}   
		}	
	}

//	public static String runSimulate ( HybridProgram program, String valuationString ) throws Exception {
//
//		ValuationList valList = null;
//
//	        StringReader valuationReader = new StringReader( valuationString );
//	        Lexer valuationLexer = new Lexer( valuationReader );
//	        YYParser valuationParser = new YYParser( valuationLexer );
//	        valuationParser.parse();
//
//		Interpretation interpretation = new NativeInterpretation();
//
//		if (  valuationParser.valuation != null ) ) {
//
//			ValuationList initialState = new ValuationList();
//			initialState.add( myParser.valuation );
//			NativeExecutionEngine engine = new NativeExecutionEngine( interpretation );
//			valList = engine.runDiscreteSteps( (HybridProgram)myParser.parsedStructure,
//									initialState );
//			if ( debug ) {
//				System.out.println( "PARSED: " + myParser.parsedStructure.toKeYmaeraString() );
//				System.out.println("Valuation is: " + myParser.valuation.toString() );
//				System.out.println("Result of discrete execution is: " + valList.toString() );
//			}
//
//		}
//
//		Valuation toReturn = valList.get(0);		
//		return toReturn.toString();
//
//	}


	public static String runSimulate ( String input ) throws Exception {

		ValuationList valList = null;

	        StringReader inreader = new StringReader( input );
	        Lexer myLexer = new Lexer( inreader );
	        YYParser myParser = new YYParser( myLexer );
	        myParser.parse();

		Interpretation interpretation = new NativeInterpretation();

		if ( (myParser.parsedStructure instanceof HybridProgram) && ( myParser.valuation != null ) ) {

			ValuationList initialState = new ValuationList();
			initialState.add( myParser.valuation );
			NativeExecutionEngine engine = new NativeExecutionEngine( interpretation );
			valList = engine.runDiscreteSteps( (HybridProgram)myParser.parsedStructure,
									initialState );
			if ( debug ) {
				System.out.println( "PARSED: " + myParser.parsedStructure.toKeYmaeraString() );
				System.out.println("Valuation is: " + myParser.valuation.toString() );
				System.out.println("Result of discrete execution is: " + valList.toString() );
			}

		}

		Valuation toReturn = valList.get(0);		
		return toReturn.toString();

	}

	

	//public static void runExecute ( String input ) throws Exception {
	//        StringReader inreader = new StringReader( input );
	//        Lexer myLexer = new Lexer( inreader );
	//        YYParser myParser = new YYParser( myLexer );
	//        myParser.parse();

	//	Interpretation interpretation = new NativeInterpretation();

	//	if ( (myParser.parsedStructure instanceof HybridProgram) && ( myParser.valuation != null ) ) {
	//		ValuationList result;
	//		System.out.println( "PARSED: " + myParser.parsedStructure.toKeYmaeraString() );
	//		System.out.println("Valuation is: " + myParser.valuation.toString() );

	//		NativeExecutionEngine engine = new NativeExecutionEngine( interpretation );
	//		result = engine.runDiscreteSteps ((HybridProgram) myParser.);
	//		System.out.println("Result of discrete execution is: " + result.toString() );
	//	}

	//}

	public static void runEvaluate( String input ) throws Exception {
	        StringReader inreader = new StringReader( input );
	        Lexer myLexer = new Lexer( inreader );
	        YYParser myParser = new YYParser( myLexer );
	        myParser.parse();

		Interpretation interpretation = new NativeInterpretation();

		if ( (myParser.parsedStructure instanceof dLFormula) && ( myParser.valuation != null ) ) {
			System.out.println( "PARSED: " + myParser.parsedStructure.toKeYmaeraString() );
			System.out.println("Valuation is: " + myParser.valuation.toString() );
			System.out.println("Evaluated formula is: " 
				+ interpretation.evaluateFormula( (dLFormula)myParser.parsedStructure, myParser.valuation ) );
		}
	}

	public static dLStructure runParser( String input ) throws Exception {
	        StringReader inreader = new StringReader( input );
	        Lexer myLexer = new Lexer( inreader );
	        YYParser myParser = new YYParser( myLexer );
	        myParser.parse();

		System.out.println( "PARSED: " + myParser.parsedStructure.toKeYmaeraString() );

		if ( myParser.parsedStructure instanceof HybridProgram ) {
			
			HybridProgram parsedProgram = (HybridProgram)myParser.parsedStructure;

			System.out.println("Hybrid Program Data==============================================");
			System.out.println("Is purely continuous: " + parsedProgram.isPurelyContinuous() );
			System.out.println("Is purely discrete: " + parsedProgram.isPurelyDiscrete() );
			System.out.println("Is hybrid: " + parsedProgram.isHybrid() );
			System.out.println("Is program primitive: " + parsedProgram.isProgramPrimitive());
			System.out.println("=================================================================");
			
		}

		if ( myParser.parsedStructure instanceof dLFormula ) {
			System.out.println("dL Formula Data==============================================");
			dLFormula parsedFormula = (dLFormula)myParser.parsedStructure;
			System.out.println("Is first order: " + parsedFormula.isFirstOrder());
			System.out.println("Is modal: " + parsedFormula.isModal());
			System.out.println("Is propositional primitive: " + parsedFormula.isPropositionalPrimitive());
			System.out.println("=================================================================");
		}

		System.out.println("Continuous blocks================================================");
		ArrayList<ContinuousProgram> continuousblocks = myParser.parsedStructure.extractContinuousBlocks();
		System.out.println("Continuous blocks found: " + continuousblocks.size() );
		System.out.println( continuousblocks );
		System.out.println("=================================================================");
		System.out.println("First program================================================");
		System.out.println( myParser.parsedStructure.extractFirstHybridProgram() );
		System.out.println("=================================================================");

		System.out.println("The variables that occur in this structure are:");
		System.out.println( myParser.parsedStructure.getVariables() );

		return myParser.parsedStructure;
					
					
	}

}
