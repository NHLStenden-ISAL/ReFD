package nl.ou.refd.locations.streams;

import nl.ou.refd.analysis.subdetectors.FieldSubdetectors;
import nl.ou.refd.analysis.subdetectors.Subdetector;
import nl.ou.refd.locations.collections.FieldSet;

/**
 * Class which represents a stream of fields. Much like a Java stream,
 * this stream chains a number of operations called subdetectors which operate
 * on these program locations. Through these subdetectors, the program locations
 * can be queried in this way.
 */
public class FieldStream extends Stream {

	/**
	 * Creates a stream of fields from a collection of fields.
	 * @param source the collection of fields
	 */
	public FieldStream(FieldSet source) {
		super(source);
	}
	
	/**
	 * Creates a stream from a previous stream and a next step in the stream, being
	 * the subdetector. The previous stream is not changed, and this stream works on
	 * a copy of that stream.
	 * @param precursor the previous stream
	 * @param next the next step in the chain within the stream
	 */
	protected FieldStream(Stream precursor, Subdetector next) {
		super(precursor, next);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldSet collect() {
		return new FieldSet(this.locations());
	}
	
	/**
	 * Filters stream for fields belonging to instances of classes.
	 * @return the fields contained within this stream that belong to instances of classes
	 */
	public FieldStream instanceFields() {
		return new FieldStream(this, new FieldSubdetectors.InstanceFields());
	}
	
	/**
	 * Filters stream for static fields belonging to classes.
	 * @return the static fields contained within this stream that belong to classes
	 */
	public FieldStream staticFields() {
		return new FieldStream(this, new FieldSubdetectors.StaticFields());
	}
	
	/**
	 * Queries the locations in the codebase where the fields contained in this stream are called.
	 * @return the callsites of the fields contained within this stream
	 */
	public InstructionStream fieldsCalledAt() {
		return new InstructionStream(this, new FieldSubdetectors.FieldsCalledAt());
	}

	/**
	 * Filters the contained fields by name.
	 * @param fieldName the name of the field
	 * @return a stream containing fields with name specified by fieldName
	 */
	public FieldStream filterByName(String fieldName) {
		return new FieldStream(this, new FieldSubdetectors.FilterByName(fieldName));
	}
	
	/**
	 * Queries the classes the fields belong to.
	 * @return the classes the fields belong to
	 */
	public ClassStream parentClasses() {
		return new ClassStream(this, new FieldSubdetectors.ParentClasses());
	}
	
	/**
	 * Filters the current stream of fields and keeps only fields also contained
	 * within incoming streams intersectWith.
	 * @param intersectWith the streams of fields to keep
	 * @return the filtered stream
	 */
	public FieldStream intersectionWithFields(FieldStream... intersectWith) {
		return new FieldStream(this, new FieldSubdetectors.IntersectionWithFields(intersectWith));
	}
}
