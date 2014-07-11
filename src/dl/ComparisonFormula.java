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

	public String toMathematicaString () {
		if ( inequality().equals( new Operator("=") ) ) {
			return "( " + getLHS().toMathematicaString() + "=="
					+ getRHS().toMathematicaString() + " )";
		} else {
			return "( " + getLHS().toMathematicaString() + inequality().toMathematicaString() 
					+ getRHS().toMathematicaString() + " )";
		}
	}

	public boolean isFirstOrder() {
		return true;
	}

	public boolean isPropositionalPrimitive() {
		return true;
	}

}
