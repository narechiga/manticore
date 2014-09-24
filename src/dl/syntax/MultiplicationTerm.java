package manticore.dl.syntax;

import manticore.dl.semantics.*;

public class MultiplicationTerm extends Term {

	public MultiplicationTerm( Term leftFactor, Term rightFactor ) {
		this.operator = new Operator("*", 2, true);

		spawnChildren();
		addChild( leftFactor );
		addChild( rightFactor );
	}

// Getters
	public Term getLeftFactor() {
	       return (Term)getChild( 0 );
	}

	public Term getRightFactor() {
		return (Term)getChild( 1 );
	}

// Clone
	public MultiplicationTerm clone() {
		return new MultiplicationTerm( getLeftFactor(), getRightFactor() );
	}

}

