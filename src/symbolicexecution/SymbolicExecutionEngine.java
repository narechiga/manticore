
// Basically does nothing, for now

package manticore.symbolicexecution;

import manticore.dl.*;

public class SymbolicExecutionEngine {

	dLFormula initialSet;
	HybridProgram program;

	public SymbolicExecutionEngine( HybridProgram program, dLFormula initialSet ) {
		this.program = program;
		this.initialSet = initialSet;
	}

}
