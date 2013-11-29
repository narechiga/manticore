import java.io.*;
import java.util.*;
import java.util.regex.*;


public class KeYmaeraParser extends Parser {

	boolean debugMode;
	
	// Constructor
	public KeYmaeraParser ( String fileName) throws FileNotFoundException {
		this.setFileName(fileName);
		this.stableModeList = new LinkedList<String>();
		this.debugMode = false;
	 }
	public KeYmaeraParser ( String fileName, boolean debugMode ) throws FileNotFoundException {
		this.setFileName(fileName);
		this.stableModeList = new LinkedList<String>();
		this.debugMode = debugMode;
	 }

	// inputReader is the name of the BufferedReader that points to the file we want
	public LinkedList<String> parseVariableList() {
		LinkedList<String> variableList = new LinkedList<String>();
		variableList.addLast("variable!");

		return variableList;
	}

	public LinkedList<String> parseResetList( String modeID ){
		LinkedList<String> resetList = new LinkedList<String>();
		resetList.addLast("incoming!");

		return resetList;
	}

	public void parseGuardList( Mode thisMode ){
		LinkedList<String> guardList = new LinkedList<String>();

		/* Pattern matching setup */
		Pattern thisModeAlertPattern = Pattern.compile("M(\\s)*=(\\s)*" + thisMode.getID());
		Pattern modeAlertPattern = Pattern.compile("M(\\s)*=");
		LinkedList<Pattern> varPlusAssignStarPatterns = generateVarPlusAssignStarPatterns( thisMode );
		//LinkedList<Pattern> varPlusPatterns = generateVarPlusPatterns( thisMode );
		Pattern plusPattern = Pattern.compile(Pattern.quote("_plus"));
		Pattern questionMarkPattern = Pattern.compile(Pattern.quote("?"));
		Pattern openBracePattern = Pattern.compile("\\{");
		Pattern assignmentPattern = Pattern.compile(Pattern.quote(":="));
		System.out.println( varPlusAssignStarPatterns );

		/* Parser is a state machine */
		final int INIT = 0;
		final int GETSTARASSIGNS = 1;
		final int GETQUESTION = 2;
		final int GETGUARDS = 3;
		final int TERMINATE = 4;

		int STATE = 0;
		this.rewindFile();
		String line = "";
		
		try { line = inputReader.readLine(); } catch ( Exception ex ) { ex.printStackTrace(); }
		while( (line != null) & ( STATE != TERMINATE ) ) {
			System.out.println("Running parser loop...");

			Matcher thisModeAlert = thisModeAlertPattern.matcher( line );
			//Matcher modeAlert = modeAlertPattern.matcher( line );
			Matcher questionMark = questionMarkPattern.matcher( line );
			Matcher openBrace = openBracePattern.matcher( line );
			Matcher assignment = assignmentPattern.matcher( line );
			//Matcher odeStructure = odeStructurePattern.matcher( line );
			//Matcher closeBrace = closeBracePattern.matcher( line );
			//**
			LinkedList<Matcher> starAssign = new LinkedList<Matcher>();
			Iterator<Pattern> sap = varPlusAssignStarPatterns.iterator();
			while ( sap.hasNext() ) {
				starAssign.add( sap.next().matcher( line ) );
			}
			//**

			boolean thisModeAlertFlag = thisModeAlert.find();
			//boolean modeAlertFlag = modeAlert.find();
			boolean questionFlag = questionMark.find();
			boolean openBraceFlag = openBrace.find();
			boolean assignmentFlag = assignment.find();
			//boolean odeStructureFlag = odeStructure.find();
			//boolean closeBraceFlag = closeBrace.find();
			LinkedList<Boolean> starAssignFlags = new LinkedList<Boolean>();
			Iterator<Matcher> sa = starAssign.iterator();
			while ( sa.hasNext() ) {
				starAssignFlags.add( sa.next().find() );
			}

			if ( STATE == INIT ) {
				if ( thisModeAlertFlag ) {
					STATE = GETSTARASSIGNS;
					System.out.println("Transition to state GETSTARASSIGNS on input: ");
					System.out.println( line );
				}
			}

			if ( STATE == GETSTARASSIGNS ){
				if ( openBraceFlag ) {
					STATE = INIT;
					System.out.println("Transition to state INIT on input: ");
					System.out.println( line );
				}
				if ( orList(starAssignFlags) ) {
					STATE = GETQUESTION;
					System.out.println("Transition to state GETQUESTION on input: ");
					System.out.println( line );
				}
			}
			if ( STATE == GETQUESTION ) {
				if ( questionFlag ) {
					STATE = GETGUARDS;
					System.out.println("Transition to state GETGUARDS on input: ");
					System.out.println( line );
				}
			}
			if ( STATE == GETGUARDS ) {
				System.out.println( "In state GETGUARDS...");
				System.out.println( line );

				// split the string on ";"
				LinkedList<String> candidates = new LinkedList<String>();
				String [] components = line.split(";");
				for ( int i = 0; i < components.length; i = i + 1 ) {
					// discard bits that include ":="
					// discard bits that include "_plus"
					// --> TODO: in the future, be able to split logical connectives with things that include plus
					Matcher localAssignment = assignmentPattern.matcher( components[i] );
					Matcher localPlus = plusPattern.matcher( components[i] );
					Matcher localModeAlert = modeAlertPattern.matcher( components[i] );

					if( (!localAssignment.find() ) & (!localPlus.find()) & (!localModeAlert.find()) ) {
						thisMode.guards.add( components[i].replace("?",""));
						System.out.println("Detected guard: "+ components[i].replace("?","") );
					} else {
						System.out.println("Discarding string: " + components[i] );
					}
			
				}

				if ( assignmentFlag ) {
					STATE = TERMINATE;
					System.out.println("Transition to state TERMINATE on input: ");
					System.out.println( line );
				}
			}
		//	TODO: Resets actually requires looking at which modes you can come from
		//	if ( STATE == GETRESETS ) {
		//		// split the string on ";"
		//		LinkedList<String> candidates = new LinkedList<String>();
		//		String [] components = line.split(";");
		//		for ( int i = 0; i < components.length; i = i + 1 ) {
		//			// discard bits that include ":="
		//			// collect only bits that include "_plus"
		//			Matcher localAssignment = assignmentPattern.matcher( components[i] );
		//			Matcher localPlus = plusPattern.matcher( components[i] );
		//			Matcher localModeAlert = modeAlertPattern.matcher( components[i] );

		//			if( (!localAssignment.find() ) & (localPlus.find()) & (!localModeAlert.find()) ) {
		//				thisMode.guards.add( components[i].replace("?",""));
		//				System.out.println("Detected reset: "+ components[i].replace("?","") );
		//			} else {
		//				System.out.println("Discarding string: " + components[i] );
		//			}
		//	
		//		}


		//	}

			if ( STATE == TERMINATE ) {
				STATE = TERMINATE;
			}

			try { line = inputReader.readLine(); } catch ( Exception ex ) { ex.printStackTrace(); }
		}

	}

