package nl.ou.refd.locations.streams;

import java.util.List;

import nl.ou.refd.analysis.subdetectors.MethodSubdetectors;
import nl.ou.refd.analysis.subdetectors.Subdetector;
import nl.ou.refd.locations.collections.MethodSet;
import nl.ou.refd.locations.specifications.MethodSpecification;
import nl.ou.refd.locations.specifications.LocationSpecification.AccessModifier;

/**
 * Class which represents a stream of methods. Much like a Java stream,
 * this stream chains a number of operations called subdetectors which operate
 * on these program locations. Through these subdetectors, the program locations
 * can be queried in this way.
 */
public class MethodStream extends Stream {

	/**
	 * Creates a stream of methods from a collection of methods.
	 * @param source the collection of classes
	 */
	public MethodStream(MethodSet source) {
		super(source);
	}
	
	/**
	 * Creates a stream from a previous stream and a next step in the stream, being
	 * the subdetector. The previous stream is not changed, and this stream works on
	 * a copy of that stream.
	 * @param precursor the previous stream
	 * @param next the next step in the chain within the stream
	 */
	protected MethodStream(Stream precursor, Subdetector next) {
		super(precursor, next);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodSet collect() {
		return new MethodSet(this.locations());
	}
	
	/**
	 * Filters the contained methods by name.
	 * @param methodName the name of the method
	 * @return a stream containing methods with name specified by methodName
	 */
	public MethodStream filterByName(String methodName) {
		return new MethodStream(this, new MethodSubdetectors.FilterByName(methodName));
	}
	
	/**
	 * Queries the bodies of the methods contained in this stream.
	 * @return the bodies of the methods contained in this stream
	 */
	public InstructionStream bodies() {
		return new InstructionStream(this, new MethodSubdetectors.Bodies());
	}
	
	/**
	 * Filters methods contained in this stream and only keeps those with a return type
	 * specified by type, or a covariant type of that.
	 * @param type the return type, or covariant of, to filter with
	 * @return methods with a return type specified by type, or a covariant type of that
	 */
	public MethodStream methodsWithCovariantReturnTypes(String type) {
		return new MethodStream(this, new MethodSubdetectors.MethodsWithCovariantReturnTypes(type));
	}
	
	/**
	 * Filters methods contained in this stream and only keeps those with a visibility
	 * equal or greater than the supplied AccessModifier.
	 * @param visibility the minimal visibility of the method
	 * @return methods with a visibility equal or greater than the supplied AccessModifier
	 */
	public MethodStream methodsEquallyOrMoreVisible(AccessModifier visibility) {
		return new MethodStream(this, new MethodSubdetectors.MethodsEquallyOrMoreVisible(visibility));
	}
	
	/**
	 * Filters methods and keeps only those whose parameter's types correspond to the
	 * provided list of types, as an exact match of order and size.
	 * @param parameterTypesSource the list of types as strings
	 * @return methods whose parameter's types correspond to the provided list of types, as an exact match of order and size
	 */
	public MethodStream methodsWithParameters(List<String> parameterTypesSource) {
		return new MethodStream(this, new MethodSubdetectors.MethodsWithParameters(parameterTypesSource));
	}

	/**
	 * Queries locations where the methods contained in this stream are called.
	 * @return locations where the methods contained in this stream are called
	 */
	public InstructionStream methodsCalledAt() {
		return new InstructionStream(this, new MethodSubdetectors.MethodsCalledAt());
	}
	
	/**
	 * Queries the methods which the methods contained in this stream override.
	 * @return the methods which the methods contained in this stream override
	 */
	public MethodStream overrides() {
		return new MethodStream(this, new MethodSubdetectors.Overrides());
	}
	
	/**
	 * Filters methods and only keeps the concrete methods.
	 * @return concrete methods from the stream
	 */
	public MethodStream concreteMethods() {
		return new MethodStream(this, new MethodSubdetectors.ConcreteMethods());
	}
	
	/**
	 * Queries the methods which override those in the stream.
	 * @return the methods which override those in the stream
	 */
	public MethodStream overriddenBy() {
		return new MethodStream(this, new MethodSubdetectors.OverriddenBy());
	}
	
	/**
	 * Filters the current stream of methods and keeps only methods also contained
	 * within incoming streams intersectWith.
	 * @param intersectWith the streams of methods to keep
	 * @return the filtered stream
	 */
	public MethodStream intersectionWithMethods(MethodStream... intersectWith) {
		return new MethodStream(this, new MethodSubdetectors.IntersectionWithMethods(intersectWith));
	}
	
	/**
	 * Removes the methods resulting from differenceWith from the methods contained in
	 * this stream.
	 * @param differenceWith the methods to remove from this stream
	 * @return the remaining methods not present in differenceWith
	 */
	public MethodStream differenceWithMethods(MethodStream... differenceWith) {
		return new MethodStream(this, new MethodSubdetectors.DifferenceWithMethods(differenceWith));
	}
	
	/**
	 * Queries the classes the methods in this stream are contained within.
	 * @return the classes the methods in this stream are contained within
	 */
	public ClassStream parentClasses() {
		return new ClassStream(this, new MethodSubdetectors.ParentClasses());
	}
	
	/**
	 * Filters methods and only keeps the abstract methods.
	 * @return abstract methods from the stream
	 */
	public MethodStream abstractMethods() {
		return new MethodStream(this, new MethodSubdetectors.AbstractMethods());
	}

	/**
	 * Filters the methods contained in this stream and keeps methods with parameters which could result in a
	 * narrowing overload when method subject would be added to their context. This means that previous automatic
	 * primitive type conversion allowed a method in the stream to be called with a wider type, but the addition of
	 * the method subject could divert that call to itself.
	 * @param subject the method to check against
	 * @return methods liable for automatic parameter conversion in relation to the method subject
	 */
	public MethodStream autoNarrowingOverloads(MethodSpecification subject) {
		return new MethodStream(this, new MethodSubdetectors.AutoNarrowingOverloads(subject));
	}
	
	/**
	 * Filters methods in stream and keeps only those which contain instructions from the given
	 * InstructionStream.
	 * @param instructions the instructions to check against
	 * @return methods which contain instructions from the given InstructionStream
	 */
	public MethodStream containInstruction(InstructionStream instructions) {
		return new MethodStream(this, new MethodSubdetectors.ContainInstruction(instructions));
	}
	
	/**
	 * Integrates incoming streams unionWith into this stream, removing duplicates.
	 * @param unionWith methods to add to current stream, removing duplicates
	 * @return the result of integrating both streams
	 */
	public MethodStream union(MethodStream... m) {
		return new MethodStream(this, new MethodSubdetectors.Union(m));
	}
	
	/**
	 * Filters methods in stream and keeps only those which have the signature described
	 * by methodName and the list or parameter types parameterTypes.
	 * @param methodName the name of the method
	 * @param parameterTypes list of parameter types
	 * @return methods which have the signature described by methodName and the list or parameter types parameterTypes
	 */
	public MethodStream methodsWithSignature(String methodName, List<String> parameterTypes) {
		return new MethodStream(this, new MethodSubdetectors.MethodsWithSignature(methodName, parameterTypes));
	}
	
	/**
	 * Filters methods in stream and keeps only those which are overloads of the
	 * method subject.
	 * @param subject the method to filter overloads of
	 * @return methods which are overloads of the method subject
	 */
	public MethodStream overloadsOfMethod(MethodSpecification subject) {
		return new MethodStream(this, new MethodSubdetectors.OverloadsOfMethod(subject));
	}
	
	/**
	 * Filters methods in stream and keeps only those which method subject
	 * would override if placed in the correct context.
	 * @param subject the method to check against
	 * @return methods which method subject would override if placed in the correct context
	 */
	public MethodStream overrideEquivalentMethods(MethodSpecification subject) {
		return new MethodStream(this, new MethodSubdetectors.OverrideEquivalentMethods(subject));
	}
}
