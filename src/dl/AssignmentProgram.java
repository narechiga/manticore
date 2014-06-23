
package manticore.dl;

import java.util.*;

public class AssignmentProgram extends DiscreteProgram {

	public AssignmentProgram( Term leftChild, Term rightChild ) {
		operator = new Operator( "assign" );

		children = new ArrayList<dLStructure>();
		children.add( leftChild );
		children.add( rightChild );
	}

	public String toKeYmaeraString() {
		return "( " + children.get(0).toKeYmaeraString() + " := " + children.get(1).toKeYmaeraString() +" )";
	}

	// Administrative
	public boolean isPurelyDiscrete() {
		return true;
	}

	public boolean isPrimitive() {
		return true;
	}

}
