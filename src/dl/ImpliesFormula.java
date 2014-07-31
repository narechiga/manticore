package manticore.dl;
import java.util.*;


public class ImpliesFormula extends dLFormula {

// Constructor and field getters
	public ImpliesFormula ( dLFormula antecedent, dLFormula succedent ) {
		operator = new Operator("implies", 2, true); //

		children = new ArrayList<dLStructure>();
		children.add( antecedent );
		children.add( succedent );
	}

	public dLFormula getAntecedent() {
		return (dLFormula)(children.get(0));
	}

	public dLFormula getSuccedent() {
		return (dLFormula)(children.get(1));
	}

// Substitution method
	public ImpliesFormula substituteConcreteValuation( Valuation substitution ) {
		return new ImpliesFormula( getAntecedent().substituteConcreteValuation( substitution ),
						getSuccedent().substituteConcreteValuation( substitution ) );
	}

// Clone method
	public ImpliesFormula clone() {
		return new ImpliesFormula( getAntecedent().clone(), getSuccedent().clone() );
	}

// Strint methods
	public String toKeYmaeraString () {
		return "(" + getAntecedent().toKeYmaeraString() + " -> " + getSuccedent().toKeYmaeraString() + ")";
	}

	public String toMathematicaString () {
		return "Implies[ " + getAntecedent().toMathematicaString() 
				+ ", " + getSuccedent().toMathematicaString() + " ]";
	}

	public String toManticoreString () {
		return "(" + getAntecedent().toManticoreString() + " -> " + getSuccedent().toManticoreString() + ")";
	}

	public String todRealString() {
		return "(implies " + getAntecedent().todRealString() + " " + getSuccedent().todRealString() + ")\n";
	}

// Assorted convenience functions
	public boolean isFirstOrder() {
		return (getAntecedent().isFirstOrder() && getSuccedent().isFirstOrder() );
	}

	public boolean isModal() {
		return (getAntecedent().isModal() && getSuccedent().isModal() );
	}
        public boolean isStatic() {
                return (getAntecedent().isStatic() && getSuccedent().isStatic());
        }

        public boolean isQuantifierFree() {
                return (getAntecedent().isQuantifierFree() && getSuccedent().isQuantifierFree());
        }

}
