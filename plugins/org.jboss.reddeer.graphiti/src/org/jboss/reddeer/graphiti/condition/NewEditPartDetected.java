package org.jboss.reddeer.graphiti.condition;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.jboss.reddeer.graphiti.editor.GefEditor;
import org.jboss.reddeer.graphiti.matcher.All;
import org.jboss.reddeer.swt.condition.WaitCondition;

public class NewEditPartDetected implements WaitCondition {

	private GefEditor gefEditor;
	private int oldCount;

	public NewEditPartDetected(GefEditor gefEditor, int oldCount) {
		this.gefEditor = gefEditor;
		this.oldCount = oldCount;
	}

	@Override
	public boolean test() {
		List<EditPart> list = gefEditor.getEditParts(new All());
		return list.size() > oldCount;
	}

	@Override
	public String description() {
		return "No new edit part has been detected! The count is still " + oldCount;
	}

}
