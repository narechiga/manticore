import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import java.util.regex.*;

class SoSLyapunovCandidateGenerator extends CandidateGenerator {

	boolean debugMode;

	public SoSLyapunovCandidateGenerator( Mode myMode ) {
		this.myMode = myMode;
		this.debugMode = false;
	}
	public SoSLyapunovCandidateGenerator( Mode myMode, boolean debugMode ) {
		this.myMode = myMode;
		this.debugMode = debugMode;
	}

	public String generateCandidate() {
		String lyapunovCandidate = "";

		try {
			generateQueryFile();

			ProcessBuilder builder = new ProcessBuilder("matlab", "-nojvm", "-nosplash", "< ./scratch/SoSLyapunovQuery.m");
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));

			String line;
			Pattern lyapunovCandidatePattern = Pattern.compile("V = .+");

			while ((line = reader.readLine()) != null) {
				if ( debugMode ) {
					System.out.println ("Matlab output: " + line);
				}

				Matcher lyapunovCandidateMatcher = lyapunovCandidatePattern.matcher( line );
				if ( lyapunovCandidateMatcher.find() ) {
					lyapunovCandidate = lyapunovCandidateMatcher.group();

					lyapunovCandidate = lyapunovCandidate.replace("V = ", "");
					lyapunovCandidate = lyapunovCandidate.replace(">","");
				}
			}

		} catch ( Exception ex ) {
			ex.printStackTrace();
		}


		return lyapunovCandidate;
	}

	private void generateQueryFile() throws Exception {

		// Write a timestamp at the top
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		String fileContents = "% Automatically generated on " + timeStamp + "\n\n";

		// For now, we only do global Lyapunov functions
		// TODO : incorporate a local Lyapunov function search
		/*********************************************************/
		/* * * * * Generate Yalmip variable declarations * * * * */
		Iterator<String> varList = myMode.getVariableList().iterator();
		String Xvector = "X = [ ";
		boolean firstTimeAround = true;
		String thisvar = "";
		while ( varList.hasNext() ) {
			thisvar = varList.next();
			fileContents = fileContents + "sdpvar " + thisvar + ";\n";
			
			if ( firstTimeAround ) {
				Xvector = Xvector + thisvar;
				firstTimeAround = false;
			} else {
				Xvector = Xvector + "; " + thisvar;
			}
		}
		Xvector = Xvector + " ];\n";
		fileContents = fileContents + Xvector + "\n";
		/*=======================================================*/
		/*********************************************************/

		/*********************************************************/
		/* * * * * Generate dynamics declarations * * * * */
		Iterator<String> odeList = myMode.getODEList().iterator();
		String fvector = "f = [ ";
		int fcounter = 1;
		firstTimeAround = true;
		String thisode = "";
		while ( odeList.hasNext() ) {
			thisode = odeList.next();
			fileContents = fileContents + "f" + fcounter + " = " + thisode + ";\n";

			if ( firstTimeAround ) {
				fvector = fvector + "f" + fcounter;
				firstTimeAround = false;
			} else {
				fvector = fvector + "; " + "f" + fcounter;
			}
			fcounter++;
		}
		fvector = fvector + " ];\n";
		fileContents = fileContents + fvector + "\n";
		/*=======================================================*/
		/*********************************************************/

		/*********************************************************/
		/* * * * * Setup and solve SoS problem * * * * */
		fileContents = fileContents + "sdpvar P(" + myMode.getVariableList().size() + ");\n";
		fileContents = fileContents + "sdpvar epsilon;\n";
		fileContents = fileContents + "V = X'*P*X;\n";
		fileContents = fileContents + "dV = jacobian(V,X)*f;\n\n";

		fileContents = fileContents + "constraint = [ epsilon > 0 ] + sos(-dV) + sos(V - epsilon*X'*X) + [ P >= 0 ];\n";
		fileContents = fileContents + "[sol, m, B, residual] = solvesos( constraint );\n\n";
		/*=======================================================*/
		/*********************************************************/

		/*********************************************************/
		/* * * * * Clean up and return * * * * */
		fileContents = fileContents + "P = clean(double(P),1e-3); epsilon = double(epsilon);\n";
		fileContents = fileContents + "V = X'*P*X;\n";
		fileContents = fileContents + "V = sdisplay(V); V = V{1};";
		fileContents = fileContents + "fprintf('V = %s', V);";
		//fileContents = fileContents + "dV = jacobian(V,X)*f;\n";
		//fileContents = fileContents + "sdisplay(sosd(-dV));\n";
		/*=======================================================*/
		/*********************************************************/

		if ( debugMode ) {
			System.out.println(fileContents);
		}
		PrintWriter writer = new PrintWriter("scratch/SoSLyapunovQuery.m", "UTF-8");
		writer.println(fileContents);
		writer.close();

		//return "a query file";
	}

}
