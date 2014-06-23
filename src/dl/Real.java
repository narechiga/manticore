package manticore.dl;

public class Real extends Term {

	public Real ( Operator operator ) {
		this.operator = operator;
		children = null;
	}

	public Real ( String value ) {
		operator = new Operator( value );
		children = null;
	}

	public String toKeYmaeraString() {
		return this.operator.toKeYmaeraString();
	}

}

