package manticore.dl.syntax;

import java.util.*;
import manticore.dl.semantics.*;

public class VectorTerm extends MatricialTerm {
	int length;

	ArrayList<Term> vector;
	 
	public VectorTerm( int length ) {
		this.length = length;
	 	vector = new ArrayList<Term>( length );
	}

	public VectorTerm( ArrayList<Term> list ) {
	 	vector = list;
	 	this.length = list.size();
	}

	public Term getElement( int index ) {
		// Indexed from 1, matlab style. Sorry
		if ( 1 <= index && index <= length ) {
	 		return vector.get( index - 1 );
		} else {
			return null;
		}
	}

	public void setElement( int index, Term term ) {
		// Indexed from 1, matlab style. Sorry
		if ( 1 <= index && index <= length ) {
	 		vector.set( index - 1, term );
		}
	}

// clone
	public VectorTerm clone() {
		ArrayList<Term> vectorClone;
	}

	public Set<RealVariable> getFreeVariables() {
		return null;
	}

	public Set<RealVariable> getDynamicVariables() {
		return null;
	}

}


