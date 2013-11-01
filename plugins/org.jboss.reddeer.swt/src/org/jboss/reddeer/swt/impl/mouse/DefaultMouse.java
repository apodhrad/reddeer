package org.jboss.reddeer.swt.impl.mouse;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
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
				event.type = SWT.MouseDown;
				event.count = 2;
				Display.getDisplay().post(event);
				event.type = SWT.MouseDoubleClick;
				Display.getDisplay().post(event);
				event.type = SWT.MouseUp;
				Display.getDisplay().post(event);
			}
		});
	}

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

	public void dragAndDrop(final int x1, final int y1, final int x2, final int y2) {
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				Event move1 = new Event();
				move1.x = x1;
				move1.y = y1;
				move1.count = 0;
				move1.button = 0;
				move1.type = SWT.MouseMove;
				Display.getDisplay().post(move1);
				Event down = new Event();
				down.x = x1;
				down.y = y1;
				down.count = 1;
				down.button = 1;
				down.type = SWT.MouseDown;
				Display.getDisplay().post(down);
				Event drag = new Event();
				drag.x = x1;
				drag.y = y1;
				drag.count = 1;
				drag.button = 1;
				drag.type = DND.DragStart;
				Display.getDisplay().post(drag);
				Event move2 = new Event();
				move2.x = x2;
				move2.y = y2;
				move2.count = 0;
				move2.button = 0;
				move2.type = SWT.MouseMove;
				Display.getDisplay().post(move2);
				Event up = new Event();
				up.x = x2;
				up.y = y2;
				up.count = 1;
				up.button = 1;
				up.type = SWT.MouseUp;
				Display.getDisplay().post(up);
				Event setData = new Event();
				setData.x = x2;
				setData.y = y2;
				setData.count = 1;
				setData.button = 1;
				setData.type = DND.DragSetData;
				Display.getDisplay().post(setData);
				Event drop = new Event();
				drop.x = x2;
				drop.y = y2;
				drop.count = 1;
				drop.button = 1;
				drop.type = DND.Drop;
				Display.getDisplay().post(drop);
			}
		});
	}
}
