package nl.ou.refd.locations.collections;

import java.util.Set;

import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.streams.FieldStream;
import nl.ou.refd.locations.streams.Stream;

/**
 * Class representing a set of field locations that can be streamed
 * with the specialized field location streams.
 */
public class FieldSet extends LocationSet {

	/**
	 * Creates the field set from a set of program locations.
	 * @param locations the set of program locations
	 */
	public FieldSet(Set<ProgramLocation> locations) {
		super(locations);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stream stream() {
		return new FieldStream(this);
	}

}
