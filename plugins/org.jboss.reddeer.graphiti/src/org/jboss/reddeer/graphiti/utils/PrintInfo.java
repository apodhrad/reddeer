package org.jboss.reddeer.graphiti.utils;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.internal.ui.palette.editparts.ToolEntryEditPart;

public class PrintInfo {

	public static void editParts(EditPart editPart) {
		System.out.println(editPart.getClass());
		if (editPart instanceof ToolEntryEditPart) {
			ToolEntryEditPart teep = (ToolEntryEditPart) editPart;
			System.out.println("Content Pane");
			figures(teep.getContentPane());
			System.out.println("getFigure");
			figures(teep.getFigure());
		}
		List<EditPart> editParts = editPart.getChildren();
		for (EditPart ep : editParts) {
			editParts(ep);
		}
	}

	public static void figures(IFigure figure) {
		System.out.println(figure.getClass());
		if (figure instanceof Label) {
			System.out.println("Label: " + ((Label) figure).getText());
		}
		List<IFigure> figures = figure.getChildren();
		for (IFigure fig : figures) {
			figures(fig);
		}
	}
}
