package manticore.dl;

import java.util.*;

public class RepetitionProgram extends HybridProgram {

	//ArrayList<HybridProgram> children;

	public RepetitionProgram( HybridProgram onlyChild ) {

		this.operator = new Operator("repeat");

		this.children = new ArrayList<dLStructure>();
		this.children.add( onlyChild );
	}
	

	// String methods
	public String toString() {
		return "(repeat" + children.get(0).toString() + " )";
	}

	public String toInfix() {
		return "(" + children.get(0).toString() + "*)";
	}

	public String toKeYmaera() {
		return toInfix();
	}


	// Administrative
	public boolean isPurelyContinuous() {
		return false;
	}

	public boolean isPurelyDiscrete() {
		HybridProgram onlyChild = (HybridProgram)children.get(0);
		return onlyChild.isPurelyDiscrete();
	}

	public boolean isHybrid() {
		if ( (!isPurelyContinuous()) && (!isPurelyDiscrete()) ) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isPrimitive() {
		return false;
	}

}