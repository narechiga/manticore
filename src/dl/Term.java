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

	public Operator getOperator() {
		return this.operator;
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

	//public boolean equals( Object otherObject ) { // This is too restrictive, because does not allow for commutativity

	//	if ( !(otherObject instanceof Term ) ) {
	//		return false;
	//	}

	//	Term otherTerm = (Term)otherObject;
	//	if ( !(this.operator.equals( otherTerm.operator ) ) ) {
	//		return false;
	//	}

	//	Iterator<dLStructure> myChildIterator = this.children.iterator();
	//	Iterator<dLStructure> otherChildIterator = otherTerm.children.iterator();

	//	boolean result = true; //will be anded with the condition that each child must be equal, then returned
	//	Term myChild; Term otherChild;
	//	// Look at the constructor, and note that all the children of terms are terms.
	//	// Then all the typecasts below should succeed.
	//	while ( myChildIterator.hasNext() ) {

	//		if ( otherChildIterator.hasNext() ) {

	//			myChild = (Term)(myChildIterator.next());
	//			otherChild = (Term)(otherChildIterator.next());
	//			
	//			result = result && myChild.equals( otherChild );

	//		} else {
	//			return false; // because then they are not the same size, cannot be equal!
	//		}
	//	}
	//}


	// Following two methods really only used for the "arbitrary" term, as in x := *
	public Term ( Operator operator ) {
		this.operator = operator;
		this.children = null;
	}

	public Term ( String operator ) {
		this.operator = new Operator( operator );
		this.children = null;
	}

}
