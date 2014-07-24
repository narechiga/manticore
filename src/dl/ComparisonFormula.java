package manticore.dl;
import java.util.*;


public class ComparisonFormula extends dLFormula {

	public ComparisonFormula ( Operator inequality, Term lhs, Term rhs ) {
		operator = inequality; //

		children = new ArrayList<dLStructure>();
		children.add( lhs );
		children.add( rhs );
	}

	public Operator inequality() {
		return (Operator)operator;
	}

	public Term getLHS() {
		return (Term)(children.get(0));
	}

	public Term getRHS() {
		return (Term)(children.get(1));
	}

	public String toKeYmaeraString () {
		return "( " + getLHS().toKeYmaeraString() + inequality().toKeYmaeraString() 
				+ getRHS().toKeYmaeraString() + " )";
	}

	public String toManticoreString () {
		return "( " + getLHS().toManticoreString() + inequality().toManticoreString() 
				+ getRHS().toManticoreString() + " )";
	}

	public String toMathematicaString () {
		if ( inequality().equals( new Operator("=") ) ) {
			return "( " + getLHS().toMathematicaString() + "=="
					+ getRHS().toMathematicaString() + " )";
		} else {
			return "( " + getLHS().toMathematicaString() + inequality().toMathematicaString() 
					+ getRHS().toMathematicaString() + " )";
		}
	}

	public String todRealString () {

		return "(" + this.getOperator().toString() 
				+ " " +getLHS().todRealString()
				+ " " +getRHS().todRealString() + ")";


	}

	public boolean isFirstOrder() {
		return true;
	}

	public boolean isPropositionalPrimitive() {
		return true;
	}

        public boolean isStatic() {
		return true;
        }

        public boolean isQuantifierFree() {
		return true;
        }

}
