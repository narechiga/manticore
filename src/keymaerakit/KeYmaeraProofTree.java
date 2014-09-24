package manticore.keymaerakit;

import hephaestos.dl.*;
import java.util.*;

public class KeYmaeraProofTree {

	KeYmaeraProofNode rootNode;
	KeYmaeraProofNode focusNode;
	//int nodeNumber; // which KeYmaera likes, for some reason
	//but apparently is perfectly capable of filling out itself
	String BRANCH = "branch";
	String RULE = "rule";
	String BUILTIN = "builtin";

	public KeYmaeraProofTree( Sequent sequent ) {
		this.sequent = sequent;
		this.nodeNumber = 0;

		rootNode = new KeYmaeraProofNode();
		rootNode.parent = null;
		rootNode.children = new ArrayList<KeYmaeraProofNode>();
		rootNode.kind = BRANCH;
		rootNode.name = "root";
		root.extraText = "";

		focusNode = rootNode;
	}

	public class KeYmaeraProofNode {

		public  Sequent sequent;
		public  KeYmaeraProofNode parent;
		public  String kind;
		public  String name;
		public  String extraText;
		public  List<KeYmaeraProofNode> children;

		//public static enum NodeKind {
		//	BRANCH, RULE, BUILTIN
		//}
		
		public void addChild( KeYmaeraProofNode child ) {
			child.parent = this;
			this.children.add( child );
			this.sequent = null; // you're not a leaf anymore, forget your sequent to save memory
		}

	}
//******************************************************************************
//******************************************************************************
//******************************************************************************
// forward invariant cut
	public void forwardInvariantCut( dLFormula forwardInvariant, int formulaNumber ) 
		throws ManticoreKeYmaeraKitException {

		// Preliminary stuff, extract relevant bits, negate invariant etc
		Sequent workingSequent = focusNode.getSequent();
		if ( !(workingSequent.getFormula( formulaNumber ) instanceof BoxModalityFormula ) ) {
			throw new ManticoreKeYmaeraKitException("Selected formula is not a box modality formula");
		}
		dLFormula workingFormula = (BoxModalityFormula)(workingSequent.getFormula( formulaNumber ));
		dLFormula postCondition = workingFormula.getFormula();
		HybridProgram thisProgram = workingFormula.getProgram();

		dLFormula negatedInvariant = forwardInvariant.negate();

		/****************************************************************************/
		// fInvCut root node
		KeYmaeraProofNode newNode = new KeYmaeraProofNode();
		fInvNode.kind = RULE;
		fInvNode.name = "Forward Invariant Cut";
		fInvNode.extraText = "(formula \"" + formulaNumber + "\")";
		fInvNode.extraText = fInvNode.extraText + "(inst \"forwardinvariant="
					+ forwardInvariant.toKeYmaeraString()
					+ "\")";
		fInvNode.children = new ArrayList<KeYmaeraProofNode>();
		fInvNode.sequent = focusNode.sequent;
		focusNode.addChild( newNode );
		focusNode = fInvNode;

		/****************************************************************************/
		// Invariant holds branch
		// dLUniversalClosure is hard to implement in a way that ensures we will assign the
		// same Skolem names as KeYmaera, so instead we will delete all formulas that contain
		// the dynamic variables of the program as free variables
		KeYmaeraProofNode invarianceBranch = new KeYmaeraProofNode();
		invarianceBranch.kind = BRANCH;
		invarianceBranch.name = " Invariant holds";
		invarianceBranch.extraText = "";
		invarianceBranch.children = new ArrayList<KeYmaeraProofNode>();

		/***/
		invarianceBranch.sequent = workingSequent.clone();
		ArrayList<int> indicesToHide = invarianceBranch.sequent.skolemHideAround( formulaNumber );
		fInvNode.addChild( invarianceBranch );

		// Now sync the KeYmaera representation by hiding the same formulas
		focusNode = invarianceBranch;
		hideFormulas( indicesToHide, workingSequent.getAntecedentSize() );

		/****************************************************************************/
		// Invariant provides safety branch
		// See the above note on dLUniversalClosure
		KeYmaeraProofNode safetyBranch = new KeYmaeraProofNode();
		safetyBranch.kind = BRANCH;
		safetyBranch.name = " Invariant imples safety";
		safetyBranch.extraText = "";
		safetyBranch.children = new ArrayList<KeYmaeraProofNode>();

		/***/
		safetyBranch.sequent = workingSequent.clone();
		ArrayList<int> indicesToHide = safetyBranch.sequent.skolemHideAround( formulaNumber );
		fInvNode.addChild( invarianceBranch );

		// Now sync the KeYmaera representation by hiding the same formulas
		focusNode = safetyBranch;
		hideFormulas( indicesToHide, workingSequent.getAntecedentSize() );
		
		
		/****************************************************************************/
		// Remaining behaviors are safe branch
		KeYmaeraProofNode remainingBranch = new KeYmaeraProofNode();
		remainingBranch.kind = BRANCH;
		remainingBranch.name = " Remaining states are safe";
		remainingBranch.extraText = "";
		remainingBranch.children = new ArrayList<KeYmaeraProofNode>();

		/***/ /* build the new sequent */
		remainingBranch.sequent = workingSequent.clone();
		HybridProgram modifiedProgram = new SequenceProgram( thisProgram, new TestProgram( negatedInvariant ) );
		BoxModalityFormula newFormula = new BoxModalityFormula( modifiedProgram, postCondition );
		remainingBranch.sequent.replace( formulaNumber, newFormula );
		remainingBranch.sequent.addToAntecedent( negatedInvariant );
		/***/

		// Post-processing
		fInvNode.addChild( remainingBranch );
		focusNode = remainingBranch;
		updateSimplification( formulaNumber );
		focusNode = remainingBranch;
		simplifyFormula( 1 );
	}

//******************************************************************************
//******************************************************************************
//******************************************************************************

// Some non-atomic manipulations
	public void hideFormulas( ArrayList<int> formulaIndices, int antecedentLength ) {
		// Meant to be called after skolemHideAround() as a workaround for dlUniversalClosure
		Iterator<int> indexIterator = formulaIndices.iterator();

		int currentAntecedentLength = antecedentLength;
		int thisIndex;
		while( indexIterator.hasNext() ) {
			thisIndex = indexIterator.next();
			if ( thisIndex <= currentAntecedentLength ) {
				//hide left
				KeYmaeraProofNode newNode = new KeYmaeraProofNode();
				newNode.kind = RULE;
				newNode.name = "hide_left";
				newNode.extraText = "(formula \"" + formulaNumber + "\")";
				newNode.children = new ArrayList<KeYmaeraProofNode>();
				newNode.sequent = focusNode.sequent;

				focusNode.addChild( newNode );
				focusNode = newNode;
				currentAntecedentLength = currentAntecedentLength - 1;
			} else {
				//hide right
				KeYmaeraProofNode newNode = new KeYmaeraProofNode();
				newNode.kind = RULE;
				newNode.name = "hide_right";
				newNode.extraText = "(formula \"" + formulaNumber + "\")";
				newNode.children = new ArrayList<KeYmaeraProofNode>();
				newNode.sequent = focusNode.sequent;

				focusNode.addChild( newNode );
				focusNode = newNode;
			}
		}

		
	}

// Some common atomic manipulations
	public void simplifyFormula( int formulaNumber ) {
		// Does nothing to the internal representation, just tells KeYmaera to
		// simplify its representation
		KeYmaeraProofNode newNode = new KeYmaeraProofNode();
		newNode.kind = RULE;
		newNode.name = "simplify_form";
		newNode.extraText = "(formula \"" + formulaNumber + "\")";
		newNode.children = new ArrayList<KeYmaeraProofNode>();
		newNode.sequent = focusNode.sequent;

		focusNode.addChild( newNode );
		focusNode = newNode;
	}

