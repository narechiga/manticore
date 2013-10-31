import java.io.*;

class EITool {

	public static void main( String [] args ) {

		System.out.println("Hello world!");

		if ( args.length < 1 ) {
			System.out.println("No input file given.");
			System.exit(1);
		} else if ( args.length > 1 ) {
			System.out.println("Too many arguments.");
			System.exit(1);
		} else {
			System.out.println("Input argument is: " + args[0] );
		}

		
		try {
			Preprocessor myPreProc = new Preprocessor( args[0] );
			Parser myParser = new KeYmaeraParser("M1", args[0]);

			myPreProc.die();
			myParser.die();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
		

	} 

}
