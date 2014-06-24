package manticore.dl;

import java.util.*;

public class RepetitionProgram extends HybridProgram {

	//ArrayList<HybridProgram> children;

	public RepetitionProgram( HybridProgram onlyChild ) {

		this.operator = new Operator("repeat");

		this.children = new ArrayList<dLStructure>();
		this.children.add( onlyChild );
	}

	public HybridProgram getSubProgram() {
		return (HybridProgram)(children.get(0));
	}
	

	// String methods
	public String toKeYmaeraString() {
		return "(" + children.get(0).toKeYmaeraString() + "*)";
	}


	// Administrative
	public boolean isPurelyContinuous() {
		return false;
	}

	public boolean isPurelyDiscrete() {
		return getSubProgram().isPurelyDiscrete();
	}

	public boolean isHybrid() {
		if ( (!isPurelyContinuous()) && (!isPurelyDiscrete()) ) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isProgramPrimitive() {
		return false;
	}

}
