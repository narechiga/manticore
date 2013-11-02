import java.util.LinkedList;


class Mode {

	protected String modeID;
	protected LinkedList<String> variables;
	protected LinkedList<String> resets;
	protected LinkedList<String> guards;
	protected LinkedList<String> odes;
	protected LinkedList<String> does;

	public Mode( String modeID ) {
		this.modeID = modeID;

		this.variables = new LinkedList<String>();
		this.resets = new LinkedList<String>();
		this.guards = new LinkedList<String>();
		this.odes = new LinkedList<String>();
		this.does = new LinkedList<String>();
	}

	public String getID () {
		return modeID;
	}

	public void addVariable( String newVariable ) {
		this.variables.addLast( newVariable );
	}
	public void addReset( String newReset ) {
		this.resets.addLast( newReset );
	}
	public void addGuard( String newGuard ) {
		this.guards.addLast( newGuard );
	}
	public void addODE( String newODE ) {
		this.odes.addLast( newODE );
	}
	public void addDOE( String newDOE ) {
		this.does.addLast( newDOE );
	}

	public LinkedList<String> getVariableList() {
		return variables;
	}
	public LinkedList<String> getResetList() {
		return resets;
	}
	public LinkedList<String> getGuardList() {
		return guards;
	}
	public LinkedList<String> getODEList() {
		return odes;
	}
	public LinkedList<String> getDOEList() {
		return does;
	}
}	

