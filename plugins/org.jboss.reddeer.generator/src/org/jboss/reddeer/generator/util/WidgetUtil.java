package org.jboss.reddeer.generator.util;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Widget;
import org.jboss.reddeer.swt.handler.WidgetHandler;

public class WidgetUtil {

	public static String getLabel(Widget widget) {
		String label = WidgetHandler.getInstance().getLabel(widget);
		return label != null ? removeMnemonics(label) : null;
	}

	public static String getText(Widget widget) {
		String text = WidgetHandler.getInstance().getText(widget);
		return text != null ? removeMnemonics(text) : null;
	}

	public static String getGroup(Control control) {
		Composite parent;
		while ((parent = control.getParent()) != null) {
			if (parent instanceof Group) {
				return WidgetHandler.getInstance().getText(parent);
			}
			control = parent;
		}
		return null;
	}

	public static int getStyle(Widget widget) {
		return WidgetHandler.getInstance().getStyle(widget);
	}

	public static boolean hasStyle(Widget widget, int style) {
		return (getStyle(widget) & style) != 0;
	}

	public static String removeMnemonics(String text) {
		return text.replaceAll("&", "");
	}

	public static String removeSpecialChars(String text) {
		return text.replaceAll(" |\\(|\\)|\\*|\\:|<|>|&|\\.", "");
	}
}
