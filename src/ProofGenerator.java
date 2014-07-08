import java.io.*;
import java.util.*;
import java.util.regex.*;


class ProofGenerator {

	int nodeNumber; // Which KeYmaera likes, for some reason that I do not fully undrestand

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

			//printSettings( proofWriter );
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
		proofWriter.println("(rule \"HybridCut\" (formula \"2\") (inst \"#hybridinvariant="+hybridInvariant+"\") (userinteraction) (nodenum \""+nodeNumber+"\"))");
		this.nodeNumber = this.nodeNumber + 1;
	}

	public void applyPreprocessingRules( String inputFilename, PrintWriter proofWriter ) {
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
		int numberOfDeclarations = 0;
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
		proofWriter.println("(branch \"dummy ID\"");
		for ( int i = 0; i < numberOfDeclarations; i++ ) {
			proofWriter.println("(rule \"modality_split_right\" (formula \"1\") (userinteraction) (nodenum \"" +nodeNumber+"\"))");
			this.nodeNumber = this.nodeNumber + 1;
			proofWriter.println("(rule \"eliminate_variable_decl\" (formula \"1\") (userinteraction) (nodenum \""+nodeNumber+"\"))");
			this.nodeNumber = this.nodeNumber + 1;
		}

		for ( int i = 0; i < numberOfAssignments; i++ ) {
			proofWriter.println("(rule \"modality_split_right\" (formula \"1\") (userinteraction) (nodenum \"" +nodeNumber+"\"))");
			this.nodeNumber = this.nodeNumber + 1;
			proofWriter.println("(rule \"assignment_to_update_right\" (formula \"1\") (userinteraction) (nodenum \""+nodeNumber+"\"))");
			this.nodeNumber = this.nodeNumber + 1;
		}
		proofWriter.println("(rule \"imp_right\" (formula \"1\") (userinteraction) (nodenum \""+nodeNumber+"\"))");
		this.nodeNumber = this.nodeNumber + 1;

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

	public void printSettings( PrintWriter proofWriter ) {
		proofWriter.println("\\settings {");
		proofWriter.println("\"#Proof-Settings-Config-File");
		proofWriter.println("#Wed Oct 02 19:30:01 EDT 2013");
		proofWriter.println("[DLOptions]counterExampleGenerator=Mathematica");
		proofWriter.println("[DecisionProcedure]Exec=");
		proofWriter.println("[DLOptions]termFactoryClass=de.uka.ilkd.key.dl.model.impl.TermFactoryImpl");
		proofWriter.println("[Z3Options]z3ElimExPrefix=true");
		proofWriter.println("[ReduceOptions]qepcadFallback=false");
		proofWriter.println("[MathematicaOptions]eliminateFractions=false");
		proofWriter.println("[TacletTranslation]filename=");
		proofWriter.println("[Libraries]Default=/home/nikos/.keymaera/libraries/stringRules.key-false, /home/nikos/.keymaera/libraries/deprecatedRules.key-false, /home/nikos/.keymaera/libraries/acc.key-false");
		proofWriter.println("[StrategyProperty]USER_TACLETS_OPTIONS_KEY3=USER_TACLETS_OFF");
		proofWriter.println("[DLOptions]BuiltInArithmeticIneqs=OFF");
		proofWriter.println("[ReduceOptions]rlcadpbfvs=DEFAULT");
		proofWriter.println("[ReduceOptions]rlcadaproj=DEFAULT");
		proofWriter.println("[StrategyProperty]USER_TACLETS_OPTIONS_KEY2=USER_TACLETS_OFF");
		proofWriter.println("[StrategyProperty]USER_TACLETS_OPTIONS_KEY1=USER_TACLETS_OFF");
		proofWriter.println("[General]ProofAssistant=false");
		proofWriter.println("[ReduceOptions]rlcadprojonly=DEFAULT");
		proofWriter.println("[DLOptions]reduceOnFreshBranch=false");
		proofWriter.println("[StrategyProperty]LOOP_OPTIONS_KEY=LOOP_INVARIANT");
		proofWriter.println("[ReduceOptions]rlqeqsc=DEFAULT");
		proofWriter.println("[ReduceOptions]rlcadbaseonly=DEFAULT");
		proofWriter.println("[DLOptions]simplifyAfterReduce=false");
		proofWriter.println("[DLOptions]readdQuantifiers=true");
		proofWriter.println("[DecisionProcedure]ActiveRule=_noname_");
		proofWriter.println("[ReduceOptions]rlcadrawformula=DEFAULT");
		proofWriter.println("[DLOptions]applyLocalSimplify=false");
		proofWriter.println("[SimultaneousUpdateSimplifier]DeleteEffectLessLocations=true");
		proofWriter.println("[ReduceOptions]quantifierEliminationMethod=RLQE");
		proofWriter.println("[DLOptions]DiffSat=AUTO");
		proofWriter.println("[DLOptions]simplifier=Mathematica");
		proofWriter.println("[StrategyProperty]QUERY_OPTIONS_KEY=QUERY_NONE");
		proofWriter.println("[StrategyProperty]QUANTIFIERS_OPTIONS_KEY=QUANTIFIERS_NON_SPLITTING_WITH_PROGS");
		proofWriter.println("[DLOptions]simplifyAfterODESolve=false");
		proofWriter.println("[ReduceOptions]rlall=false");
		proofWriter.println("[MetiTarskiOptions]metitBacktracking=true");
		proofWriter.println("[DLOptions]diffSatTimeout=4000");
		proofWriter.println("[DLOptions]linearTimeoutIncreaseFactor=2");
		proofWriter.println("[ReduceOptions]groebnerBasis=false");
		proofWriter.println("[DecisionProcedure]Timeout=600");
		proofWriter.println("[ReduceOptions]rlqesqsc=DEFAULT");
		proofWriter.println("[General]SoundNotification=false");
		proofWriter.println("[StrategyProperty]STOPMODE_OPTIONS_KEY=STOPMODE_DEFAULT");
		proofWriter.println("[Choice]DefaultChoices=throughout-throughout\\\\:toutOn , transactions-transactions\\\\:transactionsOn , intRules-intRules\\\\:arithmeticSemanticsIgnoringOF , programRules-programRules\\\\:Java , initialisation-initialisation\\\\:disableStaticInitialisation , transactionAbort-transactionAbort\\\\:abortOn , nullPointerPolicy-nullPointerPolicy\\\\:nullCheck , javacard-javacard\\\\:jcOff");
		proofWriter.println("[ReduceOptions]rlsimpl=ON");
		proofWriter.println("[ReduceOptions]rlcadextonly=DEFAULT");
		proofWriter.println("[Strategy]MaximumNumberOfAutomaticApplications=2000");
		proofWriter.println("[ReduceOptions]rlanuexsgnopt=DEFAULT");
		proofWriter.println("[ReduceOptions]rlcadpartial=DEFAULT");
		proofWriter.println("[General]UseJML=true");
		proofWriter.println("[DLOptions]useSOS=false");
		proofWriter.println("[MathematicaOptions]useEliminateList=true");
		proofWriter.println("[DecisionProcedure]savefile=false");
		proofWriter.println("[DLOptions]applyGammaRules=ONLY_TO_MODALITIES");
		proofWriter.println("[DecisionProcedure]showSMTResDialog=false");
		proofWriter.println("[TacletTranslation]assignment=11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
		proofWriter.println("[General]StupidMode=true");
		proofWriter.println("[DLOptions]constantTimeoutIncreaseFactor=0");
		proofWriter.println("[DLOptions]solveODE=true");
		proofWriter.println("[StrategyProperty]METHOD_OPTIONS_KEY=METHOD_EXPAND");
		proofWriter.println("[DLOptions]loopSatTimeout=2000000");
		proofWriter.println("[DLOptions]odeSolver=Mathematica");
		proofWriter.println("[OrbitalOptions]precision=34");
		proofWriter.println("[HOLLightOptions]qeMethod=ProofProducing");
		proofWriter.println("[ReduceOptions]rlqedfs=DEFAULT");
		proofWriter.println("[HOLLightOptions]useSnapshots=false");
		proofWriter.println("[QepcadOptions]qepcadMemoryLimit=2000000");
		proofWriter.println("[ReduceOptions]rlqeheu=DEFAULT");
		proofWriter.println("[ReduceOptions]rlanuexpsremseq=DEFAULT");
		proofWriter.println("[DecisionProcedure]multprovers=Z3\\\\=true\\\\:Yices\\\\=true\\\\:Simplify\\\\=true\\\\:CVC3\\\\=true");
		proofWriter.println("[ReduceOptions]rlcadfulldimonly=DEFAULT");
		proofWriter.println("[DLOptions]csdpForceInternal=false");
		proofWriter.println("[DLOptions]invariantRule=QUANTIFIERS");
		proofWriter.println("[General]DnDDirectionSensitive=true");
		proofWriter.println("[DLOptions]simplifyBeforeReduce=false");
		proofWriter.println("[Z3Options]z3Prenex=true");
		proofWriter.println("[DLOptions]applyToModality=false");
		proofWriter.println("[DLOptions]initialTimeout=2000");
		proofWriter.println("[ReduceOptions]rlcadaprojalways=DEFAULT");
		proofWriter.println("[DLOptions]CexFinder=ITER_DEEP");
		proofWriter.println("[DLOptions]useIterativeReduceRule=false");
		proofWriter.println("[Choice]Choices=transactions-transactions\\\\:transactionsOn-transactions\\\\:transactionsOff , throughout-throughout\\\\:toutOn-throughout\\\\:toutOff , programRules-programRules\\\\:Java-programRules\\\\:ODL-programRules\\\\:dL , intRules-intRules\\\\:javaSemantics-intRules\\\\:arithmeticSemanticsIgnoringOF-intRules\\\\:arithmeticSemanticsCheckingOF , initialisation-initialisation\\\\:enableStaticInitialisation-initialisation\\\\:disableStaticInitialisation , transactionAbort-transactionAbort\\\\:abortOn-transactionAbort\\\\:abortOff , nullPointerPolicy-nullPointerPolicy\\\\:noNullCheck-nullPointerPolicy\\\\:nullCheck , javacard-javacard\\\\:jcOff-javacard\\\\:jcOn");
		proofWriter.println("[CohenhormanderOptions]eliminatorMode=DNF");
		proofWriter.println("[DLOptions]percentOfPowersetForIterativeReduce=70");
		proofWriter.println("[hints]used=");
		proofWriter.println("[ReduceOptions]rlcadhongproj=DEFAULT");
		proofWriter.println("[ReduceOptions]rlanuexgcdnormalize=DEFAULT");
		proofWriter.println("[MathematicaOptions]memoryConstraint=-1");
		proofWriter.println("[ReduceOptions]rlcadtrimtree=DEFAULT");
		proofWriter.println("[DLOptions]groebnerBasisCalculator=Mathematica");
		proofWriter.println("[ReduceOptions]rlcadfac=DEFAULT");
		proofWriter.println("[Strategy]Timeout=-1");
		proofWriter.println("[DLOptions]applyGlobalReduce=true");
		proofWriter.println("[OrbitalOptions]representation=big");
		proofWriter.println("[SimultaneousUpdateSimplifier]EagerSimplification=true");
		proofWriter.println("[TacletTranslation]maxGeneric=2");
		proofWriter.println("[DLOptions]ibcOnlyToFO=true");
		proofWriter.println("[DLOptions]TracerStat=OFF");
		proofWriter.println("[TacletTranslation]saveToFile=false");
		proofWriter.println("[DLOptions]BuiltInArithmetic=OFF");
		proofWriter.println("[DLOptions]counterexampleTest=ON");
		proofWriter.println("[StrategyProperty]GOALCHOOSER_OPTIONS_KEY=GOALCHOOSER_DEFAULT");
		proofWriter.println("[DecisionProcedure]WaitForAllProvers=false");
		proofWriter.println("[DLOptions]useODEIndFinMethods=false");
		proofWriter.println("[StrategyProperty]NON_LIN_ARITH_OPTIONS_KEY=NON_LIN_ARITH_NONE");
		proofWriter.println("[ReduceOptions]rlcadisoallroots=DEFAULT");
		proofWriter.println("[ReduceOptions]rlnzden=ON");
		proofWriter.println("[MathematicaOptions]quantifierEliminationMethod=REDUCE");
		proofWriter.println("[StrategyProperty]SPLITTING_OPTIONS_KEY=SPLITTING_DELAYED");
		proofWriter.println("[ReduceOptions]eliminateFractions=false");
		proofWriter.println("[DecisionProcedure]WeakenSMTTranslation=false");
		proofWriter.println("[ReduceOptions]rlcadte=DEFAULT");
		proofWriter.println("[DLOptions]ignoreAnnotations=false");
		proofWriter.println("[StrategyProperty]VBT_PHASE=VBT_SYM_EX");
		proofWriter.println("[DLOptions]sosChecker=HOL Light");
		proofWriter.println("[General]UseOCL=false");
		proofWriter.println("[OrbitalOptions]sparsePolynomials=true");
		proofWriter.println("[ReduceOptions]rlposden=DEFAULT");
		proofWriter.println("[DLOptions]simplifyTimeout=0");
		proofWriter.println("[Strategy]ActiveStrategy=DLStrategy");
		proofWriter.println("[DLOptions]usePowersetIterativeReduce=true");
		proofWriter.println("[DLOptions]quadricTimeoutIncreaseFactor=0");
		proofWriter.println("[DLOptions]FOStrategy=LAZY");
		proofWriter.println("[ReduceOptions]rlqepnf=DEFAULT");
		proofWriter.println("[DLOptions]applyLocalReduce=OFF");
		proofWriter.println("[DLOptions]quantifierEliminator=Mathematica");
		proofWriter.println("\"");
		proofWriter.println("}");
	}

}
