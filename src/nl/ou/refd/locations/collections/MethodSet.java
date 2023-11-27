package nl.ou.refd.locations.collections;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refd.locations.generators.ProgramComponentsGenerator;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.specifications.MethodSpecification;
import nl.ou.refd.locations.streams.MethodStream;

/**
 * Class representing a set of method locations that can be streamed
 * with the specialized method location streams.
 */
public class MethodSet extends LocationSet implements SpecifiableCollection {

	/**
	 * Creates the method set from a set of program locations.
	 * @param locations the set of program locations
	 */
	public MethodSet(Set<ProgramLocation> locations) {
		super(locations);
	}
	
	/**
	 * Creates the method set from a number of method specifications.
	 * @param locations array of method specifications
	 */
	public MethodSet(MethodSpecification... locations) {
		this(Arrays.asList(locations));
	}
	
	/**
	 * Creates the method set from a list of method specifications.
	 * @param locations the list of method specifications
	 */
	public MethodSet(List<MethodSpecification> locations) {
		this(
			locations
			.stream()
			.map(MethodSet::methodSpecificationToProgramLocation)
			.collect(Collectors.toSet())
		);
	}
	
	/**
	 * Queries the graph to find the method specified by location parameter.
	 * @param location the specification of the method to find
	 * @return the program location the method specification represents
	 */
	private static ProgramLocation methodSpecificationToProgramLocation(MethodSpecification location) {
		return
			new ProgramComponentsGenerator()
			.stream()
			.classes()
			.classesByName(location.getEnclosingClass().getClassName())
			.methods()
			.methodsWithSignature(location.getMethodName(), location.getParameterTypes())
			.collect()
			.locations()
			.iterator()
			.next();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodStream stream() {
		return new MethodStream(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MethodSpecification> toLocationSpecifications() {
		return this.locations()
				   .stream()
				   .map(MethodSpecification::new)
				   .collect(Collectors.toList());
	}
	
}
