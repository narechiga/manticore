package manticore.dl;
import java.util.*;


public class ExistsFormula extends dLFormula {

// Constructors and field getters
	public ExistsFormula ( RealVariable quantifiedVariable, dLFormula quantifiedFormula ) {
		operator = new Operator("exists"); //

		children = new ArrayList<dLStructure>();
		children.add( quantifiedVariable );
		children.add( quantifiedFormula );
	}

	public RealVariable getVariable() {
		return (RealVariable)(children.get(0));
	}

	public dLFormula getFormula() {
		return (dLFormula)(children.get(1));
	}
// Substitution method
	public ExistsFormula substituteConcreteValuation( Valuation substitution ) {
		if ( substitution.containsVariable( getVariable() ) ) {
			// It's not actually the same variable, because of the scope of the quantifier
			return this.clone();
		} else {
			return new ExistsFormula( getVariable().clone(),
						getFormula().substituteConcreteValuation( substitution ) );
		}
	}

// Clone method
	public ExistsFormula clone() {
		return new ExistsFormula( getVariable().clone(), getFormula().clone() );
	}

// String methods
	public String toKeYmaeraString () {
		return "(\\exists R " + getVariable().toKeYmaeraString() + "; " + getFormula().toKeYmaeraString() +" )";
	}

	public String toManticoreString () {
		return "(\\exists R " + getVariable().toManticoreString() + "; " + getFormula().toManticoreString() +" )";
	}

	public String toMathematicaString () {
		return "Exists[ " + getVariable().toMathematicaString() + ", " + getFormula().toMathematicaString() +" ]";
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
		return false;
	}

}
