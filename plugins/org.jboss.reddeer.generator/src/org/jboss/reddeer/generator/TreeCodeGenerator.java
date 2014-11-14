package org.jboss.reddeer.generator;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jboss.reddeer.generator.util.WidgetUtil;

public class TreeCodeGenerator implements CodeGenerator {

	@Override
	public String generate(Control control) {
		StringBuffer result = new StringBuffer();
		if (control instanceof Tree) {
			String group = WidgetUtil.getGroup(control);
			String label = WidgetUtil.getLabel(control);
			if (label == null || label.equals("")) {
				label = group == null ? "Default" : group;
			}
			if (group == null) {
				group = "";
			} else {
				group = "\"" + group + "\", ";
			}
			String method = WidgetUtil.removeSpecialChars(label);
			result.append("public Text get" + method + "TRI() ");
			result.append("{\n\treturn new DefaultTree(" + group + "0" + ");\n}\n");

			Tree tree = (Tree) control;
			Queue<TreeItem> queue = new LinkedList<TreeItem>();
			queue.addAll(Arrays.asList(tree.getItems()));
			while (!queue.isEmpty()) {
				TreeItem parent = queue.poll();
				String itemLabel = parent.getText();
				if (itemLabel != null && !itemLabel.equals("")) {
					result.append("public static final String " + parent.getText().toUpperCase().replaceAll(" ", "_")
							+ " = \"" + parent.getText() + "\";\n");
				}
				for (TreeItem child : parent.getItems()) {
					queue.add(child);
				}
			}
			return result.toString();
		}
		return null;
	}
}
