package nl.ou.refd.locations.specifications;

import org.apache.commons.lang3.NotImplementedException;

import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;

/**
 * Class representing a specification of a single field location
 * in a codebase. The specification can be of either a location that
 * already exists within the codebase, or that does not exists (yet).
 */
public class FieldSpecification extends LocationSpecification {
	
	private String fieldName;
	private ClassSpecification enclosingClass;
	
	/**
	 * Creates a field location specification from a field name and
	 * a specification of a class the field belongs to.
	 * @param fieldName the name of the field
	 * @param enclosingClass the class the field belongs to
	 */
	public FieldSpecification(String fieldName, ClassSpecification enclosingClass) {
		this.fieldName = fieldName;
		this.enclosingClass = enclosingClass;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldSpecification copy() {
		return new FieldSpecification(fieldName, enclosingClass);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.fieldName;
	}
	
	/**
	 * Returns an immutable String representing the field name.
	 * @return the name of the class as a string
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Sets the field's name.
	 * @param fieldName the new name of the field
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * Returns the enclosing class as an object. This object
	 * is mutable.
	 * @return the enclosing package as ClassSpecification
	 */
	public ClassSpecification getEnclosingClass() {
		return enclosingClass;
	}

	/**
	 * Sets the enclosing class.
	 * @param enclosingClass the new enclosing class
	 */
	public void setEnclosingClass(ClassSpecification enclosingClass) {
		this.enclosingClass = enclosingClass;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProgramLocation construct(Graph graph) {
		throw new NotImplementedException();
	}

}
