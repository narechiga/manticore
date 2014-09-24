package manticore.symbolicexecution;

import manticore.dl.syntax.*;
import manticore.dl.semantics.*;
import java.util.*;

public class SymbolicExecutionEngine {

	//Determine behavior of star operator
	int maxIterations;
	int iteration;
	
	Interpretation interpretation;

//
	public SymbolicExecutionEngine ( Interpretation interpretation ) {
		this.interpretation = interpretation;
		this.iteration = 0;
		this.maxIterations = 10; // Default
	}

//
	public SymbolicExecutionEngine ( Interpretation interpretation, int maxIterations ) {
		this.interpretation = interpretation;
		this.iteration = 0;
		this.maxIterations = maxIterations; 
	}

//
	public UpdateRuleList runDiscreteSteps( HybridProgram program, UpdateRuleList updateRules ) throws Exception {

		if ( this.iteration == this.maxIterations ) {
			return updateRules;
		}

		UpdateRuleList returnUpdateRules;

		if ( program instanceof ConcreteAssignmentProgram ) {
			returnUpdateRules = runConcreteAssignmentProgram( (ConcreteAssignmentProgram)program, updateRules );
		} else if ( program instanceof TestProgram ) {
			returnUpdateRules = runTestProgram( (TestProgram)program, updateRules );
		//} else if ( program instanceof ContinuousProgram ) {
		//	// Evaluate the RHS of the differential equations, but don't actually evolve
		//	returnUpdateRules = runContinuousProgram( (ContinuousProgram)program, updateRules );
		} else if ( program instanceof SequenceProgram ) {
			returnUpdateRules = runSequenceProgram( (SequenceProgram)program, updateRules );
		} else if ( program instanceof ChoiceProgram ) {
			returnUpdateRules = runChoiceProgram( (ChoiceProgram)program, updateRules );
		} else if ( program instanceof RepetitionProgram ) {
			returnUpdateRules = runRepetitionProgram( (RepetitionProgram)program, updateRules );
		} else {
			returnUpdateRules = null;
			throw new Exception("SymbolicExecutionEngine does not recognize this program type");
		}

		return returnUpdateRules;
	}

//
	protected UpdateRuleList runConcreteAssignmentProgram( ConcreteAssignmentProgram program, 
								UpdateRuleList updateRules ) throws Exception {
		UpdateRuleList returnUpdateRules = updateRules.clone();		
		
		Iterator<UpdateRule> updateRuleIterator = returnUpdateRules.iterator();
		while ( updateRuleIterator.hasNext() ) {
			UpdateRule thisUpdateRule = updateRuleIterator.next();

			ComparisonFormula characteristicFormula = new ComparisonFormula( 
									new Operator("="),
									program.getLHS(),
									program.getRHS() );

			thisUpdateRule.putRule( program.getLHS(), characteristicFormula );
		}

		return returnUpdateRules;
	}

//
	protected UpdateRuleList runTestProgram( TestProgram program, 
							UpdateRuleList updateRules ) throws Exception {

		UpdateRuleList returnUpdateRules = new UpdateRuleList();
		Iterator<UpdateRule> updateRuleIterator = updateRules.iterator();

		UpdateRule thisUpdateRule;
		while ( updateRuleIterator.hasNext() ) {

			thisUpdateRule = updateRuleIterator.next();
			thisUpdateRule.addConstraint( program.getFormula() );
			returnUpdateRules.add( thisUpdateRule.clone() );
		}

		return returnUpdateRules;
	}

//
	//protected UpdateRuleList runContinuousProgram( ContinuousProgram program,
	//						UpdateRuleList updateRules ) throws Exception {


	//	if ( updateRules.size() > 1 ) {
	//		System.out.println("WARNING: Your system does not seems to be deterministic; "
	//					+ updateRules.size() );
	//	}

	//	// We should kill the process, so pretend that max iterations have been reached
	//	this.iteration = this.maxIterations;

	//	ArrayList<ExplicitODE> odeList = program.getODEs();
	//	Iterator<ExplicitODE> odeIterator = odeList.iterator();

	//	Real thisRHS = null;
	//	ExplicitODE thisODE = null;
	//	RealVariable thisDerivativeVariable = null;
	//	UpdateRule thisUpdateRule = updateRules.get(0); // Remember, there should be only one!
	//	UpdateRule derivativeUpdateRule = thisUpdateRule.clone();

	//	while ( odeIterator.hasNext() ) {
	//			
	//		thisODE = odeIterator.next();
	//		thisRHS = (Real)(interpretation.evaluateTerm( thisODE.getRHS(), thisUpdateRule ));
	//		thisDerivativeVariable = new RealVariable( "_d"+thisODE.getLHS().toString()+"dt_" );
	//		derivativeUpdateRule.put( thisDerivativeVariable, thisRHS );
	//	}

	//	UpdateRuleList returnUpdateRules = new UpdateRuleList();
	//	returnUpdateRules.add( derivativeUpdateRule );
	//	return returnUpdateRules;
	//}

//
	protected UpdateRuleList runSequenceProgram( SequenceProgram program, 
							UpdateRuleList updateRules ) throws Exception {

		UpdateRuleList returnUpdateRules;

		if ( program.getFirstProgram() instanceof ContinuousProgram ) {
			throw new Exception("Sequential composition after continuous blocks are not currently supported");
		}

		// Run first step
		returnUpdateRules = runDiscreteSteps( program.getFirstProgram(), updateRules );
		//System.out.println("After sequence subthread 1: " + returnUpdateRules );

		// Run second step
		returnUpdateRules = runDiscreteSteps( program.getSecondProgram(), returnUpdateRules );
		//System.out.println("After sequence subthread 2: " + returnUpdateRules );

		return returnUpdateRules;
	}

//
	protected UpdateRuleList runChoiceProgram( ChoiceProgram program, 
							UpdateRuleList updateRules ) throws Exception {

		UpdateRuleList returnUpdateRules;
	
		//System.out.println( "Initial states for choice program: " + updateRules );
		// Run left program
		returnUpdateRules = runDiscreteSteps( program.getLHS(), updateRules );
		//System.out.println("After choice subthread 1: " + returnUpdateRules );
		
		// Run right program
		//System.out.println( "Starting choice subthread 2 with: " + updateRules );
		returnUpdateRules.addAll( runDiscreteSteps( program.getRHS(), updateRules ) );
		//System.out.println("After choice subthread 2: " + returnUpdateRules );

		return returnUpdateRules;
	}

//
	public UpdateRuleList runRepetitionProgram( RepetitionProgram program,
							UpdateRuleList updateRules ) throws Exception {

		this.iteration = this.iteration + 1;
		SequenceProgram iterate = new SequenceProgram( program.getProgram(), program );
		return runDiscreteSteps( iterate, updateRules );
	}

//
	//public boolean evaluateFormula( dLFormula formula, UpdateRule updateRule ) throws Exception {
	//	//evaluates only simple control-style logic, at the current updateRule
	//	return interpretation.evaluateFormula( formula, updateRule );
	//}

}
