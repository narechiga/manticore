package manticore.dl;

import java.util.*;

public interface Interpretation {

	public Double evaluateTerm( Term thisTerm, HashMap<RealVariable, Real> valuation ) throws Exception;
	public Boolean evaluateFormula( dLFormula thisFormula, HashMap<RealVariable, Real> valuation ) throws Exception;

}
