package org.jboss.reddeer.swt.impl.mouse;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.jboss.reddeer.swt.api.Mouse;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.OS;
import org.jboss.reddeer.swt.util.Utils;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.WaitUntil;

/**
 * Default SWT implementation of mouse.
 * 
 * This implementation is not sometimes not working, use AWTMouse.
 * 
 * @see AWTMouse
 * 
 * @author apodhrad
 * 
 */
public class DefaultMouse implements Mouse {

	@Override
	public void click(final int x, final int y) {
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				Display.getDisplay().post(mouseEvent(x, y, 0, 0, SWT.MouseMove));
				AbstractWait.sleep(300);
				Display.getDisplay().post(mouseEvent(x, y, 1, 1, SWT.MouseDown));
				AbstractWait.sleep(300);
				Display.getDisplay().post(mouseEvent(x, y, 1, 1, SWT.MouseUp));
			}
		});
	}

	@Override
	public void doubleClick(final int x, final int y) {
		/*
		 * Mac OS doesn't recognize double click by SWT events. The problem is
		 * that event.count is still 1 on macosx.
		 */
		if (Utils.isRunningOS(OS.MACOSX)) {
			new AWTMouse().doubleClick(x, y);
			return;
		}
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				Display.getDisplay().post(mouseEvent(x, y, 0, 0, SWT.MouseMove));
				AbstractWait.sleep(300);
				Display.getDisplay().post(mouseEvent(x, y, 1, 1, SWT.MouseDown));
				AbstractWait.sleep(300);
				Display.getDisplay().post(mouseEvent(x, y, 1, 1, SWT.MouseUp));
				AbstractWait.sleep(300);
				Display.getDisplay().post(mouseEvent(x, y, 1, 2, SWT.MouseDown));
				AbstractWait.sleep(300);
				Display.getDisplay().post(mouseEvent(x, y, 1, 2, SWT.MouseDoubleClick));
				AbstractWait.sleep(300);
				Display.getDisplay().post(mouseEvent(x, y, 1, 2, SWT.MouseUp));
			}
		});
	}

	@Override
	public void hover(final int x, final int y) {
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				Display.getDisplay().post(mouseEvent(0, 0, 0, 0, SWT.MouseMove));
				AbstractWait.sleep(300);
				Display.getDisplay().post(mouseEvent(x, y, 0, 0, SWT.MouseMove));
				AbstractWait.sleep(300);
				Display.getDisplay().post(mouseEvent(x, y, 0, 0, SWT.MouseHover));
			}
		});
	}

	@Override
	public void dragAndDrop(final int x1, final int y1, final int x2, final int y2) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Create mouse event
	 * 
	 * @param x
	 * @param y
	 * @param button
	 * @param count
	 * @param type
	 * @return mouse event
	 */
	private static Event mouseEvent(int x, int y, int button, int count, int type) {
		Event event = new Event();
		event.x = x;
		event.y = y;
		event.button = button;
		event.count = count;
		event.type = type;
		return event;
	}

	public static void waitForIdle() {
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				new WaitUntil(new WaitCondition() {

					@Override
					public boolean test() {

						return Display.getDisplay().readAndDispatch();
					}

					@Override
					public String description() {
						return null;
					}
				});
				Display.getDisplay().update();
			}
		});
	}

	private static void mouseEnterx(final int x, final int y) {
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				Display.getDisplay().post(mouseEvent(x, y, 0, 0, SWT.MouseEnter));
				Display.getDisplay().post(mouseEvent(x, y, 0, 0, SWT.MouseMove));
			}
		});
		AbstractWait.sleep(1000);
		waitForIdle();
	}
}
