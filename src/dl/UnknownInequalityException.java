package manticore.dl;

public class UnknownInequalityException extends RuntimeException {
	// Extends RuntimeException because if you wrote something with a bad
	// operator, we really should just crash

	public UnknownInequalityException( String message ) {
		super( message );
	}
}
