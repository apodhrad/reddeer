package org.jboss.reddeer.graphiti.test;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.ui.part.EditorPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.graphiti.editor.GefEditor;
import org.jboss.reddeer.graphiti.matcher.All;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.junit.Test;

public class DiagramTest extends RedDeerTest {

	@Test
	public void diagramTest() {
		new GeneralProjectWizard().create("test");

		new ProjectExplorer().open();
		new ProjectExplorer().getProject("test").select();

		try {
			new TutorialDiagramWizard().create("Tutorial");
		} catch (SWTLayerException ex) {
			new DefaultShell("New").close();
			new TutorialDiagramWizard2().create("Tutorial");
		}

		GefEditor editor = new GefEditor("Tutorial");
		
		editor.addToolFromPalette("EClass", "Objects", 5, 5);
		editor.addToolFromPalette("EClass", "Objects", 120, 5);
		editor.addToolFromPalette("EClass", "Objects", 230, 5);
		editor.addToolFromPalette("EClass", "Objects", 340, 5);
		editor.addToolFromPalette("EClass", "Objects", 5, 120);
		editor.addToolFromPalette("EClass", "Objects", 120, 120);

		System.out.println("Number of edit parts: " + editor.getEditParts(new IsSelectable()).size());
		EditPart editPart = editor.getEditParts(new IsSelectable()).get(0);
		
//		editPart.performRequest(RequestConstants.REQ_OPEN);
		
		editor.select(editPart);
		
		// debug
		editor.getEditParts(new All());
		
		Request request = new Request();
		request.setType(RequestConstants.REQ_OPEN);
		editPart.performRequest(request);
		
		editPart.getChildren();
		
		editor.doubleClick(editPart);

		new DefaultShell("Rename EClass");
		new DefaultText(0).setText("foo");
		new PushButton("OK").click();

		editor.deleteEditPartWithLabel("foo");

		System.out.println();
	}

	public class IsSelectable extends BaseMatcher<EditorPart> {

		@Override
		public boolean matches(Object obj) {
			if (obj instanceof EditPart) {
				EditPart editPart = (EditPart) obj;
				return editPart.isSelectable();
			}
			return false;
		}

		@Override
		public void describeTo(Description desc) {

		}

	}

	public class GeneralProjectWizard extends NewWizardDialog {

		public GeneralProjectWizard() {
			super("General", "Project");
		}

		public void create(String name) {
			open();
			new LabeledText("Project name:").setText(name);
			finish();
		}
	}

	public class TutorialDiagramWizard extends NewWizardDialog {

		public TutorialDiagramWizard() {
			super("Examples", "Graphiti Diagram");
		}

		public void create(String name) {
			open();
			new DefaultCombo("Diagram Type").setSelection("tutorial");
			next();
			new LabeledText("Diagram Name").setText(name);
			finish();
		}
	}

	public class TutorialDiagramWizard2 extends NewWizardDialog {

		public TutorialDiagramWizard2() {
			super("Examples", "Graphiti", "Graphiti Diagram");
		}

		public void create(String name) {
			open();
			new DefaultCombo("Diagram Type").setSelection("tutorial");
			next();
			new LabeledText("Diagram Name").setText(name);
			finish();
		}
	}
}
