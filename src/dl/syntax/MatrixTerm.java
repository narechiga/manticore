package manticore.dl.syntax;

public class MatrixTerm extends MatricialTerm {

	// In order to preserve the low-level interface to a "children" list
	// of the arguments, a matrix is a "list" of column vectors

	int numRows;
	int numColumns;

	public MatrixTerm ( int rows, int columns ) {
		numColumns = columns;
		numRows = rows;

		children = new ArrayList<VectorTerm>( columns );	

		Iterator<VectorTerm> columnIterator = children.iterator();
		while ( columnIterator.hasNext() ) {
			columnIterator.next() = new VectorTerm( rows );
		}
	}

	public MatrixTerm ( ArrayList<VectorTerm> matrix  ) {
		children = matrix;
	}

	public Term getElement( int row, int column ) {
		return getColumn( column ).getElement( row );
	}

	public void setElement( int row, int column, Term element ) {
		VectorTerm thisColumn = getColumn( column );

		thisColumn.setElement( row, element );
	}

	public VectorTerm getColumn( int column ) {
		return children.get( column );
	}

}
