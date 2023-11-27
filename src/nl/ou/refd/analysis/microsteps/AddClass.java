package nl.ou.refd.analysis.microsteps;

import nl.ou.refd.analysis.ModelVisitor;
import nl.ou.refd.analysis.detectors.DoubleDefinition;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.specifications.ClassSpecification;

/**
 * Class representing an AddClass microstep, an altering operation on a codebase
 * which adds a class to it. A microstep contains a number of detectors which
 * detect possible dangers (potential risks) when the microstep would be executed
 * on the codebase. The microstep can also be executed on the model of the
 * codebase to simulate its execution.
 */
public class AddClass extends Microstep {
	
	private final ClassSpecification classToAdd;
	
	/**
	 * Creates the microstep with specification of the class to add.
	 * @param classToAdd the specification of the class to add
	 */
	public AddClass(ClassSpecification classToAdd) {
		this.classToAdd = classToAdd;
		
		potentialRisk(new DoubleDefinition.Class(classToAdd));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(ModelVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void executeOnGraph(Graph graph) {
		this.classToAdd.construct(graph);
	}

}
