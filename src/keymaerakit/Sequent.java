package manticore.keymaerakit;

import proteus.dl.syntax.*;
import java.util.*;

public class Sequent {

	protected List<dLFormula> antecedent;
	protected List<dLFormula> succedent;

	public Sequent( List<dLFormula> antecedent, List<dLFormula> succedent ) {
		this.antecedent = antecedent;
		this.succedent = succedent;
	}

	public List<dLFormula> getAntecedent() {
		return this.antecedent;
	}

	public List<dLFormula> getSuccedent() {
		return this.succedent;
	}

	//public ArrayList<int> skolemHideAround( int formulaIndex ) {
	//	ArrayList<int> removalIndices;
	//	dLFormula focusFormula = getFormula( formulaIndex );
	//	ArrayList<RealVariable> taintedVariables = focusFormula.getDynamicVariables();

	//	dLFormula thisFormula;
	//	Iterator<dLFormula> formulaIterator = antecedent.iterator();
	//	while ( formulaIterator.hasNext() ) {
	//		thisFormula = formulaIterator.next();
	//		if ( thisFormula != focusFormula
	//			&& thisFormula.getFreeVariables().removeAll( taintedVariables ) ) { // true if it changes
	//			removalIndices.add( antecedent.indexOf(thisFormula) + 1);
	//			formulaIterator.remove();
	//		}
	//	}
	//	Iterator<dLFormula> formulaIterator = succedent.iterator();
	//	while ( formulaIterator.hasNext() ) {
	//		thisFormula = formulaIterator.next();
	//		if ( thisFormula != focusFormula
	//			&& thisFormula.getFreeVariables().removeAll( taintedVariables ) ) { // true if it changes
	//			removalIndices.add( antecedent.indexOf(thisFormula) + 1 + antecedent.size() );
	//			formulaIterator.remove();
	//		}
	//	}

	//	return removalIndices;
	//}

	
	public Sequent clone() {
		List antecedentClone = new ArrayList<dLFormula>();
		List succedentClone = new ArrayList<dLFormula>();

		Iterator<dLFormula> antecedentIterator = antecedent.iterator();
		while( antecedentIterator.hasNext() ) {
			antecedentClone.add( antecedentIterator.next().clone() );
		}

		Iterator<dLFormula> succedentIterator = succedent.iterator();
		while( succedentIterator.hasNext() ) {
			succedentClone.add( succedentIterator.next().clone() );
		}

		return new Sequent( antecedentClone, succedentClone );
	}

	public void addToAntecedent( dLFormula newAntecedentFormula ) {
		// Add it to the beginning. Sorry, because KeYmaera does it this silly way
		ArrayList<dLFormula> newAntecedent = new ArrayList<dLFormula>();
		newAntecedent.add( newAntecedentFormula );
		newAntecedent.addAll( this.antecedent );

		this.antecedent = newAntecedent;
	}

	public void addToSuccedent( dLFormula newSuccedentFormula ) {
		// Add it to the beginning. Sorry, because KeYmaera does it this silly way
		ArrayList<dLFormula> newSuccedent = new ArrayList<dLFormula>();
		newSuccedent.add( newSuccedentFormula );
		newSuccedent.addAll( this.succedent );

		this.succedent = newSuccedent;
	}

	public void removeAntecedentFormula( int formulaNumber ) {
		this.antecedent.remove( formulaNumber );
	}

	public void removeSuccedentFormula( int formulaNumber ) {
		this.succedent.remove( formulaNumber );
	}

	public void replaceAntecedentFormula( int formulaNumber, dLFormula newFormula ) {
		this.antecedent.set( formulaNumber, newFormula );
	}

	public void replaceSuccedentFormula( int formulaNumber, dLFormula newFormula ) {
		this.succedent.set( formulaNumber, newFormula );
	}

	public dLFormula getAntecedentFormula( int formulaNumber ) {
		return antecedent.get( formulaNumber - 1); // because KeYmaera indices start at 1 :)
	}

	public dLFormula getSuccedentFormula( int formulaNumber ) {
		return succedent.get( formulaNumber - 1 ); // because KeYmaera indices start at 1 :)
	}

	public dLFormula getFormula( int formulaNumber ) {
		if ( formulaNumber <= antecedent.size() ) {
			return getAntecedentFormula( formulaNumber );
		} else {
			return getSuccedentFormula( formulaNumber - antecedent.size() );
		}
	}

	public void removeFormula ( int formulaNumber ) {
		if ( formulaNumber <= antecedent.size() ) {
			removeAntecedentFormula( formulaNumber );
		} else {
			removeSuccedentFormula( formulaNumber - antecedent.size() );
		}
	}

	public void replaceFormula ( int formulaNumber, dLFormula newFormula ) {
		if ( formulaNumber <= antecedent.size() ) {
			replaceAntecedentFormula( formulaNumber, newFormula );
		} else {
			replaceSuccedentFormula( formulaNumber - antecedent.size(), newFormula );
		}
	}

}

