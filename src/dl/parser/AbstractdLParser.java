package manticore.dl.parser;

import manticore.dl.syntax.*;
import manticore.dl.semantics.*;

import java.util.*;

public class AbstractdLParser {

	public dLStructure parsedStructure;

	// for when "evaluate" is called from the command line
	public Valuation valuation;

	// for reading  a file with user annotations
	public ArrayList<dLFormula> annotations;
	// bounds on variables
	public dLFormula bounds;

	// Assorted fields of a KeYmaera input file
	public String declaredFunctions;
	public String declaredSchemaVariables;
	public String declaredRules;
	public ArrayList<RealVariable> declaredProgramVariables;
	public List<String> variableInitializations;

	// For EITool
	public boolean synthesis; //whether a synthesis or verification task is desired
	public ArrayList<RealVariable> statevariables;
	public ArrayList<RealVariable> eiparameters;
	public dLFormula envelope;
	public dLFormula invariant;
	public dLFormula robustparameters;
	public dLFormula domain;
	public dLFormula control;


}

