package manticore.dl;

import java.util.*;

public class SequenceProgram extends HybridProgram {

//	ArrayList<HybridProgram> children;

	public SequenceProgram( HybridProgram leftChild, HybridProgram rightChild ) {

		this.operator = new Operator("sequence");

		this.children = new ArrayList<dLStructure>();
		this.children.add( leftChild );
		this.children.add( rightChild );
	}

	// String methods
	public String toString() {
		return "( sequence " + children.get(0).toString() + ", " + children.get(1).toString() + " )";
	}

	public String toInfix() {
		return "( " + children.get(0).toString() + " ; " + children.get(1).toString() + " )";
	}

	public String toKeYmaera() {
		return toInfix();
	}


	// Administrative
	public boolean isPurelyContinuous() {
		return false;
	}

	public boolean isPurelyDiscrete() {
		HybridProgram leftChild = (HybridProgram)children.get(0);
		HybridProgram rightChild = (HybridProgram)children.get(1);
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
