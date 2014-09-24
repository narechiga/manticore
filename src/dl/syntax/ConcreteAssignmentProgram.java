
package manticore.dl.syntax;

import manticore.dl.semantics.*;

import java.util.*;

public class ConcreteAssignmentProgram extends DiscreteProgram {

// Constructors and field getters
	public ConcreteAssignmentProgram( RealVariable leftChild, Term rightChild ) {
		operator = new Operator( "assign", 2, true );

		children = new ArrayList<dLStructure>();
		children.add( leftChild );
		children.add( rightChild );
	}

	public RealVariable getLHS() {
		return (RealVariable)children.get(0);
	}

	public Term getRHS() {
		return (Term)children.get(1);
	}

// Substitution method
	public ConcreteAssignmentProgram substituteConcreteValuation( Valuation substitution ) {
		// Don't mess with the LHS, just the RHS
		return new ConcreteAssignmentProgram( getLHS().clone(),
							getRHS().substituteConcreteValuation( substitution ) );
	}

// Clone method
	public ConcreteAssignmentProgram clone() {
		return new ConcreteAssignmentProgram( getLHS().clone(), getRHS().clone() );
	}

// String methods
	public String toKeYmaeraString() {
		return "( " + getLHS().toKeYmaeraString() + " := " + getRHS().toKeYmaeraString() +" )";
	}

	public String toManticoreString() {
		return "( " + getLHS().toManticoreString() + " := " + getRHS().toManticoreString() +" )";
	}

	public String toMathematicaString() {
		return "( " + getLHS().toMathematicaString() + " = " + getRHS().toMathematicaString() +" )";
	}

	public String todRealString() {
		return "(= " + getLHS().todRealString() + " " + getRHS().todRealString() +" )";
	}

// Assorted convenience functions
	public boolean isPurelyDiscrete() {
		return true;
	}

	public boolean isProgramPrimitive() {
		return true;
	}

	public boolean isQuantifierFree() {
		return true;
	}


// This program cannot quantify variables
//	public Set<RealVariable> getBoundVariables();

	public Set<RealVariable> getFreeVariables() {
		HashSet<RealVariable> freeVariables = new HashSet<RealVariable>();
		freeVariables.addAll( getLHS().getFreeVariables() );
		freeVariables.addAll( getRHS().getFreeVariables() );
		return freeVariables;
	}

	public Set<RealVariable> getDynamicVariables() {
		HashSet<RealVariable> dynamicVariables = new HashSet<RealVariable>();
		dynamicVariables.add( getLHS() );
		return dynamicVariables;
	}

}
