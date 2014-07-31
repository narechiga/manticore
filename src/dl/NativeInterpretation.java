package manticore.dl;

import java.util.*;;
import java.lang.*;;

public class NativeInterpretation implements Interpretation {

	//Arithmetic operators
	protected final Operator addition = new Operator("+");
	protected final Operator subtraction = new Operator("-");
	protected final Operator multiplication = new Operator("*");
	protected final Operator division = new Operator("/");
	protected final Operator power = new Operator("^");
	protected final Operator absolutevalue = new Operator("abs");
	protected final Operator arccosine = new Operator("acos");
	protected final Operator arcsine = new Operator("asin");
	protected final Operator arctangent = new Operator("atan");
	protected final Operator arctangent2 = new Operator("atan2");
	protected final Operator cosine = new Operator("cos");
	protected final Operator hypcosine = new Operator("cosh");
	protected final Operator exponential = new Operator("exp");
	protected final Operator floor = new Operator("floor");
	protected final Operator lognatural = new Operator("ln");
	protected final Operator log10 = new Operator("log");
	protected final Operator maximum = new Operator("max");
	protected final Operator minimum = new Operator("min");
	protected final Operator sign = new Operator("sign");
	protected final Operator sine = new Operator("sin");
	protected final Operator hypsine = new Operator("sinh");
	protected final Operator sqrt = new Operator("sqrt");
	protected final Operator tangent = new Operator("tan");
	protected final Operator hyptangent = new Operator("tanh");
	protected final Operator Sine = new Operator("Sin");
	protected final Operator Cosine = new Operator("Cos");

	//Comparison operators
	protected final Operator gt = new Operator(">");
	protected final Operator ge = new Operator(">=");
	protected final Operator lt = new Operator("<");
	protected final Operator le = new Operator("<=");
	protected final Operator eq = new Operator("=");

	public NativeInterpretation () {
	}

