package manticore.dl;


public class TrueFormula extends dLFormula {

	public TrueFormula () {
		this.operator = new Operator("true"); //
	}

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


}
