package nl.ou.refd.analysis.microsteps;

import nl.ou.refd.analysis.ModelVisitor;
import nl.ou.refd.analysis.detectors.BrokenSubTyping;
import nl.ou.refd.analysis.detectors.CorrespondingSubclassSpecification;
import nl.ou.refd.analysis.detectors.DoubleDefinition;
import nl.ou.refd.analysis.detectors.OverloadParameterConversion;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.specifications.MethodSpecification;

/**
 * Class representing an AddMethod microstep, an altering operation on a codebase
 * which adds a method to a class. A microstep contains a number of detectors which
 * detect possible dangers (potential risks) when the microstep would be executed
 * on the codebase. The microstep can also be executed on the model of the
 * codebase to simulate its execution.
 */
public class AddMethod extends Microstep {
	
	private final MethodSpecification methodToAdd;
	
	/**
	 * Creates the microstep with specification of the method to add.
	 * @param classToAdd the specification of the method to add
	 */
	public AddMethod(MethodSpecification methodToAdd) {
		this.methodToAdd = methodToAdd;
		
		potentialRisk(new DoubleDefinition.Method(methodToAdd));
		potentialRisk(new BrokenSubTyping.Method(methodToAdd));
		potentialRisk(new CorrespondingSubclassSpecification.Method(methodToAdd));
		potentialRisk(new OverloadParameterConversion.Method(methodToAdd));
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
		this.methodToAdd.construct(graph);
	}

}
