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

	public Set<RealVariable> keySet() {
		return valuation.keySet();
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

	public String todRealString() {
		String returnString = "";

		Set<RealVariable> variables = valuation.keySet();
		Iterator<RealVariable> varIterator = variables.iterator();
		
		RealVariable thisVariable;
		while ( varIterator.hasNext() ) {
			thisVariable = varIterator.next();
			returnString = returnString + "(assert (= " + thisVariable 
					+ " " + get( thisVariable )
					+ "))\n";
		}
		return returnString;
	}

	public String toMathematicaString() {
		String returnString = "{";

		Set<RealVariable> variables = valuation.keySet();
		Iterator<RealVariable> varIterator = variables.iterator();
		
		RealVariable thisVariable;
		while ( varIterator.hasNext() ) {
			thisVariable = varIterator.next();

			if ( varIterator.hasNext() ) {
				returnString = returnString + thisVariable 
					+ "->" +  get( thisVariable )
					+ ", ";
			} else {
				returnString = returnString + thisVariable 
					+ "->" +  get( thisVariable )
					+ " }";
			}


		}
		return returnString;
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
