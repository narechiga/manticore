package manticore.dl;
import java.util.*;


public class BoxModalityFormula extends dLFormula {

	public BoxModalityFormula ( HybridProgram program, dLFormula formula ) {
		operator = new Operator("box-modality"); //

		children = new ArrayList<dLStructure>();
		children.add( program );
		children.add( formula );
	}

	public HybridProgram program() {
		return (HybridProgram)(children.get(0));
	}

	public dLFormula formula() {
		return (dLFormula)(children.get(1));
	}

	public String toKeYmaeraString () {
		return "\\[" + program().toKeYmaeraString() +" \\]" + formula().toKeYmaeraString();
	}

	public boolean isFirstOrder() {
		return false;
	}
	
	public boolean isModal() {
		return true;
	}

}
