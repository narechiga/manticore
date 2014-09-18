package manticore.dl;

public class AdditionTerm extends Term {

// Constructor
	public AdditionTerm ( Term leftSummand, Term rightSummand ) {
		this.operator = new Operator("+", true );

		spawnChildren();
		addChild( leftSummand );
		addChild( rightSummand );
	}

// Getters, with different names
	//public Term getLeftTerm() {
	//	return (Term)getChild( 0 );
	//}

	//public Term getRightTerm() {
	//	return (Term)getChild( 1 );
	//}

	public Term getLeftSummand() {
		return (Term)getChild( 0 );
	}

	public Term getRightSummand() {
		return (Term)getChild( 1 );
	}

	public Term getLHS() {
		return (Term)getChild( 0 );
	}

	public Term getRHS() {
		return (Term)getChild( 1 );
	}

// Clone
	public AdditionTerm clone() {
		return new AdditionTerm( getLeftSummand().clone() , getRightSummand().clone() );
	}

// Arithmetic
	public boolean isLinearIn( ArrayList<RealVariable> variables ) {
		if ( getLeftSummand().isLinearIn( variables )
			&& getRightSummand().isLinearIn( variables ) ) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isAffineIn( ArrayList<RealVariable> variables ) {
		if ( getLeftSummand().isAffineIn( variables )
			&& getRightSummand().isAffineIn( variables ) ) {
			return true;
		} else {
			return false;
		}
	}

}
