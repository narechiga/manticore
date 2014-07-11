package manticore.dl;
import java.util.*;


public class NotFormula extends dLFormula {

	public NotFormula ( dLFormula child ) { 
		operator = new Operator("not"); //

		children = new ArrayList<dLStructure>();
		children.add( child );
	}

	public dLFormula getChild() {
		return (dLFormula)(children.get(0));
	}

	public String toKeYmaeraString () {
		return "(! " + getChild().toKeYmaeraString() + " )";
	}

	public String toMathematicaString () {
		return "Not[ " + getChild().toMathematicaString() + " ]";
	}

	public boolean isFirstOrder() {
		return getChild().isFirstOrder();
	}

	public boolean isModal() {
		return getChild().isModal();
	}

}
