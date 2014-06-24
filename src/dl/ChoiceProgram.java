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

	public HybridProgram getLeftProgram() {
		return (HybridProgram)(children.get(0));
	}

	public HybridProgram getRightProgram() {
		return (HybridProgram)(children.get(1));
	}

	// Administrative
	public boolean isPurelyContinuous() {
		return false;
	}

	public boolean isPurelyDiscrete() {
		return ( getLeftProgram().isPurelyDiscrete() && getRightProgram().isPurelyDiscrete() );
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
