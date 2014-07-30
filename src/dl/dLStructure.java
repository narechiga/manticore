package manticore.dl;

import java.util.*;
import java.io.*;

public class dLStructure {

	protected Operator operator;
	protected ArrayList<dLStructure> children;

// Constructors and assorted getters and setters
	public dLStructure() {
		operator = null;
		children = null;
	}

	public dLStructure( String operator ) {
		this.operator = new Operator( operator );
		children = null;
	}

	public dLStructure( Operator operator ) {
		this.operator = operator;
		children = null;
	}

	public dLStructure( Operator operator, ArrayList<dLStructure> children ) {
		this.operator = operator;
		this.children = children;
	}

	public dLStructure( String operator, ArrayList<dLStructure> children ) {
		this.operator = new Operator( operator );
		this.children = children;
	}

	public Operator getOperator() {
		return this.operator;
	}

	public dLStructure getChild( int index ) {
		if ( children != null ) {
			return children.get( index );
		} else {
			return null;
		}
	}

	public boolean setChild( int index, dLStructure newChild ) {
		if ( children != null ) {
			children.set( index, newChild );
			return true;
		} else {
			return false;
		}
	}

	public boolean addChild( dLStructure newChild ) {
		if ( children != null ) {
			children.add( newChild );
			return true;
		} else {
			return false;
		}
	}

// Parse a dLStructure from a string
	public static dLStructure parseStructure( String structureString ) throws Exception {
		// returns the dLStructure that exists in the string
		StringReader thisReader = new StringReader( structureString );
		Lexer thisLexer = new Lexer( thisReader );
		YYParser thisParser = new YYParser( thisLexer );

		thisParser.parse();

		return thisParser.parsedStructure;
	}

// Return a dLStructure that is the same as this one, but with syntactic substitution
// of real variables according to the valuation given as an argument
	public dLStructure substitute( Valuation substitutions ) {
		if ( this instanceof Real ) {
			return ((Real)this).clone();
		} else if ( this instanceof RealVariable ) {
			return substitutions.get( (RealVariable)this ).clone();
		} else {

			dLStructure newStructure = new dLStructure( this.getOperator() );
			Iterator<dLStructure> childIterator = children.iterator();
			while( childIterator.hasNext() ) {
				newStructure.addChild(
					childIterator.next().substitute( substitutions )
					);
			}

			return newStructure;
		}
	}


// Extract assorted bits and pieces
// 1. getVariables
// 2. extractContinuousBlocks
// 3. extractFirstHybridProgram
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


// Export toString methods
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

	public String toMathematicaString() {
		return null;
	}

	public String toManticoreString() {
		System.out.println("Failed to find an appropriate Manticore string method for: " +this.toString());
		return null;
	}

	public String todRealString() {
		return null;
	}

}
