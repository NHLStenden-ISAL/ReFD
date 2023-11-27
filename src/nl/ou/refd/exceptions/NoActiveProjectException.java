package nl.ou.refd.exceptions;

/**
 * Class representing an exception which occurs when no project is open
 * to be used in the Eclipse editor.
 */
public class NoActiveProjectException extends Exception {

	private static final long serialVersionUID = -8707299408205082683L;

	/**
	 * Create the exception with a message to display.
	 * @param message a message to display
	 */
	public NoActiveProjectException(String message) {
		super(message);
	}
	
}