	public Real evaluateTerm( Term thisTerm, Valuation valuation) throws Exception {

		Double doubleResult = null;
		Real result = null;

		if ( thisTerm instanceof Real ) {
			try{
				result = (Real)thisTerm;
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for base case Real");
				e.printStackTrace();
			}
		} else if ( thisTerm instanceof RealVariable ) {
			try{
				result = valuation.get((RealVariable)thisTerm);
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for base case RealVariable");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( addition ) ) {
			try {
				doubleResult = (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation )).toDouble()
						+ (evaluateTerm( (Term)(thisTerm.children.get(1)), valuation )).toDouble();
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: addition ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( subtraction ) ) {
			try {
				doubleResult = (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation )).toDouble()
						- (evaluateTerm( (Term)(thisTerm.children.get(1)), valuation )).toDouble();
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: subtraction ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( multiplication ) ) {
			try {
				doubleResult = (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation )).toDouble()
						* (evaluateTerm( (Term)(thisTerm.children.get(1)), valuation )).toDouble();
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: multiplication ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( division ) ) {
			try {
				doubleResult = (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation )).toDouble()
						/ (evaluateTerm( (Term)(thisTerm.children.get(1)), valuation )).toDouble();
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: division ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( power ) ) {
			try {
				doubleResult = Math.pow( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation )).toDouble(),
						(evaluateTerm( (Term)(thisTerm.children.get(1)), valuation )).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: power ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( absolutevalue ) ) {
			try {
				doubleResult = Math.abs( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: absolutevalue ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( arccosine ) ) {
			try {
				doubleResult = Math.acos( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: arccosine ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( arcsine ) ) {
			try {
				doubleResult = Math.asin( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: arcsine ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( arctangent ) ) {
			try {
				doubleResult = Math.atan( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: arctangent ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( arctangent2 ) ) {
			try {
				doubleResult = Math.atan2( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation )).toDouble(),
						(evaluateTerm( (Term)(thisTerm.children.get(1)), valuation )).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: arctangent2 ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( cosine ) ) {
			try {
				doubleResult = Math.cos( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: cosine ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( hypcosine ) ) {
			try {
				doubleResult = Math.cosh( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: hypcosine ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( exponential ) ) {
			try {
				doubleResult = Math.exp( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: exponential ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( floor ) ) {
			try {
				doubleResult = Math.floor( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: floor ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( lognatural ) ) {
			try {
				doubleResult = Math.log( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: lognatural ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( log10 ) ) {
			try {
				doubleResult = Math.log10( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: log10 ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( maximum ) ) {
			try {
				doubleResult = Math.max( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation )).toDouble(),
						(evaluateTerm( (Term)(thisTerm.children.get(1)), valuation )).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: maximum ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( minimum ) ) {
			try {
				doubleResult = Math.min( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation )).toDouble(),
						(evaluateTerm( (Term)(thisTerm.children.get(1)), valuation )).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: minimum ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( sign ) ) {
			try {
				doubleResult = Math.signum( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: sign ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( sine ) ) {
			try {
				doubleResult = Math.sin( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: sine ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( hypsine ) ) {
			try {
				doubleResult = Math.sinh( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: hypsine ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( sqrt ) ) {
			try {
				doubleResult = Math.sqrt( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: sqrt ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( tangent ) ) {
			try {
				doubleResult = Math.tan( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: tangent ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( hyptangent ) ) {
			try {
				doubleResult = Math.tanh( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: hyptangent ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( Sine ) ) {
			try {
				doubleResult = Math.sin( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: Sine ::");
				e.printStackTrace();
			}
		} else if ( thisTerm.operator.equals( Cosine ) ) {
			try {
				doubleResult = Math.cos( (evaluateTerm( (Term)(thisTerm.children.get(0)), valuation ) ).toDouble() );
				result = new Real( doubleResult.toString() );
			} catch ( Exception e ) {
				System.err.println("Exception encountered evaluating rule for operator :: Cosine ::");
				e.printStackTrace();
			}
		} else {
			throw new Exception("This arithemtic operator is not implemented in the native interpretation: " + thisTerm.getOperator().toString() );
		}

		return result;
	}

	public Boolean evaluateFormula( dLFormula thisFormula, Valuation valuation ) throws Exception {

		if ( thisFormula instanceof TrueFormula ) {
			return true;
		} else if ( thisFormula instanceof FalseFormula ) {
			return false;
		} else if ( thisFormula instanceof ComparisonFormula ) {


		/**/ if ( thisFormula.getOperator().equals( gt ) ) {
		/**/ 	return ( (evaluateTerm( ((ComparisonFormula)thisFormula).getLHS(), valuation ).toDouble())
		/**/		> (evaluateTerm( ((ComparisonFormula)thisFormula).getRHS(), valuation ) ).toDouble());
		/**/	} else if ( thisFormula.getOperator().equals( ge ) ) {
		/**/		return ( (evaluateTerm( ((ComparisonFormula)thisFormula).getLHS(), valuation ).toDouble()) 
		/**/			>= (evaluateTerm( ((ComparisonFormula)thisFormula).getRHS(), valuation ) ).toDouble());
		/**/	}else if ( thisFormula.getOperator().equals( lt ) ) {
		/**/		return ( (evaluateTerm( ((ComparisonFormula)thisFormula).getLHS(), valuation ).toDouble())
		/**/			< (evaluateTerm( ((ComparisonFormula)thisFormula).getRHS(), valuation ) ).toDouble());
		/**/	}else if ( thisFormula.getOperator().equals( le ) ) {
		/**/		return ( (evaluateTerm( ((ComparisonFormula)thisFormula).getLHS(), valuation ).toDouble())
		/**/			<= (evaluateTerm( ((ComparisonFormula)thisFormula).getRHS(), valuation ) ).toDouble());
		/**/	}else if ( thisFormula.getOperator().equals( eq ) ) {
		/**/		return ( (evaluateTerm( ((ComparisonFormula)thisFormula).getLHS(), valuation )).equals(
		/**/			 (evaluateTerm( ((ComparisonFormula)thisFormula).getRHS(), valuation ) )));
		/**/	} else {
		/**/		throw new Exception("This comparison operator is not implemented in the native interpretation: "
		/**/			+ thisFormula.getOperator().toString());
		/**/	}


		} else if ( thisFormula instanceof NotFormula ) {
			return (! evaluateFormula( ((NotFormula)thisFormula).getFormula(), valuation ) );
		} else if ( thisFormula instanceof AndFormula ) {
			return (evaluateFormula( ((AndFormula)thisFormula).getLHS(), valuation ) 
				&& evaluateFormula( ((AndFormula)thisFormula).getRHS(), valuation ));
		} else if ( thisFormula instanceof OrFormula ) {
			return (evaluateFormula( ((OrFormula)thisFormula).getLHS(), valuation ) 
				|| evaluateFormula( ((OrFormula)thisFormula).getRHS(), valuation ));
		} else if ( thisFormula instanceof ImpliesFormula ) {
			return ( (! evaluateFormula( ((ImpliesFormula)thisFormula).getAntecedent(), valuation )) 
				|| evaluateFormula( ((ImpliesFormula)thisFormula).getSuccedent(), valuation ) );
		} else if ( thisFormula instanceof IffFormula ) {
			return ( ( (! evaluateFormula( ((IffFormula)thisFormula).getAntecedent(), valuation )) 
				|| evaluateFormula( ((IffFormula)thisFormula).getSuccedent(), valuation ) )
			&& ( (! evaluateFormula( ((IffFormula)thisFormula).getSuccedent(), valuation )) 
				|| evaluateFormula( ((IffFormula)thisFormula).getAntecedent(), valuation ) ) );
		} else {
			throw new Exception("This logical operator is not implemented in the native interpretation: :: "
				+ thisFormula.getOperator() + " ::");
			}
		}
	}
