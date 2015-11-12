package org.jboss.reddeer.generator.test.utils;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.core.util.ResultRunnable;
import org.jboss.reddeer.generator.CodeGenerator;
import org.junit.Assert;

public class CodeGeneratorTestUtils {

	private static Shell shell;

	public static void assertConstructorCode(String expectedCode, final CodeGenerator generator, final Control control) {
		String actualCode = Display.syncExec(new ResultRunnable<String>() {

			@Override
			public String run() {
				return generator.getConstructor(control);
			}

		});
		Assert.assertEquals(expectedCode, actualCode);
	}

	public static void closeTestingShell() {
		ShellHandler.getInstance().closeShell(shell);
	}

	public static <T> T createTestedControlWithShell(final TestingShell<T> testingShell) {
		return Display.syncExec(new ResultRunnable<T>() {
			@Override
			public T run() {
				shell = new Shell();
				shell.setText("Testing Shell");
				T control = testingShell.createControls(shell);
				shell.open();
				shell.setFocus();
				shell.layout();
				return control;
			}
		});
	}

	public interface TestingShell<T> {

		T createControls(Shell shell);
	}
}
