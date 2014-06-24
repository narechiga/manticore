package manticore.symbolicexecution;

import manticore.dl.*;
import java.util.*;

public class SymbolicExecutionThread {

	HybridProgram program;
	ArrayList<HashMap<RealVariable,Real>> currentStateValuations; // TODO: support more than just concrete real return values
	Interpretation interpretation;

	public SymbolicExecutionThread ( HybridProgram program, ArrayList<HashMap<RealVariable,Real>> initialValuations ) {
		this.program = program;
		this.currentStateValuations = initialValuations;

		this.interpretation = new NativeInterpretation();
	}

	public void runDiscreteSteps() throws Exception {

		if ( currentStateValuations == null ) { 
			// Nothing to be done here
		} else if ( program instanceof AssignmentProgram ) {
			runAssignmentProgram( (AssignmentProgram)program );
		} else if ( program instanceof TestProgram ) {
			runTestProgram( (TestProgram)program  );
		} else if ( program instanceof ContinuousProgram ) {
			// Nothing to be done here
		} else if ( program instanceof SequenceProgram ) {
			runSequenceProgram( (SequenceProgram)program );
		} else if ( program instanceof ChoiceProgram ) {
			runChoiceProgram( (ChoiceProgram)program );
		} else if ( program instanceof RepetitionProgram ) {
			runRepetitionProgram( (RepetitionProgram)program );
		} else {
			throw new Exception("SymbolicExecutionThread does not recognize this program type");
		}
	}

	protected void runAssignmentProgram( AssignmentProgram program ) throws Exception {

		Iterator<HashMap<RealVariable,Real>> valuationIterator = currentStateValuations.iterator();
		while ( valuationIterator.hasNext() ) {
			HashMap<RealVariable,Real> thisValuation = valuationIterator.next();

			if ( program.getRHS() instanceof Real ) {
				thisValuation.put( program.getLHS(), (Real)program.getRHS() );
			} else {
				double rhs = interpretation.evaluateTerm( program.getRHS(), thisValuation );
				Real Rhs = new Real( Double.toString( rhs ) );

				thisValuation.put( program.getLHS(), Rhs );
			}
		}
	}

	protected void runTestProgram( TestProgram program ) throws Exception {

		ArrayList<HashMap<RealVariable,Real>> newValuations = new ArrayList<HashMap<RealVariable,Real>>();
		HashMap<RealVariable,Real> thisValuation;
		Iterator<HashMap<RealVariable,Real>> valuationIterator = currentStateValuations.iterator();

		while ( valuationIterator.hasNext() ) {

			thisValuation = valuationIterator.next();
			if ( evaluateFormula( program.getFormula(), thisValuation ) == true ) {
				newValuations.add( thisValuation );
			}

		}
		currentStateValuations = newValuations;
	}

	protected void runSequenceProgram( SequenceProgram program ) throws Exception {
		// Run first step
		SymbolicExecutionThread subthread1 = new SymbolicExecutionThread( program.getFirstProgram(), currentStateValuations );
		
		subthread1.runDiscreteSteps();
		currentStateValuations = subthread1.currentStateValuations;

		// Run second step
		SymbolicExecutionThread subthread2 = new SymbolicExecutionThread( program.getSecondProgram(), currentStateValuations);

		subthread2.runDiscreteSteps();
		currentStateValuations = subthread2.currentStateValuations;
	}

	protected void runChoiceProgram( ChoiceProgram program ) throws Exception {
		// Run left program
		SymbolicExecutionThread subthread1 = new SymbolicExecutionThread( program.getLeftProgram(), currentStateValuations );

		subthread1.runDiscreteSteps();
		currentStateValuations = subthread1.currentStateValuations; //Replace our old state values with the new state values

		// Run right program
		SymbolicExecutionThread subthread2 = new SymbolicExecutionThread( program.getRightProgram(), currentStateValuations);

		subthread2.runDiscreteSteps();
		currentStateValuations.addAll(subthread2.currentStateValuations);
	}

	public void runRepetitionProgram( RepetitionProgram program ) throws Exception {
		SymbolicExecutionThread subthread = new SymbolicExecutionThread( program.getSubProgram(), currentStateValuations );

		subthread.runDiscreteSteps();
		currentStateValuations.addAll( subthread.currentStateValuations );
	}

	public boolean evaluateFormula( dLFormula formula, HashMap<RealVariable,Real> valuation ) throws Exception {
		//evaluates only simple control-style logic, at the current valuation
		return evaluateFormula( formula, valuation );
	}

}
