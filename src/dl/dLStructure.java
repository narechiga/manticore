package manticore.dl;

import java.util.*;

public class dLStructure {

	public Operator operator;
	public List<dLStructure> children;

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


	public Set<RealVariable> getVariables () {
		Set<RealVariable> myVariables = new HashSet<RealVariable>();

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

				if ( thisChild.children != null ) { 
					continuousBlocks.addAll( thisChild.extractContinuousBlocks() );
				}
			}
		}

		return continuousBlocks;

	}

	public HybridProgram extractFirstHybridProgram() {
		HybridProgram myProgram = null;

		if ( this instanceof HybridProgram ) {
			return (HybridProgram)this;
		} else if ( children!= null ) {

			Iterator<dLStructure> childrenIterator = children.iterator();
			dLStructure thisChild;
			while ( childrenIterator.hasNext() ) {
				thisChild = childrenIterator.next();

				if ( thisChild.extractFirstHybridProgram() != null ) {
					myProgram = thisChild.extractFirstHybridProgram();
				}
			}
		}

		return myProgram;
	}


	public String toString() {
		if ( (operator != null) && (children != null) ) {
			return "(" + operator.toString() + " " + children.toString() + " )";
		} else if ( (operator != null) && (children == null) ) {
			return operator.toString();
		} else {
			return "(uninitialized structure)";
		}
	}

	public String toKeYmaeraString() {
		return null;
	}


}
