package manticore.dl.syntax.syntax;

import java.util.*;

public class SequenceProgram extends HybridProgram {

// Constructors and field getters
	public SequenceProgram( HybridProgram firstProgram, HybridProgram secondProgram ) {

		this.operator = new Operator("sequence", true);

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

	public HybridProgram getFirstProgram() {
		return (HybridProgram)(children.get(0));
	}

	public HybridProgram getSecondProgram() {
		return (HybridProgram)(children.get(1));
	}
	
// Substition method
	public SequenceProgram substituteConcreteValuation( Valuation substitution ) {
		return new SequenceProgram( getLHS().substituteConcreteValuation( substitution ),
						getRHS().substituteConcreteValuation( substitution ) );
	}

// Clone method
	public SequenceProgram clone() {
		return new SequenceProgram( getLHS().clone(), getRHS().clone() );
	}

// String methods
	public String toKeYmaeraString() {
		return "( " + children.get(0).toKeYmaeraString() + " ; " + children.get(1).toKeYmaeraString() + " )";
	}

	public String toManticoreString() {
		return "( " + children.get(0).toManticoreString() + " ; " + children.get(1).toManticoreString() + " )";
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

        public Set<RealVariable> getDynamicVariables() {
                HashSet<RealVariable> dynamicVariables = new HashSet<RealVariable>();
                dynamicVariables.addAll( getLHS().getDynamicVariables() );
                dynamicVariables.addAll( getRHS().getDynamicVariables() );
                return dynamicVariables;
        }

}
