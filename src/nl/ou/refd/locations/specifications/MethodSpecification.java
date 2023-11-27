package nl.ou.refd.locations.specifications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refd.exceptions.IncompatibleProgramLocationException;
import nl.ou.refd.locations.collections.ClassSet;
import nl.ou.refd.locations.generators.ProgramComponentsGenerator;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.GraphQuery;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Relation;
import nl.ou.refd.locations.graph.Tags;
import nl.ou.refd.locations.streams.InstructionStream;

/**
 * Class representing a specification of a single method location
 * in a codebase. The specification can be of either a location that
 * already exists within the codebase, or that does not exists (yet).
 */
public class MethodSpecification extends LocationSpecification {

	private String methodName;
	private List<ParameterSpecification> parameters;
	private AccessModifier visibility;
	private boolean isStatic;
	private boolean isAbstract;
	private String returnType;
	private ClassSpecification enclosingClass;
	
	/**
	 * Creates a method location specification from a number of parameters
	 * describing the method's signature.
	 * @param methodName the name of the method
	 * @param parameters an ordered list of ParameterSpecification objects
	 * @param visibility the visibility of the method
	 * @param isStatic boolean signifying if a method is coupled to a class or an instance
	 * @param isAbstract boolean signifying if a method has an implementation
	 * @param returnType the return type of the method as String
	 * @param enclosingClass the class which the method belongs to
	 */
	public MethodSpecification(String methodName, List<ParameterSpecification> parameters, AccessModifier visibility,
			boolean isStatic, boolean isAbstract, String returnType, ClassSpecification enclosingClass) {
		super();
		this.methodName = methodName;
		this.parameters = parameters;
		this.visibility = visibility;
		this.isStatic = isStatic;
		this.isAbstract = isAbstract;
		this.returnType = returnType;
		this.enclosingClass = enclosingClass;
	}
	
	/**
	 * Creates a method location specification from a ProgramLocation (graph node). This
	 * value is not stored inside the MethodSpecification. Rather, it is used to get the
	 * relevant data to create the ProgramLocation.
	 * @param pl the ProgramLocation containing the relevant data to create the MethodSpecification
	 * @throws IncompatibleProgramLocationException if the provided ProgramLocation is not a valid method
	 */
	public MethodSpecification(ProgramLocation pl) {
		if (!locationIsMethod(pl)) {
			throw new IncompatibleProgramLocationException("Node not tagged with Tags.Node.METHOD");
		}
		
		GraphQuery nQ = Graph.query(pl);
		
		this.methodName = pl.<String>getAttribute(Tags.Attributes.NAME);
		this.returnType = nQ.forwardDifference(Tags.Relation.RETURNS).singleLocation().<String>getAttribute(Tags.Attributes.NAME);
		
		Set<ProgramLocation> parameterNodes = nQ.forwardDifference(Tags.Relation.HAS_PARAMETER).locations();
		this.parameters = new ArrayList<ParameterSpecification>(Collections.nCopies(parameterNodes.size(), null));
		for (ProgramLocation parNameNode : parameterNodes) {
			this.parameters.set((int)parNameNode.getAttribute(Tags.Attributes.PARAMETER_INDEX),
					new ParameterSpecification(
							parNameNode.<String>getAttribute(Tags.Attributes.NAME),
							Graph.query(parNameNode)
								.forwardDifference(Tags.Relation.TYPE_OF)
								.singleLocation()
								.<String>getAttribute(Tags.Attributes.NAME)
							)
						);
		}
		
		this.visibility = null;
		
		this.visibility = AccessModifier.fromTaggedLocation(pl);
		
		this.isStatic = pl.taggedWith(Tags.ProgramLocation.CLASS_METHOD);
		this.isAbstract = pl.taggedWith(Tags.ProgramLocation.ABSTRACT_METHOD);
		
		this.enclosingClass = new ClassSpecification(Graph.query(pl).parent().singleLocation());
	}
	
