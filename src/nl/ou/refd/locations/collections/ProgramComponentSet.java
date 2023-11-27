package nl.ou.refd.locations.collections;

import java.util.Set;

import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.streams.ProgramComponentStream;

/**
 * Class representing a set of program component locations that can be streamed
 * with the specialized program component location streams.
 */
public class ProgramComponentSet extends LocationSet {

	/**
	 * Creates the program component set from a set of program locations.
	 * @param locations the set of program locations
	 */
	public ProgramComponentSet(Set<ProgramLocation> locations) {
		super(locations);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProgramComponentStream stream() {
		return new ProgramComponentStream(this);
	}

}
