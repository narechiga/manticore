package manticore.dl;

import java.util.*;

public class dLParser {

	public dLStructure parsedStructure;

	// for when "evaluate" is called from the command line
	public Valuation valuation;

	// for reading  a file with user annotations
	public ArrayList<dLFormula> annotations;

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
	public ConcreteAssignmentProgram control;


}