	private LinkedList<Pattern> generateVarPlusAssignStarPatterns( Mode thisMode ){
		LinkedList<Pattern> patterns = new LinkedList<Pattern>();
		Iterator<String> varIterator = thisMode.variables.iterator();

		String thisvarname = "";
		while ( varIterator.hasNext() ) {
			thisvarname = varIterator.next();
			patterns.add( Pattern.compile( thisvarname +"_plus(\\s)*:=(\\s)*.+;") );
		}

		return patterns;
	}

	private LinkedList<Pattern> generateVarPlusPatterns( Mode thisMode ) {
		LinkedList<Pattern> patterns = new LinkedList<Pattern>();
		Iterator<String> varIterator = thisMode.variables.iterator();

		String thisvarname = "";
		while ( varIterator.hasNext() ) {
			thisvarname = varIterator.next();
			patterns.add( Pattern.compile( thisvarname +"_plus") );
		}

		return patterns;
	}


	
	private boolean orList( LinkedList<Boolean> myList ) {
		boolean orresult = false;
		Iterator<Boolean> items = myList.iterator();

		while ( items.hasNext() ) {
			orresult = orresult | items.next();
		}

		return orresult;
	}

	public void parseODEs( Mode thisMode ) {
		//LinkedList<String> odeList = new LinkedList<String>();
		//odeList.addLast("ODE!");
		
		Pattern thisModeAlertPattern = Pattern.compile("M(\\s)*=(\\s)*" + thisMode.getID());
		Pattern modeAlertPattern = Pattern.compile("M(\\s)*=(\\s)*"); // So I know to stand down if another mode is being discussed
		Pattern openBracePattern = Pattern.compile("\\{");
		Pattern odeStructurePattern = Pattern.compile("(.)+'(\\s)*=(\\s)*.+"); // TODO: This won't allow multi line ODEs
		Pattern closeBracePattern = Pattern.compile("\\}");
		
		/* Designed as a state machine */
		final int INIT		=	0;	// The initial state
		final int ALERT		=	1;	// The state to go into when a pattern of the form M = <thisMode> is found
		final int ACQUIRE	=	2;	// The state to go into when "{" is found
		final int TERMINATE	=	3;	// The state to go into when ODEs are none are found.

		int STATE = 0;
		this.rewindFile();

		String line = "";
		try { line = inputReader.readLine(); } catch ( Exception ex ) { ex.printStackTrace(); }
		while( (line != null) & ( STATE != TERMINATE ) ) {

		Matcher thisModeAlert = thisModeAlertPattern.matcher( line );
		Matcher modeAlert = modeAlertPattern.matcher( line );
		Matcher openBrace = openBracePattern.matcher( line );
		Matcher odeStructure = odeStructurePattern.matcher( line );
		Matcher closeBrace = closeBracePattern.matcher( line );

		boolean thisModeAlertFlag = thisModeAlert.find();
		boolean modeAlertFlag = modeAlert.find();
		boolean openBraceFlag = openBrace.find();
		boolean odeStructureFlag = odeStructure.find();
		boolean closeBraceFlag = closeBrace.find();

			
		if ( STATE == INIT ) {

			if ( thisModeAlertFlag ) {
				System.out.println("Transitioning to state ALERT on input: " + thisModeAlert.group() );
				STATE = ALERT;
			}
		}

		if ( STATE == ALERT ) {
			System.out.println("Entered state ALERT, input is " + line );
			System.out.println("This mode alert: " + thisModeAlertFlag );
			System.out.println("Mode alert: " + modeAlertFlag );
			System.out.println("Open brace alert: " + openBraceFlag );

			if ( (!thisModeAlertFlag) & ( modeAlertFlag ) ) {
				// If the hybrid program decides it wants to talk about a different mode, relax.
				System.out.println("Transitioning to state INIT on input: " + line );
				STATE = INIT;
			} else if ( openBraceFlag ) {
				// Oh snap, ODEs are coming!
				System.out.println("Transitioning to state ACQUIRE on input: " + line );
				STATE = ACQUIRE;
			}
		}

		if ( STATE == ACQUIRE ) {

			if ( odeStructureFlag ) {
				String thisstring = odeStructure.group();
				System.out.println("Found ODE structure on input: " + line );
				String [] theseodes = thisstring.split(",");
				for( int j = 0; j < theseodes.length; j++ ) {
					String thisode = theseodes[j];
					thisode = thisode.replace(" ","");
					thisode = thisode.replace("\t", "");
					thisode = thisode.replace("}", "");

					String [] substrings = thisode.split("=");
					thisMode.addVariable( substrings[0].replace("'", "") );
					thisMode.addODE( substrings[1] );
					
					System.out.println("In mode "+thisMode.getID()+";");
					System.out.println("Found variable: "+ substrings[0].replace("'", ""));
					System.out.println("Found ODE: " + substrings[1]);
				}
					
			}

			if ( closeBraceFlag ) {
				System.out.println("Transitioning to state TERMINATE on input: " + line );
				STATE = TERMINATE;
			} 
			
		}
		
		if ( STATE == TERMINATE ) {

			STATE = TERMINATE;
		}

			try {
				line = inputReader.readLine();
			} catch ( Exception ex ) {
				ex.printStackTrace();
			}
		}
		
		



		//return odeList;
	}
	public LinkedList<String> parseDOEList( String modeID ) {
		LinkedList<String> doeList = new LinkedList<String>();
		doeList.addLast("DOE!");

		return doeList;
	}

