import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.String;
import java.util.LinkedList;


public abstract class Parser {

	protected LinkedList<String> stableModeList;
	protected String fileName;
	protected File inputFile;
	protected BufferedReader inputReader;

	public void setFileName( String fileName ) throws java.io.FileNotFoundException{
		this.fileName = fileName;

		this.inputFile = new File( this.fileName );
		this.inputReader = new BufferedReader(new FileReader( this.inputFile ));
	}

	public LinkedList<String> getStableModeList() {
		return this.stableModeList;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void die() throws java.io.IOException {
		this.inputReader.close();
	}

	public abstract LinkedList<String> parseVariableList();
	public abstract LinkedList<String> parseResetList( String ModeID );
	public abstract LinkedList<String> parseGuardList( String ModeID );
	public abstract LinkedList<String> parseODEList( String ModeID );
	public abstract LinkedList<String> parseDOEList( String ModeID );

}


