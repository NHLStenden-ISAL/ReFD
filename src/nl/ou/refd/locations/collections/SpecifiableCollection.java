package nl.ou.refd.locations.collections;

import java.util.List;

import nl.ou.refd.locations.specifications.LocationSpecification;

/**
 * Interface representing objects which can be converted to a list of location specifications.
 */
public interface SpecifiableCollection {
	
	/**
	 * Converts data in the object to a list of program location specifications.
	 * @return a list of program location specifications
	 */
	public List<? extends LocationSpecification> toLocationSpecifications();
	
}
