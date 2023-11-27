package nl.ou.refd.locations.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import nl.ou.refd.locations.graph.GraphQuery;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;

/**
 * Class representing a set of locations which is labeled with a string. This is
 * a special set and cannot be streamed.
 */
public class LabeledLocationSet {
	
	/**
	 * Class representing a correspondence to source code. The correspondence
	 * is made up of a file path, an offset in the file and the length of the
	 * correspondence.
	 */
	public class SourceCorrespondence {
		public final String filePath;
		public final int offset;
		public final int length;
		
		/**
		 * Creates the SourceCorrespondence.
		 * @param filePath the path of the source
		 * @param offset the offset to the specified part of the source
		 * @param length the length of the correspondence
		 */
		public SourceCorrespondence(String filePath, int offset, int length) {
			this.filePath = filePath;
			this.offset = offset;
			this.length = length;
		}
	}
	
	/**
	 * Interface representing a method elements in the labeled location collection can be
	 * marked with. This defers the actual marking, thus probably the UI logic, to the calling
	 * party.
	 */
	public interface MarkFunction {
		/**
		 * Marks the location detailed by the source correspondence with a label, a name.
		 * @param label the label to mark with
		 * @param name the name of the marked location
		 * @param sc the source correspondence of the marked location
		 */
		void mark(String label, String name, SourceCorrespondence sc);
	}
	
	private String label;
	private Set<ProgramLocation> locations;
	
	/**
	 * Creates the labeled location set with a set of locations and a string label.
	 * @param locations set of locations
	 * @param label string label
	 */
	public LabeledLocationSet(Set<ProgramLocation> locations, String label) {
		this.locations = new HashSet<ProgramLocation> (locations);
		this.label = label;
	}
	
	/**
	 * Returns the size of the set.
	 * @return size of the set
	 */
	public long size() {
		return this.locations.size();
	}

	/**
	 * Returns the label as string.
	 * @return label as string
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Sets the label by way of providing a class. The class's
	 * canonical name is used as label.
	 * @param label a class whose canonical name will be used as label
	 */
	public void setLabel(Class<?> label) {
		this.label = label.getCanonicalName();
	}
	
	/**
	 * Checks if the set is labeled with the canonical name of the given
	 * class.
	 * @param label the class to check the label for
	 * @return true if the set has the same label as the coninical name of
	 * the class, false otherwise
	 */
	public boolean hasLabel(Class<?> label) {
		return this.label.equals(label.getCanonicalName());
	}
	
	/**
	 * Marks each element from the set with a provided MarkFunction. The source correspondence
	 * the MarkFunction expects can be null when no source correspondence is found.
	 * @param f the MarkFunction to use for marking
	 */
	public void mark(MarkFunction f) {
		this.locations.forEach(node -> {
			String name = node.<String>getAttribute(Tags.Attributes.NAME);
			com.ensoftcorp.atlas.core.index.common.SourceCorrespondence asc = (com.ensoftcorp.atlas.core.index.common.SourceCorrespondence) node.getAttribute(Tags.Attributes.SOURCE_CORRESPONDENCE);
			
			SourceCorrespondence sc = null;
			
			if ((asc != null) && (asc.sourceFile != null)) {
				List<String> pathSegments = new ArrayList<String>(Arrays.asList(asc.sourceFile.getFullPath().segments()));
				pathSegments.remove(0);
				sc = new SourceCorrespondence(String.join("/", pathSegments), asc.offset, asc.length);
			}
			
			f.mark(this.label, name, sc);
		});
	}
}
