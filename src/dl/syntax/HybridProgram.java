package manticore.dl.syntax.syntax;

public abstract class HybridProgram extends dLStructure {

// Specific classes override these.
	public boolean isPurelyContinuous() {
		return false;
	}

	public boolean isPurelyDiscrete() {
		return false;
	}

	public boolean isHybrid() {
		return false;
	}

	public boolean isProgramPrimitive() {
		return false;
	}

	public boolean isQuantifierFree() {
		return false;
	}

	public abstract HybridProgram substituteConcreteValuation( Valuation substitution );

	public abstract HybridProgram clone();


}
