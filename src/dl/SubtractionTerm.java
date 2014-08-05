package manticore.dl;

public class SubtractionTerm extends Term {

	public SubtractionTerm ( Term minuend, Term subtrahend ) {
		operator = new Operator( "-", 2, true );

		spawnChildren();
		addChild( minuend );
		addChild( subtrahend );
	}

// Getters
	public Term getMinuend() {
		return (Term)getChild( 0 );
	}

	public Term getSubtrahend() {
		return (Term)getChild( 1 );
	}

	public Term getLHS() {
		return (Term)getChild( 0 );
	}

	public Term getRHS() {
		return (Term)getChild( 1 );
	}
	
// Clone
	public SubtractionTerm clone() {
		return new SubtractionTerm( getMinuend().clone(), getSubtrahend().clone() );
	}

}

