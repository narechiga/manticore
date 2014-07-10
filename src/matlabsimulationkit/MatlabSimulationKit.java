package manticore.matlabsimulationkit;

import java.io.*;
import java.util.*;
import manticore.dl.*;

public class MatlabSimulationKit {

	public static Operator lt = new Operator("<");
	public static Operator le = new Operator("<=");
	public static Operator ball = new Operator("ball");

	public static void generateDynsysFile( HybridProgram program ) throws Exception {

		PrintWriter dynsysFile = new PrintWriter("manticore/matlabsimulationkit/dynsys.m");
		
		Date date = new Date();
		dynsysFile.println("% Automatically generated on " + date.toString() + "\n"  );	
		dynsysFile.println("function dxdt = dynsys(t,x)\n"							);
		dynsysFile.println(	"\tvaluation = '{';"								); 
		dynsysFile.println(	"\tfor i = 1:length(x)"								);
		dynsysFile.println(		"\t\tif (i == 1)"							);
		dynsysFile.println(			"\t\t\tvaluation = sprintf('%s x%i -> %g', valuation, i, x(i));");
		dynsysFile.println(		"\n\t\telse"								);
		dynsysFile.println(			"\t\t\tvaluation = sprintf('%s, x%i -> %g', valuation, i, x(i));");
		dynsysFile.println(		"\n\t\tend"								);
		dynsysFile.println(	"\tend"										);
		dynsysFile.println(	"\tvaluation = sprintf('%s }', valuation);"					);
		dynsysFile.print(	"\tvalstring = char(Manticore.runSimulate(sprintf('"				);

		try{ 
			String programString = program.toKeYmaeraString();
			programString = programString.replace("'", "''");
			dynsysFile.print(		programString							);
		} catch (Exception e) {
			System.err.println("Failed at printing out program!");
			dynsysFile.flush();
		}

		dynsysFile.print(	"%s', valuation)));\n"								);
		dynsysFile.println(	"\tcommas = strfind( valstring, ',');"						);
		dynsysFile.println(	"\tdxdt = [];"									);
		dynsysFile.println(	"\tfor i = 1:length(x)"								);
		dynsysFile.println(		"\t\tthisderivativestart = strfind(valstring, sprintf('_dx%idt_=', i ));");
		dynsysFile.println(		"\t\tinit = thisderivativestart + length(num2str(i)) + 7;"		);
		dynsysFile.println(		"\t\tdxdt(i) = str2num(valstring(init:commas(i)));"			);
		dynsysFile.println(	"\tend\n"									);
		dynsysFile.println(	"\tdxdt = transpose(dxdt);"							);

		dynsysFile.println("end");
		dynsysFile.close();

	}

	public static double getBallRadius( dLFormula annotation ) throws Exception { 
		// defaults to a radius of one if there is no ball constraint

		double radius = 1; // A reasonable default search radius,
				// and also the multiplicative identity--so use multiplication for recursion
		
		if ( isBall( annotation ) ) {
			radius =  ((Real)((ComparisonFormula)annotation).getRHS()).toDouble();
			
		} else if ( !(annotation.isPropositionalPrimitive() ) ) {
			radius = radius * getBallRadius( ((AndFormula)annotation).getLHS() ) 
							* getBallRadius( ((AndFormula)annotation).getRHS() );

		} else {
			radius = 1;
		}

		return radius;

	}

	public static ArrayList<RealVariable> getBallVariables( dLFormula annotation ) throws Exception { 

		ArrayList<RealVariable> varList = new ArrayList<RealVariable>();
		
		if ( isBall( annotation ) ) {

			// Figure out if each subterm is a RealVariable, otherwise throw an exception
			ArrayList<Term> termList =  ((ComparisonFormula)annotation).getLHS().getSubTerms();
			Iterator<Term> termIterator = termList.iterator();
			Term thisTerm;
			while ( termIterator.hasNext() ) {
				thisTerm = termIterator.next();
				if ( thisTerm instanceof RealVariable ) {
					varList.add( (RealVariable)thisTerm );
				} else {
					throw new Exception("Unsupported argument to ball function: "
							+ thisTerm.toKeYmaeraString() );
				}
			}
				
			
		} else if ( !(annotation.isPropositionalPrimitive() ) ) {
			varList.addAll( getBallVariables( ((AndFormula)annotation).getLHS() ) ); 
			varList.addAll( getBallVariables( ((AndFormula)annotation).getRHS() ) ); 

		} else {
			//do nothing, return empty list
		}

		return varList;

	}

