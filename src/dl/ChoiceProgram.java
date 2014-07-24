package manticore.dl;

import java.util.*;

public class ChoiceProgram extends HybridProgram {

	//public ArrayList<HybridProgram> children;

	public ChoiceProgram( HybridProgram leftProgram, HybridProgram rightProgram ) {

		this.operator = new Operator("choice");

		this.children = new ArrayList<dLStructure>();
		this.children.add( leftProgram );
		this.children.add( rightProgram );
	}

	public String toKeYmaeraString() {
		return "( " + children.get(0).toKeYmaeraString() + " ++ " + children.get(1).toKeYmaeraString() + " )";
	}

	public String toManticoreString() {
		return "( " + children.get(0).toManticoreString() + " ++ " + children.get(1).toManticoreString() + " )";
	}

	public HybridProgram getLHS() {
		return (HybridProgram)(children.get(0));
	}

	public HybridProgram getRHS() {
		return (HybridProgram)(children.get(1));
	}

	// Administrative
	public boolean isPurelyContinuous() {
		return false;
	}

	public boolean isPurelyDiscrete() {
		return ( getLHS().isPurelyDiscrete() && getRHS().isPurelyDiscrete() );
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
		return (getLHS().isQuantifierFree() && getRHS().isQuantifierFree());
	}

}
