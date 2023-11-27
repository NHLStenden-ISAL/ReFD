package nl.ou.refd.plugin;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

import nl.ou.refd.locations.collections.LabeledLocationSet.SourceCorrespondence;

/**
 * Central class to place source markers in a project. These markers show up
 * in the problems section of the Eclipse environment.
 */
class MarkerCreator {
	
	private final static String MARKER_TYPE = "nl.ou.refd.markers.dangerMarker";
	
	private final IProject project;
	
	/**
	 * Central class to place source markers in a project. These markers show up
	 * in the problems section of the Eclipse environment.
	 * @param project the project to place markers in
	 */
	public MarkerCreator(IProject project) {
		super();
		this.project = project;
	}

	/**
	 * Place a default marker on a location in the project. Default markers show up as
	 * information markers, meaning they are neither an error, nor a warning.
	 * @param label the label (description) for the marker
	 * @param location the location within the project
	 * @param sc the source file and location in this file. If sc is null, the entire
	 * project is chosen as correspondence.
	 */
	public void defaultMarker(String label, String location, SourceCorrespondence sc) {
		try {
			List<String> splitLabel = Arrays.asList(label.split("\\."));
			
			IMarker marker;
			
			if (sc != null) {
				marker = project.getFile(new Path(sc.filePath)).createMarker(MARKER_TYPE);
				marker.setAttribute(IMarker.CHAR_START, sc.offset);
				marker.setAttribute(IMarker.CHAR_END, sc.offset + sc.length);
			}
			else {
				marker = project.createMarker(MARKER_TYPE);
			}
			
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
			marker.setAttribute(IMarker.MESSAGE, splitLabel.get(splitLabel.size()-1).replace("$", " - "));
			marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
			marker.setAttribute(IMarker.LOCATION, location);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
