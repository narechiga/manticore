import proteus.dl.syntax.*;
import proteus.dl.parser.*;
import java.util.*;
import manticore.invariantsearch.*;

import java.io.*;

public class TacticalEngine {

	// Holds a "checklist" of which continuous blocks
	// we have found invariants for
	HashMap<ContinuousProgram, Boolean> checkList;

	ProofGenerator proofGenerator;
	
	public TacticalEngine() {
	}

	protected List<ContinuousProgram> extractLinearSubsystems( List<ContinuousProgram> continuousBlocks ) {

		ArrayList<ContinuousProgram> linearSubsystems = new ArrayList<>();

		ContinuousProgram untimedBlock;
		for ( ContinuousProgram thisContinuousBlock : continuousBlocks ) {
			untimedBlock = thisContinuousBlock.removeTimers();

			if ( untimedBlock.isLinear() ) {
				linearSubsystems.add( thisContinuousBlock );
			}
		}

		return linearSubsystems;
	}

	protected List<dLFormula> linearityTactic( List<ContinuousProgram> continuousBlocks, 
							List<dLFormula> annotations ) {
		List<dLFormula> forwardInvariants = new ArrayList<>();

		// get linear pieces
		System.out.println("Extracting linear subsystems...");
		List<ContinuousProgram> linearSubsystems = extractLinearSubsystems( continuousBlocks );

		for ( ContinuousProgram thisLinearProgram : linearSubsystems ) {
			for ( dLFormula annotation : annotations ) {
				forwardInvariants.add(linearityTactic( thisLinearProgram, annotation ));
			}
		}

		return forwardInvariants;

	}

