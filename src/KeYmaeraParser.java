import java.util.LinkedList;


public class KeYmaeraParser extends Parser {

	// Constructor
	public KeYmaeraParser () {
	 	 //does nothing!
	 }
	public KeYmaeraParser ( String modeID, String fileName ) throws java.io.FileNotFoundException {
		this.setModeID( modeID );
		this.setFileName( fileName );
	}

	// inputReader is the name of the BufferedReader that points to the file we want
	public LinkedList<String> extractResetList(){
		LinkedList<String> resetList = new LinkedList<String>();
		resetList.addLast("incoming!");

		return resetList;
	}

	public LinkedList<String> extractGuardList(){
		LinkedList<String> guardList = new LinkedList<String>();
		guardList.addLast("outgoing!");

		return guardList;
	}
	public LinkedList<String> extractODEs() {
		LinkedList<String> odeList = new LinkedList<String>();
		odeList.addLast("ODE!");

		return odeList;

	}
	public LinkedList<String> extractDOEs() {
		LinkedList<String> doeList = new LinkedList<String>();
		doeList.addLast("DOE!");

		return doeList;
	}

}
