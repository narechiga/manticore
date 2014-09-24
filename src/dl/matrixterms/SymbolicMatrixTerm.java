package manticore.dl;

public class SymbolicMatrix extends MatrixTerm {

	// In order to preserve the low-level interface to a "children" list
	// of the arguments, a matrix is a "list" of column vectors

	int numRows;
	int numColumns;

	public SymbolicMatrix ( int rows, int columns ) {
		numColumns = columns;
		numRows = rows;

		children = new ArrayList<SymbolicVector>( columns );	

		Iterator<SymbolicVector> columnIterator = children.iterator();
		while ( columnIterator.hasNext() ) {
			columnIterator.next() = new SymbolicVector( rows );
		}
	}

	public SymbolicMatrix ( ArrayList<SymbolicVector> matrix  ) {
		children = matrix;
	}

	public Term getElement( int row, int column ) {
		return getColumn( column ).getElement( row );
	}

	public void setElement( int row, int column, Term element ) {
		SymbolicVector thisColumn = getColumn( column );

		thisColumn.setElement( row, element );
	}

	public SymbolicVector getColumn( int column ) {
		return children.get( column );
	}

}
