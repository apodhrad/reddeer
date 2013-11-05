package org.jboss.reddeer.swt.impl.mouse;

import org.jboss.reddeer.swt.api.Mouse;
import org.jboss.reddeer.swt.util.OS;
import org.jboss.reddeer.swt.util.Utils;

/**
 * Default mouse implementation.
 * 
 * @author apodhrad
 * 
 */
public class DefaultMouse implements Mouse {

	@Override
	public void click(final int x, final int y) {
		new SWTMouse().click(x, y);
	}

	@Override
	public void doubleClick(final int x, final int y) {
		if (Utils.isRunningOS(OS.LINUX)) {
			new SWTMouse().doubleClick(x, y);
			return;
		} else {
			new AWTMouse().click(x, y);
		}
	}

	@Override
	public void hover(final int x, final int y) {
		new SWTMouse().hover(x, y);
	}

	@Override
	public void dragAndDrop(final int x1, final int y1, final int x2, final int y2) {
		throw new UnsupportedOperationException();
	}
}
