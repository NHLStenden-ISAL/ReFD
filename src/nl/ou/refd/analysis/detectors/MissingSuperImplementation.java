package nl.ou.refd.analysis.detectors;

import nl.ou.refd.analysis.DetectorVisitor;
import nl.ou.refd.locations.collections.ClassSet;
import nl.ou.refd.locations.collections.MethodSet;
import nl.ou.refd.locations.specifications.MethodSpecification;
import nl.ou.refd.locations.streams.MethodStream;

/**
 * A collection of classes which represent MissingSuperImplementation detectors,
 * each with its own type of context.
 */
public final class MissingSuperImplementation {
	private MissingSuperImplementation(){}
	
	/**
	 * Class representing a MissingSuperImplementation detector for a method.
	 * A detector checks the program graph for potential risks. If it finds
	 * these, they are determined to be actual risks.
	 */
	public static class Method extends Detector<ClassSet> {
		
		private final MethodSpecification subject;
		
		/**
		 * Creates the detector with its context.
		 * @param subject the context
		 */
		public Method(MethodSpecification subject) {
			this.subject = subject;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public ClassSet actualRisks() {
			MethodStream concreteMethod = new MethodSet(subject).stream().concreteMethods();
			
			return concreteMethod.parentClasses()
				.directSubclasses()
				.differenceWithClasses(
						concreteMethod.overriddenBy()
						.parentClasses()
				).collect();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void accept(DetectorVisitor visitor) {
			visitor.visit(this);
		}
		
	}

}
