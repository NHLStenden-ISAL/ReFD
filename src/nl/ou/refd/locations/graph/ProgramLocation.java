package nl.ou.refd.locations.graph;

import java.util.HashSet;
import java.util.Set;

import com.ensoftcorp.atlas.core.db.graph.Node;

/**
 * Class representing an program location in the graph. These
 * ProgramLocation objects have attributes and can be tagged.
 * The ProgramLocation can also have relations to other elements
 * in the graph. The type of the ProgramLocation is based on the
 * tags it has.
 */
public class ProgramLocation extends SimpleGraphElement<Node, Tags.ProgramLocation> {
	
	/**
	 * Creates the program location from an Atlas graph node.
	 * @param node the Atlas graph node to create the ProgramLocation from
	 */
	ProgramLocation(com.ensoftcorp.atlas.core.db.graph.Node node) {
		super(node);
	}
	
	/**
	 * Gets the relations which end in this program location.
	 * @return a list of in relations
	 */
	public Set<Relation> in() {
		Set<Relation> rEdges = new HashSet<Relation>();
		this.getAtlasElement().in().forEach(edge -> rEdges.add(new Relation(edge)));
		return rEdges;
	}
	
	/**
	 * Gets the relations which end in this program location and are tagged with a specific tag.
	 * @param tag the tag the relations should be tagged with
	 * @return a list of in relations
	 */
	public Set<Relation> in(Tags.Relation tag) {
		Set<Relation> rEdges = new HashSet<Relation>();
		this.getAtlasElement().in(tag.toString()).forEach(edge -> rEdges.add(new Relation(edge)));
		return rEdges;
	}
	
	/**
	 * Gets the relations which start from this program location.
	 * @return a list of out relations
	 */
	public Set<Relation> out() {
		Set<Relation> rEdges = new HashSet<Relation>();
		this.getAtlasElement().out().forEach(edge -> rEdges.add(new Relation(edge)));
		return rEdges;
	}
	
	/**
	 * Gets the relations which start from this program location and are tagged with a specific tag.
	 * @param tag the tag the relations should be tagged with
	 * @return a list of out relations
	 */
	public Set<Relation> out(Tags.Relation tag) {
		Set<Relation> rEdges = new HashSet<Relation>();
		this.getAtlasElement().out(tag.toString()).forEach(edge -> rEdges.add(new Relation(edge)));
		return rEdges;
	}

}
