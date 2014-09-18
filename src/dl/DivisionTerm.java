package manticore.dl;

public class DivisionTerm extends Term {

	public DivisionTerm ( Term dividend, Term divisor ) {
		operator = new Operator( "/", 2, true );

		spawnChildren();
		addChild( dividend );
		addChild( divisor );
	}

	public Term getDividend() {
		return (Term)getChild( 0 );
	}

	public Term getDivisor() {
		return (Term)getChild( 1 );
	}

	public Term getNumerator() {
		return (Term)getChild( 0 );
	}

	public Term getDenominator() {
		return (Term)getChild( 1 );
	}

	public Term getLHS() {
		return (Term)getChild( 0 );
	}

	public Term getRHS() {
		return (Term)getChild( 1 );
	}

	public boolean isLinearIn( ArrayList<RealVariable> variables ) {
		// If the dividend is linear and the denominator does not contain
		// the variables, then it is linear.

		boolean linearity;

		if ( getNumerator().isLinearIn( variables ) ) {
			linearity = true;
			
			// Now check if any of the variables appears in the denom
			Set<RealVariable> denomVars = getDenominator().getFreeVariables();
			int denomVarsCardinality = denomVars.size();
			denomVars.removeAll( variables );
			if ( denomVars.size() < denomVarsCardinality ) {
				linearity = false;
			}

		} else {
			linearity = false;
		}

		return linearity;
	}

	public boolean isAffineIn( ArrayList<RealVariable> variables ) {
		// If the dividend is affine and the denominator does not contain
		// the variables, then it is affine.

		boolean affinity;

		if ( getNumerator().isAffineIn( variables ) ) {
			affinity = true;
			
			// Now check if any of the variables appears in the denom
			Set<RealVariable> denomVars = getDenominator().getFreeVariables();
			int denomVarsCardinality = denomVars.size();
			denomVars.removeAll( variables );
			if ( denomVars.size() < denomVarsCardinality ) {
				affinity = false;
			}

		} else {
			affinity = false;
		}

		return affinity;
	}

}

