package org.jboss.reddeer.generator;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.jboss.reddeer.generator.util.WidgetUtil;
import org.jboss.reddeer.swt.handler.ComboHandler;

public class ComboCodeGenerator implements CodeGenerator {

	@Override
	public String generate(Control control) {
		StringBuffer result = new StringBuffer();
		if (control instanceof Combo) {
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
			result.append("public Combo get" + method + "CMB() ");
			result.append("{\n\treturn new LabeledCombo(" + group + "\"" + label + "\");\n}");
			String[] items = ComboHandler.getInstance().getItems((Combo) control);
			if (items.length > 0) {
				result.append("\n");
			}
			for (String item : items) {
				result.append("\n");
				result.append("public static final String "
						+ WidgetUtil.removeSpecialChars(label.toUpperCase().replaceAll(" ", "_") + "_"
								+ item.toUpperCase()) + " = " + item);
			}
			result.append("\n");
			return result.toString();
		}
		return null;
	}
}
