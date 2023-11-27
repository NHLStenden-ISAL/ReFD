package nl.ou.refd.locations.specifications;

import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;

/**
 * Class representing a specification of a single parameter. The
 * specification can be of either a parameter that already exists
 * within the codebase, or that does not exists (yet).
 */
public class ParameterSpecification extends LocationSpecification {
	
	private String name;
	private String type;
	
	//TODO: the type should be fully qualified
	/**
	 * Creates a parameter specification.
	 * @param name the name of the parameter
	 * @param type the type of the parameter
	 */
	public ParameterSpecification(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Gets the name of the parameter.
	 * @return the name of the parameter as String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the type of the parameter.
	 * @return the type of the parameter as String
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the name of the parameter.
	 * @param name the name of the parameter
	 */
	public void setName(String name) {
		this.name = name;
	}

	//TODO: the type should be fully qualified
	/**
	 * Sets the type of the parameter.
	 * @param type the type of the parameter
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ParameterSpecification copy() {
		return new ParameterSpecification(name, type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "<"+this.name+","+this.type+">";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProgramLocation construct(Graph graph) {
		ProgramLocation param = Graph.getInstance().createProgramLocation();
		param.tag(Tags.ProgramLocation.PARAMETER);
		param.tag(Tags.ProgramLocation.REFACTOR_CREATED_PARAMETER);
		param.tag(Tags.ProgramLocation.CALL_INPUT);
		param.tag(Tags.ProgramLocation.VARIABLE);
		param.tag(Tags.ProgramLocation.PACKAGE_VISIBILITY); //Strange, but the example has this
		param.putAttribute(Tags.Attributes.NAME, this.name);
		return param;
	}
	
}
