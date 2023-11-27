package nl.ou.refd.locations.collections;

import java.util.HashSet;
import java.util.Set;

import nl.ou.refd.exceptions.LocationSetException;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.streams.Stream;

/**
 * Class representing a set of program locations that can be streamed
 * with the specialized location streams.
 */
public abstract class LocationSet {
	
	private final Set<ProgramLocation> locations;
	
	/**
	 * Creates a LocationSet from a regular set of program locations
	 * @param locations the regular set of program locations
	 */
	protected LocationSet(Set<ProgramLocation> locations) {
		this.locations = locations;
	}
	
	/**
	 * Returns a shallow copy of the set of program locations contained within.
	 * @return a shallow copy of the set of program locations contained within
	 */
	public Set<ProgramLocation> locations() {
		return new HashSet<ProgramLocation>(this.locations);
	}
	
	/**
	 * Checks if the set has a single program location and returns this location.
	 * @return the single program location
	 * @throws LocationSetException if the set contains more than one program location
	 */
	public ProgramLocation singleLocation() {
		Set<ProgramLocation> nodes = this.locations();
		if (nodes.size() != 1) {
			throw new LocationSetException("Location set did not have exactly one program location");
		}
		return nodes.iterator().next();
	}
	
	/**
	 * Returns the number of program location in the location set.
	 * @return the number of program location in the location set
	 */
	public long size() {
		return this.locations.size();
	}
	
	/**
	 * From this set, creates a LabeledLocationSet with a specific label.
	 * @param label the label as string
	 * @return the LabeledLocationSet containing this set and the provided label
	 */
	public LabeledLocationSet label(String label) {
		return new LabeledLocationSet(this.locations, label);
	}
	
	/**
	 * From this set, creates a LabeledLocationSet with a specific label.
	 * @param label the label as class
	 * @return the LabeledLocationSet containing this set and the provided label
	 */
	public LabeledLocationSet label(Class<?> label) {
		return this.label(label.getTypeName());
	}
	
	/**
	 * Creates a stream with this location collection as its source.
	 * @return a stream with this location collection as its source
	 */
	public abstract Stream stream();

}
