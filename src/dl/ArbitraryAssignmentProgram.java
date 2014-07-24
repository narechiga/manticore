
package manticore.dl;

import java.util.*;

public class ArbitraryAssignmentProgram extends DiscreteProgram {

	public ArbitraryAssignmentProgram( RealVariable child ) {
		operator = new Operator( "arbitrary-assign" );

		children = new ArrayList<dLStructure>();
		children.add( child );
	}

	public RealVariable getLHS() {
		return (RealVariable)children.get(0);
	}

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

}
