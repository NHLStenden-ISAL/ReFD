package nl.ou.refd.locations.graph;

import nl.ou.refd.exceptions.IncorrectAttributeTypeException;

/**
 * Simple implementation of the GraphElement class. This class is constrained
 * to the current package because it uses Atlas elements.
 * @param <GraphElementType> the type of specific Atlas graph element used
 * @param <ElementTagType> the type of tag used
 */
class SimpleGraphElement<GraphElementType extends com.ensoftcorp.atlas.core.db.graph.GraphElement, ElementTagType> extends GraphElement<ElementTagType> {
	
	private GraphElementType graphElement;
	
	/**
	 * Creates a SimpleGraphElement.
	 * @param graphElement The Atlas graph element to use
	 */
	SimpleGraphElement(GraphElementType graphElement) {
		this.graphElement = graphElement;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(Tags.Attributes key) {
		try {
			return (T)this.graphElement.getAttr(key.toString());
		} catch (ClassCastException e) {
			throw new IncorrectAttributeTypeException("Attribute for key " + key.toString() + "of incorrect type");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasAttribute(Tags.Attributes key) {
		return this.graphElement.hasAttr(key.toString());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> void putAttribute(Tags.Attributes key, T attribute) {
		this.graphElement.putAttr(key.toString(), attribute);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAttribute(Tags.Attributes key) {
		if (this.hasAttribute(key)) {
			this.graphElement.removeAttr(key.toString());
			return true;
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean taggedSameAs(GraphElement<ElementTagType> other) {
		return this.graphElement.taggedSameAs(other.getAtlasElement());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean taggedWith(ElementTagType tag) {
		return this.graphElement.taggedWith(tag.toString());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean tag(ElementTagType tag) {
		if (!this.taggedWith(tag)) {
			this.graphElement.tag(tag.toString());
			return true;
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean untag(ElementTagType tag) {
		if (this.taggedWith(tag)) {
			this.graphElement.untag(tag.toString());
			return true;
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.graphElement.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	GraphElementType getAtlasElement() {
		return this.graphElement;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return this.graphElement.equals(obj);
	}
}
