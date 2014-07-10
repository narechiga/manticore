package manticore.dl;
import java.util.*;


public class OrFormula extends dLFormula {

	public OrFormula ( dLFormula leftChild, dLFormula rightChild ) {
		operator = new Operator("or"); //

		children = new ArrayList<dLStructure>();
		children.add( leftChild );
		children.add( rightChild );
	}

	public dLFormula getLHS() {
		return (dLFormula)(children.get(0));
	}

	public dLFormula getRHS() {
		return (dLFormula)(children.get(1));
	}

	public String toKeYmaeraString () {
		return "( " + getLHS().toKeYmaeraString() + " | " + getRHS().toKeYmaeraString() + " )";
	}

	public boolean isFirstOrder() {
		return (getLHS().isFirstOrder() && getRHS().isFirstOrder() );
	}

	public boolean isModal() {
		return (getLHS().isModal() && getRHS().isModal() );
	}
}
