package nl.ou.refd.locations.collections;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refd.locations.generators.ProgramComponentsGenerator;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.specifications.ClassSpecification;
import nl.ou.refd.locations.streams.ClassStream;

/**
 * Class representing a set of class locations that can be streamed
 * with the specialized class location streams.
 */
public class ClassSet extends LocationSet implements SpecifiableCollection {

	/**
	 * Creates the class set from a set of program locations.
	 * @param locations the set of program locations
	 */
	public ClassSet(Set<ProgramLocation> locations) {
		super(locations);
	}

	/**
	 * Creates the class set from a number of class specifications.
	 * @param locations array of class specifications
	 */
	public ClassSet(ClassSpecification... locations) {
		this(Arrays.asList(locations));
	}
	
	/**
	 * Creates the class set from a list of class specifications.
	 * @param locations the list of class specifications
	 */
	public ClassSet(List<ClassSpecification> locations) {
		this(
			locations
			.stream()
			.map(ClassSet::classSpecificationToProgramLocation)
			.collect(Collectors.toSet())
		);
	}
	
	/**
	 * Queries the graph to find the class specified by location parameter.
	 * @param location the specification of the class to find
	 * @return the program location the class specification represents
	 */
	private static ProgramLocation classSpecificationToProgramLocation(ClassSpecification location) {
		return
			new ProgramComponentsGenerator()
			.stream()
			.classes()
			.classesByName(location.getClassName())
			.collect()
			.singleLocation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClassStream stream() {
		return new ClassStream(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ClassSpecification> toLocationSpecifications() {
		return this
				.locations()
				.stream()
				.map(ClassSpecification::new)
				.collect(Collectors.toList());
	}
	
}
