package manticore.symbolicexecution;

import manticore.dl.*;
import java.util.*;

public class NativeExecutionEngine {

	//Determine behavior of star operator
	int maxIterations;
	int iteration;
	

	Interpretation interpretation;


	public NativeExecutionEngine ( Interpretation interpretation ) {
		this.interpretation = interpretation;
		this.iteration = 0;
		this.maxIterations = 10; // Default
	}

	public NativeExecutionEngine ( Interpretation interpretation, int maxIterations ) {
		this.interpretation = interpretation;
		this.iteration = 0;
		this.maxIterations = 10; // Default
	}

	public ValuationList runDiscreteSteps( HybridProgram program, ValuationList valuations ) throws Exception {

		if ( this.iteration == this.maxIterations ) {
			return valuations;
		}

		ValuationList returnValuations;

		if ( program instanceof ConcreteAssignmentProgram ) {
			returnValuations = runConcreteAssignmentProgram( (ConcreteAssignmentProgram)program, valuations );
		} else if ( program instanceof TestProgram ) {
			returnValuations = runTestProgram( (TestProgram)program, valuations );
		} else if ( program instanceof ContinuousProgram ) {
			// Evaluate the RHS of the differential equations, but don't actually evolve
			returnValuations = runContinuousProgram( (ContinuousProgram)program, valuations );
		} else if ( program instanceof SequenceProgram ) {
			returnValuations = runSequenceProgram( (SequenceProgram)program, valuations );
		} else if ( program instanceof ChoiceProgram ) {
			returnValuations = runChoiceProgram( (ChoiceProgram)program, valuations );
		} else if ( program instanceof RepetitionProgram ) {
			returnValuations = runRepetitionProgram( (RepetitionProgram)program, valuations );
		} else {
			returnValuations = null;
			throw new Exception("NativeExecutionEngine does not recognize this program type");
		}

		return returnValuations;
	}

	protected ValuationList runConcreteAssignmentProgram( ConcreteAssignmentProgram program, 
								ValuationList valuations ) throws Exception {
		ValuationList returnValuations = valuations.clone();		
		
		Iterator<Valuation> valuationIterator = returnValuations.iterator();
		while ( valuationIterator.hasNext() ) {
			Valuation thisValuation = valuationIterator.next();

			if ( program.getRHS() instanceof Real ) {
				thisValuation.put( program.getLHS(), (Real)program.getRHS() );
			} else {
				double rhs = ((Real)interpretation.evaluateTerm( program.getRHS(), thisValuation )).toDouble();
				Real Rhs = new Real( Double.toString( rhs ) );

				thisValuation.put( program.getLHS(), Rhs );
			}
		}

		return returnValuations;
	}

	protected ValuationList runTestProgram( TestProgram program, 
							ValuationList valuations ) throws Exception {

		ValuationList returnValuations = new ValuationList();
		Iterator<Valuation> valuationIterator = valuations.iterator();

		Valuation thisValuation;
		while ( valuationIterator.hasNext() ) {

			thisValuation = valuationIterator.next();
			if ( evaluateFormula( program.getFormula(), thisValuation ) == true ) {
			    returnValuations.add( thisValuation.clone() );
			}

		}

		return returnValuations;
	}

	protected ValuationList runContinuousProgram( ContinuousProgram program,
							ValuationList valuations ) throws Exception {


		if ( valuations.size() > 1 ) {
			throw new Exception("System is not deterministic! Size of valuation set at continuous block is: "
						+ valuations.size());
		}

		// We should kill the process, so pretend that max iterations have been reached
		this.iteration = this.maxIterations;

		ArrayList<ExplicitODE> odeList = program.getODEs();
		Iterator<ExplicitODE> odeIterator = odeList.iterator();

		Real thisRHS = null;
		ExplicitODE thisODE = null;
		RealVariable thisDerivativeVariable = null;
		Valuation thisValuation = valuations.get(0); // Remember, there should be only one!
		Valuation derivativeValuation = thisValuation.clone();

		while ( odeIterator.hasNext() ) {
				
			thisODE = odeIterator.next();
			thisRHS = (Real)(interpretation.evaluateTerm( thisODE.getRHS(), thisValuation ));
			thisDerivativeVariable = new RealVariable( "_d"+thisODE.getLHS().toString()+"dt_" );
			derivativeValuation.put( thisDerivativeVariable, thisRHS );
		}

		ValuationList returnValuations = new ValuationList();
		returnValuations.add( derivativeValuation );
		return returnValuations;
	}

	protected ValuationList runSequenceProgram( SequenceProgram program, 
							ValuationList valuations ) throws Exception {

		ValuationList returnValuations;

		if ( program.getFirstProgram() instanceof ContinuousProgram ) {
			throw new Exception("Sequential composition after continuous blocks are not currently supported");
		}

		// Run first step
		returnValuations = runDiscreteSteps( program.getFirstProgram(), valuations );
		//System.out.println("After sequence subthread 1: " + returnValuations );

		// Run second step
		returnValuations = runDiscreteSteps( program.getSecondProgram(), returnValuations );
		//System.out.println("After sequence subthread 2: " + returnValuations );

		return returnValuations;
	}

	protected ValuationList runChoiceProgram( ChoiceProgram program, 
							ValuationList valuations ) throws Exception {

		ValuationList returnValuations;
	
		//System.out.println( "Initial states for choice program: " + valuations );
		// Run left program
		returnValuations = runDiscreteSteps( program.getLeftProgram(), valuations );
		//System.out.println("After choice subthread 1: " + returnValuations );
		
		// Run right program
		//System.out.println( "Starting choice subthread 2 with: " + valuations );
		returnValuations.addAll( runDiscreteSteps( program.getRightProgram(), valuations ) );
		//System.out.println("After choice subthread 2: " + returnValuations );

		return returnValuations;
	}

	public ValuationList runRepetitionProgram( RepetitionProgram program,
							ValuationList valuations ) throws Exception {

		this.iteration = this.iteration + 1;
		SequenceProgram iterate = new SequenceProgram( program.getSubProgram(), program );
		return runDiscreteSteps( iterate, valuations );
	}

	public boolean evaluateFormula( dLFormula formula, Valuation valuation ) throws Exception {
		//evaluates only simple control-style logic, at the current valuation
		return interpretation.evaluateFormula( formula, valuation );
	}

}
