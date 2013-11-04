package org.jboss.reddeer.swt.impl.mouse;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import org.jboss.reddeer.swt.api.Mouse;
import org.jboss.reddeer.swt.exception.SWTLayerException;

/**
 * AWT implementation of mouse
 * 
 * @author apodhrad
 * 
 */
public class AWTMouse implements Mouse {

	protected Robot robot;

	public AWTMouse() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			throw new SWTLayerException("Problem with java.awt.Robot occured", e);
		}
	}

	@Override
	public void click(final int x, final int y) {
		robot.mouseMove(x, y);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		robot.waitForIdle();

	}

	@Override
	public void doubleClick(final int x, final int y) {
		robot.mouseMove(x, y);
		robot.mousePress(java.awt.event.InputEvent.BUTTON1_MASK);
		robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_MASK);
		robot.mousePress(java.awt.event.InputEvent.BUTTON1_MASK);
		robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_MASK);
		robot.waitForIdle();
	}

	@Override
	public void hover(final int x, final int y) {
		robot.mouseMove(0, 0);
		robot.mouseMove(x, y);
		robot.waitForIdle();
	}

	@Override
	public void dragAndDrop(final int x1, final int y1, final int x2, final int y2) {
		throw new UnsupportedOperationException();
	}
}
