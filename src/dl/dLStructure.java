package manticore.dl;

import java.util.*;

public class dLStructure {

	public Operator operator;
	public List<dLStructure> children;

	public String declaredFunctions;
	public String declaredSchemaVariables;
	public String declaredRules;
	public List<String> declaredProgramVariables;
	public List<String> variableInitializations;


	public dLStructure() {
		operator = null;
		children = null;
	}

	public dLStructure( String operator ) {
		this.operator = new Operator( operator );
		children = null;
	}

	public dLStructure( Operator operator, List<dLStructure> children ) {
		this.operator = operator;
		this.children = children;
	}

	public dLStructure( String operator, List<dLStructure> children ) {
		this.operator = new Operator( operator );
		this.children = children;

	}


	public ArrayList<RealVariable> getVariables () {
		ArrayList<RealVariable> myVariables = new ArrayList<RealVariable>();

		if ( this instanceof RealVariable ) {
			myVariables.add( (RealVariable)this );
		} else if ( children != null ) {
			Iterator<dLStructure> childIterator = children.iterator();

			while ( childIterator.hasNext() ) {
				myVariables.addAll( childIterator.next().getVariables() );
			}

		}

		return myVariables;
	}

	public ArrayList<ContinuousProgram> extractContinuousBlocks() {

		ArrayList<ContinuousProgram> continuousBlocks = new ArrayList<ContinuousProgram>();

		if ( getClass().equals( ContinuousProgram.class ) ) {
			continuousBlocks.add( (ContinuousProgram)this );
		} else if ( children != null ) {

			Iterator<dLStructure> childrenIterator = children.iterator();

			dLStructure thisChild;
			while ( childrenIterator.hasNext() ) {
				thisChild = childrenIterator.next();

				//if ( thisChild instanceof ContinuousProgram ) {
				//	continuousBlocks.add( (ContinuousProgram)thisChild );
				//} else 
				if ( thisChild.children != null ) { 
					continuousBlocks.addAll( thisChild.extractContinuousBlocks() );
				}
			}
		}

		return continuousBlocks;

	}

	// String operations
	//public String toString() {
	//	return toInfix();
	//}

	public String toString() {
		if ( (operator != null) && (children != null) ) {
			return "(" + operator.toString() + " " + children.toString() + " )";
		} else if ( (operator != null) && (children == null) ) {
			//return "(ground term: " + operator.toString() + " )";
			return operator.toString();
		} else {
			return "(uninitialized)";
		}
	}

	public String toKeYmaeraString() {
		return null;
	}




	//public String toInfix() {
	//	
	//	if ( (operator != null) && (children != null) && (children.size() == 2) ) {
	//		return "("+ (children.get(0)).toInfix() + operator.toString() + (children.get(1)).toInfix() + " )";
	//	} else {
	//		return toPrefix();
	//	}
	//}


}
