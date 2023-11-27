package nl.ou.refd.locations.generators;

import java.util.Set;

import nl.ou.refd.locations.collections.LocationSet;
import nl.ou.refd.locations.graph.ProgramLocation;

/**
 * Abstract class that represents a program location generator used
 * as a starting point for stream queries. A generator generates locations
 * from the main program graph.
 */
public abstract class LocationGenerator extends LocationSet {

	/**
	 * Creates the LocationGenerator as a LocationCollection without contents.
	 */
	protected LocationGenerator() {
		super(null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<ProgramLocation> locations() {
		return this.generate();
	}

	/**
	 * Generates the program locations.
	 * @return a set of generated program locations
	 */
	public abstract Set<ProgramLocation> generate();

}
