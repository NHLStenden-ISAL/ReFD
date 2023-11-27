package nl.ou.refd.analysis;

/**
 * Interface which represents a node in a model which can be visited.
 */
public interface ModelNode {
	void accept(ModelVisitor visitor);
}
