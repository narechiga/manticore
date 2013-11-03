import java.io.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import java.util.regex.*;


class MathematicaCandidateValidator extends CandidateValidator {

	boolean debugMode;

	public MathematicaCandidateValidator( Mode myMode ) {
		this.myMode = myMode;
		this.debugMode = false;
	}
	public MathematicaCandidateValidator( Mode myMode, boolean debugMode ) {
		this.myMode = myMode;
		this.debugMode = debugMode;
	}
	

	public boolean checkLyapunov( String lyapunovCandidate ) {
		boolean isLyapunov = true;

		isLyapunov = isLyapunov & checkLyapunovPositivity( lyapunovCandidate );
		isLyapunov = isLyapunov & checkLyapunovDerivativeNegativity( lyapunovCandidate );

		return isLyapunov;
	}

	public boolean checkLyapunovPositivity( String lyapunovCandidate  ) {
		boolean result = false;
		generateLyapunovPositivityQuery( lyapunovCandidate );

		try {
			ProcessBuilder builder = new ProcessBuilder("MathematicaScript", "-script", "scratch/MathematicaLyapunovPositivityQuery.m");
                	builder.redirectErrorStream(true);
                	Process process = builder.start();
                	InputStream stdout = process.getInputStream();
                	BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));

                	Pattern truth = Pattern.compile("True");
                	Pattern falsehood = Pattern.compile("False");

