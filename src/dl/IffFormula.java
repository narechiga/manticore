package manticore.dl;
import java.util.*;


public class IffFormula extends dLFormula {

	public IffFormula ( dLFormula antecedent, dLFormula succedent ) {
		operator = new Operator("iff"); //

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
		return "( " + getAntecedent().toKeYmaeraString() + " <-> " + getSuccedent().toKeYmaeraString() + " )";
	}

	public boolean isFirstOrder() {
		return (getAntecedent().isFirstOrder() && getSuccedent().isFirstOrder() );
	}

	public boolean isModal() {
		return (getAntecedent().isModal() && getSuccedent().isModal() );
	}

}
