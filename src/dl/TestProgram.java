
package manticore.dl;

import java.util.*;

public class TestProgram extends DiscreteProgram {


	public TestProgram( dLStructure onlyChild ) {
		operator = new Operator( "test" );

		children = new ArrayList<dLStructure>();
		children.add( onlyChild );
	}

	// String methods
	public String toKeYmaeraString() {
		return "(? " + children.get(0).toKeYmaeraString() + " )";
	}

	// Administrative
	public boolean isPurelyDiscrete() {
		return true;
	}

	public boolean isPrimitive() {
		return true;
	}

}
