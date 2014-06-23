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

	public Term ( String operator, ArrayList<Term> subterms ) {
		this.operator = new Operator( operator );
		this.children = new ArrayList<dLStructure>();
		this.children.addAll( subterms );
	}
	
	// Following two methods really only used for the "arbitrary" term, as in x := *
	public Term ( Operator operator ) {
		this.operator = operator;
		this.children = null;
	}

	public Term ( String operator ) {
		this.operator = new Operator( operator );
		this.children = null;
	}

	public String toKeYmaeraString() {

		String returnString = "";

		if ( operator.infix == false ) {
			returnString = returnString + operator.toKeYmaeraString() + "(";
			if ( children != null ) {
				Iterator<dLStructure> childIterator = children.iterator();
				while ( childIterator.hasNext() ) {
					returnString = returnString + childIterator.next().toKeYmaeraString();
				}
			}
			returnString = returnString + " )";
		} else {
			returnString = returnString + "( ";
			if ( children != null ) {
				Iterator<dLStructure> childIterator = children.iterator();
				while ( childIterator.hasNext() ) {
					returnString = returnString + childIterator.next().toKeYmaeraString();

					if ( childIterator.hasNext() ) {
						returnString = returnString + operator.toKeYmaeraString();
					}
				}
			}
			returnString = returnString + " )";
		}

		return returnString;

	}

}