	public static dLFormula getNonBallPortion( dLFormula annotation ) throws Exception {
		//dLFormula nonBallPortion;

		if ( !isPureConjunction( annotation ) ) {
			throw new Exception("MatlabSimulationKit currently only supports conjunctive annotations");
		}
		
		if ( isBall( annotation ) ) {
			return new TrueFormula();
		} else if ( !(containsBall( annotation )) ) {
			return annotation;
		} else if ( containsBall( ((AndFormula)annotation).getLHS() ) 
				&& !containsBall( ((AndFormula)annotation).getRHS() ) ) {
			return new AndFormula( getNonBallPortion( ((AndFormula)annotation).getLHS() ), 
						((AndFormula)annotation).getRHS() );
		} else if ( (!containsBall( ((AndFormula)annotation).getLHS())) 
				&& containsBall( ((AndFormula)annotation).getRHS()) ) {
			return new AndFormula( ((AndFormula)annotation).getLHS(), 
						getNonBallPortion( ((AndFormula)annotation).getRHS()) );
		} else {
			throw new Exception("Found exception while trying to getNonBallPortion of annotation: " 
				+ annotation.toKeYmaeraString() );
		}


	}

	public static boolean isPureConjunction( dLFormula annotation ) {
		
		if ( annotation.isPropositionalPrimitive() ) {
			return true; //dummy base case
		} else if ( annotation instanceof AndFormula
				&& isPureConjunction( ((AndFormula)annotation).getLHS() )
				&& isPureConjunction( ((AndFormula)annotation).getRHS() )
				) {
			return true; 
		} else {
			return false;
		}
	}

	public static boolean containsBall( dLFormula annotation ) {
		if ( isBall(annotation) ) {
			return true;
		} else if ( annotation.isPropositionalPrimitive() ) {
			return false;
		} else {
			return ( containsBall(((AndFormula)annotation).getLHS()) 
				|| containsBall(((AndFormula)annotation).getRHS()) );
		}
	}

	public static boolean isBall( dLFormula annotation ) {


		if ( (annotation instanceof ComparisonFormula)
			&& ( annotation.operator.equals( lt ) || annotation.operator.equals( le ) )
			&& ( ((ComparisonFormula)annotation).getLHS().operator.equals( ball ) )
			&& ( ((ComparisonFormula)annotation).getRHS() instanceof Real )
			 ) {

			System.out.println("Found ball with radius: " 
					+ ((ComparisonFormula)annotation).getRHS().toKeYmaeraString());
			return true;

		} else {
			return false;
		}
	}

	public static void generateProblemStatementFile( ArrayList<dLFormula> annotations,
							int templateDegree ) throws Exception {

		// First, sort out the annotations
		System.out.println("INFO: MatlabSimulationKit currently only supports conjunctive annotations");
		if ( annotations.size() != 1 ) {
			throw new Exception( 
				"MatlabSimulationKit (currently) only supports annotations lists of length 1, found length: "
				+ annotations.size() );
		}
		dLFormula annotation = annotations.get(0);

		System.out.println("TODO: I should support multiple continuous blocks and multiple annotations!");

		if ( !(containsBall(annotation)) ) {
			throw new Exception( "Annotation does not contain a ball to search for an invariant: " 
				+ annotation.toKeYmaeraString() );
		}

		double radius = getBallRadius( annotation );

		// Now write the file
		PrintWriter probFile = new PrintWriter("manticore/matlabsimulationkit/problemstatement.m");

		Date date = new Date();
		probFile.println("% Automatically generated on " + date.toString() + "\n" );	

		// Form state variable list
		ArrayList<RealVariable> varList = getBallVariables( annotation );
		String varListString = "";
		Iterator<RealVariable> varIterator = varList.iterator();
		while ( varIterator.hasNext() ) {
			varListString = varListString + " " + varIterator.next().toKeYmaeraString();
		}

		probFile.println("% Declare state variables");
		probFile.println("sdpvar" + varListString + ";");
		probFile.println("X = [" + varListString + " ];\n");
		probFile.println("lenX = length(X);");

		// for s-procedure
		probFile.println("% For s-procedure");
		probFile.println("sdpvar alph\n");

		probFile.println("% Generate monomials for the template");
		probFile.println("Z = monolist(X, " + templateDegree + " );");

		probFile.println("% Drop constant term");
		probFile.println("Z = Z(2:length(Z));\n");
		probFile.println("lenZ = length(Z);\n");

		probFile.println("% Matrix for template V = Z'*P*Z");
		probFile.println("P = sdpvar(length(Z));\n");

		probFile.println("search_radius = " + radius + ";\n");

		probFile.println("x0 = search_radius*0.5*ones(lenX,1);");

		// Settings we may want to change later
		probFile.print("\n\n\n");
		probFile.println("%%%%%%%%%%%");
		probFile.println("% Settings ");
		probFile.println("%%%%%%%%%%%");

		probFile.println("trace_length = 10;");
		probFile.println("step_size = .1;");
		probFile.println("single_step_flag = false;");
		probFile.println("fixed_step_flag = true;");
		probFile.println("initial_conditions_list = [];");
		probFile.println("create_plots = false;");

		probFile.print("\n\n\n");
		probFile.println("dynamics = @dynsys;");


		probFile.close();
		

	}


}

