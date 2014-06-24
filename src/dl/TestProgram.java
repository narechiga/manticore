
package manticore.dl;

import java.util.*;

public class TestProgram extends DiscreteProgram {


	public TestProgram( dLStructure onlyChild ) {
		operator = new Operator( "test" );

		children = new ArrayList<dLStructure>();
		children.add( onlyChild );
	}

	public dLFormula getFormula() {
		return (dLFormula)(children.get(0));
	}

	// String methods
	public String toKeYmaeraString() {
		return "(? " + getFormula().toKeYmaeraString() + " )";
	}

	// Administrative
	public boolean isPurelyDiscrete() {
		return true;
	}

	public boolean isProgramPrimitive() {
		return true;
	}

}
