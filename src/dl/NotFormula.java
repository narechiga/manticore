package manticore.dl;
import java.util.*;


public class NotFormula extends dLFormula {

// Constructors and field getters
	public NotFormula ( dLFormula child ) { 
		operator = new Operator("not", 1); //

		children = new ArrayList<dLStructure>();
		children.add( child );
	}

	public dLFormula getFormula() {
		return (dLFormula)(children.get(0));
	}

// Substition method
	public NotFormula substituteConcreteValuation( Valuation substitution ) {
		return new NotFormula( getFormula().substituteConcreteValuation( substitution ) );
	}

// Clone method
	public NotFormula clone() {
		return new NotFormula( getFormula().clone() );
	}

// String methods
	public String toKeYmaeraString () {
		return "(! " + getFormula().toKeYmaeraString() + " )";
	}

	public String toManticoreString () {
		return "(! " + getFormula().toManticoreString() + " )";
	}

	public String toMathematicaString () {
		return "Not[ " + getFormula().toMathematicaString() + " ]";
	}

	public String todRealString () {
		return "(not " + getFormula().todRealString() + " )";
	}

// Assorted convenience functions
	public boolean isFirstOrder() {
		return getFormula().isFirstOrder();
	}

	public boolean isModal() {
		return getFormula().isModal();
	}

        public boolean isStatic() {
                return getFormula().isStatic(); 
        }

        public boolean isQuantifierFree() {
                return getFormula().isQuantifierFree();
        }	

}
