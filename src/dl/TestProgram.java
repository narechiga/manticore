
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

// Substitute
	public TestProgram substituteConcreteValuation( Valuation substitution ) {
		return new TestProgram( getFormula().substituteConcreteValuation( substitution ) );
	}

// Clone
	public TestProgram clone() {
		return new TestProgram( getFormula().clone() );
	}

// String methods
	public String toKeYmaeraString() {
		return "(? " + getFormula().toKeYmaeraString() + " )";
	}

	public String toManticoreString() {
		return "(? " + getFormula().toManticoreString() + " )";
	}

// Administrative
	public boolean isPurelyDiscrete() {
		return true;
	}

	public boolean isProgramPrimitive() {
		return true;
	}

	public boolean isQuantifierFree() {
		return getFormula().isQuantifierFree();
	}

}
