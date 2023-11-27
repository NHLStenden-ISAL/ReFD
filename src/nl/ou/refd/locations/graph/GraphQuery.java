package nl.ou.refd.locations.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ensoftcorp.atlas.core.db.graph.Graph;
import com.ensoftcorp.atlas.core.db.set.AtlasHashSet;
import com.ensoftcorp.atlas.core.db.set.AtlasSet;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.query.Query;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.script.CommonQueries;

import nl.ou.refd.exceptions.LocationSetException;

/**
 * Class representing a query of the graph.
 */
public class GraphQuery {
	
	private Q q;
	
	/**
	 * Creates an empty graph query.
	 */
	GraphQuery() {
		this.q = Query.empty();
	}
	
	/**
	 * Creates a graph query from an Atlas query.
	 * @param q the Atlas query
	 */
	GraphQuery(Q q) {
		this.q = q;
	}
	
	/**
	 * Creates a graph query from a set of program locations.
	 * @param locations a set of program locations
	 */
	GraphQuery(Set<ProgramLocation> locations) {
		this(Common.toQ(programLocationSetToAtlasHashSet(locations)));
	}
	
	/**
	 * Converts a set of program locations to an AtlasHashSet of the Atlas graph elements contained within the program locations.
	 * @param locations the program locations to get the Atlas elements from to put into the AtlasHashSet
	 * @return an AtlasHashSet containing the Atlas graph elements contained within the supplied program locations
	 */
	private static AtlasSet<com.ensoftcorp.atlas.core.db.graph.GraphElement> programLocationSetToAtlasHashSet(Set<ProgramLocation> locations) {
		AtlasSet<com.ensoftcorp.atlas.core.db.graph.GraphElement> atlasNodes = new AtlasHashSet<com.ensoftcorp.atlas.core.db.graph.GraphElement>();
		locations.forEach(node -> atlasNodes.add(node.getAtlasElement()));
		return atlasNodes;
	}
	
	/**
	 * Gets the set of program locations resulting from the query.
	 * @return the set of program locations resulting from the query
	 */
	public Set<ProgramLocation> locations() {
		return this.atlasSetToList(this.eval().nodes())
				.stream()
				.map(node -> new ProgramLocation(node))
				.collect(Collectors.toSet());
	}
	
	/**
	 * Checks if the query results in a single program location and returns this location.
	 * @return the single program location
	 * @throws LocationSetException if the query results in more than one program location
	 */
	public ProgramLocation singleLocation() {
		Set<ProgramLocation> nodes = this.locations();
		if (nodes.size() != 1) {
			throw new LocationSetException("GraphQuery did not result in exactly one program location");
		}
		return nodes.iterator().next();
	}
	
	/**
	 * Returns a set of all relations in the current query space.
	 * @return a set of all relations in the current query space
	 */
	public Set<Relation> relations() {
		return this.atlasSetToList(this.eval().edges())
				.stream()
				.map(edge -> new Relation(edge))
				.collect(Collectors.toSet());
	}
	
	/**
	 * Converts an AtlasSet to a List.
	 * @param <T> the specific Atlas GraphElement type to type the list with
	 * @param set the AtlasSet to convert
	 * @return a list containing elements of the generic type T
	 */
	private <T extends com.ensoftcorp.atlas.core.db.graph.GraphElement> List<T> atlasSetToList(AtlasSet<T> set) {
		List<T> returnList = new ArrayList<T>();
		set.forEach(node -> returnList.add(node));
		return returnList;
	}

	/**
	 * From relations, selects the subgraph reachable from the program locations in this using reverse transitive traversal.
	 * The program locations are only included if they are themselves reachable.
	 * @param relations the relations to select the program locations from
	 * @return the subgraph reachable from the given program locations using reverse transitive traversal
	 */
	public GraphQuery ancestorsOn(GraphQuery relations) {
		return this.predecessorsOn(relations).reverseOn(relations);
	}

	/**
	 * For each program location in the query space, select the program location which are successors along
	 * Tags.Relation.Contains, not including the origin.
	 * @return the resulting query
	 */
	public GraphQuery children() {
		return new GraphQuery(this.q.children());
	}

	/**
	 * Select the program locations which are descendants along Tags.Relation.Contains, including the origin.
	 * @return the resulting query
	 */
	public GraphQuery contained() {
		return new GraphQuery(this.q.contained());
	}

	/**
	 * Select the program locations which are ancestors along Tags.Relation.Contains, including the origin.
	 * @return the resulting query
	 */
	public GraphQuery containers() {
		return new GraphQuery(this.q.containers());
	}
	
