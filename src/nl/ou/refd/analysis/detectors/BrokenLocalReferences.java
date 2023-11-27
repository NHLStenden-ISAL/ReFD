package nl.ou.refd.analysis.detectors;

import nl.ou.refd.analysis.DetectorVisitor;
import nl.ou.refd.locations.collections.InstructionSet;
import nl.ou.refd.locations.streams.ClassStream;
import nl.ou.refd.locations.streams.InstructionStream;

/**
 * A collection of classes which represent BrokenLocalReferences detectors,
 * each with its own type of context.
 */
public final class BrokenLocalReferences {
	private BrokenLocalReferences(){}
	
	/**
	 * Class representing a BrokenLocalReferences detector for a body of code.
	 * A detector checks the program graph for potential risks. If it finds
	 * these, they are determined to be actual risks.
	 */
	public static class Body extends Detector<InstructionSet> {
		
		private final InstructionStream existingBody;
		
		/**
		 * Creates the detector with its context.
		 * @param existingBody the context
		 */
		public Body(InstructionStream existingBody) {			
			this.existingBody = existingBody;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public InstructionSet actualRisks() {
			ClassStream localClassContext = existingBody.parentMethods().parentClasses();
			InstructionStream body = existingBody;
			
			return body.methodCalls().union(body.fieldCalls())
			.intersectionWithInstructions(
				localClassContext
				.methods()
				.methodsCalledAt()
				.union(localClassContext
						.fields()
						.fieldsCalledAt()
					)
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
