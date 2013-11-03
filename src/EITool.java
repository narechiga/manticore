import java.io.*;
import java.util.*;

class EITool {

	public static void main( String [] args ) {

		System.out.println("Hello world!");

		if ( args.length < 1 ) {
			System.out.println("No input file given.");
			System.exit(1);
		} else if ( args.length > 1 ) {
			System.out.println("Too many arguments.");
			System.exit(1);
		} else {
			System.out.println("Input argument is: " + args[0] );
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

	} 

}
