package manticore.dl;

import java.util.*;

public class RealVariable extends Term {

	public RealVariable ( String name ) {
		operator = new Operator( name );
		children = null;
	}

	public boolean equals( Object otherObject ) {
		if ( otherObject instanceof RealVariable ) {
			return operator.equals( ((RealVariable)otherObject).operator );
		} else {
			return false;
		}
	}

	public int hashCode() {
		return operator.toString().hashCode();
	}

	public String toKeYmaeraString() {
		return operator.toKeYmaeraString();
	}

	public RealVariable clone() {
		return new RealVariable( this.operator.toString() );
		
	}


}

