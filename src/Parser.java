import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.String;
import java.util.ArrayList;


public abstract class Parser {

	protected ArrayList<String> stableModeList;
	protected String fileName;
	protected File inputFile;
	protected BufferedReader inputReader;

	public void setFileName( String fileName ) {//throws FileNotFoundException{
		this.fileName = fileName;

		this.inputFile = new File( this.fileName );
		try {
			this.inputReader = new BufferedReader(new FileReader( this.inputFile ));
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}

	public ArrayList<String> getStableModeList() {
		return this.stableModeList;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void die() {
		try {
			this.inputReader.close();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}

	public void rewindFile() {
		this.die();
		this.setFileName( this.fileName );
	}

	public abstract ArrayList<String> parseVariableList();
	public abstract ArrayList<String> parseResetList( String ModeID );
	public abstract void parseGuardList( Mode thisMode );
	public abstract void parseODEs( Mode thisMode );
	public abstract ArrayList<String> parseDOEList( String ModeID );

}


