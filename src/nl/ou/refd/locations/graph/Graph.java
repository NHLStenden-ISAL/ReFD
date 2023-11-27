package nl.ou.refd.locations.graph;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.ensoftcorp.atlas.core.db.graph.UniverseGraph;
import com.ensoftcorp.atlas.core.xcsg.XCSG;

/**
 * Class representing the main graph used to query a codebase. This
 * graph is a singleton because only one program can be under review.
 * The class can be used to create queries on this graph, but its
 * instance can be used to create program locations in the graph,
 * or remove them.
 */
public class Graph {
	private static Graph instance;
	
	/**
	 * Gets the central graph.
	 * @return the central graph
	 */
	public static Graph getInstance() {
		if (instance == null) {
			instance = new Graph();
		}
		
		return instance;
	}
	
	/**
	 * Creates the graph.
	 */
	private Graph(){}
	
	/**
	 * Creates an empty query on the graph.
	 * @return an empty query
	 */
	public static GraphQuery query() {
		return new GraphQuery();
	}
	
	/**
	 * Creates a query from a set of program locations which serve as its starting context.
	 * @param locations the starting context of the query
	 * @return a query from a set of program locations which serve as its starting context
	 */
	public static GraphQuery query(Set<ProgramLocation> locations) {
		return new GraphQuery(locations);
	}
	
	/**
	 * Creates a query from a number of program locations which serve as its starting context.
	 * @param locations the starting context of the query
	 * @return a query from a set of program locations which serve as its starting context
	 */
	public static GraphQuery query(ProgramLocation... locations) {
		return new GraphQuery(new HashSet<ProgramLocation>(Arrays.asList(locations)));
	}

	/**
	 * Creates a new program location node in the graph. This is automatically tagged with some
	 * necessary tags for the node to fit into the graph.
	 * @return a fresh program location node already inserted into the graph
	 */
	public ProgramLocation createProgramLocation() {
		com.ensoftcorp.atlas.core.db.graph.Node atlasNode = com.ensoftcorp.atlas.core.db.graph.Graph.U.createNode();
		atlasNode.tag(XCSG.ModelElement);
		atlasNode.tag(XCSG.Language.Java);
		
		ProgramLocation rNode = new ProgramLocation(atlasNode);
		rNode.tag(Tags.ProgramLocation.NODE);
		return rNode;
	}

	/**
	 * Creates a new relation between two program location nodes in the graph.
	 * @param pl1 the starting node
	 * @param pl2 the ending node
	 * @return a fresh relation between pl1 and pl2, already inserted into the graph
	 */
	public Relation createRelation(ProgramLocation pl1, ProgramLocation pl2) {
		com.ensoftcorp.atlas.core.db.graph.Edge atlasEdge = com.ensoftcorp.atlas.core.db.graph.Graph.U.createEdge(pl1.getAtlasElement(), pl2.getAtlasElement());
		atlasEdge.tag(XCSG.ModelElement);
		atlasEdge.tag(XCSG.Language.Java);
		
		Relation rEdge = new Relation(atlasEdge);
		rEdge.tag(Tags.Relation.EDGE);
		rEdge.tag(Tags.Relation.REFACTOR_CREATED_EDGE);
		
		return rEdge;
	}
	
	/**
	 * Removes a program location node from the graph
	 * @param pr the program location to remove
	 */
	public void removeProgramLocation(ProgramLocation pr) {
		UniverseGraph universe = com.ensoftcorp.atlas.core.db.graph.Graph.U;
		universe.delete(pr.getAtlasElement());
	}
	
}
