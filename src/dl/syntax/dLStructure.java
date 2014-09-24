package manticore.dl.syntax;

import manticore.dl.semantics.*;
import manticore.dl.parser.*;

import java.util.*;
import java.io.*;

public abstract class dLStructure {

	public boolean debug = true;
	public Operator operator;
	public ArrayList<dLStructure> children;

        // COLORS! OMG COLORS!
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_BLACK = "\u001B[30m";
        public static final String ANSI_RED = "\u001B[31m";
        public static final String ANSI_GREEN = "\u001B[32m";
        public static final String ANSI_YELLOW = "\u001B[33m";
        public static final String ANSI_BLUE = "\u001B[34m";
        public static final String ANSI_PURPLE = "\u001B[35m";
        public static final String ANSI_CYAN = "\u001B[36m";
        public static final String ANSI_WHITE = "\u001B[37m";
        public static final String ANSI_BOLD = "\u001B[1m";


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

	public void spawnChildren() {
		children = new ArrayList<dLStructure>();
	}

// Parse a dLStructure from a string
	public static dLStructure parseStructure( String structureString ) throws Exception {
		// returns the dLStructure that exists in the string
		StringReader thisReader = new StringReader( structureString );
		dLLexer thisdLLexer = new dLLexer( thisReader );
		dLParser thisParser = new dLParser( thisdLLexer );

		thisParser.parse();

		return thisParser.parsedStructure;
	}

// Return a dLStructure that is the same as this one, but with syntactic substitution
// of real variables according to the valuation given as an argument
//	public dLStructure substitute( Valuation substitutions ) {
//
//		if ( substitutions == null ) {
//			 System.out.println("WARNING: manticore.dLStructure.substitute received a null subsitituion valuation");
//			 return this;
//			
//		} else if ( this instanceof Real ) {
//			if( debug ) {
//				System.out.println("manticore.dLStructure.substitute returning: " 
//					+ this.toMathematicaString() );
//			}
//			return ((Real)this).clone();
//
//		} else if ( this instanceof RealVariable ) {
//
//			if ( substitutions.get( (RealVariable)this ) != null ) {	
//				if( debug ) {
//					System.out.println("manticore.dLStructure.substitute returning: " + 
//						substitutions.get( (RealVariable)this ).clone().toMathematicaString());
//				}
//
//				return substitutions.get( (RealVariable)this ).clone();
//			} else {
//				if( debug ) {
//					System.out.println("manticore.dLStructure.substitute returning: " + 
//								this.toMathematicaString() );
//				}
//
//				return this;
//			}
//
//
//		} else {
//
//			dLStructure newStructure = new dLStructure( this.getOperator() );
//			Iterator<dLStructure> childIterator = children.iterator();
//			while( childIterator.hasNext() ) {
//				newStructure.addChild(
//					childIterator.next().substitute( substitutions )
//					);
//			}
//
//			if( debug ) {
//				System.out.println("manticore.dLStructure.substitute returning: " 
//					+ newStructure.toMathematicaString() );
//			}
//
//			return newStructure;
//		}
//	}

// Extract assorted bits and pieces
// 1. getVariables
// 2. getBoundVariables
// 3. getFreeVariables
// 4. extractContinuousBlocks
// 5. extractFirstHybridProgram
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

	public Set<RealVariable> getBoundVariables() {
		return new HashSet<RealVariable>();
	}

	public abstract Set<RealVariable> getFreeVariables();
	public abstract Set<RealVariable> getDynamicVariables();

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
