package nl.ou.refd.plugin.ui;

import com.ensoftcorp.atlas.core.markup.Markup;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.script.StyledResult;
import com.ensoftcorp.atlas.ui.scripts.selections.FilteringAtlasSmartViewScript;
import com.ensoftcorp.atlas.ui.selection.event.IAtlasSelectionEvent;

/**
 * Class containing a basic configuration for the Atlas GUI.
 */
public class DefaultUiScript extends FilteringAtlasSmartViewScript {

	@Override
	public String getTitle() {
		return "Refactoring Guidance";
	}
	
	@Override
	public String[] getSupportedNodeTags() {
		return FilteringAtlasSmartViewScript.EVERYTHING;
	}

	@Override
	public String[] getSupportedEdgeTags() {
		return FilteringAtlasSmartViewScript.EVERYTHING;
	}

	@Override
	public StyledResult selectionChanged(IAtlasSelectionEvent input, Q filteredSelection) {
						
		
		Q res = filteredSelection;
		
		//  return the styled result for display
		return new StyledResult(res, new Markup());
	}
	
}