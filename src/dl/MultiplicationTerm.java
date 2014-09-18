package manticore.dl;

public class MultiplicationTerm extends Term {

	public MultiplicationTerm( Term leftFactor, Term rightFactor ) {
		this.operator = new Operator("*", 2, true);

		spawnChildren();
		addChild( leftFactor );
		addChild( rightFactor );
	}

// Getters
	//public Term getLeftTerm() {
	//	return (Term)getChild( 0 );
	//}

	//public Term getRightTerm() {
	//	return (Term)getChild( 1 );
	//}

	public Term getLeftFactor() {
	       return (Term)getChild( 0 );
	}

	public Term getRightFactor() {
		return (Term)getChild( 1 );
	}

	public Term getLHS() {
		return (Term)getChild( 0 );
	}

	public Term getRHS() {
		return (Term)getChild( 1 );
	}

// Clone
	public MultiplicationTerm clone() {
		return new MultiplicationTerm( getLeftFactor(), getRightFactor() );
	}

// Arithmetic
	public boolean isLinearIn( ArrayList<RealVariable> variables ) {
		// If exactly one of the factors contains any of the variables
		// of interest, then the product is linear

		boolean linearity;




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

