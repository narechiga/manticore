import manticore.dl.*;
import java.util.*;

class dLParser {

	dLStructure parsedStructure;

	// for when "evaluate" is called from the command line
	Valuation valuation;

	// for reading  a file with user annotations
	ArrayList<dLFormula> annotations;

	// Assorted fields of an input file
	public String declaredFunctions;
	public String declaredSchemaVariables;
	public String declaredRules;
	public ArrayList<RealVariable> declaredProgramVariables;
	public List<String> variableInitializations;


}

