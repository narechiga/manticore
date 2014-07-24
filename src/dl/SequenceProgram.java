package manticore.dl;

import java.util.*;

public class SequenceProgram extends HybridProgram {


	public SequenceProgram( HybridProgram firstProgram, HybridProgram secondProgram ) {

		this.operator = new Operator("sequence");

		this.children = new ArrayList<dLStructure>();
		this.children.add( firstProgram );
		this.children.add( secondProgram );
	}

	public HybridProgram getLHS() {
		return (HybridProgram)(children.get(0));
	}

	public HybridProgram getRHS() {
		return (HybridProgram)(children.get(1));
	}

	// String methods
	public String toKeYmaeraString() {
		return "( " + children.get(0).toKeYmaeraString() + " ; " + children.get(1).toKeYmaeraString() + " )";
	}

	public String toManticoreString() {
		return "( " + children.get(0).toManticoreString() + " ; " + children.get(1).toManticoreString() + " )";
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

	public boolean isQuantifierFree() {
		return (getLHS().isQuantifierFree() && getRHS().isQuantifierFree() );
	}

}
