package manticore.dl;
import java.util.*;


public class ComparisonFormula extends dLFormula {

// Constructors and field getters
	public ComparisonFormula ( Operator inequality, Term lhs, Term rhs ) {
		operator = inequality; //

		children = new ArrayList<dLStructure>();
		children.add( lhs );
		children.add( rhs );
	}

	public Operator getInequality() {
		return (Operator)operator;
	}

	public Term getLHS() {
		return (Term)(children.get(0));
	}

	public Term getRHS() {
		return (Term)(children.get(1));
	}

// Substitution method
	public ComparisonFormula substituteConcreteValuation( Valuation substitution ) {
		return new ComparisonFormula( getInequality().clone(),
						getLHS().substituteConcreteValuation( substitution ),
						getRHS().substituteConcreteValuation( substitution ) );
	}

// Clone method
	public ComparisonFormula clone() {
		return new ComparisonFormula( getInequality().clone(),
						getLHS().clone(),
						getRHS().clone());
	}

// String methods
	public String toKeYmaeraString () {
		return "( " + getLHS().toKeYmaeraString() 
				+ " " + getInequality().toKeYmaeraString() + " "
				+ getRHS().toKeYmaeraString() + " )";
	}

	public String toManticoreString () {
		return "( " + getLHS().toManticoreString() + getInequality().toManticoreString() 
				+ getRHS().toManticoreString() + " )";
	}

	public String toMathematicaString () {
		if ( getInequality().equals( new Operator("=") ) ) {
			return "( " + getLHS().toMathematicaString() + " == "
					+ getRHS().toMathematicaString() + " )";
		} else {
			return "( " + getLHS().toMathematicaString() 
					+ " " + getInequality().toMathematicaString() + " "
					+ getRHS().toMathematicaString() + " )";
		}
	}

	public String todRealString () {

		return "(" + this.getOperator().toString() 
				+ " " +getLHS().todRealString()
				+ " " +getRHS().todRealString() + ")";


	}

// Assorted convenience functions
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
