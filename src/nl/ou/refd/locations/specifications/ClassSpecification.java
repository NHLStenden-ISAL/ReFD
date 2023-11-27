package nl.ou.refd.locations.specifications;

import java.util.stream.Collectors;

import nl.ou.refd.exceptions.IncompatibleProgramLocationException;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Relation;
import nl.ou.refd.locations.graph.Tags;

/**
 * Class representing a specification of a single class location
 * in a codebase. The specification can be of either a location that
 * already exists within the codebase, or that does not exists (yet).
 */
public class ClassSpecification extends LocationSpecification {
	
	private PackageSpecification enclosingPackage;
	private String className;
	private AccessModifier accessModifier;
	
	/**
	 * Creates a class location specification from a class name and a 
	 * specification of a package the class belongs to.
	 * @param className the name of the class
	 * @param enclosingPackage the package the class belongs to
	 */
	public ClassSpecification(String className, AccessModifier accessModifier, PackageSpecification enclosingPackage) {
		this.className = className;
		this.accessModifier = accessModifier;
		this.enclosingPackage = enclosingPackage;
	}
	
	/**
	 * Creates a class location specification from a ProgramLocation
	 * (graph node) by selecting the correct attributes from it.
	 * @param location the ProgramLocation to get relevant information from
	 * @throws IncompatibleProgramLocationException if the provided ProgramLocation is not a valid class
	 */
	public ClassSpecification(ProgramLocation location) {
		if (!locationIsClass(location))
			throw new IncompatibleProgramLocationException("Node not tagged with Tags.Node.CLASS");
		this.className = location.<String>getAttribute(Tags.Attributes.NAME);
		this.accessModifier = AccessModifier.fromTaggedLocation(location);
		this.enclosingPackage = new PackageSpecification(Graph.query(location).parent().singleLocation());
	}
	
	/**
	 * Checks if the provided ProgramLocation is tagged as a class.
	 * @param n the ProgramLocation to check
	 * @return true if ProgramLocation instance is a class, false otherwise
	 */
	public static boolean locationIsClass(ProgramLocation pl) {
		return pl.taggedWith(Tags.ProgramLocation.CLASS);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClassSpecification copy() {
		return new ClassSpecification(className, accessModifier, enclosingPackage.copy());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.enclosingPackage.toString() + "." + this.className;
	}

	/**
	 * Returns an immutable String representing the class name.
	 * @return the name of the class as a string
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Sets the class's name.
	 * @param className the new name of the class
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Returns the enclosing package as an object. This object
	 * is mutable.
	 * @return the enclosing package as PackageSpecification
	 */
	public PackageSpecification getEnclosingPackage() {
		return enclosingPackage;
	}

	/**
	 * Sets the enclosing package.
	 * @param enclosingPackage the new enclosing package
	 */
	public void setEnclosingPackage(PackageSpecification enclosingPackage) {
		this.enclosingPackage = enclosingPackage;
	}
	
	/**
	 * Returns the class's AccessModifier.
	 * @return the AccessModifier of the class
	 */
	public AccessModifier getAccessModifier() {
		return accessModifier;
	}

	/**
	 * Sets the AccessModifier of the class.
	 * @param accessModifier the AccessModifier of the class
	 */
	public void setAccessModifier(AccessModifier accessModifier) {
		this.accessModifier = accessModifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProgramLocation construct(Graph graph) {
		ProgramLocation parentPackage = Graph.query()
				 .universe()
				 .locations(Tags.ProgramLocation.PACKAGE)
				 .locations()
				 .stream()
				 .filter(node -> (node.<String>getAttribute(Tags.Attributes.NAME)).equals(this.getEnclosingPackage().getPackageName()))
				 .collect(Collectors.toList())
				 .get(0);

		ProgramLocation nClass = createClass(graph);
		nClass.tag(this.accessModifier.toTag());
		nClass.putAttribute(Tags.Attributes.NAME, this.getClassName());
		
		Relation contains = graph.createRelation(parentPackage, nClass);
		contains.tag(Tags.Relation.CONTAINS);
		
		return nClass;
	}
	
	/**
	 * Creates an empty ProgramLocation representation of a class.
	 * @param graph the Graph to create the ProgramLocation in
	 * @return the created ProgramLocation
	 */
	private static ProgramLocation createClass(Graph graph) {
		ProgramLocation rNode = graph.createProgramLocation();
		rNode.tag(Tags.ProgramLocation.CLASS);
		rNode.tag(Tags.ProgramLocation.NAMESPACE);
		rNode.tag(Tags.ProgramLocation.TYPE);
		rNode.tag(Tags.ProgramLocation.CLASSIFIER);
		rNode.tag(Tags.ProgramLocation.REFACTOR_CREATED_CLASS);
		return rNode;
	}
	
}
