package manticore.dl;

import java.util.*;

public class SequenceProgram extends HybridProgram {

//	ArrayList<HybridProgram> children;

	public SequenceProgram( HybridProgram firstProgram, HybridProgram secondProgram ) {

		this.operator = new Operator("sequence");

		this.children = new ArrayList<dLStructure>();
		this.children.add( firstProgram );
		this.children.add( secondProgram );
	}

	// String methods
	public String toKeYmaeraString() {
		return "( " + children.get(0).toKeYmaeraString() + " ; " + children.get(1).toKeYmaeraString() + " )";
	}


	public HybridProgram getFirstProgram() {
		return (HybridProgram)(children.get(0));
	}

	public HybridProgram getSecondProgram() {
		return (HybridProgram)(children.get(1));
	}

	// Administrative
	public boolean isPurelyContinuous() {
		return false;
	}

	public boolean isPurelyDiscrete() {
		return ( getFirstProgram().isPurelyDiscrete() && getSecondProgram().isPurelyDiscrete() );
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
