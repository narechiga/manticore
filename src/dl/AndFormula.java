package manticore.dl;
import java.util.*;


public class AndFormula extends dLFormula {

	public AndFormula ( dLFormula leftChild, dLFormula rightChild ) {
		operator = new Operator("and"); //

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
		return "( " + getLHS().toKeYmaeraString() + " & " + getRHS().toKeYmaeraString() + " )";
	}

	public String toMathematicaString () {
		return "( " + getLHS().toMathematicaString() + " && " + getRHS().toMathematicaString() + " )";
	}

	public boolean isFirstOrder() {
		return (getLHS().isFirstOrder() && getRHS().isFirstOrder() );
	}

	public boolean isModal() {
		return (getLHS().isModal() && getRHS().isModal() );
	}

}
