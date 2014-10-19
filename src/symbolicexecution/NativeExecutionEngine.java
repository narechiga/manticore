package manticore.symbolicexecution;

import proteus.dl.syntax.*;
import proteus.dl.semantics.*;
import java.util.*;
import java.lang.*;

public class NativeExecutionEngine {

	//Determine behavior of star operator
	int maxIterations;
	int iteration;

	// bounds
	HashMap<RealVariable,Double> lowerBounds;
	HashMap<RealVariable,Double> upperBounds;
	

	Interpretation interpretation;

//
	public NativeExecutionEngine () {
		// Defaults to a NativeInterpretation, because after all this is NativeExecution
		// and I don't want to have to think about interpretations all the time
		this.interpretation = new NativeInterpretation();
		this.iteration = 0;
		this.maxIterations = 10; // Default
		lowerBounds = new HashMap<RealVariable, Double>();
		upperBounds = new HashMap<RealVariable, Double>();
	}


//
	public NativeExecutionEngine ( Interpretation interpretation ) {
		this.interpretation = interpretation;
		this.iteration = 0;
		this.maxIterations = 10; // Default
		lowerBounds = new HashMap<RealVariable, Double>();
		upperBounds = new HashMap<RealVariable, Double>();
	}

//
	public NativeExecutionEngine ( Interpretation interpretation, int maxIterations ) {
		this.interpretation = interpretation;
		this.iteration = 0;
		this.maxIterations = maxIterations;
		lowerBounds = new HashMap<RealVariable, Double>();
		upperBounds = new HashMap<RealVariable, Double>();
	}
//
	public NativeExecutionEngine ( Interpretation interpretation, int maxIterations, dLFormula bounds ) {
		this.interpretation = interpretation;
		this.iteration = 0;
		this.maxIterations = maxIterations;
		lowerBounds = new HashMap<RealVariable, Double>();
		upperBounds = new HashMap<RealVariable, Double>();
		parseBounds( bounds );
	}
//
	public NativeExecutionEngine ( Interpretation interpretation, dLFormula bounds ) {
		this.interpretation = interpretation;
		this.iteration = 0;
		this.maxIterations = 10; // Default
		lowerBounds = new HashMap<RealVariable, Double>();
		upperBounds = new HashMap<RealVariable, Double>();
		parseBounds( bounds );
	}

//
	protected void parseBounds( dLFormula bounds ) {
		if ( bounds != null ) {
			ArrayList<dLFormula> boundsList = bounds.splitOnAnds();

			Iterator<dLFormula> boundsIterator = boundsList.iterator();
			ComparisonFormula thisBound;
			Set<RealVariable> theseVariables;
			RealVariable thisVariable;

			while ( boundsIterator.hasNext() ) {


				try {
					// Try to parse the bounds, but don't worry too
					// much if it fails, since then bounds are
					// replaced by unitary bounds and everything works
					// out anyway

					thisBound = (ComparisonFormula)(boundsIterator.next());

					if ( thisBound.getOperator().equals( new Operator("<=") ) 
						|| thisBound.getOperator().equals( new Operator("<") )) {

						if ( (thisBound.getLHS() instanceof Real)
							&& (thisBound.getRHS() instanceof RealVariable) ) {

							lowerBounds.put( (RealVariable)(thisBound.getRHS()), 
								((Real)(thisBound.getLHS())).toDouble() );

						} else if ( (thisBound.getLHS() instanceof RealVariable)
							&& (thisBound.getRHS() instanceof Real ) ) {

							upperBounds.put( (RealVariable)(thisBound.getRHS()), 
								((Real)(thisBound.getLHS())).toDouble() );
						}

					} else if ( thisBound.getOperator().equals( new Operator(">=") )
						|| thisBound.getOperator().equals( new Operator(">") )) {

						if ( (thisBound.getLHS() instanceof Real)
							&& (thisBound.getRHS() instanceof RealVariable) ) {

							upperBounds.put( (RealVariable)(thisBound.getRHS()), 
								((Real)(thisBound.getLHS())).toDouble() );

						} else if ( (thisBound.getLHS() instanceof RealVariable)
							&& (thisBound.getRHS() instanceof Real ) ) {

							lowerBounds.put( (RealVariable)(thisBound.getRHS()), 
								((Real)(thisBound.getLHS())).toDouble() );
						}
					}
				} catch ( Exception e ) {
					System.out.println("WARNING: Bounds couldn't be parsed,"
						+ " replacing with unitary bounds; ");
					e.printStackTrace();
				}

			}

		} else {

			System.out.println("WARNING: Bounds couldn't be parsed,"
				+ " replacing with unitary bounds; ");
		}

	}

//
	public Valuation sampleVectorField( HybridProgram program, Valuation initialState ) throws Exception {
		ValuationList valuations = new ValuationList();
		valuations.add( initialState );

		valuations = runDiscreteSteps( program, valuations );

		if ( valuations.size() == 1 ) {
			return valuations.get( 0 );

		} else {
			int randomIndex = (int)(Math.round((valuations.size() - 1)*Math.random()));

			return valuations.get( randomIndex );
		}
	}
//
	public ValuationList runDiscreteSteps( HybridProgram program, ValuationList valuations ) throws Exception {

		if ( this.iteration == this.maxIterations ) {
			return valuations;
		}

		ValuationList returnValuations;

		if ( program instanceof ConcreteAssignmentProgram ) {
			returnValuations = runConcreteAssignmentProgram( (ConcreteAssignmentProgram)program, valuations );
		} else if ( program instanceof ArbitraryAssignmentProgram ) {
			returnValuations = runArbitraryAssignmentProgram( (ArbitraryAssignmentProgram)program, valuations );
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
//
	public ProgramTrace runTrace( HybridProgram program, ProgramTrace trace ) throws Exception {

		if ( this.iteration == this.maxIterations ) {
			return trace;
		}
	}

//
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
	protected ValuationList runArbitraryAssignmentProgram( ArbitraryAssignmentProgram program, 
								ValuationList valuations ) throws Exception {
		ValuationList returnValuations = valuations.clone();		

		Iterator<Valuation> valuationIterator = returnValuations.iterator();

		// Recover upper and lower bounds, replace with unitary bounds if
		// no bounds are given
		double lower; double upper;
		RealVariable thisVariable = program.getLHS();
		if ( lowerBounds.get( thisVariable ) != null ) {
			lower = lowerBounds.get( thisVariable );
		} else {
			lower = -1;
		}
		if ( upperBounds.get( thisVariable ) != null ) {
			upper = upperBounds.get( thisVariable );
		} else {
			upper = 1;
		}

		double randomNumber;
		while ( valuationIterator.hasNext() ) {
			Valuation thisValuation = valuationIterator.next();

			randomNumber = new Double( ( upper - lower )*Math.random() + lower );

			thisValuation.put( thisVariable, new Real( randomNumber) );
		}

		return returnValuations;

	}

//
	protected ValuationList runTestProgram( TestProgram program, 
							ValuationList valuations ) throws Exception {

		ValuationList returnValuations = new ValuationList();
		Iterator<Valuation> valuationIterator = valuations.iterator();

		Valuation thisValuation;
		while ( valuationIterator.hasNext() ) {
			thisValuation = valuationIterator.next();

			if ( evaluateFormula( program.getFormula(), thisValuation ) ) {
			    returnValuations.add( thisValuation.clone() );
			} 

		}

		return returnValuations;
	}

//
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

//
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

//
	protected ValuationList runChoiceProgram( ChoiceProgram program, 
							ValuationList valuations ) throws Exception {

		ValuationList returnValuations;
	
		//System.out.println( "Initial states for choice program: " + valuations );
		// Run left program
		returnValuations = runDiscreteSteps( program.getLHS(), valuations );
		//System.out.println("After choice subthread 1: " + returnValuations );
		
		// Run right program
		//System.out.println( "Starting choice subthread 2 with: " + valuations );
		returnValuations.addAll( runDiscreteSteps( program.getRHS(), valuations ) );
		//System.out.println("After choice subthread 2: " + returnValuations );

		return returnValuations;
	}

//
	public ValuationList runRepetitionProgram( RepetitionProgram program,
							ValuationList valuations ) throws Exception {

		this.iteration = this.iteration + 1;
		SequenceProgram iterate = new SequenceProgram( program.getProgram(), program );
		return runDiscreteSteps( iterate, valuations );
	}

//
	public boolean evaluateFormula( dLFormula formula, Valuation valuation ) throws Exception {
		//evaluates only simple control-style logic, at the current valuation
		return interpretation.evaluateFormula( formula, valuation );
	}

}
