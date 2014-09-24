package manticore.dl.syntax;

import manticore.dl.semantics.*;
import java.util.*;


public class TrueFormula extends dLFormula {

// Constructor
	public TrueFormula () {
		this.operator = new Operator("true", 0); //
	}

// Substitution method
	public TrueFormula substituteConcreteValuation( Valuation substitution ) {
		return this.clone();
	}

// Clone method
	public TrueFormula clone() {
		return new TrueFormula();
	}

// String methods
	public String toKeYmaeraString () {
		return "true";
	}
	
	public String toManticoreString () {
		return "true";
	}

	public String toMathematicaString () {
		return "True";
	}

	public String todRealString () {
		return "true";
	}


// Administrative
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
	public FalseFormula negate() {
		return new FalseFormula();
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
