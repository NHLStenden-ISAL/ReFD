package nl.ou.refd.locations.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl.ou.refd.analysis.subdetectors.Subdetector;
import nl.ou.refd.locations.collections.LocationSet;
import nl.ou.refd.locations.graph.ProgramLocation;

/**
 * Class which represents a stream of ProgramLocations. Much like a Java stream,
 * this stream chains a number of operations called subdetectors which operate
 * on these program locations. Through these subdetectors, the program locations
 * can be queried in this way.
 */
public abstract class Stream {

	private final LocationSet source;
	private List<Subdetector> subdetectorChain;
	
	/**
	 * Creates a stream of program locations.
	 * @param source the source collection of this stream
	 */
	public Stream(LocationSet source) {
		this.source = source;
		this.subdetectorChain = new ArrayList<Subdetector>();
	}
	
	/**
	 * Creates a stream from a previous stream and a next step in the stream, being
	 * the subdetector. The previous stream is not changed, and this stream works on
	 * a copy of that stream.
	 * @param precursor the previous stream
	 * @param next the next step in the chain within the stream
	 */
	protected Stream(Stream precursor, Subdetector next) {
		this.source = precursor.source;
		this.subdetectorChain = new ArrayList<Subdetector>(precursor.subdetectorChain);
		this.subdetectorChain.add(next);
	}
	
	/**
	 * Returns the locations resulting from this stream as a Set of ProgramLocation objects.
	 * This method is only available in this package.
	 * @return a Set of Programlocation objects resulting from the stream
	 */
	protected Set<ProgramLocation> locations() {
		Set<ProgramLocation> temp = this.source.locations();
		for (Subdetector s : this.subdetectorChain) {
			temp = s.applyOn(temp);
		}
		return temp;
	}
	
	/**
	 * Collects and returns the locations resulting from executing the stream
	 * as an appropriate subclass of LocationCollection.
	 * @return a typed collection of program locations
	 */
	public abstract LocationSet collect();

}
