package manticore.dl;

import java.util.*;
import java.lang.*;

public class NativeInterpretation implements Interpretation {

	// Arithmetic
	protected final Operator addition = new Operator("+");
	protected final Operator subtraction = new Operator("-");
	protected final Operator multiplication = new Operator("*");
	protected final Operator division = new Operator("/");
	protected final Operator power = new Operator("^");

	// Comparison logic
	protected final Operator gt = new Operator(">");
	protected final Operator ge = new Operator(">=");
	protected final Operator lt = new Operator("<");
	protected final Operator le = new Operator("<=");
	protected final Operator eq = new Operator("=");




	public NativeInterpretation () {
	}
	
	public Double evaluateTerm( Term thisTerm, HashMap<RealVariable, Real> valuation) throws Exception {
		Double result = null;
		
		if ( thisTerm instanceof Real ) {

			try {
				result = Double.parseDouble( thisTerm.getOperator().toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered in ( thisTerm instanceof Real )");
				System.err.println(e);
				e.printStackTrace();
			}

		} else if ( thisTerm instanceof RealVariable ) {

			try {
				result = Double.parseDouble((valuation.get( (RealVariable)thisTerm )).getOperator().toString());

			} catch ( Exception e ) {
				System.err.println("Exception encountered in ( thisTerm instanceof RealVariable )");
				System.err.println(e);
				e.printStackTrace();
			}

		} else if ( thisTerm.operator.equals( addition ) ) {

			try {
				result = evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) + evaluateTerm( (Term)(thisTerm.children.get(1)), valuation );
			} catch ( Exception e ) {
				System.err.println("Exception encountered in ( thisTerm.operator.equals( addition ) )");
				System.err.println(e);
				e.printStackTrace();
			}

		} else if ( thisTerm.operator.equals( subtraction ) ) {

			try {
				result = evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) - evaluateTerm( (Term)(thisTerm.children.get(1)), valuation );
			} catch ( Exception e ) {
				System.err.println("Exception encountered in ( thisTerm.operator.equals( subtraction ) )");
				System.err.println(e);
				e.printStackTrace();
			}

		} else if ( thisTerm.operator.equals( multiplication ) ) {

			try {
				result = evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) * evaluateTerm( (Term)(thisTerm.children.get(1)), valuation );
			} catch ( Exception e ) {
				System.err.println("Exception encountered in ( thisTerm.operator.equals( multiplication ) )");
				System.err.println(e);
				e.printStackTrace();
			}

		} else if ( thisTerm.operator.equals( division ) ) {

			try {
				result = evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) / evaluateTerm( (Term)(thisTerm.children.get(1)), valuation );
			} catch ( Exception e ) {
				System.err.println("Exception encountered in ( thisTerm.operator.equals( division ) )");
				System.err.println(e);
				e.printStackTrace();
			}

		} else if ( thisTerm.operator.equals( power ) ) {

			try {
				result = Math.pow(evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ), evaluateTerm( (Term)(thisTerm.children.get(1)), valuation ));
			} catch ( Exception e ) {
				System.err.println("Exception encountered in ( thisTerm.operator.equals( power ) )");
				System.err.println(e);
				e.printStackTrace();
			}

		} else {
			throw new Exception("This arithmetic operator is not implemented in the native interpretation: " + thisTerm.getOperator().toString() );
		}

		return result;
	}

	public Boolean evaluateFormula( dLFormula thisFormula, HashMap<RealVariable, Real> valuation ) throws Exception {

		if ( thisFormula instanceof TrueFormula ) {
			return true;
		} else if ( thisFormula instanceof FalseFormula ) {
			return false;
		} else if ( thisFormula instanceof ComparisonFormula ) {

		/***/	if ( thisFormula.getOperator().equals( gt ) ) {
		/***/		return ( evaluateTerm( ((ComparisonFormula)thisFormula).getLHS(), valuation ) > evaluateTerm( ((ComparisonFormula)thisFormula).getRHS(), valuation ) );
		/***/	} else if ( thisFormula.getOperator().equals( ge ) ) {
		/***/		return ( evaluateTerm( ((ComparisonFormula)thisFormula).getLHS(), valuation ) >= evaluateTerm( ((ComparisonFormula)thisFormula).getRHS(), valuation ) );
		/***/	}else if ( thisFormula.getOperator().equals( lt ) ) {
		/***/		return ( evaluateTerm( ((ComparisonFormula)thisFormula).getLHS(), valuation ) < evaluateTerm( ((ComparisonFormula)thisFormula).getRHS(), valuation ) );
		/***/	}else if ( thisFormula.getOperator().equals( le ) ) {
		/***/		return ( evaluateTerm( ((ComparisonFormula)thisFormula).getLHS(), valuation ) <= evaluateTerm( ((ComparisonFormula)thisFormula).getRHS(), valuation ) );
		/***/	}else if ( thisFormula.getOperator().equals( eq ) ) {
		/***/		return ( evaluateTerm( ((ComparisonFormula)thisFormula).getLHS(), valuation ) == evaluateTerm( ((ComparisonFormula)thisFormula).getRHS(), valuation ) );
		/***/	} else {
		/***/		throw new Exception("This comparison operator is not implemented in the native interpretation: " + thisFormula.getOperator() );
		/***/	}

		} else if ( thisFormula instanceof NotFormula ) {
			return (! evaluateFormula( ((NotFormula)thisFormula).getChild(), valuation ) );
		} else if ( thisFormula instanceof AndFormula ) {
			return (evaluateFormula( ((AndFormula)thisFormula).getLeftChild(), valuation ) && evaluateFormula( ((AndFormula)thisFormula).getRightChild(), valuation ));
		} else if ( thisFormula instanceof OrFormula ) {
			return (evaluateFormula( ((OrFormula)thisFormula).getLeftChild(), valuation ) || evaluateFormula( ((OrFormula)thisFormula).getRightChild(), valuation ));
		} else if ( thisFormula instanceof ImpliesFormula ) {
			return ( (! evaluateFormula( ((ImpliesFormula)thisFormula).getAntecedent(), valuation )) || evaluateFormula( ((ImpliesFormula)thisFormula).getSuccedent(), valuation ) );
		} else if ( thisFormula instanceof IffFormula ) {
			return ( ( (! evaluateFormula( ((IffFormula)thisFormula).getAntecedent(), valuation )) || evaluateFormula( ((IffFormula)thisFormula).getSuccedent(), valuation ) )
				&& ( (! evaluateFormula( ((IffFormula)thisFormula).getSuccedent(), valuation )) || evaluateFormula( ((IffFormula)thisFormula).getAntecedent(), valuation ) ) );
		} else {
			throw new Exception("This logical operator is not implemented in the native interpretation: " + thisFormula.getOperator() );
		}

	}
}
