package manticore.dl.syntax.syntax;
import java.util.*;


public class NotFormula extends dLFormula {

	boolean debug = false;

// Constructors and field getters
	public NotFormula ( dLFormula child ) { 
		operator = new Operator("not", 1); //

		children = new ArrayList<dLStructure>();
		children.add( child );
	}

	public dLFormula getFormula() {
		return (dLFormula)(children.get(0));
	}

// Substition method
	public NotFormula substituteConcreteValuation( Valuation substitution ) {
		NotFormula substitutedFormula = new NotFormula( getFormula().substituteConcreteValuation( substitution ) );
		if( debug ) {
			System.out.println("Returning NotFormula: " + substitutedFormula.toMathematicaString() );
		}
		return substitutedFormula;
	}

// Clone method
	public NotFormula clone() {
		return new NotFormula( getFormula().clone() );
	}

// String methods
	public String toKeYmaeraString () {
		return "(! " + getFormula().toKeYmaeraString() + " )";
	}

	public String toManticoreString () {
		return "(! " + getFormula().toManticoreString() + " )";
	}

	public String toMathematicaString () {
		return "Not[ " + getFormula().toMathematicaString() + " ]";
	}

	public String todRealString () {
		return "(not " + getFormula().todRealString() + " )";
	}

// Assorted convenience functions
	public boolean isFirstOrder() {
		return getFormula().isFirstOrder();
	}

	public boolean isModal() {
		return getFormula().isModal();
	}

        public boolean isStatic() {
                return getFormula().isStatic(); 
        }

        public boolean isQuantifierFree() {
                return getFormula().isQuantifierFree();
        }	

// Logic
	public NotFormula negate() {
		return new NotFormula( getFormula() );
	}

	public dLFormula pushNegation() {
		return getFormula().negate();
	}

	public Set<RealVariable> getBoundVariables() {
		HashSet<RealVariable> boundVariables = new HashSet<RealVariable>();
		boundVariables.addAll( getFormula().getBoundVariables() );
		return boundVariables;
	}

	public Set<RealVariable> getFreeVariables() {
		HashSet<RealVariable> freeVariables = new HashSet<RealVariable>();
		freeVariables.addAll( getFormula().getFreeVariables() );
		return freeVariables;
	}

	public Set<RealVariable> getDynamicVariables() {
		HashSet<RealVariable> dynamicVariables = new HashSet<RealVariable>();
		dynamicVariables.addAll( getFormula().getDynamicVariables() );
		return dynamicVariables;
	}



}
