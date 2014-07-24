package manticore.dl;
import java.util.*;


public class IffFormula extends dLFormula {

	public IffFormula ( dLFormula antecedent, dLFormula succedent ) {
		operator = new Operator("iff"); //

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
		return "( " + getAntecedent().toKeYmaeraString() + " <-> " + getSuccedent().toKeYmaeraString() + " )";
	}

	public String toManticoreString () {
		return "( " + getAntecedent().toManticoreString() + " <-> " + getSuccedent().toManticoreString() + " )";
	}

	public String toMathematicaString () {
		return "Equivalent[ " + getAntecedent().toMathematicaString() 
				+ ", " + getSuccedent().toMathematicaString() + " ]";
	}

	public String todRealString () {
		AndFormula biimplies = new AndFormula( new ImpliesFormula( this.getAntecedent(), this.getSuccedent() ),
							new ImpliesFormula( this.getSuccedent(), this.getAntecedent() ) );
		
		return biimplies.todRealString();
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
