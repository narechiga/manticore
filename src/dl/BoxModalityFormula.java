package manticore.dl;
import java.util.*;


public class BoxModalityFormula extends dLFormula {

// Constructors and field getters
	public BoxModalityFormula ( HybridProgram program, dLFormula formula ) {
		operator = new Operator("box-modality"); //

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


// Substitution method
	public BoxModalityFormula substituteConcreteValuation( Valuation substitution ) {
		return new BoxModalityFormula( getProgram().substituteConcreteValuation( substitution ), 
					getFormula().substituteConcreteValuation( substitution ) );
	}

// Clone method
	public BoxModalityFormula clone() {
		return new BoxModalityFormula( getProgram().clone(), getFormula().clone() );
	}

// String methods
	public String toKeYmaeraString () {
		return "\\[" + getProgram().toKeYmaeraString() +" \\]" + getFormula().toKeYmaeraString();
	}
	
	public String toManticoreString () {
		return "\\[" + getProgram().toManticoreString() +" \\]" + getFormula().toManticoreString();
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
		return (getFormula().isQuantifierFree() && getProgram().isQuantifierFree());
	}

}
