import java.util.LinkedList;
import java.io.*;
import java.util.LinkedList;
import java.util.regex.*;


public class KeYmaeraParser extends Parser {

	// Constructor
	public KeYmaeraParser ( String fileName) throws FileNotFoundException {
		this.setFileName(fileName);

		this.stableModeList = new LinkedList<String>();
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
	public LinkedList<String> parseODEList( String modeID ) {
		LinkedList<String> odeList = new LinkedList<String>();
		odeList.addLast("ODE!");

		return odeList;

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

                        System.out.println("Line was: " + nextLine );

                        while ( declarationMatcher.find() ) { 
                                String declaration = declarationMatcher.group();
                                int start = declarationMatcher.start();
                                int end = declarationMatcher.end();
                                String mode;

                                System.out.println("Found declaration match " + declaration );
                                System.out.println("Starting at " + start );
                                System.out.println("Ending at " + end );

                                Matcher modeMatcher = modePattern.matcher( declaration );
                                modeMatcher.find();
                                mode = modeMatcher.group();
                                mode = mode.substring(1,mode.length() - 1);
                                System.out.println("Found mode " + mode );
                                this.stableModeList.addLast( mode );
                        }   
                        nextLine = inputReader.readLine();

                }   
                return this.stableModeList;
        }

}

