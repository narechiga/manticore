package manticore.dl;

import java.util.*;

public class UpdateRule {

	HashMap<RealVariable,dLFormula> updateRule;
	ArrayList<dLFormula> additionalConstraints;

	public UpdateRule () {
		this.updateRule = new HashMap<RealVariable,dLFormula>();
	}

	public UpdateRule ( HashMap<RealVariable,dLFormula> updateRule ) {
		this.updateRule = updateRule;
	}

	public UpdateRule( Valuation thisValuation ) {
		this.updateRule = new HashMap<RealVariable,dLFormula>();

		Iterator<RealVariable> variableIterator = thisValuation.getVariables().iterator();
		RealVariable thisVariable;
		while ( variableIterator.hasNext() ) {
			thisVariable = variableIterator.next();
			ComparisonFormula thisEquality = new ComparisonFormula(
								new Operator("="),
								thisVariable,
								thisValuation.get( thisVariable ) );

			this.putRule( thisVariable, thisEquality );
		}
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

	public void putRule( RealVariable var, dLFormula binding ) {
		this.updateRule.put( var, binding );
	}

	public dLFormula getRule( RealVariable var ) {
		return this.updateRule.get( var );
	}

	public void addConstraint( dLFormula constraint ) {
		additionalConstraints.add( constraint );
	}
	
	public dLFormula getConstraint( int index ) {
		return additionalConstraints.get( index );
	}

	public Iterator<dLFormula> constraintIterator() {
		return additionalConstraints.iterator();
	}

	public int size() {
		return this.updateRule.size() + this.additionalConstraints.size();
	}

	public boolean isEmpty() {
		return (this.updateRule.isEmpty() && this.additionalConstraints.isEmpty());
	}

	public boolean modifiesVariable( RealVariable var ) {
		return this.updateRule.containsKey( var );
	}

	//public String toString() {
	//	return updateRule.toString();
	//}

	public String todRealString() {
		String returnString = "";

		Set<RealVariable> variables = updateRule.keySet();
		Iterator<RealVariable> varIterator = variables.iterator();
		
		RealVariable thisVariable;
		while ( varIterator.hasNext() ) {
			thisVariable = varIterator.next();
			returnString = returnString + "(assert (= " + thisVariable 
					+ " " + getRule( thisVariable )
					+ "))\n";
		}
		return returnString;
	}

	public String toString() {
		String returnString = "{{ ";

		Set<RealVariable> variables = updateRule.keySet();
		Iterator<RealVariable> varIterator = variables.iterator();
		
		RealVariable thisVariable;
		while ( varIterator.hasNext() ) {
			thisVariable = varIterator.next();

			if ( varIterator.hasNext() ) {
				returnString = returnString + thisVariable 
					+ " :=>> " +  getRule( thisVariable ).toMathematicaString()
					+ ", ";
			} else {
				returnString = returnString + thisVariable 
					+ " :=>> " +  getRule( thisVariable ).toMathematicaString();
			}


		}
		returnString = returnString + " }}";

		returnString = returnString + " restricted to:\n";

		Iterator<dLFormula> constraintIterator = additionalConstraints.iterator();
		while ( constraintIterator.hasNext() ) {
			returnString = returnString +  constraintIterator.next().toMathematicaString() + "; ";
		}
		return returnString;
	}

	public UpdateRule clone() {
		UpdateRule newUpdateRule = new UpdateRule();

		Set<RealVariable> keySet = updateRule.keySet();
		Iterator<RealVariable> keyIterator = keySet.iterator();

		RealVariable thisKey;
		while( keyIterator.hasNext() ) {
			thisKey = keyIterator.next();
			newUpdateRule.putRule( thisKey.clone(), (updateRule.get( thisKey )).clone() );
		}

		return newUpdateRule;
	}

}
