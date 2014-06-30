package manticore.dl;

import java.util.*;

public class Valuation {

	HashMap<RealVariable,Real> valuation;

	public Valuation () {
		this.valuation = new HashMap<RealVariable,Real>();
	}

	public Valuation ( HashMap<RealVariable,Real> valuation ) {
		this.valuation = valuation;
	}

	public void put( RealVariable var, Real num ) {
		this.valuation.put( var, num );
	}

	public Real get( RealVariable var ) {
		return this.valuation.get( var );
	}

	public String toString() {
		return valuation.toString();
	}

	public Valuation clone() {
		Valuation newValuation = new Valuation();

		Set<RealVariable> keySet = valuation.keySet();
		Iterator<RealVariable> keyIterator = keySet.iterator();

		RealVariable thisKey;
		while( keyIterator.hasNext() ) {
			thisKey = keyIterator.next();
			newValuation.put( thisKey.clone(), (valuation.get( thisKey )).clone() );
		}

		return newValuation;
	}

}
