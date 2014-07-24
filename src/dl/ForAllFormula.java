package manticore.dl;
import java.util.*;


public class ForAllFormula extends dLFormula {

	public ForAllFormula ( RealVariable quantifiedVariable, dLFormula quantifiedFormula ) {
		operator = new Operator("forall"); //

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
		return "(\\forall R " + getVariable().toKeYmaeraString() + "; " + getFormula().toKeYmaeraString() +" )";
	}

	public String toManticoreString () {
		return "(\\forall R " + getVariable().toManticoreString() + "; " + getFormula().toManticoreString() +" )";
	}

	public String toMathematicaString () {
		return "ForAll[ " + getVariable().toMathematicaString() + ", " + getFormula().toMathematicaString() +" ]";
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


