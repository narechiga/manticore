package manticore.dl;

import java.io.*;

public abstract class dLFormula extends dLStructure {

	public boolean isFirstOrder() {
		return false;
	}

	public boolean isModal() {
		return false;
	}

	public boolean isPropositionalPrimitive() {
		return false;
	}

	public boolean isStatic() {
		return false;
	}

	public boolean isQuantifierFree() {
		return false;
	}

// All subclasses need to implement this guy
	public abstract dLFormula clone();

	public abstract dLFormula substituteConcreteValuation( Valuation substitution );


}
