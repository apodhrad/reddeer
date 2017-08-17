package org.eclipse.reddeer.eclipse.test.jdt.ui.javaeditor;

import org.eclipse.reddeer.eclipse.jdt.ui.javaeditor.JavaEditor;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.JavaProjectWizard;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassCreationWizard;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Andrej Podhradsky (apodhrad@redhat.com)
 *
 */
@CleanWorkspace
@RunWith(RedDeerSuite.class)
public class JavaEditorTest {

	public static final String PROJECT_NAME = "java_project";
	public static final String CLASS_NAME = "Demo";

	@BeforeClass
	public static void prepareJavaProject() {
		JavaProjectWizard javaProjectWizard = new JavaProjectWizard();
		javaProjectWizard.open();
		new NewJavaProjectWizardPageOne(javaProjectWizard).setProjectName(PROJECT_NAME);
		javaProjectWizard.finish();

		NewClassCreationWizard classCreationWizard = new NewClassCreationWizard();
		classCreationWizard.open();
		new NewClassWizardPage(classCreationWizard).setName(CLASS_NAME);
		new NewClassWizardPage(classCreationWizard).setStaticMainMethod(true);
		classCreationWizard.finish();

		new JavaEditor(CLASS_NAME + ".java").insertLine(6, "\t\tSystem.out.println(\"Hello World\");");
	}

	@Test
	public void testTogglingBreakpoint() {
		foo();
	}

	private void foo() {
		try {
			new JavaEditor(CLASS_NAME + ".java").toggleBreakpoint(6);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
}
