package nl.ou.refd.analysis.refactorings;

import nl.ou.refd.analysis.DangerAggregator;
import nl.ou.refd.analysis.VerdictFunction;
import nl.ou.refd.analysis.detectors.BrokenLocalReferences;
import nl.ou.refd.analysis.detectors.CorrespondingSubclassSpecification;
import nl.ou.refd.analysis.detectors.LostSpecification;
import nl.ou.refd.analysis.detectors.MissingAbstractImplementation;
import nl.ou.refd.analysis.detectors.MissingDefinition;
import nl.ou.refd.analysis.detectors.MissingSuperImplementation;
import nl.ou.refd.analysis.detectors.RemovedConcreteOverride;
import nl.ou.refd.analysis.microsteps.MoveMethod;
import nl.ou.refd.locations.collections.ClassSet;
import nl.ou.refd.locations.collections.MethodSet;
import nl.ou.refd.locations.specifications.ClassSpecification;
import nl.ou.refd.locations.specifications.MethodSpecification;
import nl.ou.refd.locations.streams.ClassStream;
import nl.ou.refd.locations.streams.InstructionStream;

/**
 * Class representing a Pull Up Method refactoring. This refactoring can be analyzed by
 * using a DangerAnalyzer object.
 */
public class PullUpMethod extends Refactoring {

	private final MethodSpecification target;
	
	private final boolean toDirectSuperclass;
	
	/**
	 * Creates the Pull Up Method refactoring with a target method that should be
	 * pull upped, and a destination class the method should be pull upped to.
	 * @param target method that should be pull upped
	 * @param destination class the method should be pull upped to
	 */
	public PullUpMethod(MethodSpecification target, ClassSpecification destination) {
		this.target = target;
		this.toDirectSuperclass = 
				new ClassSet(target.getEnclosingClass())
				.stream()
				.directSuperClasses()
				.intersectionWithClasses(
						new ClassSet(destination).stream()
				).collect().size() > 0;
		
		MethodSpecification newLocation = target.copy();
		newLocation.setEnclosingClass(destination);
		
		microstep(new MoveMethod(target, newLocation));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VerdictFunction verdictFunction(DangerAggregator aggregator) {
		
		return new VerdictFunction(aggregator) {
			@Override
			public void visit(CorrespondingSubclassSpecification.Method detector) {
				partial(detector, detector.actualRisks().stream().differenceWithMethods(new MethodSet(target).stream()).collect());
			}
			
			@Override
			public void visit(MissingDefinition.Method detector) {
				none(detector);
			}
			
			@Override
			public void visit(MissingAbstractImplementation.Method detector) {
				none(detector);
			}
			
			@Override
			public void visit(RemovedConcreteOverride.Method detector) {
				if (toDirectSuperclass) {
					none(detector);
				}
				else {
					all(detector);
				}
			}
			
			@Override
			public void visit(LostSpecification.Method detector) {
				if (toDirectSuperclass) {
					none(detector);
				}
				else {
					all(detector);
				}
			}
			
			@Override
			public void visit(MissingSuperImplementation.Method detector) {
				if (toDirectSuperclass) {
					none(detector);
				}
				else {
					all(detector);
				}
			}
			
		};
		
	}
	
}
