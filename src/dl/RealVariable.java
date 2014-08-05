package manticore.dl;

import java.util.*;

public class RealVariable extends Term {

// Constructor
	public RealVariable ( String name ) {
		operator = new Operator( name, 0 );
		children = null;
	}

// equals
	public boolean equals( Object otherObject ) {
		if ( otherObject instanceof RealVariable ) {
			return operator.equals( ((RealVariable)otherObject).operator );
		} else {
			return false;
		}
	}

	public Term substituteConcreteValuation( Valuation substitution ) {
		if ( substitution == null ) {
			return this;
		} else if ( substitution.get( this ) != null ) {
			return substitution.get( this );
		} else {
			return this.clone();
		}
	}

// hashCode
	public int hashCode() {
		return operator.toString().hashCode();
	}

// String methods
	public String toKeYmaeraString() {
		return operator.toKeYmaeraString();
	}

	public String toManticoreString() {
		return operator.toManticoreString();
	}

	public String toMathematicaString() {
		return operator.toMathematicaString();
	}

	public String todRealString() {
		return operator.todRealString();
	}

// Clone
	public RealVariable clone() {
		return new RealVariable( this.operator.toString() );
		
	}


}

