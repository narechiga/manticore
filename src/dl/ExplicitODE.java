package manticore.dl;

import java.util.*;

public class ExplicitODE extends dLStructure {

	public ExplicitODE ( RealVariable lhs, Term rhs ) {
		operator = new Operator( "explicit-ode" );

		children = new ArrayList<dLStructure>();
		children.add( lhs );
		children.add( rhs );

	}

	public ExplicitODE ( dLStructure lhs, dLStructure rhs ) {
		operator = new Operator( "explicit-ode" );

		children = new ArrayList<dLStructure>();
		children.add( lhs );
		children.add( rhs );

	}

	public RealVariable getLHS() {
		return (RealVariable)(children.get(0));
	}

	public Term getRHS() {
		return (Term)(children.get(1));
	}

	public String toInfix() {
		return toString();
	}

	public String toKeYmaeraString () {
		String returnString = "";

		returnString = returnString + getLHS().toKeYmaeraString() + "' = " + getRHS().toKeYmaeraString();

		return returnString;
	}


}
