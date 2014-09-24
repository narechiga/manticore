package manticore.dl.syntax;

import manticore.dl.semantics.*;
import java.util.*;


public class ComparisonFormula extends dLFormula {
	boolean debug = false;

// Constructors and field getters
	public ComparisonFormula ( Operator inequality, Term lhs, Term rhs ) {
		operator = inequality; //

		children = new ArrayList<dLStructure>();
		children.add( lhs );
		children.add( rhs );
	}

	public ComparisonFormula ( String inequality, Term lhs, Term rhs ) {
		operator = new Operator(inequality); //

		children = new ArrayList<dLStructure>();
		children.add( lhs );
		children.add( rhs );
	}

	public ComparisonFormula() {
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
		ComparisonFormula substitutedFormula  = new ComparisonFormula( getInequality().clone(),
						getLHS().substituteConcreteValuation( substitution ),
						getRHS().substituteConcreteValuation( substitution ) );
		if( debug ) {
			System.out.println("Returning ComparisonFormula: " + substitutedFormula.toManticoreString() );
		}
		return substitutedFormula;
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
		String returnString;

		if ( operator.equals( new Operator("!=") )) {
			// dReal doesn't know this operator, so convert to inequalities
			ComparisonFormula avoidLeft = new ComparisonFormula(
								"<",
								this.getLHS(),
								this.getRHS() );
			ComparisonFormula avoidRight = new ComparisonFormula(
								">",
								this.getLHS(),
								this.getRHS() );
			OrFormula avoidPoint = new OrFormula( avoidLeft, avoidRight );

			returnString = avoidPoint.todRealString();
		} else {
			returnString =  "(" + this.getOperator().toString() 
				+ " " +getLHS().todRealString()
				+ " " +getRHS().todRealString() + ")";
		}

		return returnString;


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

// Logic
	public ComparisonFormula negate() {
		ComparisonFormula returnFormula = null;

		if ( operator.equals( new Operator("<=") ) ) {
			returnFormula = new ComparisonFormula(">", this.getLHS().clone(), this.getRHS().clone() );

		} else if ( operator.equals( new Operator("<") )) {
			returnFormula = new ComparisonFormula(">=", this.getLHS().clone(), this.getRHS().clone() );

		} else if ( operator.equals( new Operator("==") )
				|| operator.equals( new Operator("=") )	
			) {
			returnFormula = new ComparisonFormula("!=", this.getLHS().clone(), this.getRHS().clone() );

		} else if ( operator.equals( new Operator("!=") )) {
			returnFormula = new ComparisonFormula("==", this.getLHS().clone(), this.getRHS().clone() );

		} else if ( operator.equals( new Operator(">") )) {
			returnFormula = new ComparisonFormula("<=", this.getLHS().clone(), this.getRHS().clone() );

		} else if ( operator.equals( new Operator(">=") )) {
			returnFormula = new ComparisonFormula("<", this.getLHS().clone(), this.getRHS().clone() );

		} else {
			throw new UnknownInequalityException( ANSI_BOLD + ANSI_RED + operator.toString() + ANSI_RESET );
		}

		return returnFormula;
	}

	public Set<RealVariable> getBoundVariables() {
		return new HashSet<RealVariable>();
	}

	public Set<RealVariable> getFreeVariables() {
		HashSet<RealVariable> freeVariables = new HashSet<RealVariable>();
		freeVariables.addAll( getLHS().getFreeVariables() );
		freeVariables.addAll( getRHS().getFreeVariables() );
		return freeVariables;
	}
	
	public Set<RealVariable> getDynamicVariables() {
		return new HashSet<RealVariable>();
	}

}
