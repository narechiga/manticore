package manticore.dl.syntax;

import manticore.dl.semantics.*;
import java.util.*;

public class MatrixTerm extends MatricialTerm {

	// In order to preserve the low-level interface to a "matrix" list
	// of the arguments, a matrix is a "list" of column vectors

	int numRows;
	int numColumns;

	ArrayList<VectorTerm> matrix;

	public MatrixTerm ( int rows, int columns ) {
		numColumns = columns;
		numRows = rows;

		matrix = new ArrayList<VectorTerm>( columns );	

		Iterator<VectorTerm> columnIterator = matrix.iterator();
		while ( columnIterator.hasNext() ) {
			columnIterator.next() = new VectorTerm( rows );
		}
	}

	public MatrixTerm ( ArrayList<VectorTerm> matrix  ) {
		matrix = matrix;
	}

	public Term getElement( int row, int column ) {
		return getColumn( column ).getElement( row );
	}

	public void setElement( int row, int column, Term element ) {
		VectorTerm thisColumn = getColumn( column );

		thisColumn.setElement( row, element );
	}

	public VectorTerm getColumn( int column ) {
		return matrix.get( column );
	}

	public Set<RealVariable> getFreeVariables() {
		return null;
	}

	public Set<RealVariable> getDynamicVariables() {
		return null;
	}

}
