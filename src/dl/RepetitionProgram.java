package manticore.dl;

import java.util.*;

public class RepetitionProgram extends HybridProgram {

	//ArrayList<HybridProgram> children;

	public RepetitionProgram( HybridProgram onlyChild ) {

		this.operator = new Operator("repeat", 1);

		this.children = new ArrayList<dLStructure>();
		this.children.add( onlyChild );
	}

	public HybridProgram getProgram() {
		return (HybridProgram)(children.get(0));
	}

// Substitution method
	public RepetitionProgram substituteConcreteValuation( Valuation substitution ) {
		return new RepetitionProgram( getProgram().substituteConcreteValuation( substitution ) );
	}

// Clone method
	public RepetitionProgram clone() {
		return new RepetitionProgram( getProgram().clone() );
	}

// String methods
	public String toKeYmaeraString() {
		return "(" + getProgram().toKeYmaeraString() + "*)";
	}

	public String toManticoreString() {
		return "(" + getProgram().toManticoreString() + "***)";
	}


// Assorted convenience functions
	public boolean isPurelyContinuous() {
		return false;
	}

	public boolean isPurelyDiscrete() {
		return getProgram().isPurelyDiscrete();
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
		return getProgram().isQuantifierFree();
	}

}
