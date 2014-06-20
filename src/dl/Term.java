package manticore.dl;

import java.util.*;

public class Term extends dLStructure {

	public Term () {
	}

	public Term ( Operator operator, ArrayList<Term> subterms ) {
		this.operator = operator;
		this.children = new ArrayList<dLStructure>();
		this.children.addAll( subterms );
	}

	public String toString() {

		String returnString = "TERM: ";

		if ( operator.infix == false ) {
			returnString = returnString + operator.toString() + "(";
			if ( children != null ) {
				Iterator<dLStructure> childIterator = children.iterator();
				while ( childIterator.hasNext() ) {
					returnString = returnString + childIterator.next().toString();
				}
			}
			returnString = returnString + ") ";
		} else {
			returnString = returnString + "( ";
			if ( children != null ) {
				Iterator<dLStructure> childIterator = children.iterator();
				while ( childIterator.hasNext() ) {
					returnString = returnString + childIterator.next().toString();

					if ( childIterator.hasNext() ) {
						returnString = returnString + operator.toString();
					}
				}
			}
			returnString = returnString + ") ";
		}

		return returnString;

	}

}
