package manticore.dl;

import java.util.*;

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

	public Real clone() {
		return new Real( new Operator( this.operator.toString() ));
	}

	public Double toDouble() throws Exception {
		if ( this.operator.toString().equals("*") ) {
			throw new Exception("Cannot convert arbitrary assignment to double");
		} else {
			return new Double( this.operator.toString() );
		}
	}

	public static Real add( Real a, Real b ) {
		Double aDouble = new Double( a.getOperator().toString() );
		Double bDouble = new Double( b.getOperator().toString() );

		Double result = aDouble + bDouble;
		return new Real( result.toString() );
	}

	public static Real subtract( Real a, Real b ) {
		Double aDouble = new Double( a.getOperator().toString() );
		Double bDouble = new Double( b.getOperator().toString() );

		Double result = aDouble - bDouble;
		return new Real( result.toString() );
	}

	public static Real multiply( Real a, Real b ) {
		Double aDouble = new Double( a.getOperator().toString() );
		Double bDouble = new Double( b.getOperator().toString() );

		Double result = aDouble * bDouble;
		return new Real( result.toString() );
	}

	public static Real divide( Real a, Real b ) {
		Double aDouble = new Double( a.getOperator().toString() );
		Double bDouble = new Double( b.getOperator().toString() );

		Double result = aDouble / bDouble;
		return new Real( result.toString() );
	}

	public static Real power( Real a, Real b ) {
		Double aDouble = new Double( a.getOperator().toString() );
		Double bDouble = new Double( b.getOperator().toString() );

		Double result = Math.pow(aDouble,bDouble);
		return new Real( result.toString() );
	}

	public static Real max( Real a, Real b ) {
		Double aDouble = new Double( a.getOperator().toString() );
		Double bDouble = new Double( b.getOperator().toString() );

		Double result = Math.min(aDouble,bDouble);
		return new Real( result.toString() );
	}

}

