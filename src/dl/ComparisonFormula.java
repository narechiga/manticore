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

	public Term lhs() {
		return (Term)(children.get(0));
	}

	public Term rhs() {
		return (Term)(children.get(1));
	}

	public String toKeYmaeraString () {
		return "( " + lhs().toKeYmaeraString() + inequality().toKeYmaeraString() + rhs().toKeYmaeraString() + " )";
	}

	public boolean isFirstOrder() {
		return true;
	}

	public boolean isPropositionalPrimitive() {
		return true;
	}

}
