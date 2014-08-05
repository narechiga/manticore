package manticore.dl;

public class PowerTerm extends Term {

	public PowerTerm( Term base, Term exponent ) {
		operator = new Operator("^", 2, true);

		spawnChildren();
		addChild( base );
		addChild( exponent );
	}

// Getters
	public Term getBase() {
		return (Term)getChild( 0 );
	}

	public Term getExponent() {
		return (Term)getChild( 1 );
	}

	public Term getLHS() {
		return (Term)getChild( 0 );
	}

	public Term getRHS() {
		return (Term)getChild( 1 );
	}

// Clone
	public PowerTerm clone() {
		return new PowerTerm( getBase(), getExponent() );
	}

}

