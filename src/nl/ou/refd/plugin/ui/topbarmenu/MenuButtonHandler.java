package nl.ou.refd.plugin.ui.topbarmenu;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * Abstract class which represents a handler for an Eclipse menu button
 */
public abstract class MenuButtonHandler extends AbstractHandler {

	/**
	 * Method which has to be implemented to handle menu button presses
	 * @param event represents extra Eclipse-related information about the button press event
	 */
	public abstract void handle(ExecutionEvent event);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Object execute(ExecutionEvent event) throws ExecutionException {
		handle(event);
		return null;
	}

}
