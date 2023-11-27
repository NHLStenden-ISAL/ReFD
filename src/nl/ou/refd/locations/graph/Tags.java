package nl.ou.refd.locations.graph;

import com.ensoftcorp.atlas.core.xcsg.XCSG;

/**
 * Class containing the tags that can be used in the graph.
 */
public final class Tags {
	private Tags(){}
	
	/**
	 * The tags used to tag attributes of graph elements with.
	 * The enum is backed by String values.
	 */
	public enum Attributes {
		NAME(XCSG.name),
		PARAMETER_INDEX(XCSG.parameterIndex),
		ID(XCSG.id),
		SOURCE_CORRESPONDENCE(XCSG.sourceCorrespondence);
		
		private final String tag;
		
		/**
		 * Creates an enum element from the string specified in its declaration
		 * @param tag the provided string representing the enum element's value
		 */
		private Attributes(final String tag) {
			this.tag = tag;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return this.tag;
		}
	}
	
	/**
	 * The tags used to tag ProgramLocation objects with.
	 * The enum is backed by String values.
	 */
	public enum ProgramLocation {
		NODE(XCSG.Node),
		
		PROJECT(XCSG.Project),
		PACKAGE(XCSG.Package),
		METHOD(XCSG.Method),
		FINAL_METHOD(XCSG.Java.finalMethod),
		FUNCTION(XCSG.Function),
		PARAMETER(XCSG.Parameter),
		VARIABLE(XCSG.Variable),
		INSTANCE_VARIABLE(XCSG.InstanceVariable),
		CLASS_VARIABLE(XCSG.ClassVariable),
		NAMESPACE(XCSG.Namespace),
		TYPE(XCSG.Type),
		CLASS(XCSG.Java.Class),
		ABSTRACT_CLASS(XCSG.Java.AbstractClass),
		FINAL_CLASS(XCSG.Java.finalClass),
		CLASSIFIER(XCSG.Classifier),
		FIELD(XCSG.Field),
		
		CLASS_METHOD(XCSG.ClassMethod),
		INSTANCE_METHOD(XCSG.InstanceMethod),
		ABSTRACT_METHOD(XCSG.abstractMethod),
		
		PUBLIC_VISIBILITY(XCSG.publicVisibility),
		PROTECTED_VISIBILITY(XCSG.protectedVisibility),
		PROTECTED_PACKAGE_VISIBILITY(XCSG.protectedPackageVisibility),
		PACKAGE_VISIBILITY(XCSG.packageVisibility),
		PRIVATE_VISIBILITY(XCSG.privateVisibility),
		
		IDENTITY(XCSG.Identity),
		IDENTITY_PASS(XCSG.IdentityPass),
		
		DATAFLOW(XCSG.DataFlow_Node),
		
		CALL_INPUT(XCSG.CallInput),
		
		REFACTOR_CREATED_METHOD("ModelAdapter.REFACTOR_CREATED_METHOD"),
		REFACTOR_CREATED_CLASS("ModelAdapter.REFACTOR_CREATED_CLASS"),
		REFACTOR_CREATED_PARAMETER("ModelAdapter.REFACTOR_CREATED_PARAMETER");
		
		private final String tag;
		
		/**
		 * Creates an enum element from the string specified in its declaration
		 * @param tag the provided string representing the enum element's value
		 */
		private ProgramLocation(final String tag) {
			this.tag = tag;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return this.tag;
		}
	}
	
	/**
	 * The tags used to tag Relation objects with.
	 * The enum is backed by String values.
	 */
	public enum Relation {
		EDGE(XCSG.Edge),
		
		RETURNS(XCSG.Returns),
		CONTAINS(XCSG.Contains),
		HAS_VARIABLE(XCSG.HasVariable),
		HAS_PARAMETER(XCSG.HasParameter),
		HAS_CONTROL_FLOW(XCSG.HasControlFlow),
		TYPE_OF(XCSG.TypeOf),
		EXTENDS(XCSG.Java.Extends),
		DATAFLOW(XCSG.DataFlow_Edge),
		SUPERTYPE(XCSG.Supertype),
		IDENTITY_PASSED_TO(XCSG.IdentityPassedTo),
		INVOKED_SIGNATURE(XCSG.InvokedSignature),
		INVOKED_FUNCTION(XCSG.InvokedFunction),
		OVERRIDES(XCSG.Overrides),
		DECLARES(com.ensoftcorp.atlas.core.query.Attr.Edge.DECLARES),
		
		REFACTOR_CREATED_EDGE("ModelAdapter.REFACTOR_CREATED_EDGE");
		
		private final String tag;
		
		/**
		 * Creates an enum element from the string specified in its declaration
		 * @param tag the provided string representing the enum element's value
		 */
		private Relation(final String tag) {
			this.tag = tag;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return this.tag;
		}
	}
}
