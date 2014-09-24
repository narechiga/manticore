
package manticore.dl.syntax.syntax;

import java.util.*;

public class ArbitraryAssignmentProgram extends DiscreteProgram {

	public ArbitraryAssignmentProgram( RealVariable child ) {
		operator = new Operator( "arbitrary-assign", 1, true );

		children = new ArrayList<dLStructure>();
		children.add( child );
	}

	public RealVariable getLHS() {
		return (RealVariable)children.get(0);
	}


// Substitution method
	public ArbitraryAssignmentProgram substituteConcreteValuation( Valuation substitution ) {
		return this;
	}

// Clone method
	public ArbitraryAssignmentProgram clone() {
		return new ArbitraryAssignmentProgram( getLHS().clone() );
	}

// String methods
	public String toKeYmaeraString() {
		return "( " + getLHS().toKeYmaeraString() + " := * )";
	}
	
	public String toManticoreString() {
		return "( " + getLHS().toManticoreString() + " := ** )";
	}

// Administrative
	public boolean isPurelyDiscrete() {
		return true;
	}

	public boolean isProgramPrimitive() {
		return true;
	}

	public boolean isQuantifierFree() {
		return true;
	}

// public Set<RealVariable> getBoundVariables();
// Assignment program cannot bind any variables, so we do not return any bound variables

	public Set<RealVariable> getFreeVariables() {
		return getLHS().getFreeVariables();
	}

	public Set<RealVariable> getDynamicVariables() {
		HashSet<RealVariable> dynamicVariables = new HashSet<RealVariable>();
		dynamicVariables.add( getLHS() );
		return dynamicVariables;
	}

}
