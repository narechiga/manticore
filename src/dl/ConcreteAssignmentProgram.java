
package manticore.dl;

import java.util.*;

public class ConcreteAssignmentProgram extends DiscreteProgram {

	public ConcreteAssignmentProgram( RealVariable leftChild, Term rightChild ) {
		operator = new Operator( "assign" );

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
		return "(= " + getLHS().toMathematicaString() + " " + getRHS().toMathematicaString() +" )";
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

}
