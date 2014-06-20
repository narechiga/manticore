package manticore.dl;

public class Operator {

	public String operator;
	//public String infixORprefix;
	public int arity;
	public boolean infix;


	public Operator () { }

	public Operator ( String operator ) {
		this.operator = operator;
		this.arity = 2; //default
		this.infix = false; //default is prefix
	}

	public Operator ( String operator, boolean infix ) {
		this.operator = operator;
		this.arity = 2;
		this.infix = infix;
	}

	public Operator ( String operator, int arity ) {
		this.operator = operator;
		this.arity = arity;
		this.infix = false;
	}

	public Operator ( String operator, int arity, boolean infix ) {
		this.operator = operator;
		this.arity = arity;
		this.infix = infix;
	}

	public String toString() {
		return operator;
	}

}
