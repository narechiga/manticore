import java.io.*;
import java.util.*;
import java.util.regex.*;


class ProofGenerator {

	int nodeNumber; // Which KeYmaera likes, for some reason that I do not fully understand
	int numberOfDeclarations = 0;

	public ProofGenerator() {
		this.nodeNumber = 1;
	}

	/* Write the partial proof file */
	public void applyFirstCut( String hybridInvariant, String inputFilename ){
		applyFirstCut( hybridInvariant, inputFilename, generateDefaultOutputFilename(inputFilename) );
	}
	private String generateDefaultOutputFilename( String inputFilename ) {
		return inputFilename.concat(".partial.proof");
	}
	public void applyFirstCut( String hybridInvariant, String inputFilename, String outputFilename) {

		try {
			File inputFile = new File( inputFilename );
			Scanner inputScanner = new Scanner( inputFile );
			System.out.println("Will print partial proof file to: " + outputFilename );
			PrintWriter proofWriter = new PrintWriter( outputFilename );

			copyProblemStatement( inputScanner, proofWriter );
			
			applyPreprocessingRules( inputFilename, proofWriter );
			applyHCut( hybridInvariant, proofWriter );
			proofWriter.println(")");
			proofWriter.println("}");
			proofWriter.flush(); proofWriter.close();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}

	public void applyHCut( String hybridInvariant, PrintWriter proofWriter ) {
		proofWriter.println("(rule \"FInvCut\" (formula \"2\") (inst \"#finvariant="
					+hybridInvariant+"\"))");

		proofWriter.println("(branch \" Invariant holds\"");
		for ( int i = 0; i < numberOfDeclarations - 1; i++ ) {
			proofWriter.println("\t(rule \"all_right\" (formula \"2\") )");
			this.nodeNumber = this.nodeNumber + 1;
			proofWriter.println("\t(builtin \"Update Simplification\" (formula \"2\") )");
		}
		//Apparently, the last one does not require an accompanying "Update simplification".
		proofWriter.println("\t(rule \"all_right\" (formula \"2\") )");
		proofWriter.println("\t(rule \"imp_right\" (formula \"2\") )");

		proofWriter.println(")");

		proofWriter.println("(branch \" Invariant implies safety\"");

		//for ( int i = 0; i < numberOfDeclarations - 1; i++ ) {
		//	proofWriter.println("\t(rule \"all_right\" (formula \"2\") )");
		//	this.nodeNumber = this.nodeNumber + 1;
		//	proofWriter.println("\t(builtin \"Update Simplification\" (formula \"2\"))");
		//}
		proofWriter.println("\t(builtin \"Update Simplification\" (formula \"2\"))");
		//Apparently, the last one does not require an accompanying "Update simplification".
		proofWriter.println("\t(rule \"all_right\" (formula \"2\") )");
		proofWriter.println("(rule \"imp_right\" (formula \"2\") )");

		// Totally worth it when it works!
		proofWriter.println("(builtin \"Eliminate Universal Quantifiers\" (quantifierEliminator \"Mathematica\") )");
		proofWriter.println(")");

		proofWriter.println("(branch \" Remaining states are safe\"");
		proofWriter.println("\t(builtin \"Update Simplification\" (formula \"1\"))");
		proofWriter.println("\t(rule \"simplify_form\" (formula \"1\") )");
		proofWriter.println(")");

	}

	public void applyPreprocessingRules( String inputFilename, PrintWriter proofWriter ) {
		System.out.println("TODO: ProofWriter should have access to parser data, to make better decisions");
		System.out.println("TODO: For that matter, a full-on representation of the proof tree might be in order");
		try{
		File inputFile = new File( inputFilename );
		Scanner inputScanner = new Scanner( inputFile );
		String preamble = "";

		/*
		 *Step 0: Detect problem beginning (don't detect braces in rule declarations and so on)
		 *Step 1: Detect beginning of preamble
		 *Step 2: Count declarations and preambles, until 
		 *Step 3: Write the appropriate number of rules:
		 *        -> split box, eliminate declarations--how many?
		 *        -> split box, perform assignments--how many?
		 *        -> assume right (to process implication)
		 */
		final int INIT = 0;
		final int PROBLEMSTART = 1;
		final int PREAMBLESTART = 2;
		final int PREAMBLEEND = 3;
		int STATE = 0;

		/* TODO: Support multiple assignments on the same line */
		Pattern problemStartPattern = Pattern.compile(Pattern.quote("\\problem"));
		Pattern preambleStartPattern = Pattern.compile(Pattern.quote("\\["));
		Pattern declarationPattern = Pattern.compile("R(\\s)+(.)+;");
		Pattern assignmentPattern = Pattern.compile("(.)+:=[^;]+;");
		Pattern preambleEndPattern = Pattern.compile(Pattern.quote("\\]"));

		Matcher problemStartMatcher;
		Matcher preambleStartMatcher; Matcher declarationMatcher;
		Matcher assignmentMatcher; Matcher preambleEndMatcher;

		String thisString = "";
		//int numberOfDeclarations = 0; Already declared as global above
		int numberOfAssignments = 0;

		while ( inputScanner.hasNextLine() ){
			thisString = inputScanner.nextLine();
			problemStartMatcher	=	problemStartPattern.matcher( thisString );
			preambleStartMatcher	=	preambleStartPattern.matcher( thisString );
			declarationMatcher	=	declarationPattern.matcher( thisString );
			assignmentMatcher	=	assignmentPattern.matcher( thisString );
			preambleEndMatcher	=	preambleEndPattern.matcher( thisString );

			if ( STATE == INIT ) {
				if ( problemStartMatcher.find() ) {
					System.out.println("Found beginning of problem statement: "+problemStartMatcher.group() );
					STATE = PROBLEMSTART;
				}
			}

			if ( STATE == PROBLEMSTART ) {
				if ( preambleStartMatcher.find() ) {
					System.out.println("Found beginning of preamble: "+ preambleStartMatcher.group() );
					STATE = PREAMBLESTART;
				}
			}//end-if: INIT

			if ( STATE == PREAMBLESTART ) {
				while ( declarationMatcher.find() ) {
					numberOfDeclarations = numberOfDeclarations + 1;
					System.out.println("Found declaration: "+declarationMatcher.group() );
				}
				while ( assignmentMatcher.find() ) {
					numberOfAssignments = numberOfAssignments + 1;
					System.out.println("Found assignment: "+assignmentMatcher.group() );
				}
				if ( preambleEndMatcher.find () ) {
					System.out.println("Found end of preamble: "+preambleEndMatcher.group() );
					STATE = PREAMBLEEND;
				}
			}//end-if: PREAMBLESTART

			if ( STATE == PREAMBLEEND ) {
				System.out.println("Breaking parse loop...");
				break;
			}//end-if: PREAMBLEEND

		}//end-while


		proofWriter.println("\\proof{");
		proofWriter.println("(branch \"root\"");
		for ( int i = 0; i < numberOfDeclarations; i++ ) {
			proofWriter.println("(rule \"modality_split_right\" (formula \"1\"))");
			proofWriter.println("(rule \"eliminate_variable_decl\" (formula \"1\"))");
		}

		//// Get the last one
		//proofWriter.println("(rule \"eliminate_variable_decl\" (formula \"1\"))");

		for ( int i = 0; i < numberOfAssignments; i++ ) {
			proofWriter.println("(rule \"modality_split_right\" (formula \"1\") )"); 
			proofWriter.println( "(rule \"assignment_to_update_right\" (formula \"1\"))");
		}
		proofWriter.println("(rule \"imp_right\" (formula \"1\") )");

		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}

	// TODO: Add in declarations of barrier certs, hybrid cuts, and so on
	public void copyProblemStatement( Scanner inputScanner, PrintWriter proofWriter ){
		String thisString = "";
		boolean skipThis = false;
		Pattern annotationStartPattern = Pattern.compile(Pattern.quote("\\annotations"));
		Pattern annotationEndPattern = Pattern.compile(Pattern.quote("}"));

		while( inputScanner.hasNextLine() ) {
			thisString = inputScanner.nextLine();

			// Skip annotations!
			Matcher annotationStartMatcher	=	annotationStartPattern.matcher( thisString );
			Matcher annotationEndMatcher	=	annotationEndPattern.matcher( thisString );
			if ( annotationStartMatcher.find() ) {
				skipThis = true;
			}


			// Copy stuff
			if ( !skipThis ) {
				proofWriter.println( thisString  );
				System.out.println("Copying string: "+thisString);
			} else {
				System.out.println("Skipping string: "+thisString);

			}

			// Stop skipping after annotation
			if ( annotationEndMatcher.find() ) {
				skipThis = false;
			}

			
		}
	}


}
