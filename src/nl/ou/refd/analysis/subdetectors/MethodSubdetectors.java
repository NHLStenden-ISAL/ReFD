package nl.ou.refd.analysis.subdetectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refd.analysis.subdetectors.Constants.Types;
import nl.ou.refd.locations.collections.MethodSet;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.GraphQuery;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;
import nl.ou.refd.locations.specifications.MethodSpecification;
import nl.ou.refd.locations.specifications.ParameterSpecification;
import nl.ou.refd.locations.specifications.LocationSpecification.AccessModifier;
import nl.ou.refd.locations.streams.InstructionStream;
import nl.ou.refd.locations.streams.MethodStream;

/**
 * Class containing all subdetectors that work on sets of method locations.
 */
public final class MethodSubdetectors {
	private MethodSubdetectors(){}
	
	/**
	 * Checks if the provided primitive type2 is narrower than primitive type type1.
	 * <br><br>
	 * Lists of type precedence from OCP Oracle Certified Professional Java SE 17 Developer (Exam 1ZO-829) Programmer's Guide
	 * by Khalid A. Mughal, Vasily A. Strelnikov.
	 * @param type1 the reference type
	 * @param type2 the type to check narrowing for
	 * @return true if type2 is narrowing, false otherwise
	 */
	private static boolean isNarrowingConversion(String type1, String type2) {
		final List<String> CHAR_RELATIONS = Arrays.asList(Types.CHAR, Types.INT, Types.LONG, Types.FLOAT, Types.DOUBLE);
		final List<String> BYTE_RELATIONS = Arrays.asList(Types.BYTE, Types.SHORT, Types.INT, Types.LONG, Types.FLOAT, Types.DOUBLE);
		
		List<String> l = (type1 == Types.CHAR || type2 == Types.CHAR) ? CHAR_RELATIONS : BYTE_RELATIONS;
		
		int type1Index = l.indexOf(type1);
		int type2Index = l.indexOf(type2);
		
		if(type1Index < 0 || type2Index < 0) {
			//Given type string is not a primitive type
			return false;
		}
		
		return type1Index - type2Index > 0;
	}
	
	/**
	 * Filters the provided method locations by name.
	 * @param methodName the name of the method
	 * @return a set containing method locations with name specified by methodName
	 */
	public static class FilterByName extends Subdetector {
		
		private final String methodName;
		
