package nl.ou.refd.analysis.subdetectors;

import java.util.Set;

import nl.ou.refd.locations.collections.InstructionSet;
import nl.ou.refd.locations.generators.ProgramComponentsGenerator;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.streams.InstructionStream;

/**
 * Class containing all subdetectors that work on sets of instruction locations.
 */
public final class InstructionSubdetectors {
	private InstructionSubdetectors(){}
	
	/**
	 * Queries the methods the provided instructions belong to.
	 * @return the methods the provided instructions belong to
	 */
	public static class ParentMethods extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return
					new ProgramComponentsGenerator()
					.stream()
					.classes()
					.methods()
					.containInstruction(
							new InstructionSet(locations)
							.stream()
					)
					.collect()
					.locations();
		}
	}
	
	/**
	 * Integrates incoming streams of instructions unionWith into the provided
	 * set of instructions, removing duplicates.
	 * @param unionWith instructions to add to current set, removing duplicates
	 * @return the result of integrating both sets
	 */
	public static class Union extends Subdetector {
		
		private final InstructionStream[] unionWith;
		
		public Union(InstructionStream... unionWith) {
			this.unionWith = unionWith;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).union(streamVarArgToGraphQueryArray(unionWith)).locations();
		}
	}
	
	/**
	 * Filters the provided set of instruction locations and keeps only instructions also contained
	 * within incoming streams intersectWith.
	 * @param intersectWith the streams of instruction locations to keep
	 * @return the filtered set
	 */
	public static class IntersectionWithInstructions extends Subdetector {
		
		private final InstructionStream[] intersectWith;
		
		public IntersectionWithInstructions(InstructionStream... intersectWith) {
			this.intersectWith = intersectWith;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).contained().intersection(streamVarArgToGraphQueryArray(intersectWith)).locations();
		}
	}
	
	/**
	 * Filters the provided set of instruction locations and keeps only the instructions representing
	 * calls to fields.
	 * @return instructions representing calls to fields
	 */
	public static class FieldCalls extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return
					new ProgramComponentsGenerator()
					.stream()
					.classes()
					.fields()
					.fieldsCalledAt()
					.intersectionWithInstructions(
							new InstructionSet(Graph.query(locations).contained().locations()).stream()
					)
					.collect()
					.locations();
		}
	}
	
	/**
	 * Filters the set of instruction locations and keeps only the instructions representing
	 * calls to methods.
	 * @return instructions representing calls to methods
	 */
	public static class MethodCalls extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return
					new ProgramComponentsGenerator()
					.stream()
					.classes()
					.methods()
					.methodsCalledAt()
					.intersectionWithInstructions(
							new InstructionSet(Graph.query(locations).contained().locations()).stream()
					)
					.collect()
					.locations();
		}
	}
}
