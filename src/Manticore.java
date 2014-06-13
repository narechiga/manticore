import java.io.*;
import java.util.*;

class Manticore {

	public static void main( String [] args ) {

		System.out.println("This is Manticore, with a human head and the body of a lion");

		//if ( args.length < 1 ) {
		//	System.out.println("No input file given.");
		//	System.exit(1);
		//} else if ( args.length > 1 ) {
		//	System.out.println("Too many arguments.");
		//	System.exit(1);
		//} else {
		//	System.out.println("Input argument is: " + args[0] );
		//}

	        String input = null;
                Scanner in = new Scanner( System.in );

                try {
                        do {
                                System.out.print("INPUT: ");
                                input = in.nextLine() + "\n";
                                StringReader inreader = new StringReader( input );
                                Lexer myLexer = new Lexer( inreader );
                                YYParser myParser = new YYParser( myLexer );
                                myParser.parse();

                        } while ( input != null );
                } catch ( Exception e ) { 
                        System.err.println( e );
                }   
		
		/* * * Everything below is still in testing! * * */
		/* Parse the input file */	
		Mode myMode = new Mode("M1");
		try {
			KeYmaeraParser myParser = new KeYmaeraParser( args[0] );

			LinkedList<String> listOfModes = myParser.parseStableModes();
			System.out.println("Stable modes are: ");
			System.out.println(listOfModes);
			
			myParser.parseODEs( myMode );
			myParser.parseGuardList( myMode );

			myParser.die();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}

		/* Generate an SoS invariant */
		/* * * Artificially generate a mode, because mode parsing is not yet functional * * */
		//Mode myMode = new Mode("M1");
		//myMode.addVariable("x1"); myMode.addVariable("x2"); myMode.addVariable("x3");
		//myMode.addODE("(-x1^3 - x1*x3^2)*(x3^2 + 1)");
		//myMode.addODE("(-x2 - x1^2*x2)*(x3^2 + 1)");
		//myMode.addODE("(-x3 + 3*x1^2*x3)*(x3^2 + 1) - 3*x3");

		//myMode.addVariable("firstVar"); myMode.addVariable("secondVar");
		//myMode.addODE("secondVar"); myMode.addODE("-firstVar");

		SoSLyapunovCandidateGenerator candidateGenerator = new SoSLyapunovCandidateGenerator( myMode );
		MathematicaCandidateValidator candidateValidator = new MathematicaCandidateValidator( myMode );

		String lyapCandidate = candidateGenerator.generateCandidate();
		System.out.println("A candidate Lyapunov function is: " + lyapCandidate);

		/* Conditions can be checked separately */
		/*candidateValidator.checkLyapunovPositivity( lyapCandidate );
		candidateValidator.checkLyapunovDerivativeNegativity( lyapCandidate );*/
		/* or all at once */

		boolean isLyapunov = candidateValidator.checkLyapunov( lyapCandidate );
		System.out.println("The result of checking the Lyapunov conditions is: " + isLyapunov);

		System.out.println("I will now try to find a sublevel set that excludes the mode guard.");
		String level = MathematicaSublevelSetFinder.findSublevelSet( myMode, lyapCandidate );

		String hybridInvariant = "("+lyapCandidate+" - "+level+" < 0 ) & M = " + myMode.modeID;
		System.out.println("Hybrid invariant is: " + hybridInvariant);

		ProofGenerator myProofGenerator = new ProofGenerator();
		myProofGenerator.applyFirstCut( hybridInvariant, args[0] );
		//myProofGenerator.writePartialProof( args[0] );
		//my

	} 

}
