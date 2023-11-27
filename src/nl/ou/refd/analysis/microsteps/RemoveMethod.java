package nl.ou.refd.analysis.microsteps;

import nl.ou.refd.analysis.ModelVisitor;
import nl.ou.refd.analysis.detectors.LostSpecification;
import nl.ou.refd.analysis.detectors.MissingAbstractImplementation;
import nl.ou.refd.analysis.detectors.MissingDefinition;
import nl.ou.refd.analysis.detectors.MissingSuperImplementation;
import nl.ou.refd.analysis.detectors.RemovedConcreteOverride;
import nl.ou.refd.locations.collections.MethodSet;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.specifications.MethodSpecification;

/**
 * Class representing a RemoveMethod microstep, an altering operation on a codebase
 * which removes a method. A microstep contains a number of detectors which
 * detect possible dangers (potential risks) when the microstep would be executed
 * on the codebase. The microstep can also be executed on the model of the
 * codebase to simulate its execution.
 */
public class RemoveMethod extends Microstep {

	private MethodSpecification subject;
	
	/**
	 * Creates the microstep with a specification of the method to remove.
	 * @param subject the method to remove
	 */
	public RemoveMethod(MethodSpecification subject) {
		this.subject = subject;
		
		potentialRisk(new MissingDefinition.Method(subject));
		potentialRisk(new RemovedConcreteOverride.Method(subject));
		potentialRisk(new LostSpecification.Method(subject));
		potentialRisk(new MissingSuperImplementation.Method(subject));
		potentialRisk(new MissingAbstractImplementation.Method(subject));
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
		graph.removeProgramLocation(new MethodSet(this.subject).singleLocation());
	}

}
