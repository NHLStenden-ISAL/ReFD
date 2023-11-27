package nl.ou.refd.analysis.microsteps;

import java.util.HashSet;
import java.util.Set;

import nl.ou.refd.analysis.ModelNode;
import nl.ou.refd.analysis.detectors.Detector;
import nl.ou.refd.locations.graph.Graph;

/**
 * Class representing a microstep, an altering operation on a codebase.
 * A microstep contains a number of detectors which detect possible
 * dangers (potential risks) when the microstep would be executed
 * on the codebase. The microstep can also be executed on the model
 * of the codebase to simulate its execution.
 */
public abstract class Microstep implements ModelNode {
	
	private Set<Detector<?>> potentialRisks = new HashSet<>();
	
	/**
	 * Adds a potential risk in the form of a detector.
	 * @param d the detector representing a potential risk
	 */
	protected void potentialRisk(Detector<?> d) {
		potentialRisks.add(d);
	}
	
	/**
	 * Gets the set of detectors contained in this microstep.
	 * @return the set of detectors contained in this microstep
	 */
	public Set<Detector<?>> getDetectors() {
		return new HashSet<Detector<?>>(this.potentialRisks);
	}
	
	/**
	 * Executes the microstep on a program graph, thus simulating the change.
	 * @param graph the graph to simulate the change on
	 */
	public abstract void executeOnGraph(Graph graph);
}
