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
		}

		
		// Try to parse the input file
		try {
			FileReader inputReader = new FileReader( args[0] );
			dLLexer filedLLexer = new dLLexer( inputReader );
			dLParser fileParser = new dLParser( filedLLexer );

			fileParser.parse();
			System.out.println( "PARSED: " + fileParser.parsedStructure.toKeYmaeraString() );
			System.out.println("Continuous blocks================================================");
			ArrayList<ContinuousProgram> continuousblocks = fileParser.parsedStructure.extractContinuousBlocks();
			System.out.println("Continuous blocks found: " + continuousblocks.size() );

			Iterator<ContinuousProgram> cbit = continuousblocks.iterator();

			ContinuousProgram thisBlock;
			while ( cbit.hasNext() ) {
				thisBlock = cbit.next();

				System.out.println("_________________________________________________________________");
				System.out.println("Continuous block:");
				System.out.println( thisBlock.toKeYmaeraString() );
				System.out.println("With continuous variables: ");
				System.out.println( thisBlock.getContinuousVariables() );
				System.out.println("_________________________________________________________________");
			}

			System.out.println("With annotations: " + fileParser.annotations);
			ArrayList<dLFormula> annotations = fileParser.annotations;

			// Check linearity of the continuous blocks
			boolean linearity = true;
			for ( ContinuousProgram thisContinuousBlock : continuousblocks ) {
				linearity = linearity && thisContinuousBlock.isLinearIn( 
					new ArrayList<RealVariable> ( thisContinuousBlock.getContinuousVariables() ) );
			}

			if ( linearity ) {
				ArrayList<dLFormula> forwardInvariants = new ArrayList<>();

				InvariantGenerator invGen = new LinearContinuousStrategy();
				dLFormula finv = new TrueFormula();
				for ( ContinuousProgram thisContinuousBlock : continuousblocks ) {

					// Search throuth the annotations, generate an invariant for every invariant that
					// seems to apply to this
					for ( dLFormula annotation : annotations ) {

						if ( annotation.getFreeVariables().containsAll( thisContinuousBlock.getContinuousVariables() ) ) {
							try {
								finv = invGen.computeInvariant( thisContinuousBlock, new TrueFormula(), new NotFormula( annotation ));

								forwardInvariants.add( finv );
								System.out.println("found finv at: " + finv.toMathematicaString() );
							} catch ( InvariantNotFoundException e ) {
								System.out.println("Could not find an invariant here, moving on");
							}

						}
					}

					ProofGenerator myPG = new ProofGenerator();
					myPG.applyFirstCut( finv.toKeYmaeraString(), args[0] );


				}

			} else {
				// Simulation-driven stuff
				System.out.println("Writing to dynsys.m file...");
				MatlabSimulationKit.generateDynsysFile( fileParser.parsedStructure.extractFirstHybridProgram(), fileParser.annotations.get(0) );
				System.out.println("Writing to problemstatement.m file...");
				MatlabSimulationKit.generateProblemStatementFile( fileParser.annotations, 1);

				ProcessBuilder builder = new ProcessBuilder("matlab", "-nodesktop", "-nosplash",
								"< manticore/matlabsimulationkit/run.m");
				builder.redirectErrorStream(true);
				Process process = builder.start();
				InputStream stdout = process.getInputStream();
				BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));


				
				String lyapunovCandidate = "";
				Double levelset = 0.0;
				String line;
				Pattern lyapunovCandidatePattern = Pattern.compile("V = (.+)");
				Pattern levelsetPattern = Pattern.compile("Optimized levelset size: (\\d+.?\\d*)");
				while ((line = reader.readLine()) != null) {
					if ( true ) {
						System.out.println ("Matlab output: " + line);
					}

					Matcher lyapunovCandidateMatcher = lyapunovCandidatePattern.matcher( line );
					Matcher levelsetMatcher = levelsetPattern.matcher( line );

					if ( lyapunovCandidateMatcher.find() ) {
						lyapunovCandidate = lyapunovCandidateMatcher.group(1);

						lyapunovCandidate = lyapunovCandidate.replace("V = ", "");
						lyapunovCandidate = lyapunovCandidate.replace(">","");
					}

					if ( levelsetMatcher.find() ) {
						levelset = Double.parseDouble(levelsetMatcher.group(1));
					}
				}

				System.out.println("Lyapunov candidate: " + lyapunovCandidate );
				System.out.println("Level: " + levelset );
				ComparisonFormula invariant = new ComparisonFormula(new Operator("<"),
									(Term)runParser( lyapunovCandidate ),
									new Real( levelset.toString() ) );
				AndFormula finvcut = new AndFormula( invariant, 
							MatlabSimulationKit.getNonBallPortion( fileParser.annotations.get(0) ) );
			

				System.out.println("Generating partial proof file...");

				ProofGenerator myPG = new ProofGenerator();
				myPG.applyFirstCut( finvcut.toKeYmaeraString(), args[0] );
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
