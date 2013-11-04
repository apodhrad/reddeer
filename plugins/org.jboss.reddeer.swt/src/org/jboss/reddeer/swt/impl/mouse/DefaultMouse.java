package org.jboss.reddeer.swt.impl.mouse;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.jboss.reddeer.swt.api.Mouse;
import org.jboss.reddeer.swt.util.Display;

/**
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
				Event event = new Event();
				event.x = x;
				event.y = y;
				event.count = 0;
				event.button = 1;
				event.type = SWT.MouseMove;
				Display.getDisplay().post(event);
				event.count = 1;
				event.type = SWT.MouseDown;
				Display.getDisplay().post(event);
				event.type = SWT.MouseUp;
				Display.getDisplay().post(event);
			}
		});
	}

	@Override
	public void doubleClick(final int x, final int y) {
		/*
		 * Display.syncExec(new Runnable() {
		 * 
		 * @Override public void run() { Event event = new Event(); event.x = x;
		 * event.y = y; event.count = 0; event.button = 1; event.type =
		 * SWT.MouseMove; Display.getDisplay().post(event); event.count = 1;
		 * event.type = SWT.MouseDown; Display.getDisplay().post(event);
		 * event.type = SWT.MouseUp; Display.getDisplay().post(event);
		 * event.type = SWT.MouseDown; event.count = 2;
		 * Display.getDisplay().post(event); event.type = SWT.MouseDoubleClick;
		 * Display.getDisplay().post(event); event.type = SWT.MouseUp;
		 * Display.getDisplay().post(event); } });
		 */
		try {
			Robot robot = new Robot();
			robot.mouseMove(x, y);
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			robot.waitForIdle();
		} catch (AWTException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void hover(final int x, final int y) {
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				Event move1 = new Event();
				move1.x = 0;
				move1.y = 0;
				move1.type = SWT.MouseMove;
				Display.getDisplay().post(move1);
				Event move2 = new Event();
				move2.x = x;
				move2.y = y;
				move2.type = SWT.MouseMove;
				Display.getDisplay().post(move2);
				Event hover = new Event();
				hover.x = x;
				hover.y = y;
				hover.type = SWT.MouseHover;
				Display.getDisplay().post(hover);
			}
		});
	}

	@Override
	public void dragAndDrop(final int x1, final int y1, final int x2, final int y2) {
		throw new UnsupportedOperationException();
	}
}
