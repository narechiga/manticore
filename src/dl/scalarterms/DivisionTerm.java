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

}

