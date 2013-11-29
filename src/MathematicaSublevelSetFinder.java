import java.io.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import java.util.regex.*;


class MathematicaSublevelSetFinder extends SublevelSetFinder {

	private static boolean debugMode = true;

	public static String findSublevelSet( Mode thisMode, String lyapunovFunction ) {
		String result = "";
		generateSublevelSetQuery( thisMode, lyapunovFunction );

		try {
                        ProcessBuilder builder = new ProcessBuilder("MathematicaScript", "-script", "scratch/MathematicaSublevelSetQuery.m");
                        builder.redirectErrorStream(true);
                        Process process = builder.start();
                        InputStream stdout = process.getInputStream();
                        BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));


			Pattern closedConstraintPattern = Pattern.compile("level(\\s)*<=(\\s)*.+");
			Pattern openConstraintPattern = Pattern.compile("level(\\s)*<(\\s)*.+");

                        String line = ""; 
                        while ((line = reader.readLine()) != null) {

                                if ( debugMode ) { 
                                        System.out.println ("Mathematica output (+): " + line);
                                }   

                                Matcher closedConstraintMatcher = closedConstraintPattern.matcher( line );
                                Matcher openConstraintMatcher = openConstraintPattern.matcher( line );
                                String [] components;
                                if ( closedConstraintMatcher.find() ) {
                                	components = line.split("<=");
                                	result = components[1];
				}
				if ( openConstraintMatcher.find() ) {
                                	components = line.split("<");
                                	result = components[1];
				}

                        }   


                } catch ( Exception ex ) { 
                        ex.printStackTrace();
                }  
                System.out.println("Level is: " + result);
		return result;
	}

	private static void generateSublevelSetQuery( Mode thisMode, String lyapunovFunction ) {
		System.out.println(" I want to find a sublevel set! ");

		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                String fileContents = "(* Automatically generated on " + timeStamp + "*)\n";
                fileContents = fileContents + "(* Sublevel set generation with quantifier elimination *)\n\n";

                fileContents = fileContents + generateQuerySetup( thisMode, lyapunovFunction ) + "\n";
                fileContents = fileContents + "Print[ ";

		String guardString = generateGuardString( thisMode );
		String implication = "Implies[ V <= level, Not[ " + guardString +" ] ]";

                fileContents = fileContents + generateRealReduceQuery( thisMode, implication, generateVariableVector( thisMode ) );
                fileContents = fileContents + "];\n";

                try {
                        PrintWriter writer = new PrintWriter("scratch/MathematicaSublevelSetQuery.m");
                        writer.println(fileContents);
                        writer.close();
                } catch ( Exception ex ) { 
                        ex.printStackTrace();
                }   

                if ( debugMode ) { 
                        System.out.println( fileContents );
                } 
	}

	private static String generateGuardString( Mode thisMode ) {
		String guardString = "";
		Iterator<String> guards = thisMode.guards.iterator();

		// TODO : what if there are no guards?
		guardString = guards.next();
		while ( guards.hasNext() ) {
			guardString = guardString +" && "+guards.next();
		}

		return guardString;
	}

        private static String generateVariableVector( Mode thisMode ) {
                Iterator<String> varList = thisMode.getVariableList().iterator();
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


        private static String generateQuerySetup( Mode thisMode, String lyapunovFunction ) {

                String querySetup = "";

                /*********************************************************/
                /* * * * * Generate variable vector * * * * */
                querySetup = querySetup + "X = " + generateVariableVector( thisMode ) + ";\n\n";
                /*=======================================================*/
                /*********************************************************/

                /*********************************************************/
                /* * * * * Generate dynamics declarations * * * * */
                Iterator<String> odeList = thisMode.getODEList().iterator();
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
                querySetup = querySetup + "V = " + lyapunovFunction + ";\n";

                /*=======================================================*/
                /*********************************************************/

                return querySetup;

        }

        private static String generateRealReduceQuery( Mode thisMode, String logicalFormula, String eliminationVariables ) {
                String reduceQuery = "Reduce[\n";
                String thisvar = "";
                boolean firstTimeAround = true;
                Iterator<String> varList = thisMode.getVariableList().iterator();
                while ( varList.hasNext() ) {
                        thisvar = varList.next();
                        reduceQuery = reduceQuery + "\tForAll[ " + thisvar + ",\n";
                }
                reduceQuery = reduceQuery + "\t\t" + logicalFormula + "\n";
                reduceQuery = reduceQuery + "\t";
                for ( int counter = 0; counter < thisMode.getVariableList().size(); counter++ ) {
                        reduceQuery = reduceQuery + "]";
                }
                reduceQuery = reduceQuery + ",\n" + eliminationVariables + ", Reals ]\n";

                return reduceQuery;
        }




}
