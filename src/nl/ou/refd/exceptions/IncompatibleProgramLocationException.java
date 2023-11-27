package nl.ou.refd.exceptions;

/**
 * Class representing an exception which occurs when an unsupported program location
 * is being used for an operation.
 */
public class IncompatibleProgramLocationException extends RuntimeException {

	private static final long serialVersionUID = 1292060948103099107L;

	/**
	 * Create the exception with a message to display.
	 * @param message a message to display
	 */
	public IncompatibleProgramLocationException(String message) {
		super(message);
	}
}
