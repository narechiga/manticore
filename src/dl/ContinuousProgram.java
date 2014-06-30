package manticore.dl;

import java.util.*;

public class ContinuousProgram extends HybridProgram {

	// constructor with DOE
	public ContinuousProgram ( List<dLStructure> odeList, dLFormula doe ) {
		this.operator = new Operator("continuous-evolution");
		this.children = odeList;

		// Guarantee: Must always have a doe, even if it's just "true"
		this.children.add( doe );
	}

	// constructor without DOE
	public ContinuousProgram ( List<dLStructure> odeList ) {
		this.operator = new Operator("continuous-evolution");
		this.children = odeList;

		// Guarantee: Must always have a doe, even if it's just "true"
		this.children.add( new TrueFormula() );
	}

	// Operations on ODE List
	public ArrayList<ExplicitODE> getODEs () {

		ArrayList<ExplicitODE> odeList = new ArrayList<ExplicitODE>();

		Iterator<dLStructure> childIterator = children.iterator();
		dLStructure thisChild;
		while ( childIterator.hasNext() ) {
			thisChild = childIterator.next();
			if ( thisChild instanceof ExplicitODE) {
				odeList.add( (ExplicitODE)thisChild );
			}
		}

		return odeList;
	}

	public ExplicitODE getODE( int index ) {
		return (ExplicitODE)children.get( index );
	}

	public void addODE( dLStructure ode ) {
		int doeIndex = children.size() - 1;
		
		children.add( doeIndex, ode );
	}

	public void addODEs( List<dLStructure> odeList ) {
		int doeIndex = children.size() - 1;
		
		children.addAll( doeIndex, odeList );
	}


	// Operations on DOE
	public dLFormula getDOE() {
		int doeIndex = children.size() - 1;

		if ( children.get(doeIndex) instanceof dLFormula ) {
			return ((dLFormula)children.get(doeIndex));
		} else {
			return null;
		}
	}

	public void setDOE ( dLFormula doe ) {
		int doeIndex = children.size();

		// Assume: Must always have a doe, even if it's just "true"
		children.set( doeIndex, doe );
	}

	public void restrictDOE () {}

	public void enlargeDOE () {}

	// Implementing abstract methods from superclass
	public boolean isPurelyContinuous() {
		return true;
	}

	public boolean isPurelyDiscrete() {
		return false;
	}

	public boolean isHybrid() {
		return false;
	}

	public boolean isProgramPrimitive() {
		return true;
	}

	// S tring operations
	public String toKeYmaeraString() {
		
		String returnString = "{ ";

		Iterator<dLStructure> childIterator = children.iterator();
		dLStructure thisChild;
		ExplicitODE thisODE;

		while( childIterator.hasNext() ) {
			
			thisChild = childIterator.next();

			if ( childIterator.hasNext() ) { // then this is an ode, not the doe

				thisODE = (ExplicitODE)thisChild;
				returnString = returnString + thisODE.toKeYmaeraString() + ", ";

			} else { //then this is the doe
				returnString = returnString.substring(0, returnString.length() -2 );
				returnString = returnString + " & " + getDOE().toKeYmaeraString(); // TODO: Write a toKeYmaeraString for logical formulas
			}
		}
		returnString = returnString + " }";
		
		return returnString;
	}


}
