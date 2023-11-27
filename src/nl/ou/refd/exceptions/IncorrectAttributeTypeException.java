package nl.ou.refd.exceptions;

/**
 * Class representing an exception which occurs when the cast of an attribute
 * in the graph is incorrect.
 */
public class IncorrectAttributeTypeException extends RuntimeException {

	private static final long serialVersionUID = 789120610747614736L;

	/**
	 * Create the exception with a message to display.
	 * @param message a message to display
	 */
	public IncorrectAttributeTypeException(String message) {
		super(message);
	}
	
}
