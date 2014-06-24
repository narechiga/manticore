package manticore.dl;

public class dLFormula extends dLStructure {

	public boolean isFirstOrder() {
		return false;
	}

	public boolean isModal() {
		return false;
	}

	public boolean isPropositionalPrimitive() {
		return false;
	}

	public Operator getOperator() {
		return this.operator;
	}

}
