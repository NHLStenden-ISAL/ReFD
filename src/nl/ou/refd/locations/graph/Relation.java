package nl.ou.refd.locations.graph;

/**
 * Class representing an relation in the graph. These
 * Relation objects have attributes and can be tagged.
 * The Relation has a ProgramLocation from which it
 * relates to another ProgramLocation. The type of
 * relation is based on the tags of the relation.
 */
public class Relation extends SimpleGraphElement<com.ensoftcorp.atlas.core.db.graph.Edge, Tags.Relation> {
	
	/**
	 * Creates the relation from an Atlas Edge.
	 * @param edge the Atlas Edge to create the relation from
	 */
	Relation(com.ensoftcorp.atlas.core.db.graph.Edge edge) {
		super(edge);
	}

	/**
	 * Gets the ProgramLocation that is the base for the relation.
	 * @return the ProgramLocation from which the relation is going out
	 */
	public ProgramLocation from() {
		return new ProgramLocation(this.getAtlasElement().from());
	}

	/**
	 * Gets the ProgramLocation that the relation points to.
	 * @return the ProgramLocation that the relation points to
	 */
	public ProgramLocation to() {
		return new ProgramLocation(this.getAtlasElement().to());
	}

}
