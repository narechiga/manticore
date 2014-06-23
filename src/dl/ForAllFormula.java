package manticore.dl;
import java.util.*;


public class ForAllFormula extends dLFormula {

	public ForAllFormula ( RealVariable quantifiedVariable, dLFormula quantifiedFormula ) {
		operator = new Operator("forall"); //

		children = new ArrayList<dLStructure>();
		children.add( quantifiedVariable );
		children.add( quantifiedFormula );
	}

	public RealVariable quantifiedVariable() {
		return (RealVariable)(children.get(0));
	}

	public dLFormula quantifiedFormula() {
		return (dLFormula)(children.get(1));
	}

	public String toKeYmaeraString () {
		return "(\\forall R " + quantifiedVariable().toKeYmaeraString() + "; " + quantifiedFormula().toKeYmaeraString() +" )";
	}

	public boolean isFirstOrder() {
		return quantifiedFormula().isFirstOrder();
	}
	
	public boolean isModal() {
		return quantifiedFormula().isModal();
	}

}
