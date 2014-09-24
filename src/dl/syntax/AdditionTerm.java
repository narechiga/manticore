package manticore.dl.syntax;

import manticore.dl.semantics.*;

public class AdditionTerm extends Term {

// Constructor
	public AdditionTerm ( Term leftSummand, Term rightSummand ) {
		this.operator = new Operator("+", true );

		spawnChildren();
		addChild( leftSummand );
		addChild( rightSummand );
	}

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


}
