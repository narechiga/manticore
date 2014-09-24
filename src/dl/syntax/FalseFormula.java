package manticore.dl.syntax;

import manticore.dl.semantics.*;

import java.util.*;


public class FalseFormula extends dLFormula {

// Constructor
	public FalseFormula () {
		this.operator = new Operator("false", 0); //
	}

// Substitution method
	public FalseFormula substituteConcreteValuation( Valuation substitution ) {
		return new FalseFormula();
	}

// Clone method
	public FalseFormula clone() {
		return new FalseFormula();
	}

// String methods
	public String toKeYmaeraString () {
		return "false";
	}

	public String toMathematicaString () {
		return "False";
	}
	
	public String todRealString () {
		return "false";
	}

// Assorted convenience functions
	public boolean isFirstOrder() {
		return true;
	}

	public boolean isPropositionalPrimitive() {
		return true;
	}

	public boolean isStatic() {
		return true;
	}

	public boolean isQuantifierFree() {
		return true;
	}

// Logic
	public TrueFormula negate() {
		return new TrueFormula();
	}

	public Set<RealVariable> getBoundVariables() {
		return new HashSet<RealVariable>();
	}

	public Set<RealVariable> getFreeVariables() {
		return new HashSet<RealVariable>();
	}


	public Set<RealVariable> getDynamicVariables() {
		HashSet<RealVariable> dynamicVariables = new HashSet<RealVariable>();
		return dynamicVariables;
	}
}
