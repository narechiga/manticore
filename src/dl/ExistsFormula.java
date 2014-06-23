package manticore.dl;
import java.util.*;


public class ExistsFormula extends dLFormula {

	public ExistsFormula ( RealVariable quantifiedVariable, dLFormula quantifiedFormula ) {
		operator = new Operator("exists"); //

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
		return "(\\exists R " + quantifiedVariable().toKeYmaeraString() + "; " + quantifiedFormula().toKeYmaeraString() +" )";
	}

	public boolean isFirstOrder() {
		return quantifiedFormula().isFirstOrder();
	}
	
	public boolean isModal() {
		return quantifiedFormula().isModal();
	}

}
