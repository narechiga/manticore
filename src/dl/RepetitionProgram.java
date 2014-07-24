package manticore.dl;

import java.util.*;

public class RepetitionProgram extends HybridProgram {

	//ArrayList<HybridProgram> children;

	public RepetitionProgram( HybridProgram onlyChild ) {

		this.operator = new Operator("repeat");

		this.children = new ArrayList<dLStructure>();
		this.children.add( onlyChild );
	}

	public HybridProgram getChild() {
		return (HybridProgram)(children.get(0));
	}
	

	// String methods
	public String toKeYmaeraString() {
		return "(" + children.get(0).toKeYmaeraString() + "*)";
	}

	public String toManticoreString() {
		return "(" + children.get(0).toManticoreString() + "***)";
	}


	// Administrative
	public boolean isPurelyContinuous() {
		return false;
	}

	public boolean isPurelyDiscrete() {
		return getChild().isPurelyDiscrete();
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

	public boolean isQuantifierFree() {
		return getChild().isQuantifierFree();
	}

}
