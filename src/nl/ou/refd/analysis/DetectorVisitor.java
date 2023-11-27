package nl.ou.refd.analysis;

import nl.ou.refd.analysis.detectors.BrokenLocalReferences;
import nl.ou.refd.analysis.detectors.BrokenSubTyping;
import nl.ou.refd.analysis.detectors.CorrespondingSubclassSpecification;
import nl.ou.refd.analysis.detectors.DoubleDefinition;
import nl.ou.refd.analysis.detectors.LostSpecification;
import nl.ou.refd.analysis.detectors.MissingAbstractImplementation;
import nl.ou.refd.analysis.detectors.MissingDefinition;
import nl.ou.refd.analysis.detectors.MissingSuperImplementation;
import nl.ou.refd.analysis.detectors.OverloadParameterConversion;
import nl.ou.refd.analysis.detectors.RemovedConcreteOverride;

/**
 * Interface which represents a visitor for detectors.
 */
public interface DetectorVisitor {
	void visit(BrokenLocalReferences.Body detector);
	void visit(BrokenSubTyping.Method detector);
	void visit(CorrespondingSubclassSpecification.Method detector);
	void visit(DoubleDefinition.Method detector);
	void visit(DoubleDefinition.Class detector);
	void visit(LostSpecification.Method detector);
	void visit(MissingAbstractImplementation.Method detector);
	void visit(MissingDefinition.Method detector);
	void visit(MissingSuperImplementation.Method detector);
	void visit(OverloadParameterConversion.Method detector);
	void visit(RemovedConcreteOverride.Method detector);
}
