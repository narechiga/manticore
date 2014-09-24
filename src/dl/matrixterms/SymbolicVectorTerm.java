package manticore.dl;

public class SymbolicVector extends MatrixTerm {
	int length;
	 
	public SymbolicVector( int length ) {
		this.length = length;
	 	children = new ArrayList<Term>( length );
	}

	public SymbolicVector( ArrayList<Term> list ) {
	 	children = list;
	 	this.length = list.length();
	}

	public Term getElement( int index ) {
		// Indexed from 1, matlab style. Sorry
		if ( 1 <= index && index <= length ) {
	 		return children.get( index - 1 );
		} else {
			return null;
		}
	}

	public void setElement( int index, Term term ) {
		// Indexed from 1, matlab style. Sorry
		if ( 1 <= index && index <= length ) {
	 		children.set( index - 1, term );
		}
	}

// clone
	public SymbolicVector clone() {
		ArrayList<Term> childrenClone;
	}

}


