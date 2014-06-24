
package manticore.dl;

import java.util.*;

public class AssignmentProgram extends DiscreteProgram {

	public AssignmentProgram( RealVariable leftChild, Term rightChild ) {
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

	// Administrative
	public boolean isPurelyDiscrete() {
		return true;
	}

	public boolean isProgramPrimitive() {
		return true;
	}

}
