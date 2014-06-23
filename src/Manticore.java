import java.io.*;
import java.util.*;
import manticore.dl.*;

class Manticore {

	public static void main( String [] args ) {

		System.out.println("This is Manticore, a strategy engine for the theorem prover KeYmaera");

		if ( args.length < 1 ) {
			System.out.println("No input file given, running parser test loop");
	        	String input = null;
                	Scanner in = new Scanner( System.in );
			while (true) {
                		try {
                		        System.out.print("INPUT: ");
                		        input = in.nextLine() + "\n";
                		        StringReader inreader = new StringReader( input );
                		        Lexer myLexer = new Lexer( inreader );
                		        YYParser myParser = new YYParser( myLexer );
                		        myParser.parse();

					System.out.println( "PARSED: " + myParser.parsedStructure.toKeYmaeraString() );
					System.out.println("(keymaera string)");

					if ( myParser.parsedStructure instanceof HybridProgram ) {
						
						HybridProgram parsedProgram = (HybridProgram)myParser.parsedStructure;

						System.out.println("Hybrid Program Data==============================================");
						System.out.println("Is purely continuous: " + parsedProgram.isPurelyContinuous() );
						System.out.println("Is purely discrete: " + parsedProgram.isPurelyDiscrete() );
						System.out.println("Is hybrid: " + parsedProgram.isHybrid() );
						System.out.println("=================================================================");
						
					}

					System.out.println("Continuous blocks================================================");
					ArrayList<ContinuousProgram> continuousblocks = myParser.parsedStructure.extractContinuousBlocks();
					System.out.println("Continuous blocks found: " + continuousblocks.size() );
					System.out.println( continuousblocks );
					System.out.println("=================================================================");

					System.out.println("The variables that occur in this structure are:");
					System.out.println( myParser.parsedStructure.getVariables() );


                		} catch ( Exception e ) { 
					System.out.println("Error running parser test loop.");
                		        System.err.println( e );
                		}   
			}
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

}
