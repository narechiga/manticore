
package manticore.dl;

import java.util.*;

public class TestProgram extends DiscreteProgram {


	public TestProgram( dLStructure onlyChild ) {
		operator = new Operator( "test" );

		children = new ArrayList<dLStructure>();
		children.add( onlyChild );
	}

	// String methods
	public String toString() {
		return "( test " + children.get(0).toString() + " )";
	}

	public String toInfix() {
		return "(? " + children.get(0).toString() + " )";
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