		public FilterByName(String methodName) {
			this.methodName = methodName;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).methods(methodName).locations();
		}
	}

	/**
	 * Queries the bodies of the provided method locations.
	 * @return the bodies of the provided method locations
	 */
	public static class Bodies extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).forwardDifference(Tags.Relation.HAS_CONTROL_FLOW).locations();
		}
	}

	/**
	 * Filters provided method locations and only keeps those with a return type
	 * specified by type, or a covariant type of that.
	 * @param type the return type, or covariant of, to filter with
	 * @return method locations with a return type specified by type, or a covariant type of that
	 */
	public static class MethodsWithCovariantReturnTypes extends Subdetector {
		
		private final String type;
		
		public MethodsWithCovariantReturnTypes(String type) {
			this.type = type;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			GraphQuery gq = Graph.query(locations);
			GraphQuery qType = gq.universe().types(type);
			
			return gq
					// add returns edges and their nodes
					.successorsOn(gq.universe().relations(Tags.Relation.RETURNS))
					// remove every Type that is not type or a subtype of type
					.difference(qType.ancestorsOn(gq.universe().relations(Tags.Relation.SUPERTYPE)))

					// removes nodes without edges(methods without a type)
					.predecessorsOn(gq.universe().relations(Tags.Relation.RETURNS)).intersection(gq)
					// keep only the methods
					.locations(Tags.ProgramLocation.METHOD)
					.locations();
		}
	}

	/**
	 * Filters provided method locations and only keeps those with a visibility
	 * equal or greater than the supplied AccessModifier.
	 * @param visibility the minimal visibility of the method
	 * @return methods with a visibility equal or greater than the supplied AccessModifier
	 */
	public static class MethodsEquallyOrMoreVisible extends Subdetector {
		
		private final AccessModifier visibility;
		
		public MethodsEquallyOrMoreVisible(AccessModifier visibility) {
			this.visibility = visibility;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			GraphQuery gq = Graph.query(locations);
			
			GraphQuery resultMethods = Graph.query();
			
			switch (this.visibility) {
				case PUBLIC:
					resultMethods = resultMethods.union(gq.locations(Tags.ProgramLocation.PUBLIC_VISIBILITY));
				case PROTECTED:
					resultMethods = resultMethods.union(gq.locations(Tags.ProgramLocation.PUBLIC_VISIBILITY,
																 Tags.ProgramLocation.PROTECTED_PACKAGE_VISIBILITY));
				case PACKAGE:
					resultMethods = resultMethods.union(gq.locations(Tags.ProgramLocation.PUBLIC_VISIBILITY,
																 Tags.ProgramLocation.PACKAGE_VISIBILITY,
																 Tags.ProgramLocation.PROTECTED_PACKAGE_VISIBILITY));
				case PRIVATE:
					resultMethods = resultMethods.union(gq.locations(Tags.ProgramLocation.PRIVATE_VISIBILITY,
																 Tags.ProgramLocation.PACKAGE_VISIBILITY,
																 Tags.ProgramLocation.PROTECTED_PACKAGE_VISIBILITY,
																 Tags.ProgramLocation.PUBLIC_VISIBILITY));
			}
			return resultMethods.locations(Tags.ProgramLocation.METHOD).locations();
		}
	}

	/**
	 * Filters provided method locations and keeps only those whose parameter's types correspond to the
	 * provided list of types, as an exact match of order and size.
	 * @param parameterTypesSource the list of types as strings
	 * @return method locations whose parameter's types correspond to the provided list of types, as an
	 * exact match of order and size
	 */
	public static class MethodsWithParameters extends Subdetector {
		
		private final List<String> parameterTypesSource;
		
		public MethodsWithParameters(List<String> parameterTypesSource) {
			this.parameterTypesSource = parameterTypesSource;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			// prepare result set of nodes
			GraphQuery result = Graph.query();

			for (ProgramLocation method : Graph.query(locations).locations()) { 
				// traverse to parameters of methods(go to HasParameter edges, go to the nodes
				// they point to)
				List<ProgramLocation> parameters = new ArrayList<ProgramLocation>();
				method.out(Tags.Relation.HAS_PARAMETER).forEach(edge -> parameters.add(edge.to()));

				boolean sameNumberOfParameters = parameters.size() == parameterTypesSource.size();

				if (!sameNumberOfParameters) {
					continue;
				}

				// check if parameter types are the same, in the same order
				boolean sameTypesOfParameters = true;

				// parameters is a set(unordered), not a list(ordered), so to be safe we use the
				// parameterIndex attribute of the parameter.
				for (ProgramLocation parameter : parameters) {
					int parameterIndex = parameter.<Integer>getAttribute(Tags.Attributes.PARAMETER_INDEX);

					// get source type
					String pTypeSource = parameterTypesSource.get(parameterIndex);

					// traverse from parameter to its type
					ProgramLocation type = parameter.out(Tags.Relation.TYPE_OF).iterator().next().to();

					// the method can have less or zero parameters, could also break out of the loop
					// if numberofparameters is not equal
					if (type != null) {
						// get type name (possibly use fully qualified name instead, see CommonQueries)
						String pType = (String) type.getAttribute(Tags.Attributes.NAME);
						// check if types are equal
						if (!pType.equals(pTypeSource)) {
							sameTypesOfParameters = false;
						}

					}
				}

				if (sameNumberOfParameters && sameTypesOfParameters) {
					result = result.union(Graph.query(method));
				}
			}
			
			return result.locations();
		}
	}

	/**
	 * Queries locations where the provided method locations are called.
	 * @return locations where the provided method locations are called
	 */
	public static class MethodsCalledAt extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			GraphQuery gq = Graph.query(locations);
			GraphQuery dynamicCallSites = gq
					.contained()
					.locations(Tags.ProgramLocation.IDENTITY) //implicit 'this' parameter
					.predecessorsOn(gq.universe().relations(Tags.Relation.DATAFLOW)) //all possible inputs for 'this' parameter
					.locations(Tags.ProgramLocation.IDENTITY_PASS)
					.successorsOn(gq.universe().relations(Tags.Relation.IDENTITY_PASSED_TO)); //from the identity input to the callsite.

			GraphQuery invokedSignature = gq.predecessorsOn(gq.universe().relations(Tags.Relation.INVOKED_SIGNATURE));

			GraphQuery staticCallSites = gq.predecessorsOn(gq.universe().relations(Tags.Relation.INVOKED_FUNCTION));
			
			return dynamicCallSites.union(staticCallSites,invokedSignature).locations();
		}
	}

	/**
	 * Queries the methods which the provided method locations override.
	 * @return the methods which the provided method locations override
	 */
	public static class Overrides extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			GraphQuery gq = Graph.query(locations);
			return gq.descendantsOn(
					gq.universe()
					.relations(Tags.Relation.OVERRIDES) //TODO: This gives all "overrides". Should only be the first one in line
			).locations(Tags.ProgramLocation.METHOD).locations();
		}
	}

	/**
	 * Filters methods and only keeps the concrete methods.
	 * @return concrete methods from the provided method locations
	 */
	public static class ConcreteMethods extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			GraphQuery gq = Graph.query(locations);
			return gq.locations(Tags.ProgramLocation.METHOD).difference(gq.universe().locations(Tags.ProgramLocation.ABSTRACT_METHOD)).locations();
		}
	}

	/**
	 * Queries the methods which override the provided method locations.
	 * @return the methods which override the provided method locations
	 */
	public static class OverriddenBy extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			GraphQuery gq = Graph.query(locations);
			return gq.predecessorsOn(gq.universe().relations(Tags.Relation.OVERRIDES)).locations();
		}
	}

	/**
	 * Filters the provided method locations and keeps only methods also contained
	 * within incoming streams intersectWith.
	 * @param intersectWith the streams of methods to keep
	 * @return the filtered set of method locations
	 */
	public static class IntersectionWithMethods extends Subdetector {
		
		private final MethodStream[] intersectWith;
		
		public IntersectionWithMethods(MethodStream... intersectWith) {
			this.intersectWith = intersectWith;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).intersection(
					streamVarArgToGraphQueryArray(intersectWith)
			).locations();
		}
	}

	/**
	 * Removes the methods resulting from differenceWith from the methods contained in
	 * the provided set of method locations.
	 * @param differenceWith the methods to remove from this stream
	 * @return the remaining methods not present in the set of method locations
	 * resulting from differenceWith
	 */
	public static class DifferenceWithMethods extends Subdetector {
		
		private final MethodStream[] differenceWith;
		
		public DifferenceWithMethods(MethodStream... differenceWith) {
			this.differenceWith = differenceWith;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).difference(
					streamVarArgToGraphQueryArray(differenceWith)
			).locations();
		}
	}

	/**
	 * Queries the classes the provided method locations are contained within.
	 * @return the classes the provided method locations are contained within
	 */
	public static class ParentClasses extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).parent().locations();
		}
	}

	/**
	 * Filters methods and only keeps the abstract methods.
	 * @return abstract methods from the stream
	 */
	public static class AbstractMethods extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).locations(Tags.ProgramLocation.ABSTRACT_METHOD).locations();
		}
	}

	/**
	 * Filters the the provided method locations and keeps methods with parameters which could result in a
	 * narrowing overload when method subject would be added to their context. This means that previous automatic
	 * primitive type conversion allowed a provided method to be called with a wider type, but the addition of
	 * the method subject could divert that call to itself.
	 * @param subject the method to check against
	 * @return methods liable for automatic parameter conversion in relation to the method subject
	 */
	public static class AutoNarrowingOverloads extends Subdetector {
		
		private final MethodSpecification subject;
		
		public AutoNarrowingOverloads(MethodSpecification subject) {
			this.subject = subject;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return  new MethodSet(locations).toLocationSpecifications()
					.stream()
					.filter( //Same parameter number
							location -> location.getParameters().size() == subject.getParameters().size()
					)
					.filter(location -> location.getMethodName().equals(subject.getMethodName()))
					.filter(location -> { //At least one parameter is narrowing
						List<ParameterSpecification> pToCheck = subject.getParameters(),
													pOriginal = location.getParameters();
														
						for (int i = 0; i < pToCheck.size(); i++) {
							String type1 = pOriginal.get(i).getType();
							String type2 = pToCheck.get(i).getType();
							
							if (isNarrowingConversion(type1, type2)) {
								return true;
							}
						}
														
						return false;
					}).map(methodLocation -> new MethodSet(methodLocation).singleLocation()).collect(Collectors.toSet());
		}
	}

	/**
	 * Filters methods in the provided method locations and keeps only those which contain instructions
	 * from the given InstructionStream.
	 * @param instructions the instructions to check against
	 * @return methods which contain instructions from the given InstructionStream
	 */
	public static class ContainInstruction extends Subdetector {
		
		private final InstructionStream instructions;
		
		public ContainInstruction(InstructionStream instructions) {
			this.instructions = instructions;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			GraphQuery instructionsQ = Graph.query(instructions.collect().locations());
			
			return Graph.query(locations)
			.locations()
			.stream()
			.map(methodNode -> Graph.query(methodNode).contained())
			.filter(methodQ -> methodQ.intersection(instructionsQ).locationCount() > 0)
			.reduce(Graph.query(), (q1, q2) -> q1.union(q2))
			.locations(Tags.ProgramLocation.METHOD).locations();
		}
	}

	/**
	 * Integrates sets of method locations from incoming streams unionWith into
	 * the provided method locations, removing duplicates.
	 * @param unionWith methods to add to current stream, removing duplicates
	 * @return the result of integrating both sets
	 */
	public static class Union extends Subdetector {
		
		private final MethodStream[] m;
		
		public Union(MethodStream... m) {
			this.m = m;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).union(
					streamVarArgToGraphQueryArray(m)
			).locations();
		}
	}

	/**
	 * Filters provided method locations and keeps only those which have the signature described
	 * by methodName and the list or parameter types parameterTypes.
	 * @param methodName the name of the method
	 * @param parameterTypes list of parameter types
	 * @return methods which have the signature described by methodName and the list or parameter types parameterTypes
	 */
	public static class MethodsWithSignature extends Subdetector {
		
		private final String methodName;
		private final List<String> parameterTypes;
		
		public MethodsWithSignature(String methodName, List<String> parameterTypes) {
			this.methodName = methodName;
			this.parameterTypes = parameterTypes;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			MethodStream s = new MethodSet(locations).stream();
			
			return s.filterByName(methodName).methodsWithParameters(parameterTypes).collect().locations();
		}
	}

	/**
	 * Filters provided method locations and keeps only those which are overloads of the
	 * method subject.
	 * @param subject the method to filter overloads of
	 * @return methods which are overloads of the method subject
	 */
	public static class OverloadsOfMethod extends Subdetector {
		
		private final MethodSpecification subject;
		
		public OverloadsOfMethod(MethodSpecification subject) {
			this.subject = subject;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			MethodStream s = new MethodSet(locations).stream();
			
			MethodStream nameFiltered = s.filterByName(subject.getMethodName());
			
			return nameFiltered.differenceWithMethods(
					nameFiltered.methodsWithSignature(subject.getMethodName(), subject.getParameterTypes())
			)
			.collect()
			.locations();
		}
	}
	
	/**
	 * Filters provided method locations and keeps only those which method subject
	 * would override if placed in the correct context.
	 * @param subject the method to check against
	 * @return methods which method subject would override if placed in the correct context
	 */
	public static class OverrideEquivalentMethods extends Subdetector {
		
		private final MethodSpecification subject;
		
		public OverrideEquivalentMethods(MethodSpecification subject) {
			this.subject = subject;
		}

		//TODO This only finds methods in the direct superclass. Not sure if this is intended
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			MethodStream s = new MethodSet(locations).stream();
			
			return
			// Same name
			// this.q.methods(subject.getMethodName())
			s.filterByName(subject.getMethodName())
			
			//Rule #1:Only inherited methods can be overridden.(public or protected or default)
			//		.nodes(XCSG.publicVisibility,XCSG.protectedPackageVisibility,XCSG.packageVisibility)

			//Rule #2:Final and static methods cannot be overridden.
			//Rule #8:Constructors cannot be overridden.
			//		.nodes(XCSG.InstanceMethod)
			//		.difference(this.nodes(XCSG.Java.finalMethod))

			// Rule #4: The overriding method must have same return type (or subtype).
			.methodsWithCovariantReturnTypes(subject.getReturnType())
			
			// Rule #5: The overriding method must not have more restrictive access
			// modifier.
			.methodsEquallyOrMoreVisible(subject.getVisibility())
			
			// Rule #6: The overriding method must not throw new or broader checked
			// exceptions.
			// TODO

			// Rule #3: The overriding method must have same argument list.
			.methodsWithParameters(subject.getParameterTypes())
			
			.collect().locations();
		}
	}
}
