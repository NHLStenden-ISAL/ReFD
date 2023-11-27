package nl.ou.refd.locations.generators;

import java.util.Set;

import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.streams.ProgramComponentStream;

/**
 * Class representing a generator that generates all program components
 * from the current program graph.
 */
public class ProgramComponentsGenerator extends LocationGenerator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProgramComponentStream stream() {
		return new ProgramComponentStream(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<ProgramLocation> generate() {
		return Graph.query().universe().locations();
	}

}
