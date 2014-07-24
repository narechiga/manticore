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

	public String toManticoreString () {
		return "(! " + getChild().toManticoreString() + " )";
	}

	public String toMathematicaString () {
		return "Not[ " + getChild().toMathematicaString() + " ]";
	}

	public String todRealString () {
		return "(not " + getChild().todRealString() + " )\n";
	}

	public boolean isFirstOrder() {
		return getChild().isFirstOrder();
	}

	public boolean isModal() {
		return getChild().isModal();
	}

        public boolean isStatic() {
                return getChild().isStatic(); 
        }

        public boolean isQuantifierFree() {
                return getChild().isQuantifierFree();
        }	

}
