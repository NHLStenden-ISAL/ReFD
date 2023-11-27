package nl.ou.refd.analysis.detectors;

import nl.ou.refd.analysis.DetectorVisitor;
import nl.ou.refd.locations.collections.MethodSet;
import nl.ou.refd.locations.specifications.MethodSpecification;

/**
 * A collection of classes which represent LostSpecification detectors,
 * each with its own type of context.
 */
public final class LostSpecification {
	private LostSpecification(){}
	
	/**
	 * Class representing a LostSpecification detector for a method.
	 * A detector checks the program graph for potential risks. If it finds
	 * these, they are determined to be actual risks.
	 */
	public static class Method extends Detector<MethodSet> {
		
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
		public MethodSet actualRisks() {
			return new MethodSet(subject)
					.stream()
					.overriddenBy()
					.collect();
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
