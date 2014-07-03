package manticore.matlabsimulationkit;

import java.io.*;
import java.util.*;
import manticore.dl.*;

public class MatlabSimulationKit {


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

	public static void generateProblemStatementFile( ArrayList<RealVariable> varList, int templateDegree, double radius ) 
			throws Exception {
		
		PrintWriter probFile = new PrintWriter("manticore/matlabsimulationkit/problemstatement.m");

		Date date = new Date();
		probFile.println("% Automatically generated on " + date.toString() + "\n" );	

		// Form state variable list
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