	protected HashMap<dLFormula,ContinuousProgram> inferAnnotationPairings( HybridProgram thisProgram, List<dLFormula> theseAnnotations ) {

		HashMap<dLFormula, ContinuousProgram> annotationBinding = new HashMap<>();

		System.out.println("Warning: inference of annotation pairings is experimental!");
		// Generate some satisfying instances
		List<ValuationList> initialConditions;
		Valuation thisIC;
		ValuationList theseICs
		MathematicaInterface solver = new MathematicaInterface();
		for ( dLFormula annotation : theseAnnotations ) {
			thisIC = solver.findInstance( annotation );
			theseICs = new ValuationList();
			theseICs.add( thisIC );

			initialConditions.add( thseseICs.clone() );
		}

		// For each Valuation:
		// 	1. Run the program
		// 	2. See which annotation is active (evaluates to true)
		// 	3. Attach the active continuous program to that annotation
		Interpretation interpretation = new NativeInterpretation();
		NativeExecutionEngine engine = new NativeExecutionEngine( interpretation );
		ValuationList endpoints;
		Valuation endpoint;
		for ( ValuationList theseICs : initialConditions ) {
			endpoints = engine.runDiscreteSteps( thisProgram, theseICs );
			endpoint = endpoints.get( 0 );

			for ( dLFormula annotation : annotations ) {
				if ( interpretation.evaluateFormula( annotation, endpoint ) {
					annotationBinding.set(annotation, engine.activeContinuousBlock);
				}
			}
		}

		return annotationBinding;

	}

	protected dLFormula linearityTactic( ContinuousProgram thisLinearProgram, dLFormula annotation ) {
		System.out.println("Searching for finv for linear subsystem: " 
					+ thisLinearProgram.toKeYmaeraString() );

		dLFormula finv = new TrueFormula();
		InvariantGenerator invGen = new LinearContinuousStrategy();

		ContinuousProgram untimedProgram = thisLinearProgram.removeTimers();
		Set<RealVariable> positiveTimers = thisLinearProgram.getPositiveTimers();


		if ( annotation.getFreeVariables().containsAll( untimedProgram.getContinuousVariables() ) ) {
			try {
				finv = invGen.computeInvariant( untimedProgram, new TrueFormula(), 
								new NotFormula( annotation ));

				System.out.println("found finv at: " + finv.toMathematicaString() );

			} catch ( InvariantNotFoundException e ) {
				System.out.println("Could not find an invariant here, moving on");
			}

		}
		
		for ( RealVariable positiveTimer : positiveTimers ) {
			//proofGenerator.applyFINVCut( new ComparisonFormula(">=", positiveTimer, new Real(0)), thisLinearProgram );
			finv = new AndFormula( finv, new ComparisonFormula(">=", positiveTimer, new Real(0)));
		}

		// Should use the full linear program instead of the untimed program, since KeYmaera
		// actually doesn't know about the untimed program at all, it's just for our analysis
		// purposes
		proofGenerator.applyFINVCut( finv, thisLinearProgram);

		return finv;
	}

	public void run( String inputFilename ) {
		try {

			dLParser fileParser = parseInput( inputFilename );
			HybridProgram parsedProgram = fileParser.parsedStructure.extractFirstHybridProgram();

			List<ContinuousProgram> continuousBlocks = fileParser.parsedStructure.extractContinuousBlocks();

			ArrayList<dLFormula> forwardInvariants = new ArrayList<>();
			proofGenerator = new ProofGenerator( inputFilename );

			HashMap<dLFormula, HybridProgram> annotationBinding = inferAnnotationPairings( parsedProgram, fileParser.annotations );

			for ( dLFormula annotation : fileParser.annotations ) {
				linearityTactic( annotationBinding<annotation>, annotation );
			}

			//linearityTactic( continuousBlocks, fileParser.annotations );


			proofGenerator.close();


		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	protected dLParser parseInput( String inputFilename ) throws Exception {	
		FileReader inputReader = new FileReader( inputFilename );
		dLLexer filedLLexer = new dLLexer( inputReader );
		dLParser fileParser = new dLParser( filedLLexer );

		fileParser.parse();
		System.out.println( "PARSED: " + fileParser.parsedStructure.toKeYmaeraString() );
		System.out.println("Continuous blocks================================================");
		ArrayList<ContinuousProgram> continuousBlocks = fileParser.parsedStructure.extractContinuousBlocks();
		System.out.println("Continuous blocks found: " + continuousBlocks.size() );

		Iterator<ContinuousProgram> cbit = continuousBlocks.iterator();

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

		return fileParser;
	}
	
	protected List<dLFormula> simulationTactic( List<ContinuousProgram> continuousBlocks ) {
		return null;
			//	// Simulation-driven stuff
			//	System.out.println("Writing to dynsys.m file...");
			//	MatlabSimulationKit.generateDynsysFile( fileParser.parsedStructure.extractFirstHybridProgram(), fileParser.annotations.get(0) );
			//	System.out.println("Writing to problemstatement.m file...");
			//	MatlabSimulationKit.generateProblemStatementFile( fileParser.annotations, 1);

			//	ProcessBuilder builder = new ProcessBuilder("matlab", "-nodesktop", "-nosplash",
			//					"< manticore/matlabsimulationkit/run.m");
			//	builder.redirectErrorStream(true);
			//	Process process = builder.start();
			//	InputStream stdout = process.getInputStream();
			//	BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));


			//	
			//	String lyapunovCandidate = "";
			//	Double levelset = 0.0;
			//	String line;
			//	Pattern lyapunovCandidatePattern = Pattern.compile("V = (.+)");
			//	Pattern levelsetPattern = Pattern.compile("Optimized levelset size: (\\d+.?\\d*)");
			//	while ((line = reader.readLine()) != null) {
			//		if ( true ) {
			//			System.out.println ("Matlab output: " + line);
			//		}

			//		Matcher lyapunovCandidateMatcher = lyapunovCandidatePattern.matcher( line );
			//		Matcher levelsetMatcher = levelsetPattern.matcher( line );

			//		if ( lyapunovCandidateMatcher.find() ) {
			//			lyapunovCandidate = lyapunovCandidateMatcher.group(1);

			//			lyapunovCandidate = lyapunovCandidate.replace("V = ", "");
			//			lyapunovCandidate = lyapunovCandidate.replace(">","");
			//		}

			//		if ( levelsetMatcher.find() ) {
			//			levelset = Double.parseDouble(levelsetMatcher.group(1));
			//		}
			//	}

			//	System.out.println("Lyapunov candidate: " + lyapunovCandidate );
			//	System.out.println("Level: " + levelset );
			//	ComparisonFormula invariant = new ComparisonFormula(new Operator("<"),
			//						(Term)runParser( lyapunovCandidate ),
			//						new Real( levelset.toString() ) );
			//	AndFormula finvcut = new AndFormula( invariant, 
			//				MatlabSimulationKit.getNonBallPortion( fileParser.annotations.get(0) ) );
			//

			//	System.out.println("Generating partial proof file...");

			//	ProofGenerator proofGenerator = new ProofGenerator( args[0] );
			//	proofGenerator.applyFINVCut( finvcut,
			//				fileParser.parsedStructure.extractFirstHybridProgram() );
	}

	protected List<dLFormula> sosTactic( List<ContinuousProgram> continuousBlocks ) {
		return null;
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
}

