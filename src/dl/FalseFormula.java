package manticore.dl;


public class FalseFormula extends dLFormula {

	public FalseFormula () {
		this.operator = new Operator("false"); //
	}

	public String toKeYmaeraString () {
		return "false";
	}

	public String toMathematicaString () {
		return "False";
	}

	public boolean isFirstOrder() {
		return true;
	}

	public boolean isPropositionalPrimitive() {
		return true;
	}


}
