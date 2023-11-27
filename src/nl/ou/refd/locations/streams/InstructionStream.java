package nl.ou.refd.locations.streams;

import nl.ou.refd.analysis.subdetectors.InstructionSubdetectors;
import nl.ou.refd.analysis.subdetectors.Subdetector;
import nl.ou.refd.locations.collections.InstructionSet;

/**
 * Class which represents a stream of instructions. Much like a Java stream,
 * this stream chains a number of operations called subdetectors which operate
 * on these program locations. Through these subdetectors, the program locations
 * can be queried in this way.
 */
public class InstructionStream extends Stream {

	/**
	 * Creates a stream of instructions from a collection of methods.
	 * @param source the collection of classes
	 */
	public InstructionStream(InstructionSet source) {
		super(source);
	}
	
	/**
	 * Creates a stream from a previous stream and a next step in the stream, being
	 * the subdetector. The previous stream is not changed, and this stream works on
	 * a copy of that stream.
	 * @param precursor the previous stream
	 * @param next the next step in the chain within the stream
	 */
	protected InstructionStream(Stream precursor, Subdetector next) {
		super(precursor, next);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InstructionSet collect() {
		return new InstructionSet(this.locations());
	}

	/**
	 * Queries the methods the instructions contained in this stream belong to.
	 * @return the methods the instructions contained in this stream belong to
	 */
	public MethodStream parentMethods() {
		return new MethodStream(this, new InstructionSubdetectors.ParentMethods());
	}
	
	/**
	 * Integrates incoming streams unionWith into this stream, removing duplicates.
	 * @param unionWith instructions to add to current stream, removing duplicates
	 * @return the result of integrating both streams
	 */
	public InstructionStream union(InstructionStream... unionWith) {
		return new InstructionStream(this, new InstructionSubdetectors.Union(unionWith));
	}
	
	/**
	 * Filters the current stream of instructions and keeps only instructions also contained
	 * within incoming streams intersectWith.
	 * @param intersectWith the streams of instructions to keep
	 * @return the filtered stream
	 */
	public InstructionStream intersectionWithInstructions(InstructionStream... intersectWith) {
		return new InstructionStream(this, new InstructionSubdetectors.IntersectionWithInstructions(intersectWith));
	}
	
	/**
	 * Filters the stream of instructions and keeps only the instructions representing
	 * calls to fields.
	 * @return instructions representing calls to fields
	 */
	public InstructionStream fieldCalls() {
		return new InstructionStream(this, new InstructionSubdetectors.FieldCalls());
	}
	
	/**
	 * Filters the stream of instructions and keeps only the instructions representing
	 * calls to methods.
	 * @return instructions representing calls to methods
	 */
	public InstructionStream methodCalls() {
		return new InstructionStream(this, new InstructionSubdetectors.MethodCalls());
	}
}