			String line = "";
                	while ((line = reader.readLine()) != null) {

                		if ( debugMode ) {
                                	System.out.println ("Mathematica output (+): " + line);
				}

                                Matcher truthMatcher = truth.matcher( line );
                                Matcher falsehoodMatcher = falsehood.matcher( line );

                                if ( truthMatcher.find() ){
                                	result = true;
				}
				if ( falsehoodMatcher.find() ) {
					result = false;
				}
                        }


		} catch ( Exception ex ) {
			ex.printStackTrace();
		}

		return result;

	}
	public boolean checkLyapunovDerivativeNegativity( String lyapunovCandidate ){
		boolean result  = false;
		generateLyapunovDerivativeNegativityQuery( lyapunovCandidate );


		try {
			ProcessBuilder builder = new ProcessBuilder("MathematicaScript", "-script", "scratch/MathematicaLyapunovDerivativeNegativityQuery.m");
                	builder.redirectErrorStream(true);
                	Process process = builder.start();
                	InputStream stdout = process.getInputStream();
                	BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));

                	Pattern truth = Pattern.compile("True");
                	Pattern falsehood = Pattern.compile("False");

			String line = "";
                	while ((line = reader.readLine()) != null) {

                                if ( debugMode ) {
                                	System.out.println ("Mathematica output (-): " + line);
				}

                                Matcher truthMatcher = truth.matcher( line );
                                Matcher falsehoodMatcher = falsehood.matcher( line );

                                if ( truthMatcher.find() ){
                                	result = true;
				}
				if ( falsehoodMatcher.find() ) {
					result = false;
				}

                        }


		} catch ( Exception ex ) {
			ex.printStackTrace();
		}

		return result;
	}

	private void generateLyapunovPositivityQuery( String lyapunovCandidate ) {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		String fileContents = "(* Automatically generated on " + timeStamp + "*)\n";
		fileContents = fileContents + "(* Lyapunov candidate positivity test *)\n\n";

		fileContents = fileContents + generateQuerySetup( lyapunovCandidate ) + "\n";
		fileContents = fileContents + "Print[ ";
		fileContents = fileContents + generateRealReduceQuery( "V >= 0", generateVariableVector() );
		fileContents = fileContents + "];\n";

		try {
			PrintWriter writer = new PrintWriter("scratch/MathematicaLyapunovPositivityQuery.m");
        	        writer.println(fileContents);
        	        writer.close();
        	} catch ( Exception ex ) {
        		ex.printStackTrace();
        	}

		if ( debugMode ) {
			System.out.println( fileContents );
		}

	}
	private void generateLyapunovDerivativeNegativityQuery( String lyapunovCandidate ) {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		String fileContents = "(* Automatically generated on " + timeStamp + "*)\n";
		fileContents = fileContents + "(* Lyapunov candidate derivative negativity test *)\n\n";

		fileContents = fileContents + generateQuerySetup( lyapunovCandidate ) + "\n";

		/* Compute the Lie derivative */
		fileContents = fileContents + "dV = Grad[ V, X ].f;\n\n";

		/* Write the actual negativity query */
		fileContents = fileContents + "Print[ ";
		fileContents = fileContents + generateRealReduceQuery( "dV <= 0", generateVariableVector() );
		fileContents = fileContents + " ];\n";
		
		try {
			PrintWriter writer = new PrintWriter("scratch/MathematicaLyapunovDerivativeNegativityQuery.m");
                	writer.println(fileContents);
                	writer.close();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}

		if ( debugMode ) {
			System.out.println( fileContents );
		}
		
	}



	private String generateQuerySetup( String lyapunovCandidate ) { // Generates the part of the setup that is common to both tests

		String querySetup = "";		

		/*********************************************************/
                /* * * * * Generate variable vector * * * * */	
                querySetup = querySetup + "X = " + generateVariableVector() + ";\n\n";
		/*=======================================================*/
                /*********************************************************/

                /*********************************************************/
                /* * * * * Generate dynamics declarations * * * * */
		Iterator<String> odeList = myMode.getODEList().iterator();
                String fvector = "f = { ";
                int fcounter = 1;
                boolean firstTimeAround = true;
                String thisode = "";
                while ( odeList.hasNext() ) {
                        thisode = odeList.next();
                        querySetup = querySetup + "f" + fcounter + " = " + thisode + ";\n";

                        if ( firstTimeAround ) {
                                fvector = fvector + "f" + fcounter;
                                firstTimeAround = false;
                        } else {
                                fvector = fvector + ", " + "f" + fcounter;
                        }
                        fcounter++;
                }
                fvector = fvector + " };\n";
                querySetup = querySetup + fvector + "\n";
		/*=======================================================*/
                /*********************************************************/

                /*********************************************************/
                /* * * * * State the candidate * * * * */
                querySetup = querySetup + "V = " + lyapunovCandidate + ";\n";

		/*=======================================================*/
                /*********************************************************/

		return querySetup;

	}

	private String generateVariableVector() {
		Iterator<String> varList = myMode.getVariableList().iterator();
                String Xvector = "{ ";
                boolean firstTimeAround = true;
                String thisvar = ""; 
                while ( varList.hasNext() ) { 
                        thisvar = varList.next();
    
                        if ( firstTimeAround ) { 
                                Xvector = Xvector + thisvar;
                                firstTimeAround = false;
                        } else {
                                Xvector = Xvector + ", " + thisvar;
                        }   
                }   
                Xvector = Xvector + " }";

                return Xvector;
	}

	private String generateRealReduceQuery( String logicalFormula, String eliminationVariables ) {
		String reduceQuery = "Reduce[\n";
		String thisvar = "";
                boolean firstTimeAround = true;
		Iterator<String> varList = myMode.getVariableList().iterator();
		while ( varList.hasNext() ) {
			thisvar = varList.next();
			reduceQuery = reduceQuery + "\tForAll[ " + thisvar + ",\n";
		}
		reduceQuery = reduceQuery + "\t\t" + logicalFormula + "\n";
		reduceQuery = reduceQuery + "\t";
		for ( int counter = 0; counter < myMode.getVariableList().size(); counter++ ) {
			reduceQuery = reduceQuery + "]";
		}
		reduceQuery = reduceQuery + ",\n" + eliminationVariables + ", Reals ]\n";

		return reduceQuery;
	}

}
