package nl.ou.refd.analysis.subdetectors;

import java.util.Set;

import nl.ou.refd.locations.collections.ClassSet;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.GraphQuery;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;
import nl.ou.refd.locations.streams.ClassStream;

/**
 * Class containing all subdetectors that work on sets of class locations.
 */
public final class ClassSubdetectors {
	private ClassSubdetectors(){}
	
	/**
	 * Filters the provided classes by name.
	 * @param className the name of the class
	 * @return a set containing classes with name specified by className
	 */
	public static class ClassesByName extends Subdetector {
		
		private final String className;
		
		public ClassesByName(String className) {
			this.className = className;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations)
					.types(className)
					.locations();
		}
	}

	/**
	 * Queries the direct superclasses of the provided classes.
	 * @return the direct superclasses of the provided classes
	 */
	public static class DirectSuperClasses extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).successorsOn(
					Graph.query().universe().relations(Tags.Relation.EXTENDS)
			)
			.locations(Tags.ProgramLocation.CLASS)
			.locations();
		}
	}

	/**
	 * Queries all superclasses of the provided classes.
	 * @return all superclasses of the provided classes
	 */
	public static class AllSuperClasses extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).descendantsOn(Graph.query().universe()
					.relations(Tags.Relation.EXTENDS))
			.locations(Tags.ProgramLocation.CLASS)
			.locations();
		}
	}

	/**
	 * Queries all methods contained in the provided classes.
	 * @return all methods contained in the provided classes
	 */
	public static class Methods extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).forwardOn(Graph.query().universe().relations(Tags.Relation.DECLARES))
					.locations(Tags.ProgramLocation.METHOD)
					.locations(Tags.ProgramLocation.ABSTRACT_METHOD, Tags.ProgramLocation.INSTANCE_METHOD, Tags.ProgramLocation.CLASS_METHOD)
					.locations();
		}
	}

	/**
	 * Filters provided classes and keeps only the abstract classes.
	 * @return the abstract classes of provided classes
	 */
	public static class AbstractClasses extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations)
					.locations(Tags.ProgramLocation.ABSTRACT_CLASS)
					.locations();
		}
	}

	/**
	 * Removes the classes resulting from differenceWith from the provided classes
	 * @param differenceWith the classes to remove
	 * @return the remaining classes not present in differenceWith
	 */
	public static class DifferenceWithClasses extends Subdetector {
		
		private final ClassStream[] differenceWith;
		
		public DifferenceWithClasses(ClassStream... differenceWith) {
			this.differenceWith = differenceWith;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations)
					.difference(streamVarArgToGraphQueryArray(differenceWith))
					.locations();
		}
	}

	/**
	 * Filters provided classes and keeps only the concrete classes.
	 * @return the concrete classes of provided classes
	 */
	public static class ConcreteClasses extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			GraphQuery gq = Graph.query(locations);
			return gq
					.locations(Tags.ProgramLocation.CLASS).difference(gq.locations(Tags.ProgramLocation.ABSTRACT_CLASS))
					.locations();
		}
	}

	/**
	 * Queries the direct subclasses of the provided classes in this stream.
	 * @return the direct subclasses of the provided classes in this stream
	 */
	public static class DirectSubclasses extends Subdetector {
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			GraphQuery gq = Graph.query(locations);
			return gq.predecessorsOn(gq.universe().relations(Tags.Relation.EXTENDS)).locations(Tags.ProgramLocation.CLASS).locations();
		}
	}

	/**
	 * Queries all subclasses of the provided classes.
	 * @return all subclasses of the provided classes
	 */
	public static class AllSubclasses extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			GraphQuery gq = Graph.query(locations);
			return gq.ancestorsOn(gq.universe().relations(Tags.Relation.EXTENDS)).locations(Tags.ProgramLocation.CLASS).locations();
		}
	}

	/**
	 * Integrates sets from incoming class streams unionWith into the provided set, removing duplicates.
	 * @param unionWith classes to add to current set, removing duplicates
	 * @return the result of integrating both sets
	 */
	public static class UnionWithClasses extends Subdetector {
		
		private final ClassStream[] unionWith;
		
		public UnionWithClasses(ClassStream... unionWith) {
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
	 * Queries all fields contained in the provided classes.
	 * @return all fields contained in the provided classes
	 */
	public static class Fields extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).contained().locations(Tags.ProgramLocation.FIELD).locations();
		}
	}

	/**
	 * Filters the provided classes and keeps only classes also contained
	 * within incoming streams intersectWith.
	 * @param intersectWith the streams of sets of classes to keep
	 * @return the filtered set
	 */
	public static class IntersectionWithClasses extends Subdetector {
		
		private final ClassStream[] intersectWith;
		
		public IntersectionWithClasses(ClassStream... intersectWith) {
			this.intersectWith = intersectWith;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).intersection(streamVarArgToGraphQueryArray(intersectWith)).locations();
		}
	}

	/**
	 * Queries the provided classes for their direct subclass and filters these for
	 * concrete classes.
	 * @return the direct concrete subclasses of the provided
	 */
	public static class FirstConcreteSubclasses extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			ClassStream s = new ClassSet(locations).stream();
			
			return
				s.allSubclasses()
				.abstractClasses()
				.directSubclasses()
				.concreteClasses()
				.unionWithClasses(
						s.directSubclasses()
						.concreteClasses()
				)
				.collect()
				.locations();
		}
	}
}
