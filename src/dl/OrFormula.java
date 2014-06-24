package manticore.dl;
import java.util.*;


public class OrFormula extends dLFormula {

	public OrFormula ( dLFormula leftChild, dLFormula rightChild ) {
		operator = new Operator("or"); //

		children = new ArrayList<dLStructure>();
		children.add( leftChild );
		children.add( rightChild );
	}

	public dLFormula getLeftChild() {
		return (dLFormula)(children.get(0));
	}

	public dLFormula getRightChild() {
		return (dLFormula)(children.get(1));
	}

	public String toKeYmaeraString () {
		return "( " + getLeftChild().toKeYmaeraString() + " | " + getRightChild().toKeYmaeraString() + " )";
	}

	public boolean isFirstOrder() {
		return (getLeftChild().isFirstOrder() && getRightChild().isFirstOrder() );
	}

	public boolean isModal() {
		return (getLeftChild().isModal() && getRightChild().isModal() );
	}
}
