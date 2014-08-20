package manticore.dl;
import java.util.*;


public class OrFormula extends dLFormula {

// Constructors and field getters
	public OrFormula ( dLFormula leftChild, dLFormula rightChild ) {
		operator = new Operator("or", 2, true); //

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

// Substitution method
	public OrFormula substituteConcreteValuation( Valuation substitution ) {
		return new OrFormula( getLHS().substituteConcreteValuation( substitution ), 
					getRHS().substituteConcreteValuation( substitution ) );
	}

// Clone method
	public OrFormula clone() {
		return new OrFormula( getLHS().clone(), getRHS().clone() );
	}

// String methods
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

// Assorted convenience functions
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

// Logic
	public AndFormula negate() {
		return new AndFormula( getLHS().negate(), getRHS().negate() );
	}

	public Set<RealVariable> getBoundVariables() {
		HashSet<RealVariable> boundVariables = new HashSet<RealVariable>();
		boundVariables.addAll( getLHS().getBoundVariables() );
		boundVariables.addAll( getRHS().getBoundVariables() );
		return boundVariables;
	}

	public Set<RealVariable> getFreeVariables() {
		HashSet<RealVariable> freeVariables = new HashSet<RealVariable>();
		freeVariables.addAll( getLHS().getFreeVariables() );
		freeVariables.addAll( getRHS().getFreeVariables() );
		return freeVariables;
	}


	public Set<RealVariable> getDynamicVariables() {
		HashSet<RealVariable> dynamicVariables = new HashSet<RealVariable>();
		dynamicVariables.addAll( getLHS().getDynamicVariables() );
		dynamicVariables.addAll( getRHS().getDynamicVariables() );
		return dynamicVariables;
	}
	//public dLFormula simplifyOrFalse() {
	//	if ( getLHS() instanceof FalseFormula ) {
	//		if ( getRHS() instanceof OrFormula ) {
	//			return ((OrFormula)getRHS()).simplifyOrFalse();
	//		} else {
	//			return getRHS().clone();
	//		}
	//	else if ( getRHS() instanceof FalseFormula ) {
	//		if ( getLHS() instanceof OrFormula ) {
	//			return ((OrFormula)getLHS()).simplifyOrFalse();
	//		} else {
	//			return getLHS().clone();
	//		}
	//	else {


	//	}

}
