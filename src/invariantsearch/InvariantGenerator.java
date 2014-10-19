package manticore.invariantsearch;

import proteus.logicsolvers.abstractions.*;
import proteus.dl.syntax.*;
import java.util.*;

public abstract class InvariantGenerator {

	LogicSolverInterface logicSolver;
	Real resolution;


	public abstract dLFormula computeInvariant ( HybridProgram dynamics,
						dLFormula includedSet,
						dLFormula excludedSet ) throws InvariantNotFoundException;

}

