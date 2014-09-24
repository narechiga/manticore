package manticore.dl.syntax;

import java.util.*;

public class NormTerm extends Term {

	protected int degree;

	public NormTerm ( ArrayList<Term> terms ) { // default is degree 2
		this.degree = 2;
		this.operator = new Operator(degree + "-norm", terms.size(), false );

		spawnChildren();
		children.addAll( terms );
	}

	public NormTerm ( ArrayList<Term> terms, int degree ) {
		this.degree = degree;
		this.operator = new Operator(degree + "-norm", terms.size(), false );

		spawnChildren();
		children.addAll( terms );
	}
	
	public int getDegree() {
		return  this.degree;
	}

	public ArrayList<Term> getTerms() {
		ArrayList<Term> terms = new ArrayList<Term>();
		Iterator<dLStructure> termIterator = children.iterator();
		while ( termIterator.hasNext() ) {
			terms.add( (Term)(termIterator.next()) );
		}

		return terms;
	}
	
	public Term getTerm( int index ) {
		return (Term)getChild( index );
	}


// Clone
	public NormTerm clone () {
		ArrayList<Term> cloneTerms = new ArrayList<Term>();
		Iterator<dLStructure> termIterator = children.iterator();

		while ( termIterator.hasNext() ) {
			cloneTerms.add( ((Term)termIterator.next()).clone() );
		}

		return new NormTerm( cloneTerms, degree );
	}

// Parse a norm term!
	public static NormTerm parseNormTerm( Term thisTerm, int degree ) throws NormTermFormatException {
		ArrayList<Term> subTerms = new ArrayList<Term>();

		if ( thisTerm instanceof AdditionTerm ) {

			Term left = ((AdditionTerm)thisTerm).getLeftSummand();
			Term right = ((AdditionTerm)thisTerm).getRightSummand();

			if ( (degree > 1) 
				&& ( left instanceof PowerTerm)
				&& ( right instanceof PowerTerm)
				&& ( ((PowerTerm)left).getExponent().equals( new Real( degree )))
				&& ( ((PowerTerm)right).getExponent().equals( new Real( degree))) ) {

				subTerms.add( left );
				subTerms.add( right );

//			} else if ( (degree == 1) // Handle the 1-norm
//				&& ( left.getOperator().equals( new Operator("abs") ) )
//				&& ( right.getOperator().equals( new Operator("abs") ) )
//				) {
//
//				subTerms.add( left );
//				subTerms.add( right );
//				return new NormTerm( subTerms, degree );
//
			} else {
				throw new NormTermFormatException( thisTerm.toMathematicaString() );
			}

		}

		return new NormTerm( subTerms, degree );
	}



}

