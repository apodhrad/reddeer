package org.jboss.reddeer.swt.test.impl.mouse;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.mouse.AWTMouse;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author apodhrad
 * 
 */
public class AWTMouseTest extends RedDeerTest {

	public static final String SHELL_TITLE = "Mouse Shell";

	private Shell shell;
	private Label label;
	private Label label1;
	private Label label2;

	@Test
	public void awtMouseClickTest() {
		Rectangle rShell = getAbsoluteBounds(shell);
		new AWTMouse().click(rShell.x, rShell.y);
		new WaitUntil(new LabelHasText("Simple Click"));
	}

	@Test
	public void awtMouseDoubleClickTest() {
		Rectangle rShell = getAbsoluteBounds(shell);
		new AWTMouse().doubleClick(rShell.x, rShell.y);
		new WaitUntil(new LabelHasText("Double Click"));
	}

	@Test
	public void awtMouseHoverTest() {
		Rectangle rLabel = getAbsoluteBounds(label);
		new AWTMouse().hover(rLabel.x, rLabel.y);
		new WaitUntil(new LabelHasText("Hover"));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void awtMouseDragAndDropTest() {
		Rectangle r1 = getAbsoluteBounds(label1);
		Rectangle r2 = getAbsoluteBounds(label2);
		int c = 100;
		new AWTMouse().dragAndDrop(r1.x + c, r1.y + c, r2.x + c, r2.y + c);
		new WaitUntil(new LabelHasText("", 1));
		new WaitUntil(new LabelHasText("Test", 2));
	}

	public class LabelHasText implements WaitCondition {

		private String text;
		private int index;

		public LabelHasText(String text) {
			this(text, 0);
		}

		public LabelHasText(String text, int index) {
			this.text = text;
			this.index = index;
		}

		@Override
		public boolean test() {
			String labelText = new DefaultLabel(index).getText();
			return labelText.equals(text);
		}

		@Override
		public String description() {
			return "Label at index " + index + " doesn't contain text '" + text + "'";
		}

	}

	@Before
	public void openTestShell() {
		Display.syncExec(new Runnable() {

			@Override
			public void run() {

				org.eclipse.swt.widgets.Display display = Display.getDisplay();
				shell = new org.eclipse.swt.widgets.Shell(display);
				shell.setText(SHELL_TITLE);
				label = new Label(shell, SWT.BORDER);
				label.setText("Label");
				label.setSize(80, 30);
				label.setLocation(100, 50);

				label.addMouseTrackListener(new MouseTrackListener() {

					@Override
					public void mouseHover(MouseEvent e) {
						label.setText("Hover");
					}

					@Override
					public void mouseExit(MouseEvent e) {
					}

					@Override
					public void mouseEnter(MouseEvent e) {
					}
				});

				label1 = new Label(shell, SWT.BORDER);
				label1.setText("Test");
				label1.setSize(80, 30);
				label1.setLocation(100, 100);
				setDragAndDrop(label1);

				label2 = new Label(shell, SWT.BORDER);
				label2.setText("");
				label2.setSize(80, 30);
				label2.setLocation(200, 100);
				setDragAndDrop(label2);

				shell.addMouseListener(new MouseListener() {

					@Override
					public void mouseUp(MouseEvent e) {
						if (e.count == 1) {
							System.out.println("***************** Clicked *********************");
							label.setText("Simple Click");
						}
					}

					@Override
					public void mouseDown(MouseEvent e) {
					}

					@Override
					public void mouseDoubleClick(MouseEvent e) {
						label.setText("Double Click");
					}
				});

				for (int i = 1; i < 100; i++) {
					shell.addListener(i, new Listener() {

						@Override
						public void handleEvent(Event event) {
							System.out.println("\t> " + event.getClass() + ": " + event);
						}
					});
				}

				shell.open();
			}
		});
		new DefaultShell(SHELL_TITLE);
	}

	public static Rectangle getAbsoluteBounds(final Control control) {
		return Display.syncExec(new ResultRunnable<Rectangle>() {

			@Override
			public Rectangle run() {
				Point p = control.toDisplay(0, 0);
				Rectangle r = control.getBounds();
				return new Rectangle(p.x, p.y, r.width, r.height);
			}
		});
	}

	private static void setDragAndDrop(final Label label) {

		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;

		final DragSource source = new DragSource(label, operations);
		source.setTransfer(types);
		source.addDragListener(new DragSourceListener() {
			public void dragStart(DragSourceEvent event) {
				event.doit = (label.getText().length() != 0);
			}

			public void dragSetData(DragSourceEvent event) {
				event.data = label.getText();
			}

			public void dragFinished(DragSourceEvent event) {
				if (event.detail == DND.DROP_MOVE)
					label.setText("");
			}
		});

		DropTarget target = new DropTarget(label, operations);
		target.setTransfer(types);
		target.addDropListener(new DropTargetAdapter() {
			public void drop(DropTargetEvent event) {
				System.out.println("drop");
				System.out.println("\t" + event);
				if (event.data == null) {
					event.detail = DND.DROP_NONE;
					return;
				}
				label.setText((String) event.data);
			}
		});
	}

	@After
	public void closeTestShell() {
		new DefaultShell(SHELL_TITLE).close();
	}

}
