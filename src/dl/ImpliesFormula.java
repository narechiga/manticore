package manticore.dl;
import java.util.*;


public class ImpliesFormula extends dLFormula {

	public ImpliesFormula ( dLFormula antecedent, dLFormula succedent ) {
		operator = new Operator("implies"); //

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
		return "(" + getAntecedent().toKeYmaeraString() + " -> " + getSuccedent().toKeYmaeraString() + ")";
	}

	public String toMathematicaString () {
		return "Implies[ " + getAntecedent().toMathematicaString() + ", " + getSuccedent().toMathematicaString() + " ]";
	}

	public String toManticoreString () {
		return "(" + getAntecedent().toManticoreString() + " -> " + getSuccedent().toManticoreString() + ")";
	}

	public String todRealString() {
		return "(implies " + getAntecedent().todRealString() + " " + getSuccedent().todRealString() + ")\n";
	}

	public boolean isFirstOrder() {
		return (getAntecedent().isFirstOrder() && getSuccedent().isFirstOrder() );
	}

	public boolean isModal() {
		return (getAntecedent().isModal() && getSuccedent().isModal() );
	}
        public boolean isStatic() {
                return (getAntecedent().isStatic() && getSuccedent().isStatic());
        }

        public boolean isQuantifierFree() {
                return (getAntecedent().isQuantifierFree() && getSuccedent().isQuantifierFree());
        }

}
