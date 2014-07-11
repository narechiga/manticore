package manticore.dl;
import java.util.*;


public class ImpliesFormula extends dLFormula {

	public ImpliesFormula ( dLFormula antecedent, dLFormula succedent ) {
		operator = new Operator("implies"); //

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

	public String toKeYmaeraString () {
		return "Implies[ " + getAntecedent().toKeYmaeraString() + ", " + getSuccedent().toKeYmaeraString() + " ]";
	}

	public boolean isFirstOrder() {
		return (getAntecedent().isFirstOrder() && getSuccedent().isFirstOrder() );
	}

	public boolean isModal() {
		return (getAntecedent().isModal() && getSuccedent().isModal() );
	}

}
