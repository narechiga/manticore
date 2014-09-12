package manticore.dl;

import java.util.*;

public class UpdateRule {

	HashMap<RealVariable,dLFormula> updateRule;

	public UpdateRule () {
		this.updateRule = new HashMap<RealVariable,dLFormula>();
	}

	public UpdateRule ( HashMap<RealVariable,dLFormula> updateRule ) {
		this.updateRule = updateRule;
	}

	public Set<RealVariable> keySet() {
		return updateRule.keySet();
	}

	public Set<RealVariable> getVariables() {
		return updateRule.keySet();
	}

	public Collection<dLFormula> getRules() {
		return updateRule.values();
	}

	public void put( RealVariable var, dLFormula binding ) {
		this.updateRule.put( var, binding );
	}

	public dLFormula get( RealVariable var ) {
		return this.updateRule.get( var );
	}

	public int size() {
		return this.updateRule.size();
	}

	public boolean isEmpty() {
		return this.updateRule.isEmpty();
	}

	public boolean containsVariable( RealVariable var ) {
		return this.updateRule.containsKey( var );
	}

	public String toString() {
		return updateRule.toString();
	}

	public String todRealString() {
		String returnString = "";

		Set<RealVariable> variables = updateRule.keySet();
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
		String returnString = "{{ ";

		Set<RealVariable> variables = updateRule.keySet();
		Iterator<RealVariable> varIterator = variables.iterator();
		
		RealVariable thisVariable;
		while ( varIterator.hasNext() ) {
			thisVariable = varIterator.next();

			if ( varIterator.hasNext() ) {
				returnString = returnString + thisVariable 
					+ " :=>> " +  get( thisVariable )
					+ ", ";
			} else {
				returnString = returnString + thisVariable 
					+ " :=>> " +  get( thisVariable );
			}


		}
		returnString = returnString + " }}";
		return returnString;
	}

	public UpdateRule clone() {
		UpdateRule newUpdateRule = new UpdateRule();

		Set<RealVariable> keySet = updateRule.keySet();
		Iterator<RealVariable> keyIterator = keySet.iterator();

		RealVariable thisKey;
		while( keyIterator.hasNext() ) {
			thisKey = keyIterator.next();
			newUpdateRule.put( thisKey.clone(), (updateRule.get( thisKey )).clone() );
		}

		return newUpdateRule;
	}

}
