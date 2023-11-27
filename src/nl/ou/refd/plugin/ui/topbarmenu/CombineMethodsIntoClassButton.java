package nl.ou.refd.plugin.ui.topbarmenu;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.ensoftcorp.open.commons.ui.utilities.DisplayUtils;
import com.ensoftcorp.open.commons.utilities.MappingUtils;

import nl.ou.refd.exceptions.NoActiveProjectException;
import nl.ou.refd.locations.generators.ProjectProgramComponentsGenerator;
import nl.ou.refd.locations.specifications.ClassSpecification;
import nl.ou.refd.locations.specifications.MethodSpecification;
import nl.ou.refd.locations.specifications.PackageSpecification;
import nl.ou.refd.locations.specifications.LocationSpecification.AccessModifier;
import nl.ou.refd.plugin.Controller;
import nl.ou.refd.plugin.ui.EclipseUtil;

/**
 * Class representing the menu button for the Combine Methods into Class
 * refactoring option. The presence of this button can be configured in
 * plugin.xml.
 */
public class CombineMethodsIntoClassButton extends MenuButtonHandler {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(ExecutionEvent event) {
		try {
			MappingUtils.mapWorkspace();
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		IProject currentProject;
		
		try {
			currentProject = EclipseUtil.currentProject();
		} catch (NoActiveProjectException e) {
			DisplayUtils.showMessage("Error: No active project");
			return;
		}
		
		String newClassString = DisplayUtils.promptString("New Class", "Please provide the visibility, name and package of the new class to combine method into (pacakge, visibility, classname)");
		String[] splitClassString = newClassString.split(",");
		
		ClassSpecification newClassLocation = new ClassSpecification(splitClassString[2], AccessModifier.fromString(splitClassString[1]), new PackageSpecification(splitClassString[0]));
		
		ElementListSelectionDialog destinationSelector = new ElementListSelectionDialog(HandlerUtil.getActiveShell(event), new LabelProvider());
		destinationSelector.setElements(new ProjectProgramComponentsGenerator(currentProject.getName()).stream().classes().methods().collect().toLocationSpecifications().toArray(new MethodSpecification[]{}));
		destinationSelector.setTitle("Select methods to move into new class");
		destinationSelector.setMultipleSelection(true);
		destinationSelector.open();
		
		Object[] result = destinationSelector.getResult();
		
		List<MethodSpecification> targets = Arrays.asList(Arrays.copyOf(result, result.length, MethodSpecification[].class));
		
		try {
			Controller.getController().combineMethodsIntoClass(targets, newClassLocation);
		} catch (NoActiveProjectException e) {
			DisplayUtils.showMessage("Error: No active project");
			return;
		}
	}

}
