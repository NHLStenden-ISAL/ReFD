package nl.ou.refd.analysis.microsteps;

import nl.ou.refd.analysis.ModelVisitor;
import nl.ou.refd.analysis.detectors.BrokenLocalReferences;
import nl.ou.refd.locations.specifications.MethodSpecification;

/**
 * Class representing a MoveMethod microstep, an altering operation on a codebase
 * which moves a method. A microstep contains a number of detectors which
 * detect possible dangers (potential risks) when the microstep would be executed
 * on the codebase. The microstep can also be executed on the model of the
 * codebase to simulate its execution.
 */
public class MoveMethod extends CompositeMicrostep {

	/**
	 * Creates the microstep with specification of the method to move, and a
	 * specification of a class to move the method to.
	 * @param target the method to move
	 * @param destination the destination class to move the method to
	 */
	public MoveMethod(MethodSpecification target, MethodSpecification destination) {
		potentialRisk(new BrokenLocalReferences.Body(target.getBody(), destination.getEnclosingClass()));
		
		compositeMicrostep(new AddMethod(destination));
		compositeMicrostep(new RemoveMethod(target));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(ModelVisitor visitor) {
		visitor.visit(this);
	}

}