	/**
	 * From within relations, selects the descendants reachable from the program locations in this along a path length of 1.
	 * (An origin node is only included if it itself is reachable from another origin node)
	 * @param relations the relations to select the program locations from
	 * @return within relations, selects the subgraph reachable from the program locations in this using forward transitive traversal.
	 */
	public GraphQuery descendantsOn(GraphQuery relations) {
		return this.successorsOn(relations).forwardOn(relations);
	}

	/**
	 * Remove elements from the given queries from the current query.
	 * @param expr the queries containing elements to remove
	 * @return the resulting query
	 */
	public GraphQuery difference(GraphQuery... expr) {
		return new GraphQuery(this.q.difference(gqArrToQArr(expr)));
	}
	
	/**
	 * Converts an array of tags to their string representation.
	 * @param tags the array of tags to convert
	 * @return an array containing the string representation of the tags
	 */
	private String[] convertEdgeTagsArr(Tags.Relation[] tags) {
		String[] rArr = new String[tags.length];
		for (int i = 0; i < tags.length; i++) {
			rArr[i] = tags[i].toString();
		}
		return rArr;
	}

	/**
	 * Select relations tagged with provided tags. ProgramLocations are retained.
	 * @param tags the tags to select edges from
	 * @return the resulting query
	 */
	public GraphQuery relations(Tags.Relation... tags) {
		return new GraphQuery(this.q.edges(convertEdgeTagsArr(tags)));
	}
	
	/**
	 * Evaluates the graph query and produces an Atlas graph.
	 * @return the Atlas graph the query evaluates to
	 */
	private Graph eval() {
		return this.q.eval();
	}

	/**
	 * Selects fields by name from the query space.
	 * @param fieldName the name to select fields by
	 * @return the resulting query
	 */
	public GraphQuery fields(String fieldName) {
		return new GraphQuery(this.q.fields(fieldName));
	}

	/**
	 * Moves forward on the contained relations from the given program locations.
	 * @param locations the program locations to move forward from
	 * @return the resulting query
	 */
	public GraphQuery forward(GraphQuery locations) {
		return new GraphQuery(this.q.forward(locations.q));
	}

	/**
	 * Moves forward on the given relations from the contained program locations.
	 * @param relations the relations to move forward on
	 * @return the resulting query
	 */
	public GraphQuery forwardOn(GraphQuery relations) {
		return new GraphQuery(this.q.forwardOn(relations.q));
	}

	/**
	 * Moves 1 step forward on the given relations from the contained program locations.
	 * @param relations the relations to move forward on
	 * @return the resulting query
	 */
	public GraphQuery forwardStep(GraphQuery nodes) {
		return new GraphQuery(this.q.forwardStep(nodes.q));
	}

	/**
	 * Moves 1 step forward on the given relations from the contained program locations.
	 * @param relations the relations to move forward on
	 * @return the resulting query
	 */
	public GraphQuery forwardStepOn(GraphQuery relations) {
		return new GraphQuery(this.q.forwardStepOn(relations.q));
	}

	/**
	 * Selects the graph elements present in this query and the given one.
	 * @param expr the query to check the intersection with
	 * @return the resulting query
	 */
	public GraphQuery intersection(GraphQuery... expr) {
		return new GraphQuery(this.q.intersection(gqArrToQArr(expr)));
	}

	/**
	 * Queries methods by name from the contained program locations.
	 * @param methodName the name to select methods for
	 * @return the resulting query
	 */
	public GraphQuery methods(String methodName) {
		return new GraphQuery(this.q.methods(methodName));
	}

	/**
	 * Selects program locations tagged by at least one of the provided tags.
	 * @param tags the tags to select program locations for
	 * @return the resulting query
	 */
	public GraphQuery locations(Tags.ProgramLocation... tags) {
		return new GraphQuery(this.q.nodes(convertLocationTagsArr(tags)));
	}
	
	/**
	 * Gets the number of program locations contained.
	 * @return the number of program locations contained
	 */
	public long locationCount() {
		return CommonQueries.nodeSize(this.q);
	}
	
	/**
	 * Convenience method to convert an array of program location tags to an 
	 * array of strings.
	 * @param tags the tags to convert
	 * @return the converted tags as a string array
	 */
	private String[] convertLocationTagsArr(Tags.ProgramLocation[] tags) {
		String[] rArr = new String[tags.length];
		for (int i = 0; i < tags.length; i++) {
			rArr[i] = tags[i].toString();
		}
		return rArr;
	}

	/**
	 * Selects program locations tagged by all of the provided tags.
	 * @param tags the tags to select program locations for
	 * @return the resulting query
	 */
	public GraphQuery locationsTaggedWithAll(Tags.ProgramLocation... tags) {
		return new GraphQuery(this.q.nodesTaggedWithAll(convertLocationTagsArr(tags)));
	}
	
	/**
	 * Selects the program locations that contain the program locations currently in the query.
	 * @return the resulting query
	 */
	public GraphQuery parent() {
		return new GraphQuery(this.q.parent());
	}
	
