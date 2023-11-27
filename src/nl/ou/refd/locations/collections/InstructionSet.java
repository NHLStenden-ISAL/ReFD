package nl.ou.refd.locations.collections;

import java.util.Set;

import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.streams.InstructionStream;

/**
 * Class representing a set of instruction locations that can be streamed
 * with the specialized instruction location streams.
 */
public class InstructionSet extends LocationSet {

	/**
	 * Creates the instruction set from a set of program locations.
	 * @param locations the set of program locations
	 */
	public InstructionSet(Set<ProgramLocation> locations) {
		super(locations);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InstructionStream stream() {
		return new InstructionStream(this);
	}

}
