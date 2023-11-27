package nl.ou.refd.analysis;

import nl.ou.refd.locations.collections.LabeledLocationSet;

/**
 * Interface representing a danger aggregator, which is an object
 * that can be used to store multiple dangers in. The dangers are
 * represented as LabeledLocationSet objects.
 */
public interface DangerAggregator {
	
	/**
	 * Adds the given dangers to the pool.
	 * @param dangers the given dangers
	 */
	void aggregateDangers(LabeledLocationSet dangers);
}
