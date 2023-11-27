package nl.ou.refd.analysis.microsteps;

import java.util.ArrayList;
import java.util.List;

import nl.ou.refd.locations.graph.Graph;

/**
 * Class representing a composite microstep, an altering operation on a codebase.
 * A microstep contains a number of detectors which detect possible
 * dangers (potential risks) when the microstep would be executed
 * on the codebase. The microstep can also be executed on the model
 * of the codebase to simulate its execution.
 * 
 * This specific microstep can also contain other microsteps.
 */
public abstract class CompositeMicrostep extends Microstep {

	private final List<Microstep> compositeMicrosteps = new ArrayList<Microstep>();
	
	/**
	 * Creates the composite microstep from another microstep.
	 * @param microstep the microstep to create the composite microstep from
	 */
	protected void compositeMicrostep(Microstep microstep) {
		this.compositeMicrosteps.add(microstep);
	}
	
	/**
	 * Gets the composite microsteps.
	 * @return the composite microsteps
	 */
	public List<Microstep> getComponentMicrosteps() {
		return new ArrayList<Microstep>(this.compositeMicrosteps);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void executeOnGraph(Graph graph) {
		this.getComponentMicrosteps().forEach(microstep -> microstep.executeOnGraph(graph));
	}

}
