
package manticore.dl;

import java.util.*;

public class AssignmentProgram extends DiscreteProgram {

	public AssignmentProgram( Term leftChild, Term rightChild ) {
		operator = new Operator( "assign" );

		children = new ArrayList<dLStructure>();
		children.add( leftChild );
		children.add( rightChild );
	}

//	public String toString() {
//		return "( assign " + children.get(0).toString() + ", " + children.get(1).toString() +" )";
//	}

	public String toKeYmaeraString() {
		return "( " + children.get(0).toString() + " := " + children.get(1).toString() +" )";
	}

	// Administrative
	public boolean isPurelyDiscrete() {
		return true;
	}

	public boolean isPrimitive() {
		return true;
	}

}
