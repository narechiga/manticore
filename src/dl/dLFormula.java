package manticore.dl;

import java.io.*;
import java.util.*;

public abstract class dLFormula extends dLStructure {

	public boolean isFirstOrder() {
		return false;
	}

	public boolean isModal() {
		return false;
	}

	public boolean isPropositionalPrimitive() {
		return false;
	}

	public boolean isStatic() {
		return false;
	}

	public boolean isQuantifierFree() {
		return false;
	}

//	public abstract boolean isPurelyExistentiallyQuanfied() {
//	}
//
//	public abstract boolean isPurelyUniversallyQuantified() {
//	}


// All subclasses need to implement this guy
	public abstract dLFormula clone();

	public abstract dLFormula substituteConcreteValuation( Valuation substitution );

// Logic
	public abstract dLFormula negate();

	public boolean isClosedFormula() { // A formula is closed if it has no free variables
		if ( this.getFreeVariables().isEmpty() ) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<dLFormula> splitOnAnds () {
                ArrayList<dLFormula> subFormulas = new ArrayList<dLFormula>();

                if ( !( this instanceof AndFormula ) ) {                                                                 
                        subFormulas.add( this );                                                                        
                } else {
                        subFormulas.addAll( ((AndFormula)this ).getLHS().splitOnAnds() );                               
                        subFormulas.addAll( ((AndFormula)this ).getRHS().splitOnAnds()  );                               
                }

                return subFormulas;
        }           

        public dLFormula universalClosure() {
        	dLFormula uC;
        	if ( this.isClosedFormula() ) {
        		uC = this;
		} else {
			uC = this;
			Iterator<RealVariable> fVi = this.getFreeVariables().iterator();
			while ( fVi.hasNext() ) {
				uC = new ForAllFormula( fVi.next(), uC );
			}
		}
		return uC;
	}
	
        public dLFormula existentialClosure() {
        	dLFormula eC;
        	if ( this.isClosedFormula() ) {
        		eC = this;
		} else {
			eC = this;
			Iterator<RealVariable> fVi = this.getFreeVariables().iterator();
			while ( fVi.hasNext() ) {
				eC = new ExistsFormula( fVi.next(), eC );
			}
		}
		return eC;
	}





	//public Set<RealVariable> getBoundVariables() {
	//	return new HashSet();
	//}


//	public simplifyOrFalse() {
//		if ( !(this instanceof OrFormula) && this.isPropositionalPrimitive() ) {
//			return this.clone();	
//		} else if ( !(this instanceof OrFormula) ) {
//		}
//
//
//
//	}

}
