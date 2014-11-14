package org.jboss.reddeer.generator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.jboss.reddeer.generator.util.WidgetUtil;

public class CheckBoxCodeGenerator implements CodeGenerator {

	@Override
	public String generate(Control control) {
		StringBuffer result = new StringBuffer();
		if (control instanceof Button) {
			if (!WidgetUtil.hasStyle(control, SWT.CHECK)) {
				return null;
			}
			String text = WidgetUtil.getText(control);
			if (text == null || text.equals("")) {
				return null;
			}
			String method = WidgetUtil.removeSpecialChars(text);
			String group = WidgetUtil.getGroup(control);
			if (group == null) {
				group = "";
			} else {
				group = "\"" + group + "\", ";
			}
			result.append("public CheckBox get" + method + "CHK() ");
			result.append("{\n\treturn new CheckBox(" + group + "\"" + text + "\");\n}");
			result.append("\n");
			return result.toString();
		}
		return null;
	}
}
