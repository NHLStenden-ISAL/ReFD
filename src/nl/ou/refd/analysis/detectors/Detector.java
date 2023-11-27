package nl.ou.refd.analysis.detectors;

import nl.ou.refd.analysis.DetectorVisitor;
import nl.ou.refd.analysis.ModelNode;
import nl.ou.refd.analysis.ModelVisitor;
import nl.ou.refd.analysis.VerdictFunction;
import nl.ou.refd.analysis.Verdictable;
import nl.ou.refd.locations.collections.LocationSet;

/**
 * Class representing a detector. A detector checks the program graph
 * for potential risks. If it finds these, they are determined to be
 * actual risks.
 * @param <T> the type of LocationSet the detector outputs its results in
 */
public abstract class Detector<T extends LocationSet> implements ModelNode, Verdictable {

	/**
	 * Gets the actual risks present in the codebase.
	 * @return a LocationSet containing these actual risks
	 */
	public abstract T actualRisks();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(ModelVisitor visitor) {
		this.accept((DetectorVisitor)visitor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(VerdictFunction verdict) {
		this.accept((DetectorVisitor)verdict);
	}
	
	public abstract void accept(DetectorVisitor visitor);
}
