import manticore.dl.*;
import java.util.*;

class dLParser {

	dLStructure parsedStructure;

	// for when "evaluate" is called from the command line
	Valuation valuation;

	// for reading  a file with user annotations
	List<dLStructure> annotations;

	// Assorted fields of an input file
	public String declaredFunctions;
	public String declaredSchemaVariables;
	public String declaredRules;
	public List<String> declaredProgramVariables;
	public List<String> variableInitializations;


}

