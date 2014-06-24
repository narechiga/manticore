package manticore.dl;

public class HybridProgram extends dLStructure {



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


}
