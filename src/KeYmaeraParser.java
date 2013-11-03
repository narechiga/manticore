import java.util.LinkedList;
import java.io.*;
import java.util.LinkedList;
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

	public LinkedList<String> parseGuardList( String modeID ){
		LinkedList<String> guardList = new LinkedList<String>();
		guardList.addLast("outgoing!");

		return guardList;
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

