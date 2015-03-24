package manticore.matlabsamplingkit;

import java.io.*;
import java.util.*;
import java.lang.*;
import proteus.dl.parser.*;
import proteus.dl.syntax.*;
import proteus.dl.semantics.*;
//import manticore.matlabkit.*;

public class MatlabSamplingKit {

	public static void generateProblemFile( HybridProgram program, 
						int degree,
						ArrayList<Double> lowerBounds, 
						ArrayList<Double> upperBounds,
						double exclusionRadius,
						int precision,
						int sampleNumber,
						int maxOuterIterations ) throws Exception {

		PrintWriter problemFile = new PrintWriter("manticore/matlabsamplingkit/"
						+ "lyapunovsearch/examples/autogenerated.m");
		

		// Preamble to clear memory and add stuff to the path
		problemFile.println("clear all; close all;");
		problemFile.println("addpath(genpath('" + System.getenv("MATLABSAMPLINGKIT") + "'));");
		problemFile.println("addpath(genpath('" + System.getenv("MATLABKIT") + "'));");
		problemFile.println("javaaddpath " + System.getenv("MANTICORE") + ";");
		problemFile.println("import " + System.getenv("DL").replace("/", ".") + ".*;");
		problemFile.println("import " + System.getenv("SYMBOLICEXECUTION").replace("/", ".") + ".*;");
		problemFile.println("tic");

		// Declare state variables and state vector
		Set<RealVariable> programVariables = program.getFreeVariables();
		Iterator<RealVariable> variableIterator = programVariables.iterator();
		String variableDeclaration = "syms";
		String variableVector = "X = transpose([";
		RealVariable thisVariable;
		while ( variableIterator.hasNext() ) {
			thisVariable = variableIterator.next();

			variableDeclaration = variableDeclaration 
							+ " " + thisVariable.toMathematicaString();
			variableVector = variableVector
						+ " " + thisVariable.toMathematicaString();
		}
		variableDeclaration = variableDeclaration + ";";
		variableVector = variableVector + " ]);";
		problemFile.println( variableDeclaration );
		problemFile.println( variableVector );

		// interpretation
		// engine
		// hybrid program
		// call to engine with program
		String programString = program.toManticoreString();
		programString = programString.replace("'", "''");
		problemFile.println("programText = '" + programString + "';" );
 		problemFile.println("inputReader = java.io.StringReader( programText )");
 		problemFile.println("lexer = Lexer( inputReader );");
 		problemFile.println("parser = YYParser( lexer );");
 		problemFile.println("parser.parse();");
 		problemFile.println("program = parser.parsedStructure;\n"); 

		problemFile.println("interpretation = NativeInterpretation();");
		problemFile.println("engine = NativeExecutionEngine( interpretation );");
		problemFile.println("f = @(Y) engine.sampleVectorField( program, packValuation(X, Y) );\n");

		problemFile.println("degree = " + degree + ";\n");

		// Region of interest
		problemFile.println("% Region of interest");
		String xlower = "Xlower = transpose([";
		String xupper = "Xupper = transpose([";

		double lower; double upper;
		for ( int i = 0; i < programVariables.size(); i++ ) {
			try {
				lower = lowerBounds.get( i );

			} catch ( Exception e ) {
				lower = -1;
			}
			try {
				upper = upperBounds.get( i );
			} catch ( Exception e ) {
				upper = 1;
			}
			xlower = xlower + " " + lower; 
			xupper = xupper + " " + upper;
		}
		xlower = xlower + " ]);";
		xupper = xupper + " ]);";
		problemFile.println( xlower );
		problemFile.println( xupper );

		// Exclusion radius, precision
		problemFile.println( "exclusionRadius = " + exclusionRadius );
		problemFile.println( "precision = " + precision );

		problemFile.println( "samplenumber = " + sampleNumber );
		problemFile.println( "maxouteriterations = " + maxOuterIterations );


		problemFile.println("\n[success, lyapunov] =lyapunovgenerator( X, f, degree, Xlower, Xupper, exclusionRadius, precision, samplenumber, maxouteriterations)");
		problemFile.println("toc");

		problemFile.close();
	}



}

