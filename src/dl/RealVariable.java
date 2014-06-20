package manticore.dl;

import java.util.*;

public class RealVariable extends Term {

	public RealVariable ( String name ) {
		operator = new Operator( name );
		children = null;
	}

	public String toString () {
		return operator.toString();
	}

}

