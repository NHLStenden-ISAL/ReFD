package nl.ou.refd.analysis.detectors;

import nl.ou.refd.analysis.DetectorVisitor;
import nl.ou.refd.locations.collections.ClassSet;
import nl.ou.refd.locations.collections.MethodSet;
import nl.ou.refd.locations.specifications.MethodSpecification;

/**
 * A collection of classes which represent CorrespondingSubclassSpecification detectors,
 * each with its own type of context.
 */
public final class CorrespondingSubclassSpecification {
	private CorrespondingSubclassSpecification(){}
	
	/**
	 * Class representing a CorrespondingSubclassSpecification detector for a method.
	 * A detector checks the program graph for potential risks. If it finds
	 * these, they are determined to be actual risks.
	 */
	public static class Method extends Detector<MethodSet> {
		
		private final MethodSpecification methodToAdd;
		
		/**
		 * Creates the detector with its context.
		 * @param methodToAdd the context
		 */
		public Method(MethodSpecification methodToAdd) {
			this.methodToAdd = methodToAdd;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public MethodSet actualRisks() {
			return
				new ClassSet(methodToAdd.getEnclosingClass())
				.stream()
				.allSubclasses()
				.methods()
				.methodsWithSignature(methodToAdd.getMethodName(),
					methodToAdd.getParameterTypes())
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