	/**
	 * Selects packages for the given name.
	 * @param packageName the name to select packages for
	 * @return the resulting query
	 */
	public GraphQuery pkg(String packageName) {
		return new GraphQuery(this.q.pkg(packageName));
	}
	
	/**
	 * Moves backwards on the contained relations from the given program locations.
	 * @param locations the program locations to move backwards from
	 * @return the resulting query
	 */
	public GraphQuery predecessors(GraphQuery locations) {
		return new GraphQuery(this.q.predecessors(locations.q));
	}
	
	/**
	 * Moves backwards on the given relations from the contained program locations.
	 * @param relations the relations to move backward on
	 * @return the resulting query
	 */
	public GraphQuery predecessorsOn(GraphQuery relations) {
		return new GraphQuery(this.q.predecessorsOn(relations.q));
	}
	
	/**
	 * Selects projects by name from the locations contained in the query.
	 * @param projectName the name to select projects for
	 * @return the resulting query
	 */
	public GraphQuery project(String projectName) {
		return new GraphQuery(this.q.project(projectName));
	}
	
	/**
	 * Moves backwards on the contained relations from the given program locations.
	 * @param locations the program locations to move backwards from
	 * @return the resulting query
	 */
	public GraphQuery reverse(GraphQuery locations) {
		return new GraphQuery(this.q.reverse(locations.q));
	}
	
	/**
	 * Moves backwards on the given relations from the contained program locations.
	 * @param relations the relations to move backward on
	 * @return the resulting query
	 */
	public GraphQuery reverseOn(GraphQuery relations) {
		return new GraphQuery(this.q.reverseOn(relations.q));
	}
	
	/**
	 * Moves 1 step backwards on the contained relations from the given program locations.
	 * @param locations the program locations to move backwards from
	 * @return the resulting query
	 */
	public GraphQuery reverseStep(GraphQuery nodes) {
		return new GraphQuery(this.q.reverseStep(nodes.q));
	}
	
	/**
	 * Moves 1 step backwards on the given relations from the contained program locations.
	 * @param relations the relations to move backward on
	 * @return the resulting query
	 */
	public GraphQuery reverseStepOn(GraphQuery edges) {
		return new GraphQuery(this.q.reverseStepOn(edges.q));
	}
	/**
	 * Moves forward on the contained relations from the given program locations.
	 * @param locations the program locations to move forward from
	 * @return the resulting query
	 */
	public GraphQuery successors(GraphQuery nodes) {
		return new GraphQuery(this.q.successors(nodes.q));
	}
	
	/**
	 * Moves forward on the given relations from the contained program locations.
	 * @param relations the relations to move forward on
	 * @return the resulting query
	 */
	public GraphQuery successorsOn(GraphQuery edges) {
		return new GraphQuery(this.q.successorsOn(edges.q));
	}
	
	/**
	 * Select types in the contained program locations by given name.
	 * @param typeName the name to select types for
	 * @return the resulting query
	 */
	public GraphQuery types(String typeName) {
		return new GraphQuery(this.q.types(typeName));
	}
	
	/**
	 * Combines this query with a given one, removing duplicate elements.
	 * @param expr the query to combine with this one
	 * @return the resulting query
	 */
	public GraphQuery union(GraphQuery... expr) {
		return new GraphQuery(this.q.union(gqArrToQArr(expr)));
	}
	
	/**
	 * Returns the universe the query is a part of.
	 * @return the universe the query is a part of
	 */
	public GraphQuery universe() {
		return new GraphQuery(this.q.universe());
	}
	
	/**
	 * Forward difference for tag on contained program locations
	 * @param tag the tag to select for
	 * @return the resulting query
	 */
	public GraphQuery forwardDifference(Tags.Relation tag) {
		//TODO: This seems to work, but it inverts the order of returned nodes (from bottom to top, instead of top to bottom)
		return new GraphQuery().universe().relations(tag).forward(this).difference(this);
	}
	
	/**
	 * Selects program locations based on the value they have stored for a specific attribute.
	 * @param <T> the type of the attribute value
	 * @param attribute the tag of the attribute
	 * @param value the value the attribute is supposed to have
	 * @return the resulting query
	 */
	public <T> GraphQuery selectLocation(Tags.Attributes attribute, T value) {
		return new GraphQuery(this.q.selectNode(attribute.toString(), value));
	}
	
	/**
	 * Convenience method to convert an array of graph queries into an array of Atlas Q objects.
	 * @param args the array of graph queries
	 * @return the array of Atlas Q objects
	 */
	private static Q[] gqArrToQArr(GraphQuery[] args) {
		Q[] ar = new Q[args.length];
		
		for (int i = 0; i < args.length; i++) {
			ar[i] = args[i].q;
		}
		
		return ar;
	}
	
}
