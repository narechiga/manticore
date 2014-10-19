import java.io.*;
import java.util.*;
import java.util.regex.*;

import proteus.dl.syntax.*;

class ProofGenerator {

	PrintWriter proofWriter;
	HybridProgram thisProgram;
	String inputFilename;

	int numberOfDeclarations = 0;
	int numberOfAssignments = 0;

	int cutsApplied = 0;

	public ProofGenerator( String inputFilename ) {
		String partialProofFileName = inputFilename.concat(".partial.proof.key");

		this.inputFilename = inputFilename;

		try {
			//System.out.println("Will print partial proof file to: " + partialProofFileName );
			proofWriter = new PrintWriter( partialProofFileName );

			copyProblemStatement();
			applyPreprocessingRules();
			
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
		
			for ( int i = 0;  i < cutsApplied; i++ ) {	
				proofWriter.println(")");
			}

			proofWriter.println(")");
			proofWriter.println("}");
			proofWriter.flush(); proofWriter.close();

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}


	public void applyFINVCut( dLFormula finv, HybridProgram program ) {

		Set<RealVariable> programVariables = program.getDynamicVariables();

		proofWriter.println("(rule \"FInvCut\" (formula \"2\") (inst \"#finvariant="
					+finv.toKeYmaeraString() +"\"))");

		/* --------------------------- Invariance ---------------------------------------------------------------------*/
		proofWriter.println("(branch \" Invariant holds\"");
		for ( RealVariable var : programVariables ) {
			proofWriter.println("\t(rule \"all_right\" (formula \"2\") )");
		}
		proofWriter.println("\t(builtin \"Update Simplification\" (formula \"2\") )");
		proofWriter.println("\t(rule \"imp_right\" (formula \"2\") )");

		proofWriter.println(")");

		/* --------------------------- Safety ---------------------------------------------------------------------*/
		proofWriter.println("(branch \" Invariant implies safety\"");
		for ( RealVariable var : programVariables ) {
			proofWriter.println("\t(rule \"all_right\" (formula \"2\") )");
		}
		proofWriter.println("\t(builtin \"Update Simplification\" (formula \"2\") )");
		proofWriter.println("\t(rule \"imp_right\" (formula \"2\") )");
		// Totally worth it when it works!
		proofWriter.println("\t(builtin \"Eliminate Universal Quantifiers\" (quantifierEliminator \"Mathematica\") )");
		proofWriter.println(")");

		/* --------------------------- Remaining states ---------------------------------------------------------------------*/
		proofWriter.println("(branch \" Remaining states are safe\"");
		proofWriter.println("\t(builtin \"Update Simplification\" (formula \"1\"))");
		proofWriter.println("\t(rule \"simplify_form\" (formula \"1\") )");

		cutsApplied = cutsApplied + 1;

	}

	public void applyDiffInvCut( dLFormula finv, HybridProgram program ) {

		Set<RealVariable> programVariables = program.getDynamicVariables();

		proofWriter.println("(rule \"FInvCut\" (formula \"2\") (inst \"#finvariant="
					+finv.toKeYmaeraString() +"\"))");

		/* --------------------------- Invariance ---------------------------------------------------------------------*/
		proofWriter.println("(branch \" Invariant holds\"");
		for ( RealVariable var : programVariables ) {
			proofWriter.println("\t(rule \"all_right\" (formula \"2\") )");
		}
		proofWriter.println("\t(builtin \"Update Simplification\" (formula \"2\") )");
		proofWriter.println("\t(rule \"imp_right\" (formula \"2\") )");

		proofWriter.println(")");

		/* --------------------------- Safety ---------------------------------------------------------------------*/
		proofWriter.println("(branch \" Invariant implies safety\"");
		for ( RealVariable var : programVariables ) {
			proofWriter.println("\t(rule \"all_right\" (formula \"2\") )");
		}
		proofWriter.println("\t(builtin \"Update Simplification\" (formula \"2\") )");
		proofWriter.println("\t(rule \"imp_right\" (formula \"2\") )");
		// Totally worth it when it works!
		proofWriter.println("\t(builtin \"Eliminate Universal Quantifiers\" (quantifierEliminator \"Mathematica\") )");
		proofWriter.println(")");

		/* --------------------------- Remaining states ---------------------------------------------------------------------*/
		proofWriter.println("(branch \" Remaining states are safe\"");
		proofWriter.println("\t(builtin \"Update Simplification\" (formula \"1\"))");
		proofWriter.println("\t(rule \"simplify_form\" (formula \"1\") )");

		cutsApplied = cutsApplied + 1;

	}

	public void applyPreprocessingRules() {
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
		//int numberOfAssignments = 0;

		while ( inputScanner.hasNextLine() ){
			thisString = inputScanner.nextLine();
			problemStartMatcher	=	problemStartPattern.matcher( thisString );
			preambleStartMatcher	=	preambleStartPattern.matcher( thisString );
			declarationMatcher	=	declarationPattern.matcher( thisString );
			assignmentMatcher	=	assignmentPattern.matcher( thisString );
			preambleEndMatcher	=	preambleEndPattern.matcher( thisString );

			if ( STATE == INIT ) {
				if ( problemStartMatcher.find() ) {
					//System.out.println("Found beginning of problem statement: "+problemStartMatcher.group() );
					STATE = PROBLEMSTART;
				}
			}

			if ( STATE == PROBLEMSTART ) {
				if ( preambleStartMatcher.find() ) {
					//System.out.println("Found beginning of preamble: "+ preambleStartMatcher.group() );
					STATE = PREAMBLESTART;
				}
			}//end-if: INIT

			if ( STATE == PREAMBLESTART ) {
				while ( declarationMatcher.find() ) {
					numberOfDeclarations = numberOfDeclarations + 1;
					//System.out.println("Found declaration: "+declarationMatcher.group() );
				}
				while ( assignmentMatcher.find() ) {
					numberOfAssignments = numberOfAssignments + 1;
					//System.out.println("Found assignment: "+assignmentMatcher.group() );
				}
				if ( preambleEndMatcher.find () ) {
					//System.out.println("Found end of preamble: "+preambleEndMatcher.group() );
					STATE = PREAMBLEEND;
				}
			}//end-if: PREAMBLESTART

			if ( STATE == PREAMBLEEND ) {
				//System.out.println("Breaking parse loop...");
				break;
			}//end-if: PREAMBLEEND

		}//end-while


		proofWriter.println("\\proof{");
		proofWriter.println("(branch \"root\"");
		for ( int i = 0; i < numberOfDeclarations; i++ ) {
			proofWriter.println("(rule \"modality_split_right\" (formula \"1\"))");
			proofWriter.println("(rule \"eliminate_variable_decl\" (formula \"1\"))");
		}

		// Get the last one
		for ( int i = 0; i < numberOfAssignments; i++ ) {
			proofWriter.println("(rule \"modality_split_right\" (formula \"1\") )"); 
			proofWriter.println( "(rule \"assignment_to_update_right\" (formula \"1\"))");
		}
		proofWriter.println("(rule \"imp_right\" (formula \"1\") )");

		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}

	public void copyProblemStatement(){
		try {
			File inputFile = new File( inputFilename );
			Scanner inputScanner = new Scanner( inputFile );

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
				//System.out.println("Copying string: "+thisString);
			} else {
				//System.out.println("Skipping string: "+thisString);

			}

			// Stop skipping after annotation
			if ( annotationEndMatcher.find() ) {
				skipThis = false;
			}
		}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

			
	}


}
