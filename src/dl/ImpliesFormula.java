package manticore.dl;
import java.util.*;


public class ImpliesFormula extends dLFormula {

	public ImpliesFormula ( dLFormula antecedent, dLFormula succedent ) {
		operator = new Operator("implies"); //

		children = new ArrayList<dLStructure>();
		children.add( antecedent );
		children.add( succedent );
	}

	public dLFormula antecedent() {
		return (dLFormula)(children.get(0));
	}

	public dLFormula succedent() {
		return (dLFormula)(children.get(1));
	}

	public String toKeYmaeraString () {
		return "( " + antecedent().toKeYmaeraString() + " -> " + succedent().toKeYmaeraString() + " )";
	}

	public boolean isFirstOrder() {
		return (antecedent().isFirstOrder() && succedent().isFirstOrder() );
	}

	public boolean isModal() {
		return (antecedent().isModal() && succedent().isModal() );
	}

}
