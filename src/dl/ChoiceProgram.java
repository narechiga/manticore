package manticore.dl;

import java.util.*;

public class ChoiceProgram extends HybridProgram {

// Constructors and field getters
	public ChoiceProgram( HybridProgram leftProgram, HybridProgram rightProgram ) {

		this.operator = new Operator("choice", true);

		this.children = new ArrayList<dLStructure>();
		this.children.add( leftProgram );
		this.children.add( rightProgram );
	}

	public HybridProgram getLHS() {
		return (HybridProgram)(children.get(0));
	}

	public HybridProgram getRHS() {
		return (HybridProgram)(children.get(1));
	}
	
// Substitution method
	public ChoiceProgram substituteConcreteValuation( Valuation substitution ) {
		return new ChoiceProgram(  getLHS().substituteConcreteValuation( substitution ),
						getRHS().substituteConcreteValuation( substitution ) ) ;
	}

// Clone method
	public ChoiceProgram clone() {
		return new ChoiceProgram( getLHS().clone(), getRHS().clone());
	}


// String methods
	public String toKeYmaeraString() {
		return "( " + children.get(0).toKeYmaeraString() + " ++ " + children.get(1).toKeYmaeraString() + " )";
	}

	public String toManticoreString() {
		return "( " + children.get(0).toManticoreString() + " ++ " + children.get(1).toManticoreString() + " )";
	}

// Assorted convenience functions
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

	public Set<RealVariable> getBoundVariables() {
		HashSet<RealVariable> boundVariables = new HashSet<RealVariable>();
		boundVariables.addAll( getLHS().getBoundVariables() );
		boundVariables.addAll( getRHS().getBoundVariables() );
		return boundVariables;
	}

	public Set<RealVariable> getFreeVariables() {
		HashSet<RealVariable> freeVariables = new HashSet<RealVariable>();
		freeVariables.addAll( getLHS().getFreeVariables() );
		freeVariables.addAll( getRHS().getFreeVariables() );
		return freeVariables;
	}

}
