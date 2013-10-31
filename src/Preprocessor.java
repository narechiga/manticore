import java.io.*;
import java.util.LinkedList;
import java.util.regex;

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

	public LinkedList<String> extractStableModes() {
		String nextLine;
		LinkedList<String> stableModes;

		Pattern declarationPattern  = Pattern.compile("//#stablemode<(.*)>");

		nextLine = inputReader.readLine();
		Matcher matcher = declarationPattern( nextLine );

		while matcher.find()

		return stableModes;
	
	}


}


