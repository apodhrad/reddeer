package org.jboss.reddeer.generator;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jboss.reddeer.swt.handler.WidgetHandler;

public class ShellCodeGenerator implements CodeGenerator {

	@Override
	public String generate(Control control) {
		StringBuffer result = new StringBuffer();
		if (control instanceof Shell) {
			String title = WidgetHandler.getInstance().getText((Shell) control);
			if (title == null) {
				return null;
			}
			result.append("public void activate() ");
			result.append("{\n\tnew DefaultShell(\"" + title + "\");\n}");
			result.append("\n");
			return result.toString();
		}
		return null;
	}
}
