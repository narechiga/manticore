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

	//public RealVariable lhs() {
	//	return (RealVariable)children.get(0);
	//}

	//public Term rhs() {
	//	return (Term)children.get(1);
	//}

	public String toInfix() {
		return toString();
	}

	public String toString () {
		String returnString = "";

		//returnString = returnString + "{ ";
		returnString = returnString + children.get(0).toString() + "' = " + children.get(1).toString();
		//returnString = returnString + " }";

		return returnString;
	}


}
