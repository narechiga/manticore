import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.String;
import java.util.List;


public abstract class Parser {

	private String modeID;
	private String fileName;
	private File inputFile;
	private BufferedReader inputReader;

	public void setModeID( String modeID ) {
		this.modeID = modeID;
	}

	public void setFileName( String fileName ) throws java.io.FileNotFoundException{
		this.fileName = fileName;

		this.inputFile = new File( this.fileName );
		this.inputReader = new BufferedReader(new FileReader( this.inputFile ));
	}

	public String getModeID() {
		return this.modeID;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void die() throws java.io.IOException {
		this.inputReader.close();
	}

	public abstract List<String> extractResetList();
	public abstract List<String> extractGuardList();
	public abstract List<String> extractODEs();
	public abstract List<String> extractDOEs();

}


