package nl.ou.refd.analysis.refactorings;

import java.util.ArrayList;
import java.util.List;

import nl.ou.refd.analysis.DangerAggregator;
import nl.ou.refd.analysis.VerdictFunction;
import nl.ou.refd.analysis.microsteps.Microstep;

/**
 * Class representing a refactoring. This refactoring can be analyzed by
 * using a DangerAnalyzer object.
 */
public abstract class Refactoring {
	
	private List<Microstep> microsteps = new ArrayList<Microstep>();
	
	/**
	 * Adds a microstep to the refactoring.
	 * @param m the refactoring to add
	 */
	protected void microstep(Microstep m) {
		this.microsteps.add(m);
	}
	
	/**
	 * Returns a VerdictFunction object which filters detectors and their outcomes
	 * in the context of this refactoring.
	 * @param aggregator the aggregator to aggregate found dangers in
	 * @return the VerdictFunction for this refactoring
	 */
	public abstract VerdictFunction verdictFunction(DangerAggregator aggregator);
	
	/**
	 * Gets a shallow copy of the list of microsteps addded to this refactoring.
	 * @returna shallow copy of the list of microsteps addded to this refactoring
	 */
	public List<Microstep> getMicrosteps() {
		return new ArrayList<Microstep>(this.microsteps);
	}
}
