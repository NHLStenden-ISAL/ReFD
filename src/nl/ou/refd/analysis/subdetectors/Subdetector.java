package nl.ou.refd.analysis.subdetectors;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.GraphQuery;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.streams.Stream;

/**
 * A class representing a function which maps a set of program locations to
 * another set of program locations. This mapping can be made using elements
 * from the nl.ou.refd.graph package. This means a subdetector minimally
 * uses elementary operations on the graph, or at the greatest other
 * subdetectors. These subdetectors can be chained in a
 * stream.
 */
public abstract class Subdetector {
	
	/**
	 * Applies the subdetector on an incoming set of program locations and maps
	 * this to another set of program locations.
	 * @param locations the incoming set of program locations
	 * @return a set of program locations the input maps to
	 */
	public abstract Set<ProgramLocation> applyOn(Set<ProgramLocation> locations);
	
	/**
	 * Convenience method to convert a array of streams into an array of GraphQueries.
	 * @param args array of streams
	 * @return array of graph queries
	 */
	protected static GraphQuery[] streamVarArgToGraphQueryArray(Stream[] args) {
		return Arrays.asList(args).stream()
		.map(i -> Graph.query(i.collect().locations())).collect(Collectors.toList())
		.toArray(new GraphQuery[]{});
	}
	
}
