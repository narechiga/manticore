package manticore.dl.syntax;

import manticore.dl.semantics.*;


public abstract class DiscreteProgram extends HybridProgram {

	public boolean isPurelyDiscrete() {
		return true;
	}

	public boolean isPurelyContinuous() {
		return false;
	}

	public boolean isHybrid() {
		return false;
	}

	public boolean isProgramPrimitive() {
		// false in general, specific cases will override this
		return false;
	}

}
