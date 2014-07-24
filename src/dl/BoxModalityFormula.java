package manticore.dl;
import java.util.*;


public class BoxModalityFormula extends dLFormula {

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

	public String toKeYmaeraString () {
		return "\\[" + getProgram().toKeYmaeraString() +" \\]" + getFormula().toKeYmaeraString();
	}
	
	public String toManticoreString () {
		return "\\[" + getProgram().toManticoreString() +" \\]" + getFormula().toManticoreString();
	}

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
