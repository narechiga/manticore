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

// Substitution method
	public Term substituteConcreteValuation( Valuation substitution ) {
		ArrayList<Term> subtermsSubstituted = new ArrayList<Term>();
		Iterator<Term> subtermIterator = getSubTerms().iterator();
		while ( subtermIterator.hasNext() ) {
			subtermsSubstituted.add( subtermIterator.next().substituteConcreteValuation( substitution ) );
		}
		
		return new Term( getOperator().clone(), subtermsSubstituted );
	}

// Clone method
	public Term clone() {
		ArrayList<Term> subtermClones = new ArrayList<Term>();
		Iterator<Term> subTermIterator = getSubTerms().iterator();
		while ( subTermIterator.hasNext() ) {
			subtermClones.add( subTermIterator.next().clone() );
		}

		return new Term( getOperator().clone(), subtermClones );
	}

	public ArrayList<Term> getSubTerms() {
		ArrayList<Term> subTerms = new ArrayList<Term>();
		Iterator<dLStructure> subTermIterator = children.iterator();

		while ( subTermIterator.hasNext() ) {
			// This can be done safely because all the children of a term will be terms,
			// see constructor
			subTerms.add( (Term)subTermIterator.next() );
		}

		return subTerms;
	}


	public String toKeYmaeraString() {

		String returnString = "";

		if ( operator.infix == false ) {
			returnString = returnString + operator.toKeYmaeraString() + "(";
			if ( children != null ) {
				Iterator<dLStructure> childIterator = children.iterator();
				while ( childIterator.hasNext() ) {
					returnString = returnString + " " + childIterator.next().toKeYmaeraString();
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
						returnString = returnString + " " + operator.toKeYmaeraString() + " ";
					}
				}
			}
			returnString = returnString + " )";
		}

		return returnString;
	}

	public String toMathematicaString() {
		return toKeYmaeraString();
	}

	public String toManticoreString() {
		return toKeYmaeraString();
	}

	public String todRealString() {
		String returnString = "(" + this.operator.todRealString() ;

		Iterator<Term> subTermIterator = getSubTerms().iterator();
		while ( subTermIterator.hasNext() ) {
			returnString = returnString + " " + subTermIterator.next().todRealString();
		}
		returnString = returnString + " )";

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
