package manticore.dl.syntax;

import java.util.*;

public class Real extends Term {

// Constructors
	//public Real ( Operator operator ) {
	//	this.operator = operator;
	//	children = null;
	//}

	public Real ( String value ) {
		operator = new Operator( value, 0 );
		children = null;
	}

	public Real( Double value ) {
		operator = new Operator( value.toString(), 0 );
		children = null;
	}
	
	public Real( Float value ) {
		operator = new Operator( value.toString(), 0 );
		children = null;
	}

	public Real( Integer value ) {
		operator = new Operator( value.toString(), 0 );
		children = null;
	}

// Substitution method
	public Real substituteConcreteValuation( Valuation substitution ) {
		return this.clone();
	}

// Equality
	public boolean equals( Object otherObject ) {
		if ( otherObject instanceof Real ) {
			return operator.equals( ((Real)otherObject).operator );
		} else {
			return false;
		}
	}

// hashCode
	public int hashCode() {
		return operator.toString().hashCode();
	}

// String methods
	public String toKeYmaeraString() {
		return this.operator.toKeYmaeraString();
	}

	public String toManticoreString() {
		return this.operator.toManticoreString();
	}

	public String toMathematicaString() {
		return this.operator.toMathematicaString();
	}

	public String todRealString() {
		return this.operator.todRealString();
	}

// Clone
	public Real clone() {
		return new Real(  this.operator.toString() );
	}

// toDouble
	public Double toDouble() throws Exception {
		if ( this.operator.toString().equals("*") ) {
			throw new Exception("Cannot convert arbitrary assignment to double");
		} else {
			return new Double( this.operator.toString() );
		}
	}

// Basic arithmetic
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
// Logic
	public Set<RealVariable> getFreeVariables() {
		return new HashSet<RealVariable>();
	}

}

