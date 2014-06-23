package manticore.dl;
import java.util.*;


public class NotFormula extends dLFormula {

	public NotFormula ( dLFormula child ) { 
		operator = new Operator("not"); //

		children = new ArrayList<dLStructure>();
		children.add( child );
	}

	public dLFormula child() {
		return (dLFormula)(children.get(0));
	}

	public String toKeYmaeraString () {
		return "(! " + child().toKeYmaeraString() + " )";
	}

	public boolean isFirstOrder() {
		return child().isFirstOrder();
	}

	public boolean isModal() {
		return child().isModal();
	}

}