	/**
	 * Checks if the provided ProgramLocation is tagged as a method.
	 * @param n the ProgramLocation to check
	 * @return true if ProgramLocation instance is a method, false otherwise
	 */
	public static boolean locationIsMethod(ProgramLocation n) {
		return n.taggedWith(Tags.ProgramLocation.METHOD) || n.taggedWith(Tags.ProgramLocation.ABSTRACT_METHOD) || n.taggedWith(Tags.ProgramLocation.CLASS_METHOD) || n.taggedWith(Tags.ProgramLocation.INSTANCE_METHOD);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodSpecification copy() {
		return new MethodSpecification(methodName, parameters, visibility, isStatic, isAbstract, returnType, enclosingClass);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.enclosingClass.toString() + "." + this.methodName + "(" + this.parameters.stream().map(p -> p.getType() + " " + p.getName()).collect(Collectors.joining(", ")) + ")";
	}
	
	/**
	 * Set the name of the method
	 * @param methodName the new name of the method
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	/**
	 * Sets the list of parameters. The supplied list is shallow-copied.
	 * @param parameters the supplied list of ParameterSpecification objects
	 */
	public void setParameters(List<ParameterSpecification> parameters) {
		this.parameters = new ArrayList<ParameterSpecification>(parameters);
	}
	
	/**
	 * Sets the visibility of the method.
	 * @param visibility visibility information
	 */
	public void setVisibility(AccessModifier visibility) {
		this.visibility = visibility;
	}
	
	/**
	 * Sets if the method is linked to the containing class (true) or instances of that class (false).
	 * @param isStatic if method is linked to class
	 */
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
	
	//TODO: implement type as an element contained in a package
	/**
	 * Set return type of method.
	 * @param returnType string representation of return type
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	/**
	 * Set the enclosing class. This instance is not copied and is mutable.
	 * @param enclosingClass the enclosing class
	 */
	public void setEnclosingClass(ClassSpecification enclosingClass) {
		this.enclosingClass = enclosingClass;
	}

	/**
	 * Gets an ordered list of the types of the parameters as strings.
	 * @return an ordered list of parameter types
	 */
	public List<String> getParameterTypes() {
		List<String> parameterTypes = new ArrayList<>();
		for(ParameterSpecification tpc : this.getParameters()) {
			parameterTypes.add(tpc.getType());
		}
		return parameterTypes;
	}

	/**
	 * Gets the return type of the method.
	 * @return the return type of the method
	 */
	public String getReturnType() {
		return returnType;
	}

	/**
	 * Gets the name of the method
	 * @return the name of the method
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Gets the list of parameters. This list is shallow copied.
	 * @return a shallow copy of the list of parameters
	 */
	public List<ParameterSpecification> getParameters() {
		return new ArrayList<ParameterSpecification>(parameters);
	}

	/**
	 * Gets the visibility of the method.
	 * @return the visibility of the methat as AccessModifier
	 */
	public AccessModifier getVisibility() {
		return visibility;
	}

	/**
	 * Gets if the method is linked to a class or instance.
	 * @return if the method is linked to a class (true) or an instance (false)
	 */
	public boolean isStatic() {
		return isStatic;
	}
	
	/**
	 * Gets if the method is abstract or not.
	 * @return if the method is abstract or not
	 */
	public boolean isAbstract() {
		return isAbstract;
	}

	/**
	 * Gets the enclosing class. This object is not copied and so is mutable.
	 * @return a mutable version of the enclosing class (not a copy)
	 */
	public ClassSpecification getEnclosingClass() {
		return enclosingClass;
	}
	
	/*
	 * TODO: this should probably be its own specification type, but it is difficult to
	 * make an accurate and simple representation of the specification so it will be able
	 * to specify all kinds of statements in a body, in a way that changes in the code
	 * do not abruptly change the specification (using line numbers for instance would
	 * be subject to change once changes to the file start to happen)
	 */
	/**
	 * Gets the body of the method.
	 * @return the body of the method as a stream of instructions
	 */
	public InstructionStream getBody() {
		return new ProgramComponentsGenerator()
		.stream()
		.classes()
		.classesByName(this.getEnclosingClass().getClassName())
		.methods()
		.methodsWithSignature(this.getMethodName(), this.getParameterTypes())
		.bodies();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProgramLocation construct(Graph graph) {
		ProgramLocation parentClass = new ClassSet(this.getEnclosingClass()).singleLocation();//TODO: change this function from ClassCollection to the ProgramComponent stream
		
		ProgramLocation nMethod = createMethod(graph);
		
		if (this.isStatic()) {
			nMethod.tag(Tags.ProgramLocation.CLASS_METHOD);
		}
		else {
			nMethod.tag(Tags.ProgramLocation.INSTANCE_METHOD);
		}
		
		if (this.isAbstract()) {
			nMethod.tag(Tags.ProgramLocation.ABSTRACT_METHOD);
		}
		
		if (this.getVisibility() == AccessModifier.PUBLIC) {
			nMethod.tag(Tags.ProgramLocation.PUBLIC_VISIBILITY);
		}
		else if (this.getVisibility() == AccessModifier.PACKAGE) {
			nMethod.tag(Tags.ProgramLocation.PACKAGE_VISIBILITY);
		}
		else if (this.getVisibility() == AccessModifier.PROTECTED) {
			nMethod.tag(Tags.ProgramLocation.PROTECTED_PACKAGE_VISIBILITY);
		}
		else if (this.getVisibility() == AccessModifier.PRIVATE) {
			nMethod.tag(Tags.ProgramLocation.PRIVATE_VISIBILITY);
		}
		
		nMethod.putAttribute(Tags.Attributes.NAME, this.getMethodName());
		
		ProgramLocation rType = getType(this.getReturnType());
		
		Relation returns = graph.createRelation(nMethod, rType);
		returns.tag(Tags.Relation.RETURNS);
		
		Relation contains = graph.createRelation(parentClass, nMethod);
		contains.tag(Tags.Relation.CONTAINS);
		
		int parIndex = 0;
		for (ParameterSpecification paramCombo : this.getParameters()) {
			ProgramLocation param = paramCombo.construct(Graph.getInstance());
			param.putAttribute(Tags.Attributes.PARAMETER_INDEX, parIndex++);
			
			Relation hasParam = Graph.getInstance().createRelation(nMethod, param);
			hasParam.tag(Tags.Relation.HAS_VARIABLE);
			hasParam.tag(Tags.Relation.CONTAINS);
			hasParam.tag(Tags.Relation.HAS_PARAMETER);
			
			ProgramLocation type = getType(paramCombo.getType());
			
			Relation typeOf = graph.createRelation(param, type);
			typeOf.tag(Tags.Relation.TYPE_OF);
		}
		
		return nMethod;
	}
	
	/**
	 * Creates an empty ProgramLocation representation of a method.
	 * @param graph the Graph to create the ProgramLocation in
	 * @return the created ProgramLocation
	 */
	private static ProgramLocation createMethod(Graph graph) {
		ProgramLocation rNode = graph.createProgramLocation();
		rNode.tag(Tags.ProgramLocation.METHOD);
		rNode.tag(Tags.ProgramLocation.FUNCTION);
		rNode.tag(Tags.ProgramLocation.REFACTOR_CREATED_METHOD);
		return rNode;
	}
	
	/*
	 * TODO: this method should use a package qualifier to determine
	 * which exact type is being queried. Right now, types with the same
	 * name, but located in different packages, are not supported.
	 */
	/**
	 * Gets the ProgramLocation representing the type provided as String.
	 * @param typeName the type provided as String
	 * @return the ProgramLocation corresponding to the type
	 */
	private static ProgramLocation getType(String typeName) {
		return Graph.query().universe().types(typeName).singleLocation();
	}
	
}
