package nl.ou.refd.plugin.ui;

import org.eclipse.ui.PartInitException;

import com.ensoftcorp.atlas.ui.AtlasUI;
import com.ensoftcorp.atlas.ui.scripts.selections.AtlasSmartViewScript;
import com.ensoftcorp.atlas.ui.views.scriptView.IAtlasScriptView;

/**
 * Class to create an Atlas GUI tab in the Eclipse environment. This
 * GUI can be used to show (parts of) the tree Atlas constructs and
 * result from queries performed.
 */
class AtlasGUICreator {
	private IAtlasScriptView RGUI;

	/**
	 * Creates a Atlas GUI tab in the Eclipse environment. Although this
	 * method internally uses a new thread to create the Atlas GUI, it
	 * waits until this is done and returns, making in synchronous.
	 * @param name
	 * @return
	 */
	public IAtlasScriptView createSync(String name) {
		Thread t1 = new Thread(
			new Runnable(){
			@Override
			public void run() {
				try {
					RGUI = AtlasUI.createScriptView();
					AtlasSmartViewScript s = new DefaultUiScript();
					RGUI.script(s);
					RGUI.title(name);
					} catch (PartInitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		);
		t1.start();
		
		//TODO: We can do this with a mutex (synchronized) instead of sleep
		while(RGUI == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("Thread sleep error");
			}
		}
		
		return RGUI;
	}
}
