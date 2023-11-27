package nl.ou.refd.plugin.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import nl.ou.refd.exceptions.NoActiveProjectException;

/**
 * Class containing utility methods to work with Eclipse.
 */
public class EclipseUtil {
	
	/**
	 * Gets the current open project in the Eclipse environment.
	 * @return an object of type IProject representing the open project
	 * @throws NoActiveProjectException if there is no project currently open
	 */
	public static IProject currentProject() throws NoActiveProjectException {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		
		IWorkbenchPage activePage = window.getActivePage();
		
		IEditorPart activeEditor = activePage.getActiveEditor();
		
		if (activeEditor != null) {
			IEditorInput input = activeEditor.getEditorInput();
			
			IProject project = input.getAdapter(IProject.class);
			if (project == null) {
				IResource resource = input.getAdapter(IResource.class);
				if (resource != null) {
					project = resource.getProject();
				}
			}
			
			return project;
		}
		
		throw new NoActiveProjectException("No active project to get the name of");
	}
	
}
