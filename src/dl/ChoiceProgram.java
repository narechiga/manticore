package manticore.dl;

import java.util.*;

public class ChoiceProgram extends HybridProgram {

	//public ArrayList<HybridProgram> children;

	public ChoiceProgram( HybridProgram leftChild, HybridProgram rightChild ) {

		this.operator = new Operator("choice");

		this.children = new ArrayList<dLStructure>();
		this.children.add( leftChild );
		this.children.add( rightChild );
	}

	public String toKeYmaeraString() {
		return "( " + children.get(0).toString() + " ++ " + children.get(1).toString() + " )";
	}

	// Administrative
	public boolean isPurelyContinuous() {
		return false;
	}

	public boolean isPurelyDiscrete() {
		HybridProgram leftChild = (HybridProgram)this.children.get(0);
		HybridProgram rightChild = (HybridProgram)this.children.get(1);
		return ( leftChild.isPurelyDiscrete() && rightChild.isPurelyDiscrete() );
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
