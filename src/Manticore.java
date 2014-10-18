import java.io.*;
import java.util.*;
import java.util.regex.*;

import proteus.dl.parser.*;
import proteus.dl.syntax.*;
import proteus.dl.semantics.*;
import manticore.symbolicexecution.*;
import manticore.invariantsearch.*;
import manticore.matlabsimulationkit.*;
import manticore.matlabsamplingkit.*;

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
			TacticalEngine ta = new TacticalEngine();
			ta.run( args[0] );
		}

		System.exit(1);


		

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
				} else if ( in.hasNext("sampsearch") ) {
					in.skip("sampsearch");
					runSampSearch( in.nextLine() + "\n");
				//} else if ( in.hasNext("symsim") ) {
				//	in.skip("symsim");
				//	runSymSimulate( in.nextLine() + "\n");
				//} else if ( in.hasNext("execute") ) {
				//	in.skip("execute");
				//	runExecute( in.nextLine() + "\n");
				} else if ( in.hasNext("distribute") ) {
					in.skip("distribute");
					runDistribute( in.nextLine() + "\n");
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
//	        dLLexer valuationdLLexer = new dLLexer( valuationReader );
//	        dLParser valuationParser = new dLParser( valuationdLLexer );
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

	//public static String runSymSimulate ( String input ) throws Exception {

	//	ValuationList valList = null;

	//        StringReader inreader = new StringReader( input );
	//        dLLexer mydLLexer = new dLLexer( inreader );
	//        dLParser myParser = new dLParser( mydLLexer );
	//        myParser.parse();

	//	//Interpretation interpretation = new NativeInterpretation();

	//	if ( (myParser.parsedStructure instanceof HybridProgram) && ( myParser.valuation != null ) ) {

	//		UpdateList initialState = new UpdateList();
	//		initialState.add( new UpdateRule( myParser.valuation ) );
	//		NativeExecutionEngine engine = new NativeExecutionEngine( interpretation );
	//		valList = engine.runDiscreteSteps( (HybridProgram)myParser.parsedStructure,
	//								initialState );
	//		if ( true ) {
	//			System.out.println( "PARSED: " + myParser.parsedStructure.toKeYmaeraString() );
	//			System.out.println("Valuation is: " + myParser.valuation.toString() );

	//			if (valList == null) { throw new Exception("onoz null!"); }

	//			System.out.println("Result of discrete execution is: " + valList.toString() );
	//		}

	//	}

	//	Valuation toReturn = valList.get(0);		
	//	return toReturn.toString();

	//}
	
	public static void runDistribute( String input ) throws Exception {
		Term termToDistribute = (Term)(dLStructure.parseStructure( input ) );
		
		System.out.println("Found term: " + termToDistribute.toKeYmaeraString() );
		Term distributedTerm = termToDistribute.distributeMultiplication();
		System.out.println("Distributed term: " + distributedTerm.toKeYmaeraString() );
	}
	
	public static void runSampSearch( String input ) throws Exception {
		String[] parts = input.split("#");

		// First the program
		StringReader reader = new StringReader( parts[0] );
		dLLexer lexer = new dLLexer( reader );
		dLParser parser = new dLParser( lexer );
		parser.parse();
		HybridProgram program = (HybridProgram)(parser.parsedStructure);

		// Then the region of interest as a logical formula
		reader = new StringReader( parts[1] );
		lexer = new dLLexer( reader );
		parser = new dLParser( lexer );
		parser.parse();
		dLFormula domain = (dLFormula)(parser.parsedStructure);

		MatlabSamplingKit.generateProblemFile(
							program,
							2, //degree
							new ArrayList<Double>(), // blank lowerBounds, replaced by -1
							new ArrayList<Double>(), // blank upperBounds, replaced by 1
							0.01, // exclusionRadius
							2, //precision
							1, //initial sampleNumber
							150 //maxOuterIterations
							);
	}

	public static String runSimulate ( String input ) throws Exception {

		ValuationList valList = null;

	        StringReader inreader = new StringReader( input );
	        dLLexer mydLLexer = new dLLexer( inreader );
	        dLParser myParser = new dLParser( mydLLexer );
	        myParser.parse();

		Interpretation interpretation = new NativeInterpretation();

		if ( (myParser.parsedStructure instanceof HybridProgram) && ( myParser.valuation != null ) ) {

			ValuationList initialState = new ValuationList();
			initialState.add( myParser.valuation );
			NativeExecutionEngine engine = new NativeExecutionEngine( interpretation );
			valList = engine.runDiscreteSteps( (HybridProgram)myParser.parsedStructure,
									initialState );
			if ( true ) {
				System.out.println( "PARSED: " + myParser.parsedStructure.toKeYmaeraString() );
				System.out.println("Valuation is: " + myParser.valuation.toString() );

				if (valList == null) { throw new Exception("onoz null!"); }

				System.out.println("Result of discrete execution is: " + valList.toString() );
			}

		}

		Valuation toReturn = valList.get(0);		
		return toReturn.toString();

	}

	

	//public static void runExecute ( String input ) throws Exception {
	//        StringReader inreader = new StringReader( input );
	//        dLLexer mydLLexer = new dLLexer( inreader );
	//        dLParser myParser = new dLParser( mydLLexer );
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
	        dLLexer mydLLexer = new dLLexer( inreader );
	        dLParser myParser = new dLParser( mydLLexer );
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
	        dLLexer mydLLexer = new dLLexer( inreader );
	        dLParser myParser = new dLParser( mydLLexer );
	        myParser.parse();

		if ( myParser.parsedStructure == null ) {
			System.out.println("Parsed structure is null");
			return null;
		}

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

		if ( myParser.parsedStructure instanceof ContinuousProgram ) {
			System.out.println("Checking linearity of parsed continuous program... ");

			ContinuousProgram continuousProgram = (ContinuousProgram)(myParser.parsedStructure);
			ArrayList<RealVariable> stateList = continuousProgram.getStateList();

			System.out.println("State vector is: " + stateList.toString() );
			if ( continuousProgram.isLinearIn( stateList ) ) {
				System.out.println("Continuous program is linear, coefficient matrix is");
				MatrixTerm coefficients = continuousProgram.extractLinearCoefficients( stateList );
				System.out.println( coefficients.toMatrixFormString() );
			}
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
