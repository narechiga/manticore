package manticore.dl;

import java.io.*;

public class dLFormula extends dLStructure {

	public boolean isFirstOrder() {
		return false;
	}

	public boolean isModal() {
		return false;
	}

	public boolean isPropositionalPrimitive() {
		return false;
	}

	public boolean isStatic() {
		return false;
	}

	public boolean isQuantifierFree() {
		return false;
	}

	public static dLFormula parseFormula( String formulaString ) throws Exception {
		// returns the dLFormula that exists in the string
		StringReader myReader = new StringReader( formulaString );
		Lexer myLexer = new Lexer( myReader );
		YYParser myParser = new YYParser( myLexer );

		myParser.parse();

		if ( myParser.parsedStructure instanceof dLFormula ) {
			return (dLFormula)(myParser.parsedStructure);

		} else {
			throw new Exception("Input string does not represent a dLFormula");
		}
	}


	//public dLFormula autoCast() {


}
