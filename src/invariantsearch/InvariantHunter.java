package manticore.invariantsearch;

import proteus.logicsolvers.abstractions.*;
import proteus.dl.syntax.*;
import java.util.*;

public abstract class InvariantHunter {

	HybridProgram dynamics;

	ArrayList<RealVariable> stateList;

	dLFormula includedSet;
	dLFormula excludedSet;

	LogicSolverInterface logicSolver;


	public abstract Term generateCandidate();

}

