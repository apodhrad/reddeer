package org.jboss.reddeer.generator;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.jboss.reddeer.generator.util.WidgetUtil;

public class TableCodeGenerator implements CodeGenerator {

	private static int index = 0;

	@Override
	public String generate(Control control) {
		StringBuffer result = new StringBuffer();
		if (control instanceof Table) {
			String label = WidgetUtil.getLabel(control);
			if (label == null || label.equals("")) {
				label = "DefaultTable";
			}
			String method = WidgetUtil.removeSpecialChars(label);
			String group = WidgetUtil.getGroup(control);
			if (group == null) {
				group = "";
			} else {
				group = "\"" + group + "\", ";
			}

			result.append("public Table get" + method + "TBL() {\n");
			result.append("\treturn new DefaultTable(" + group + "" + index + ");\n}\n");

			result.append("public get" + label + "TBI(int row) {\n");
			result.append("\treturn new DefaultTableItem(" + group + "" + index + ", row);\n}\n");

			Table table = (Table) control;
			TableColumn[] tableColumns = table.getColumns();
			for (int i = 0; i < tableColumns.length; i++) {
				String columnLabel = tableColumns[i].getText();
				result.append("public static final String "
						+ WidgetUtil.removeSpecialChars(columnLabel.toUpperCase().replaceAll(" ", "_")) + " = " + i
						+ ";\n\n");
			}

			return result.toString();
		}
		return null;
	}
}
