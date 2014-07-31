package manticore.dl;
import java.util.*;


public class AndFormula extends dLFormula {

// Constructors and field getters
	public AndFormula ( dLFormula leftChild, dLFormula rightChild ) {
		operator = new Operator("and", 2, true); //

		children = new ArrayList<dLStructure>();
		children.add( leftChild );
		children.add( rightChild );
	}

	public dLFormula getLHS() {
		return (dLFormula)(children.get(0));
	}

	public dLFormula getRHS() {
		return (dLFormula)(children.get(1));
	}

// Substitution method
	public AndFormula substituteConcreteValuation( Valuation substitution ) {
		return new AndFormula( getLHS().substituteConcreteValuation( substitution ), 
					getRHS().substituteConcreteValuation( substitution ) );
	}

// Clone method
	public AndFormula clone() {
		return new AndFormula( getLHS().clone(), getRHS().clone() );
	}

// String methods
	public String toKeYmaeraString () {
		return "( " + getLHS().toKeYmaeraString() + " & " + getRHS().toKeYmaeraString() + " )";
	}

	public String toManticoreString () {
		return "( " + getLHS().toManticoreString() + " & " + getRHS().toManticoreString() + " )";
	}

	public String toMathematicaString () {
		return "( " + getLHS().toMathematicaString() + " && " + getRHS().toMathematicaString() + " )";
	}
    
	public String todRealString () {
		return "(and " + getLHS().todRealString() + " " + getRHS().todRealString() + " )";
	}

// Assorted convenience functions
	public boolean isFirstOrder() {
		return (getLHS().isFirstOrder() && getRHS().isFirstOrder() );
	}

	public boolean isModal() {
		return (getLHS().isModal() && getRHS().isModal() );
	}

	public boolean isStatic() {
		return (getLHS().isStatic() && getRHS().isStatic());
	}

	public boolean isQuantifierFree() {
		return (getLHS().isQuantifierFree() && getRHS().isQuantifierFree());
	}

}
