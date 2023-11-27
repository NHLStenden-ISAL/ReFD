package nl.ou.refd.analysis.subdetectors;

import java.util.Set;

import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;

/**
 * Class containing all subdetectors that work on sets of program component locations.
 */
public class ProgramComponentSubdetectors {
	private ProgramComponentSubdetectors(){}
	
	/**
	 * Filters the program components and keeps only those which are classes.
	 * @return program components which are classes
	 */
	public static class Classes extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).locations(Tags.ProgramLocation.CLASS).locations();
		}
	}
}
