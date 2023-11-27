package nl.ou.refd.analysis;

/**
 * Interface which represents an object whose results can be mapped by a verdict function.
 */
public interface Verdictable {
	void accept(VerdictFunction verdict);
}
