package nl.ou.refd.locations.streams;

import nl.ou.refd.analysis.subdetectors.ProgramComponentSubdetectors;
import nl.ou.refd.analysis.subdetectors.Subdetector;
import nl.ou.refd.locations.collections.ProgramComponentSet;
import nl.ou.refd.locations.generators.LocationGenerator;

/**
 * Class which represents a stream of program components. Much like a Java stream,
 * this stream chains a number of operations called subdetectors which operate
 * on these program locations. Through these subdetectors, the program locations
 * can be queried in this way.
 */
public class ProgramComponentStream extends Stream {

	/**
	 * Creates a stream of program components from a collection of methods.
	 * @param source the collection of classes
	 */
	public ProgramComponentStream(ProgramComponentSet source) {
		super(source);
	}
	
	/**
	 * Creates a stream of program components from a generator of locations
	 * within a program.
	 * @param source a generator of locations within a program
	 */
	public ProgramComponentStream(LocationGenerator source) {
		super(source);
	}
	
	/**
	 * Creates a stream from a previous stream and a next step in the stream, being
	 * the subdetector. The previous stream is not changed, and this stream works on
	 * a copy of that stream.
	 * @param precursor the previous stream
	 * @param next the next step in the chain within the stream
	 */
	protected ProgramComponentStream(Stream precursor, Subdetector next) {
		super(precursor, next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProgramComponentSet collect() {
		return new ProgramComponentSet(this.locations());
	}
	
	/**
	 * Filters the program components and keeps only those which are classes.
	 * @return program components which are classes
	 */
	public ClassStream classes() {
		return new ClassStream(this, new ProgramComponentSubdetectors.Classes());
	}
}
