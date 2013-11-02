import java.io.*;
import java.util.*;
import java.text.*;



class SoSLyapunovCandidateGenerator extends CandidateGenerator {

	public SoSLyapunovCandidateGenerator( Mode myMode ) {
		this.myMode = myMode;
	}

	public String generateCandidate() {
		String candidate;

		candidate = generateQueryFile();

		return candidate;
	}

	private String generateQueryFile() {

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
		fileContents = fileContents + "% Clean result and return\n";
		fileContents = fileContents + "P = clean(double(P),1e-3); epsilon = double(epsilon);\n";
		fileContents = fileContents + "V = X'*P*X;\n";
		fileContents = fileContents + "sdisplay(V);";
		//fileContents = fileContents + "dV = jacobian(V,X)*f;\n";
		//fileContents = fileContents + "sdisplay(sosd(-dV));\n";
		/*=======================================================*/
		/*********************************************************/

		System.out.println(fileContents);
		return "a query file";

	}



}
