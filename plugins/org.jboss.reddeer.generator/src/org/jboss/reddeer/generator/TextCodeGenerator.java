package org.jboss.reddeer.generator;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.jboss.reddeer.generator.util.WidgetUtil;

public class TextCodeGenerator implements CodeGenerator {

	@Override
	public String generate(Control control) {
		StringBuffer result = new StringBuffer();
		if (control instanceof Text) {
			String label = WidgetUtil.getLabel(control);
			if (label == null || label.equals("")) {
				return null;
			}
			String method = WidgetUtil.removeSpecialChars(label);
			String group = WidgetUtil.getGroup(control);
			if (group == null) {
				group = "";
			} else {
				group = "\"" + group + "\", ";
			}
			result.append("public Text get" + method + "TXT() ");
			result.append("{\n\treturn new LabeledText(" + group + "\"" + label + "\");\n}");
			result.append("\n");
			result.append("public void set" + method + "(String text) ");
			result.append("{\n\tget" + method + "TXT().setText(text);\n}");
			result.append("\n");
			result.append("public void get" + method + "() ");
			result.append("{\n\tget" + method + "TXT().getText();\n}");
			result.append("\n");
			return result.toString();
		}
		return null;
	}
}
