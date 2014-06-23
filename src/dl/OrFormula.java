package manticore.dl;
import java.util.*;


public class OrFormula extends dLFormula {

	public OrFormula ( dLFormula leftChild, dLFormula rightChild ) {
		operator = new Operator("or"); //

		children = new ArrayList<dLStructure>();
		children.add( leftChild );
		children.add( rightChild );
	}

	public dLFormula leftChild() {
		return (dLFormula)(children.get(0));
	}

	public dLFormula rightChild() {
		return (dLFormula)(children.get(1));
	}

	public String toKeYmaeraString () {
		return "( " + leftChild().toKeYmaeraString() + " | " + rightChild().toKeYmaeraString() + " )";
	}

	public boolean isFirstOrder() {
		return (leftChild().isFirstOrder() && rightChild().isFirstOrder() );
	}

	public boolean isModal() {
		return (leftChild().isModal() && rightChild().isModal() );
	}
}
