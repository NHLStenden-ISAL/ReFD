package nl.ou.refd.locations.streams;

import nl.ou.refd.analysis.subdetectors.ClassSubdetectors;
import nl.ou.refd.analysis.subdetectors.Subdetector;
import nl.ou.refd.locations.collections.ClassSet;

/**
 * Class which represents a stream of classes. Much like a Java stream,
 * this stream chains a number of operations called subdetectors which operate
 * on these program locations. Through these subdetectors, the program locations
 * can be queried in this way.
 */
public class ClassStream extends Stream {

	/**
	 * Creates a stream of classes from a collection of classes.
	 * @param source the collection of classes
	 */
	public ClassStream(ClassSet source) {
		super(source);
	}
	
	/**
	 * Creates a stream from a previous stream and a next step in the stream, being
	 * the subdetector. The previous stream is not changed, and this stream works on
	 * a copy of that stream.
	 * @param precursor the previous stream
	 * @param next the next step in the chain within the stream
	 */
	protected ClassStream(Stream precursor, Subdetector next) {
		super(precursor, next);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClassSet collect() {
		return new ClassSet(this.locations());
	}
	
	/**
	 * Filters the contained classes by name.
	 * @param className the name of the class
	 * @return a stream containing classes with name specified by className
	 */
	public ClassStream classesByName(String className) {
		return new ClassStream(this, new ClassSubdetectors.ClassesByName(className));
	}
	
	/**
	 * Queries the direct superclasses of the classes in this stream.
	 * @return the direct superclasses of the classes in this stream
	 */
	public ClassStream directSuperClasses() {
		return new ClassStream(this, new ClassSubdetectors.DirectSuperClasses());
	}
	
	/**
	 * Queries all superclasses of the classes in this stream.
	 * @return all superclasses of the classes in this stream
	 */
	public ClassStream allSuperClasses() {
		return new ClassStream(this, new ClassSubdetectors.AllSuperClasses());
	}
	
	/**
	 * Queries all methods contained in the classes in this stream.
	 * @return all methods contained in the classes in this stream
	 */
	public MethodStream methods() {
		return new MethodStream(this, new ClassSubdetectors.Methods());
	}

	/**
	 * Filters contained classes and keeps only the abstract classes.
	 * @return the abstract classes of contained classes
	 */
	public ClassStream abstractClasses() {
		return new ClassStream(this, new ClassSubdetectors.AbstractClasses());
	}
	
	/**
	 * Removes the classes resulting from differenceWith from the classes contained in
	 * this stream.
	 * @param differenceWith the classes to remove from this stream
	 * @return the remaining classes not present in differenceWith
	 */
	public ClassStream differenceWithClasses(ClassStream... differenceWith) {
		return new ClassStream(this, new ClassSubdetectors.DifferenceWithClasses(differenceWith));
	}
	
	/**
	 * Filters contained classes and keeps only the concrete classes.
	 * @return the concrete classes of contained classes
	 */
	public ClassStream concreteClasses() {
		return new ClassStream(this, new ClassSubdetectors.ConcreteClasses());
	}

	/**
	 * Queries the direct subclasses of the classes in this stream.
	 * @return the direct subclasses of the classes in this stream
	 */
	public ClassStream directSubclasses() {
		return new ClassStream(this, new ClassSubdetectors.DirectSubclasses());
	}

	/**
	 * Queries all subclasses of the classes in this stream.
	 * @return all subclasses of the classes in this stream
	 */
	public ClassStream allSubclasses() {
		return new ClassStream(this, new ClassSubdetectors.AllSubclasses());
	}
	
	/**
	 * Integrates incoming streams unionWith into this stream, removing duplicates.
	 * @param unionWith classes to add to current stream, removing duplicates
	 * @return the result of integrating both streams
	 */
	public ClassStream unionWithClasses(ClassStream... unionWith) {
		return new ClassStream(this, new ClassSubdetectors.UnionWithClasses(unionWith));
	}
	
	/**
	 * Queries all fields contained in the classes in this stream.
	 * @return all fields contained in the classes in this stream
	 */
	public FieldStream fields() {
		return new FieldStream(this, new ClassSubdetectors.Fields());
	}
	
	/**
	 * Filters the current stream of classes and keeps only classes also contained
	 * within incoming streams intersectWith.
	 * @param intersectWith the streams of classes to keep
	 * @return the filtered stream
	 */
	public ClassStream intersectionWithClasses(ClassStream... intersectWith) {
		return new ClassStream(this, new ClassSubdetectors.IntersectionWithClasses(intersectWith));
	}
	
	/**
	 * Queries the classes contained in this stream for their direct subclass and filters
	 * these for concrete classes.
	 * @return the direct concrete subclasses of the classes contained in this stream
	 */
	public ClassStream firstConcreteSubclasses() {
		return new ClassStream(this, new ClassSubdetectors.FirstConcreteSubclasses());
	}
	
}
