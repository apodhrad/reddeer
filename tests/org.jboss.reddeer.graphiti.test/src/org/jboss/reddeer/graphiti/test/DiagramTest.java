package org.jboss.reddeer.graphiti.test;

import org.eclipse.gef.EditPart;
import org.eclipse.ui.part.EditorPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.graphiti.editor.GefEditor;
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

		editor.getPalette().activateTool("EClass", "Objects");
		System.out.println("klik 1");
		editor.click(5, 5);
		checkCount(editor, 1);

		editor.getPalette().activateTool("EClass", "Objects");
		System.out.println("klik 2");
		editor.click(120, 5);
		checkCount(editor, 2);

		editor.getPalette().activateTool("EClass", "Objects");
		System.out.println("klik 3");
		editor.click(230, 5);
		checkCount(editor, 3);

		editor.getPalette().activateTool("EClass", "Objects");
		System.out.println("klik 4");
		editor.click(340, 5);
		checkCount(editor, 4);

		editor.getPalette().activateTool("EClass", "Objects");
		System.out.println("klik 5");
		editor.click(5, 120);
		checkCount(editor, 5);

		editor.getPalette().activateTool("EClass", "Objects");
		System.out.println("klik 6");
		editor.click(120, 120);
		checkCount(editor, 6);

		EditPart editPart = editor.getEditParts(new IsSelectable()).get(0);
		editor.doubleClick(editPart);

		new DefaultShell("Rename EClass");
		new DefaultText(0).setText("foo");
		new PushButton("OK").click();

		editor.deleteEditPartWithLabel("foo");

		System.out.println();
	}

	public void checkCount(GefEditor editor, int expectedCount) {
		int count = editor.getEditParts(new IsSelectable()).size();
		System.out.println();
		// assertEquals(expectedCount, count);
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
