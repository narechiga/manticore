package manticore.invariantgeneration;

import proteus.dl.syntax.*;
import proteus.externaltools.matlabscriptingkit.*;
import java.util.*;

// for testing
//import proteus.dl.parser.*;

public class LinearContinuousStrategy extends CandidateInvariantGenerator {

	public LinearContinuousStrategy( ContinuousProgram dynamics,
						dLFormula includedSet, 
						dLFormula excludedSet, 
						dLFormula parentSet ) 
							throws NonlinearDynamicsException,
								HybridDynamicsException {

		this.stateList = dynamics.getStateList();

		if ( !((ContinuousProgram)dynamics).isLinearIn( stateList ) ) {
			throw new NonlinearDynamicsException("Nonlinear dynamics: " + dynamics.toKeYmaeraString() );
		}

		this.dynamics = dynamics;
		this.includedSet = includedSet;
		this.excludedSet = excludedSet;
		this.parentSet = parentSet;
	}

	public Term generateCandidate() {
		MatlabScriptingKit matlab = new MatlabScriptingKit();


		try {
			System.out.println( matlab.batch( writeLyapunovQueryString() ) );

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return null;


	}

	public String writeLyapunovQueryString() {
		// 0. Make a list of the state variables
		// 1. Declare syms for each free variable, form the state vector
		// 2. Print out the matrix A
		// 3. Solve the Lyapunov equation for P, with Q = eye( size( A ) )
		// 4. Compute V = transpose(X)*P*X
		// 5. Parse back V, return the RHS of V

		String variableString = "";
		for ( RealVariable thisState : stateList ) {
			variableString = variableString + " " + thisState.toMatlabString();
		}

		String s = "";
		s = s + "% Variable declarations\n";
		s = s + "syms" + variableString +";\n";
		s = s + "X = transpose([" + variableString + " ]);\n\n";


		MatrixTerm coefficientMatrix = ((ContinuousProgram)dynamics).extractLinearCoefficients( stateList );
		s = s + "% System matrix \n";
		s = s + "A = " + coefficientMatrix.toMatlabString() + ";\n\n";

		s = s + "% Solve for Lyapunov matrix P, using Q = Identity\n";
		s = s + "P = lyap( A', eye(size(A)) );\n\n";

		s = s + "% Compute the expression for the Lyapunov function\n";
		s = s + "V = transpose(X)*P*X;\n";
		s = s + "fprintf( 'V = %s\\n', char( V ) );";

		return s;

	}


	public static void main( String[] args ) {
		try {
			ContinuousProgram continuousProgram1 = (ContinuousProgram)dLStructure.parseStructure("{x' = y, y' = -x}");

			dLFormula includedSet = (dLFormula)dLStructure.parseStructure("x^2 + y^2 < 1");
			dLFormula excludedSet = (dLFormula)dLStructure.parseStructure("x^2 + y^2 > 10");
			dLFormula parentSet = (dLFormula)dLStructure.parseStructure("true");

			LinearContinuousStrategy myStrategy = new LinearContinuousStrategy( continuousProgram1,
											includedSet,
											excludedSet,
											parentSet );
			myStrategy.generateCandidate();

			ContinuousProgram continuousProgram2 = 
				(ContinuousProgram)dLStructure.parseStructure("{x' = -10*x + y, y' = 2*x - 11*y}");

			LinearContinuousStrategy myStrategy2 = new LinearContinuousStrategy( continuousProgram2,
											includedSet,
											excludedSet,
											parentSet );
			myStrategy2.generateCandidate();

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}


}
