
package manticore.dl;

import java.util.*;

public class AssignmentProgram extends DiscreteProgram {

	public AssignmentProgram( dLStructure leftChild, dLStructure rightChild ) {
		operator = new Operator( "assign" );

		children = new ArrayList<dLStructure>();
		children.add( leftChild );
		children.add( rightChild );
	}


	// Print methods
	//public String toString() {
	//	return "( assign " + children.get(0).toString() + ", " + children.get(1).toString() +" )";
	//}

	public String toInfix() {
		return "( " + children.get(0).toString() + " := " + children.get(1).toString() +" )";
	}

	public String toKeYmaera() {
		return toInfix();
	}

	// Administrative
	public boolean isPurelyDiscrete() {
		return true;
	}

	public boolean isPrimitive() {
		return true;
	}

}
