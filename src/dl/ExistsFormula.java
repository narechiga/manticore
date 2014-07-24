package manticore.dl;
import java.util.*;


public class ExistsFormula extends dLFormula {

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

	public String toKeYmaeraString () {
		return "(\\exists R " + getVariable().toKeYmaeraString() + "; " + getFormula().toKeYmaeraString() +" )";
	}

	public String toManticoreString () {
		return "(\\exists R " + getVariable().toManticoreString() + "; " + getFormula().toManticoreString() +" )";
	}

	public String toMathematicaString () {
		return "Exists[ " + getVariable().toMathematicaString() + ", " + getFormula().toMathematicaString() +" ]";
	}

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
