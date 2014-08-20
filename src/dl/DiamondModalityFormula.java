package manticore.dl;
import java.util.*;


public class DiamondModalityFormula extends dLFormula {


// Constructors and field getters
	public DiamondModalityFormula ( HybridProgram program, dLFormula formula ) {
		operator = new Operator("diamond-modality"); //

		children = new ArrayList<dLStructure>();
		children.add( program );
		children.add( formula );
	}

	public HybridProgram getProgram() {
		return (HybridProgram)(children.get(0));
	}

	public dLFormula getFormula() {
		return (dLFormula)(children.get(1));
	}

//	public getLHS() {
//		return getProgram();
//	}
//
//	public getRHS() {
//		return getFormula();
//	}

// Substitution method
	public DiamondModalityFormula substituteConcreteValuation( Valuation substitution ) {
		return new DiamondModalityFormula( getProgram().substituteConcreteValuation( substitution ),
							getFormula().substituteConcreteValuation( substitution ) );
	}

// Clone method
	public DiamondModalityFormula clone() {
		return new DiamondModalityFormula( getProgram().clone(),
							getFormula().clone() );
	}

// String methods
	public String toKeYmaeraString () {
		return "\\<" + getProgram().toKeYmaeraString() +" \\>" + getFormula().toKeYmaeraString();
	}

	public String toManticoreString () {
		return "\\<" + getProgram().toManticoreString() +" \\>" + getFormula().toManticoreString();
	}

// Assorted convenience functions
	public boolean isFirstOrder() {
		return false;
	}
	
	public boolean isModal() {
		return true;
	}

	public boolean isStatic() {
		return false;
	}

	public boolean isQuantifierFree() {
		return ( getProgram().isQuantifierFree() && getProgram().isQuantifierFree() );
	}

// Logic
	public BoxModalityFormula negate() {
		return new BoxModalityFormula( getProgram(), getFormula().negate() );
	}

	public Set<RealVariable> getBoundVariables() {
		HashSet<RealVariable> boundVariables = new HashSet<RealVariable>();
		boundVariables.addAll( getProgram().getBoundVariables() );
		boundVariables.addAll( getFormula().getBoundVariables() );
		return boundVariables;
	}

	public Set<RealVariable> getFreeVariables() {
		HashSet<RealVariable> freeVariables = new HashSet<RealVariable>();
		freeVariables.addAll( getProgram().getFreeVariables() );
		freeVariables.addAll( getFormula().getFreeVariables() );
		return freeVariables;
	}

	public Set<RealVariable> getDynamicVariables() {
		HashSet<RealVariable> dynamicVariables = new HashSet<RealVariable>();
		dynamicVariables.addAll( getProgram().getDynamicVariables() );
		dynamicVariables.addAll( getFormula().getDynamicVariables() );
		return dynamicVariables;
	}

}
