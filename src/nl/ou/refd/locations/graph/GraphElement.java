package nl.ou.refd.locations.graph;

import nl.ou.refd.exceptions.IncorrectAttributeTypeException;

/**
 * Abstract class representing an element in the graph. These
 * GraphElements have attributes and can be tagged.
 * @param <ElementTagType> the type of tag used. Note that these tags
 * will always be converted to a string when used as a tag, so the object
 * used should have a unique response to toString() to prevent collisions
 * of tags.
 */
public abstract class GraphElement<ElementTagType> {
	
	/**
	 * Method to get an attribute from the graph element. This method
	 * can be used with a generic parameter to signify the type of the
	 * argument. If this is used, attribute found is casted to the type
	 * provided.
	 * @param <T> optional type of the attribute to get
	 * @param key the key the attribute is stored under
	 * @return the attribute found for the key
	 * @throws IncorrectAttributeTypeException if the type provided is not
	 * the correct type of the attribute
	 */
	public abstract <T> T getAttribute(Tags.Attributes key);
	
	/**
	 * Checks if the graph element has an attribute for a specific key.
	 * @param key the key for which an attribute might be present
	 * @return true if the graph element has an attribute for the given key, false otherwise
	 */
	public abstract boolean hasAttribute(Tags.Attributes key);
	
	/**
	 * Puts an attribute in the graph element for a specific key. This
	 * method infers the type of the attribute from the given parameter's
	 * type.
	 * @param <T> the type of the attribute
	 * @param arg0 the key to store the attribute under
	 * @param arg1 the attribute to store
	 */
	public abstract <T> void putAttribute(Tags.Attributes key, T attribute);
	
	/**
	 * Removes an attribute from the graph element.
	 * @param key the key for which to remove the attribute
	 * @return true if the graph element indeed had an attribute for the given key
	 * and it is not deleted, false otherwise
	 */
	public abstract boolean removeAttribute(Tags.Attributes key);
	
	/**
	 * Checks wether the given GraphElement has the same tags as this graph element.
	 * @param other the other graph element to check the tags for
	 * @return true if both this graph element and the one given have the same tags,
	 * false otherwise
	 */
	public abstract boolean taggedSameAs(GraphElement<ElementTagType> other);
	
	/**
	 * Checks whether the graph element is tagged with a specific tag. Note that
	 * the tag will be converted to a string by invoking its toString() method,
	 * so the tag used should have a unique response to this method in relation
	 * to the other tags used.
	 * @param tag the tag to check the presence of
	 * @return
	 */
	public abstract boolean taggedWith(ElementTagType tag);
	
	/**
	 * Tags the graph element with the given tag.
	 * @param tag the tag to tag the graph element with
	 * @return true if the tag was not previously present and the graph
	 * element is now tagged with it, false otherwise
	 */
	public abstract boolean tag(ElementTagType tag);
	
	/**
	 * Removes the tag from the graph element.
	 * @param tag the tag to remove from the graph element
	 * @return true if the tag was present and is now removed, false otherwise
	 */
	public abstract boolean untag(ElementTagType tag);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract String toString();
	
	/**
	 * This is an internal method for the package to use the Atlas implementation
	 * of the graph. This method should be changed when a different graph implementation
	 * is chosen.
	 * @return the Atlas representation of the graph element
	 */
	abstract com.ensoftcorp.atlas.core.db.graph.GraphElement getAtlasElement();
	
}
