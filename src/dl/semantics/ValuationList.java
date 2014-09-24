package manticore.dl.semantics;

import manticore.dl.syntax.*;

import java.util.*;

public class ValuationList {

	ArrayList<Valuation> valuationList;

	public ValuationList () {
		valuationList = new ArrayList<Valuation>();
	}

	public ValuationList ( ArrayList<Valuation> valuationList ) {
		this.valuationList = valuationList;
	}

	public void add( Valuation valuation  ) {
		this.valuationList.add( valuation );
	}

	public void addAll( ValuationList valuation  ) {
		this.valuationList.addAll( valuation.valuationList );
	}

	public Valuation get( int whichValuation ) {
		return this.valuationList.get( whichValuation );
	}

	public Iterator<Valuation> iterator () {
		return valuationList.iterator();
	}

	public int size() {
		return valuationList.size();
	}

	public String toString() {
		return valuationList.toString();
	}

	public ValuationList clone() {
		ValuationList newValuationList = new ValuationList();

		Iterator<Valuation> valIterator = valuationList.iterator();

		Valuation thisVal;
		while( valIterator.hasNext() ) {
			thisVal = valIterator.next();
			newValuationList.add( thisVal.clone() );
		}

		return newValuationList;
	}

}
