abstract class CandidateGenerator{

	protected Mode myMode;

	public abstract String generateCandidate();

	public String getMode() {
		return myMode.getID();
	}

}
