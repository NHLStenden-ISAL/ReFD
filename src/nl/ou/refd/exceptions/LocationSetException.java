package nl.ou.refd.exceptions;

/**
 * Class representing an general exception that has to do with sets of program locations.
 */
public class LocationSetException extends RuntimeException {

	private static final long serialVersionUID = -2588900476747874649L;

	/**
	 * Create the exception with a message to display.
	 * @param message a message to display
	 */
	public LocationSetException(String message) {
		super(message);
	}
}
