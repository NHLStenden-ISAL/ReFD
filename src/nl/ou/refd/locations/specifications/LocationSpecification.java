package nl.ou.refd.locations.specifications;

import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;

/**
 * Abstract class representing a specification of a single location
 * in a codebase. The specification can be of either a location that
 * already exists within the codebase, or that does not exists (yet).
 */
public abstract class LocationSpecification {
	
	private static final String publicAccessModifierValue = "public";
	private static final String packageAccessModifierValue = "package";
	private static final String protectedAccessModifierValue = "protected";
	private static final String privateAccessModifierValue = "private";
	
	/**
	 * Enum to create an AccessSpecifier. Only the access specifiers
	 * of Java are supported. The enum is backed by String values.
	 */
	public enum AccessModifier {
		PUBLIC(publicAccessModifierValue),
		PACKAGE(packageAccessModifierValue),
		PROTECTED(protectedAccessModifierValue),
		PRIVATE(privateAccessModifierValue);
		
		private final String tag;
		
		/**
		 * Creates an enum element from the string specified in its declaration
		 * @param tag the provided string representing the enum element's value
		 */
		private AccessModifier(final String tag) {
			this.tag = tag;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return this.tag;
		}
		
		/**
		 * Converts an AccessModifier to a Tags.Node value.
		 * @return the Tags.Node value which represents the AccessModifier
		 * @throws IllegalArgumentException when the enum value does not represent a
		 * Tags.Node value
		 */
		public Tags.ProgramLocation toTag() {
			switch (this) {
				case PUBLIC:
					return Tags.ProgramLocation.PUBLIC_VISIBILITY;
				case PACKAGE:
					return Tags.ProgramLocation.PACKAGE_VISIBILITY;
				case PROTECTED:
					return Tags.ProgramLocation.PROTECTED_PACKAGE_VISIBILITY;
				case PRIVATE:
					return Tags.ProgramLocation.PRIVATE_VISIBILITY;
				default:
					throw new IllegalArgumentException("Enum value \"" + this.toString() + "\" not supported for conversion to Tags.Node");
			}
		}
		
		/**
		 * Creates an AccessModifier from a string value backing the enum.
		 * @param s the string to convert to AccessModifier
		 * @return the AccessModifier representation of parameter s
		 * @throws IllegalArgumentException if the provided
		 * string is not part of the AccessModifier enum.
		 */
		public static AccessModifier fromString(String s) {
			switch (s) {
				case publicAccessModifierValue:
					return AccessModifier.PUBLIC;
				case packageAccessModifierValue:
					return AccessModifier.PACKAGE;
				case protectedAccessModifierValue:
					return AccessModifier.PROTECTED;
				case privateAccessModifierValue:
					return AccessModifier.PRIVATE;
				default:
					throw new IllegalArgumentException("Provided AccessModifier value \"" + s + "\" does not match known values");
			}
		}
		
		/**
		 * Creates an AccessModifier from a ProgramLocation tagged with a visibility tag.
		 * @param pl the ProgramLocation tagged with a visibility tag
		 * @return the AccessModifier representing the ProgramLocation's visibility tag
		 * @throws IllegalArgumentException if the ProgramLocation is not tagged with a visibility tag
		 */
		public static AccessModifier fromTaggedLocation(ProgramLocation pl) {
			if (pl.taggedWith(Tags.ProgramLocation.PUBLIC_VISIBILITY)) {
				return AccessModifier.PUBLIC;
			}
			else if (pl.taggedWith(Tags.ProgramLocation.PROTECTED_PACKAGE_VISIBILITY)) {
				return AccessModifier.PROTECTED;
			}
			else if (pl.taggedWith(Tags.ProgramLocation.PACKAGE_VISIBILITY)) {
				return AccessModifier.PACKAGE;
			}
			else if (pl.taggedWith(Tags.ProgramLocation.PRIVATE_VISIBILITY)) {
				return AccessModifier.PRIVATE;
			}
			else {
				throw new IllegalArgumentException("Provided ProgramLocation does not contain the correct tag to convert to AccessModifier");
			}
		}
	}
	
	/**
	 * Copies the specification (deep copy) into a new object
	 * @return the copy of the specification
	 */
	public abstract LocationSpecification copy();
	
	/**
	 * Constructs a ProgramLocation in the given graph conforming to
	 * this specification. This operation can lead to multiple
	 * ProgramLocations being constructed if necessary.
	 * @param graph the Graph to create the ProgramLocation in
	 * @return the main ProgramLocation created
	 */
	public abstract ProgramLocation construct(Graph graph);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract String toString();
}
