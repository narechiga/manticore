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

	public String toManticoreString () {
		return "( " + getLHS().toManticoreString() + " | " + getRHS().toManticoreString() + " )";
	}

	public String toMathematicaString () {
		return "( " + getLHS().toMathematicaString() + " || " + getRHS().toMathematicaString() + " )";
	}

	public String todRealString() {
		return "(or " + getLHS().todRealString() + " " + getRHS().todRealString() + " )";
	}

	public boolean isFirstOrder() {
		return (getLHS().isFirstOrder() && getRHS().isFirstOrder() );
	}

	public boolean isModal() {
		return (getLHS().isModal() && getRHS().isModal() );
	}

        public boolean isStatic() {
                return (getLHS().isStatic() && getRHS().isStatic());
        }

        public boolean isQuantifierFree() {
                return (getLHS().isQuantifierFree() && getRHS().isQuantifierFree());
        }
}
