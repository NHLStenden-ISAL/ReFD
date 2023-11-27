package nl.ou.refd.locations.specifications;

import org.apache.commons.lang3.NotImplementedException;

import nl.ou.refd.exceptions.IncompatibleProgramLocationException;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;

/**
 * Class representing a specification of a single package location
 * in a codebase. The specification can be of either a location that
 * already exists within the codebase, or that does not exists (yet).
 */
public class PackageSpecification extends LocationSpecification {
	
	private String packageName;

	/**
	 * Creates a PackageSpecification from a name. This is the fully qualified
	 * name.
	 * @param packageName the fully qualified package name
	 */
	public PackageSpecification(String packageName) {
		this.packageName = packageName;
	}
	
	/**
	 * Creates a package location specification from a ProgramLocation (graph node). This
	 * value is not stored inside the PackageSpecification. Rather, it is used to get the
	 * relevant data to create the ProgramLocation.
	 * @param pl the ProgramLocation containing the relevant data to create the PackageSpecification
	 * @throws IncompatibleProgramLocationException if the provided ProgramLocation is not a valid package
	 */
	public PackageSpecification(ProgramLocation pl) {
		if (!locationIsPackage(pl))
			throw new IncompatibleProgramLocationException("Node not tagged with Tags.Node.PACKAGE");
		this.packageName = pl.<String>getAttribute(Tags.Attributes.NAME);
	}
	
	/**
	 * Checks if the provided ProgramLocation is a package.
	 * @param pl the provided PorgramLocation
	 * @return true if the ProgramLocation is a package, false otherwise
	 */
	public static boolean locationIsPackage(ProgramLocation pl) {
		return pl.taggedWith(Tags.ProgramLocation.PACKAGE);
	}

	/**
	 * Gets the package's name.
	 * @return the package's name as a string
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * Sets the package's name.
	 * @param packageName the package's name as a string
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PackageSpecification copy() {
		return new PackageSpecification(packageName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.packageName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProgramLocation construct(Graph graph) {
		throw new NotImplementedException();
	}
	
}
