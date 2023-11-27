package nl.ou.refd.analysis;

import nl.ou.refd.analysis.detectors.BrokenLocalReferences;
import nl.ou.refd.analysis.detectors.BrokenSubTyping;
import nl.ou.refd.analysis.detectors.CorrespondingSubclassSpecification;
import nl.ou.refd.analysis.detectors.Detector;
import nl.ou.refd.analysis.detectors.DoubleDefinition;
import nl.ou.refd.analysis.detectors.LostSpecification;
import nl.ou.refd.analysis.detectors.MissingAbstractImplementation;
import nl.ou.refd.analysis.detectors.MissingDefinition;
import nl.ou.refd.analysis.detectors.MissingSuperImplementation;
import nl.ou.refd.analysis.detectors.OverloadParameterConversion;
import nl.ou.refd.analysis.detectors.RemovedConcreteOverride;
import nl.ou.refd.locations.collections.LocationSet;

/**
 * Abstract class which represents a verdict function. The class is
 * abstract, but has a full implementation, because every time the
 * class is used, a body has to be supplied in which overrides can be
 * implemented. This mechanism is used to implement different behaviour
 * of the verdict function depending on the detector and refactoring
 * context the verdict function is used in.
 */
public abstract class VerdictFunction implements DetectorVisitor {

	private DangerAggregator aggregator;
	
	/**
	 * Creates the verdict function with an aggregator to aggregate the dangers in.
	 * @param aggregator an aggregator to aggregate the dangers in
	 */
	public VerdictFunction(DangerAggregator aggregator) {
		this.aggregator = aggregator;
	}
	
	/**
	 * Allows a partial result from the detector.
	 * @param detector the detector used for labeling.
	 * @param verdict the set of locations being the partial verdict
	 */
	protected void partial(Detector<?> detector, LocationSet verdict) {
		if (verdict.size() > 0) {
			aggregator.aggregateDangers(verdict.label(detector.getClass()));
		}
	}
	
	/**
	 * Allows all results from the given detector.
	 * @param detector all results from the given detector, marked by that detector
	 */
	protected void all(Detector<?> detector) {
		partial(detector, detector.actualRisks());
	}
	
	/**
	 * Allow no results from the given detector.
	 * @param detector the detector whose results to ignore
	 */
	protected void none(Detector<?> detector) {
		//Do nothing
	}
	
	@Override
	public void visit(BrokenLocalReferences.Body detector) {
		all(detector);
	}

	@Override
	public void visit(BrokenSubTyping.Method detector) {
		all(detector);
	}

	@Override
	public void visit(CorrespondingSubclassSpecification.Method detector) {
		all(detector);
	}

	@Override
	public void visit(DoubleDefinition.Method detector) {
		all(detector);
	}

	@Override
	public void visit(LostSpecification.Method detector) {
		all(detector);
	}

	@Override
	public void visit(MissingAbstractImplementation.Method detector) {
		all(detector);
	}

	@Override
	public void visit(MissingDefinition.Method detector) {
		all(detector);
	}

	@Override
	public void visit(MissingSuperImplementation.Method detector) {
		all(detector);
	}

	@Override
	public void visit(OverloadParameterConversion.Method detector) {
		all(detector);
	}

	@Override
	public void visit(RemovedConcreteOverride.Method detector) {
		all(detector);
	}

	@Override
	public void visit(DoubleDefinition.Class detector) {
		all(detector);
	}

}
