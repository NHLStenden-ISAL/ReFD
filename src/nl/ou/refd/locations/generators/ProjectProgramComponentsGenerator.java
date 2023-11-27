package nl.ou.refd.locations.generators;

import java.util.Set;

import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;
import nl.ou.refd.locations.streams.ProgramComponentStream;

/**
 * Class representing a generator that generates only program components
 * from the current program graph belonging to a specific project.
 */
public class ProjectProgramComponentsGenerator extends LocationGenerator {

	private final String projectName;
	
	/**
	 * Creates the generator with a project name.
	 * @param projectName the name of the project
	 */
	public ProjectProgramComponentsGenerator(String projectName) {
		super();
		this.projectName = projectName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProgramComponentStream stream() {
		return new ProgramComponentStream(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<ProgramLocation> generate() {
		return Graph.query().universe().locations(Tags.ProgramLocation.PROJECT).selectLocation(Tags.Attributes.NAME, this.projectName).contained().locations();
	}

}
