import java.io.*;
import java.util.LinkedList;
import java.util.regex.*;

class Preprocessor {

	private String inputFile;
	private BufferedReader inputReader;

	public Preprocessor( String inputFile ) throws FileNotFoundException {
		this.inputFile = inputFile;

		this.inputReader = new BufferedReader(new FileReader( this.inputFile ));
	}

	public void die() throws IOException {
		this.inputReader.close();
	}

	public LinkedList<String> extractStableModes() throws IOException {

		boolean cont = true;
		String nextLine;
		LinkedList<String> stableModes = new LinkedList<String>();

		Pattern declarationPattern  = Pattern.compile("// #stablemode<M(\\d)*>");
		Pattern modePattern = Pattern.compile("M(\\d)*");
		
		nextLine = inputReader.readLine();
		while ( nextLine != null ) {
			Matcher declarationMatcher = declarationPattern.matcher( nextLine );

			System.out.println("Line was: " + nextLine );

			if ( declarationMatcher.find() ) {
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
				System.out.println("Found mode " + mode );
				stableModes.addLast( mode );
			} 
			nextLine = inputReader.readLine();

		}
		return stableModes;
	}
}


