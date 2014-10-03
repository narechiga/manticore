package manticore.invariantgeneration;

import proteus.dl.syntax.*;
import java.util.*;

public abstract class CandidateInvariantGenerator {

	HybridProgram dynamics;

	ArrayList<RealVariable> stateList;

	dLFormula includedSet;
	dLFormula excludedSet;
	dLFormula parentSet;


	public abstract Term generateCandidate();

}