	public void updateSimplification( int formulaNumber ) {
		KeYmaeraProofNode newNode = new KeYmaeraProofNode();
		newNode.kind = BUILTIN;
		newNode.name = "Update Simplification";
		newNode.extraText = "(formula \"" + formulaNumber + "\")";
		newNode.children = new ArrayList<KeYmaeraProofNode>();
		newNode.sequent = focusNode.sequent;

		focusNode.addChild( newNode );
		focusNode = newNode;
	}

	public void modalitySplitRight( int formulaNumber ) {
		KeYmaeraProofNode newNode = new KeYmaeraProofNode();
		newNode.kind = RULE;
		newNode.name = "modality_split_right";
		newNode.extraText = "(formula \""+ formulaNumber + "\")";
		newNode.children = new ArrayList<KeYmaeraProofNode>();
		newNode.sequent = focusNode.sequent;

		focusNode.addChild( newNode );
		focusNode = newNode;
	}

	public void implicationRight( int formulaNumber ) 
		throws ManticoreKeYmaeraKitException {

		KeYmaeraProofNode newNode = new KeYmaeraProofNode();
		newNode.kind = RULE;
		newNode.name = "imp_right";
		newNode.extraText = "(formula \""+ formulaNumber + "\")";
		newNode.children = new ArrayList<KeYmaeraProofNode>();

		newNode.sequent = focusNode.sequent;
		ImpliesFormula thisImplication = null;
		try{
			thisImplication = (ImpliesFormula)(
							thisNode.sequent.getFormula( formulaNumber ));

		} catch ( Exception e ) {
			e.KeYmaera();
			throw new ManticoreKeYmaeraKitException("Failed to find implication at the specified position");
		}
		thisNode.sequent.removeFormula( formulaNumber );
		thisNode.sequent.addToAntecedent( thisImplication.getAntecedent() );
		thisNode.sequent.addToSuccedent( thisImplication.getSuccedent() );

		focusNode.addChild( newNode );
		focusNode = newNode;
	}

	public void eliminateVariableDeclarations( int formulaNumber ) {
		KeYmaeraProofNode newNode = new KeYmaeraProofNode();
		newNode.kind = RULE;
		newNode.name = "eliminate_variable_declarations";
		newNode.extraText = "(formula \"" + formulaNumber + "\")";
		newNode.children = new ArrayList<KeYmaeraProofNode>();
		newNode.sequent = focusNode.sequent;

		focusNode.addChild( newNode );
		focusNode = newNode;
	}

	public void eliminateUniversalQuantifiers() {
		KeYmaeraProofNode newNode = new KeYmaeraProofNode();
		newNode.kind = BUILTIN;
		newNode.name = "Eliminate Universal Quantifiers";
		newNode.extraText = "(quantifierEliminator \"Mathematica\")";
		newNode.children = null; // because hopefully proof is done
		newNode.sequent = null; // because hopefully the proof is done after QElim

		focusNode.addChild( newNode );
		focusNode = newNode;
	}


// This is the function which generates the partial proof file for KeYmaera
	public String toString() {
		return null;
	}

}
