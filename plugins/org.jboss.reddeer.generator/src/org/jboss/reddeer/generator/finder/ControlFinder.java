package org.jboss.reddeer.generator.finder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ControlFinder extends Finder<Control> {

	@Override
	public List<Control> getChildren(Control child) {
		List<Control> children = new ArrayList<Control>();
		if (child instanceof Composite) {
			Composite composite = (Composite) child;
			Control[] controls = composite.getChildren();
			for (Control control : controls) {
				children.add(control);
			}
		}
		return children;
	}

}