	public LinkedList<String> parseStableModes() throws IOException {

                boolean cont = true;
                String nextLine;

                Pattern declarationPattern  = Pattern.compile("#stablemode<.+>");
                Pattern modePattern = Pattern.compile("<.+>??");
                    
                nextLine = inputReader.readLine();
                while ( nextLine != null ) { 
                        Matcher declarationMatcher = declarationPattern.matcher( nextLine );

                        if ( debugMode ) {
                        	System.out.println("(KeYmaera parser@parseStableModes) Line was: " + nextLine );
			}

                        while ( declarationMatcher.find() ) { 
                                String declaration = declarationMatcher.group();
                                int start = declarationMatcher.start();
                                int end = declarationMatcher.end();
                                String mode;

                                Matcher modeMatcher = modePattern.matcher( declaration );
                                modeMatcher.find();
                                mode = modeMatcher.group();
                                mode = mode.substring(1,mode.length() - 1);
                                this.stableModeList.addLast( mode );

                                if ( debugMode ) {
                                	System.out.println("Found declaration match " + declaration );
                                	System.out.println("Starting at " + start );
                                	System.out.println("Ending at " + end );
                                	System.out.println("Found mode " + mode );
				}
                        }   
                        nextLine = inputReader.readLine();
                }   
                return this.stableModeList;
        }

}

