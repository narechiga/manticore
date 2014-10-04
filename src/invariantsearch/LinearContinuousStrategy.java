package manticore.invariantsearch;

import proteus.externaltools.matlabscriptingkit.*;
import proteus.logicsolvers.abstractions.*;
import proteus.dl.syntax.*;
import proteus.dl.semantics.*; // because of the valuation that comes from the logic solver
import java.util.*;

// for testing
import proteus.logicsolvers.mathematicakit.*;
//import proteus.dl.parser.*;

public class LinearContinuousStrategy extends InvariantHunter {

	public LinearContinuousStrategy( ContinuousProgram dynamics,
						dLFormula includedSet, 
						dLFormula excludedSet, 
						LogicSolverInterface logicSolver ) 
							throws NonlinearDynamicsException,
								HybridDynamicsException {

		this.stateList = dynamics.getStateList();

		if ( !((ContinuousProgram)dynamics).isLinearIn( stateList ) ) {
			throw new NonlinearDynamicsException("Nonlinear dynamics: " + dynamics.toKeYmaeraString() );
		}

		this.dynamics = dynamics;
		this.includedSet = includedSet;
		this.excludedSet = excludedSet;

		this.logicSolver = logicSolver;
	}

	public Term generateCandidate() {
		MatlabScriptingKit matlab = new MatlabScriptingKit();


		Term lyapunovFunction = null;
		try {
			String matlabString = matlab.batch( writeLyapunovQueryString() );
			Scanner matlabScanner = new Scanner( matlabString );

			while ( matlabScanner.hasNext() ) {
				if ( matlabScanner.hasNext("ResultingLyapunovFunction") ) {
					System.out.println("Found lyapunov function!");
					matlabScanner.next();
					matlabScanner.next();
					lyapunovFunction = (Term)(dLStructure.parseStructure( matlabScanner.nextLine() ) );
				} else {
					//System.out.println("No lyapfun found in token: " + matlabScanner.next() );
					matlabScanner.next();
				}

			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return lyapunovFunction;
	}

	public ComparisonFormula sliceBarrier( Term barrierFunction ) {
		// Want to find a sublevelset of the Lyapunov or Barrier function that
		// 	1. excludes the undesired set. (safety)
		// 	2. includes the desired set, (initialization), and
		//
		// If these two requirements conflict, opt to exclude the undesired set,
		// even if the desired set is not fully contained. That's what FINVcut is for :)

		// Specifically, need to find an instance of a constant such that
		// 	forall x, V(x) <= c implies not( excludedSet ) (safety)
		//	and includedSet implies V(x) < c (initialization)
		
		RealVariable c = new RealVariable("c");
		ComparisonFormula sublevelSet = new ComparisonFormula( new Operator("<="), barrierFunction, c );

		// V(x) <= c implies not( excludedSet ) (safety)
		ImpliesFormula safetyUnquantified = new ImpliesFormula( sublevelSet, new NotFormula( excludedSet ) );
		// includedSet implies V(x) < c (initialization)
		ImpliesFormula initializationUnquantified = new ImpliesFormula( includedSet, sublevelSet );
		// safety-certificate-ness (since it is invariant by construction, modulo validation, performed by somebody else)
		AndFormula certificateUnquantified = new AndFormula( safetyUnquantified, initializationUnquantified );

		// Now we need to add universal quantifiers, so that c is the only free variable
		// We will do this for the certificate and for the safety formula, but won't bother to do this
		// for intiialization, since if the certificate fails we won't care about initialization alone,
		// only about safety.
		dLFormula certificate  = certificateUnquantified.universalClosure( stateList );
		dLFormula safety = safetyUnquantified.universalClosure( stateList );

		// First, try to find a sublevel set that will give us certificate-ness

		LogicSolverResult result = null;
		try {
			result = logicSolver.findInstance( certificate );
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		if ( (result != null ) && result.satisfiability.equals("sat") ) {
			System.out.println("Hooray! found a sublevelset that satisfies both exclusion and inclusion requirements: " + result.valuation.toMathematicaString() );

			return new ComparisonFormula( new Operator("<="), barrierFunction, result.valuation.get( c ) );
		} else {
			System.out.println("Could not find one that would work for both, trying to just do safety now" );
		}

		try {
			result = logicSolver.findInstance( safety );
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		if ( (result != null) && result.satisfiability.equals("sat") ) {
			System.out.println("Mildly happy: found a sublevelset that excludes the set to be excluded: " + result.valuation.toMathematicaString() );

			return new ComparisonFormula( new Operator("<="), barrierFunction, result.valuation.get( c ) );

		} else {
			// TODO: This should definitely not be a runtime exception, because it is not programmer error,
			// it just doesn't work for this problem instance
			throw new RuntimeException("Could not find any sublevelset to guarantee anything!");
		}

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
		s = s + "fprintf( 'ResultingLyapunovFunction = %s\\n', char( V ) );";

		return s;

	}


	public static void main( String[] args ) {
		try {
			dLFormula includedSet = (dLFormula)dLStructure.parseStructure("x^2 + y^2 < 1");
			dLFormula excludedSet = (dLFormula)dLStructure.parseStructure("x^2 + y^2 > 10");

			MathematicaInterface mathematica = new MathematicaInterface();
			
			//ContinuousProgram continuousProgram1 = (ContinuousProgram)dLStructure.parseStructure("{x' = y, y' = -x}");
			//LinearContinuousStrategy myStrategy = new LinearContinuousStrategy( continuousProgram1,
			//								includedSet,
			//								excludedSet,
			//								mathematica );
			//Term barrier = myStrategy.generateCandidate();
			//System.out.println( barrier.toMathematicaString() );
			//System.out.println( myStrategy.sliceBarrier( barrier ).toMathematicaString() );

			ContinuousProgram continuousProgram2 = 
				(ContinuousProgram)dLStructure.parseStructure("{x' = -10*x + y, y' = 2*x - 11*y}");

			LinearContinuousStrategy myStrategy2 = new LinearContinuousStrategy( continuousProgram2,
											includedSet,
											excludedSet,
											mathematica );
			Term barrier2 = myStrategy2.generateCandidate();
			System.out.println( barrier2.toMathematicaString() );
			System.out.println( myStrategy2.sliceBarrier( barrier2 ).toMathematicaString() );

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}


}
