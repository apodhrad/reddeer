package org.jboss.reddeer.graphiti.condition;

import org.jboss.reddeer.graphiti.editor.Palette;
import org.jboss.reddeer.swt.condition.WaitCondition;

/**
 * 
 * @author apodhrad
 *
 */
public class IsToolActivated implements WaitCondition {

	private Palette palette;
	private String tool;

	public IsToolActivated(Palette palette, String tool) {
		this.palette = palette;
		this.tool = tool;
	}

	@Override
	public boolean test() {
		return palette.getActiveTool().equals(tool);
	}

	@Override
	public String description() {
		return null;
	}

}
