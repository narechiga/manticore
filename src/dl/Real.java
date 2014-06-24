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

	public boolean equals( Object otherObject ) {
		if ( otherObject instanceof Real ) {
			return operator.equals( ((Real)otherObject).operator );
		} else {
			return false;
		}
	}

	public int hashCode() {
		return operator.toString().hashCode();
	}

	public String toKeYmaeraString() {
		return this.operator.toKeYmaeraString();
	}

}

