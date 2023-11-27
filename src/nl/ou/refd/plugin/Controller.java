package nl.ou.refd.plugin;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import nl.ou.refd.analysis.DangerAnalyser;
import nl.ou.refd.analysis.refactorings.CombineMethodsIntoClass;
import nl.ou.refd.analysis.refactorings.PullUpMethod;
import nl.ou.refd.exceptions.NoActiveProjectException;
import nl.ou.refd.locations.specifications.ClassSpecification;
import nl.ou.refd.locations.specifications.MethodSpecification;
import nl.ou.refd.plugin.ui.EclipseUtil;

/**
 * The controller class which is the central point of the plugin. It is also the
 * activator class (Eclipse-specific) which controls the plug-in life cycle. The
 * controller contains a singleton to access its functionality.
 */
public class Controller extends AbstractUIPlugin {

	private static Controller controller;
	
	/**
	 * Singleton of controller.
	 * @return
	 */
	public static Controller getController() {
		return controller;
	}

	/**
	 * Standard method to start Eclipse plugin. This gets called before
	 * internal methods of the plugin, so it initialized the singleton
	 * of controller as well.
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		controller = this;
	}

	/**
	 * Standard method to stop Eclipse plugin.
	 */
	public void stop(BundleContext context) throws Exception {
		controller = null;
		super.stop(context);
	}
	
	/**
	 * Start a refactoring analysis for the Pull Up Method refactoring.
	 * This method starts a new thread to not block the program during analysis.
	 * @param target the method to pull up
	 * @param destination the class to pull target up to
	 * @throws NoActiveProjectException 
	 */
	public void pullUpMethod(MethodSpecification target, ClassSpecification destination) throws NoActiveProjectException {
		final IProject project = EclipseUtil.currentProject();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				PullUpMethod refactoring = new PullUpMethod(target, destination);
				new DangerAnalyser(refactoring).analyse().forEach(danger -> danger.mark(new MarkerCreator(project)::defaultMarker));
			}
		}).start();
	}
	
	/**
	 * Start a refactoring analysis for the Combine Methods into Class refactoring.
	 * This method starts a new thread to not block the program during analysis.
	 * @param targets the methods to move to the new class
	 * @param destination the new class to combine the targets into
	 * @throws NoActiveProjectException 
	 */
	public void combineMethodsIntoClass(List<MethodSpecification> targets, ClassSpecification destination) throws NoActiveProjectException {
		final IProject project = EclipseUtil.currentProject();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				CombineMethodsIntoClass refactoring = new CombineMethodsIntoClass(destination, targets);
				new DangerAnalyser(refactoring).analyse().forEach(danger -> danger.mark(new MarkerCreator(project)::defaultMarker));
			}
		}).start();
	}
}