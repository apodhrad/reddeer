package org.jboss.reddeer.graphiti.editor;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.List;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.hamcrest.Matcher;
import org.jboss.reddeer.graphiti.condition.NewEditPartDetected;
import org.jboss.reddeer.graphiti.finder.EditPartFinder;
import org.jboss.reddeer.graphiti.finder.FigureFinder;
import org.jboss.reddeer.graphiti.matcher.All;
import org.jboss.reddeer.graphiti.matcher.WithLabel;
import org.jboss.reddeer.graphiti.matcher.WithTooltip;
import org.jboss.reddeer.graphiti.utils.BoundsCalculation;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.handler.WidgetHandler;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.lookup.WidgetLookup;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.editor.DefaultEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public class GefEditor extends DefaultEditor {

	protected GraphicalViewer viewer;

	public GefEditor() {
		super();
		init();
	}

	public GefEditor(String title) {
		super(title);
		init();
	}

	@Override
	protected IWorkbenchPart getPartByTitle(final String title) {
		return Display.syncExec(new ResultRunnable<IEditorPart>() {

			@Override
			public IEditorPart run() {
				IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();
				IEditorReference[] editors = activeWorkbenchWindow.getActivePage()
						.getEditorReferences();
				for (IEditorReference iEditorReference : editors) {
					if (iEditorReference.getTitle().equals(title)) {
						return iEditorReference.getEditor(false);
					}
				}
				return null;
			}
		});
	}

	private void init() {
		viewer = Display.syncExec(new ResultRunnable<GraphicalViewer>() {
			@Override
			public GraphicalViewer run() {
				return (GraphicalViewer) getEditorPart().getAdapter(GraphicalViewer.class);
			}

		});
		if (viewer == null) {
			throw new RuntimeException("Cannot find graphical viewer which is needed for GEF");
		}
		Display.syncExec(new Runnable() {

			@Override
			public void run() {

				getFigureCanvas().addMouseListener(new MouseListener() {

					@Override
					public void mouseUp(MouseEvent e) {
						System.out.println("=== mouseUp ===");
						System.out.println(e);
						System.out.println("button = " + e.button);
						System.out.println("stateMask = " + e.stateMask);
						System.out.println("count = " + e.count);
						System.out.println("e.time = " + e.time);
						System.out.println("===============");
					}

					@Override
					public void mouseDown(MouseEvent e) {
						System.out.println("=== mouseDown ===");
						System.out.println(e);
						System.out.println("button = " + e.button);
						System.out.println("stateMask = " + e.stateMask);
						System.out.println("count = " + e.count);
						System.out.println("e.time = " + e.time);
						System.out.println("=================");
					}

					@Override
					public void mouseDoubleClick(MouseEvent e) {
						System.out.println("=== mouseDoubleClick ===");
						System.out.println(e);
						System.out.println("button = " + e.button);
						System.out.println("stateMask = " + e.stateMask);
						System.out.println("count = " + e.count);
						System.out.println("e.time = " + e.time);
						System.out.println("========================");
					}
				});
			}
		});
	}

	public FigureCanvas getFigureCanvas() {
		return (FigureCanvas) viewer.getControl();
	}

	public void printAllFigures() {
		IFigure parent = getFigureCanvas().getContents();
		List<IFigure> list = new FigureFinder().find(parent, new All());
		for (IFigure figure : list) {
			System.out.println(figure.getClass());
			if (figure instanceof Label) {
				System.out.println("> Label: " + ((Label) figure).getText());
			}
			IFigure tooltip = figure.getToolTip();
			if (tooltip instanceof Label) {
				System.out.println("> Tooltip: " + ((Label) tooltip).getText());
			}
		}
	}

	public List<EditPart> getEditParts(final Matcher<?> matcher) {
		return Display.syncExec(new ResultRunnable<List<EditPart>>() {
			@Override
			public List<EditPart> run() {
				EditPart root = viewer.getContents();
				return new EditPartFinder().find(root, matcher);
			}

		});
	}

	protected EditPart getEditPartWithLabel(String label) {
		return getEditPartWithLabel(label, 0);
	}

	protected EditPart getEditPartWithLabel(String label, int index) {
		List<EditPart> list = getEditParts(new WithLabel(label));
		if (list.isEmpty()) {
			throw new RuntimeException("Cannot find edit part with label '" + label + "'");
		}
		return list.get(index);
	}

	protected void select(final EditPart editPart) {
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				viewer.select(editPart);
			}
		});
	}

	public void hover(final EditPart editPart) {
		hover(getFigure(editPart));
	}

	public void hover(final IFigure figure) {
		hover(figure, 1);
	}

	protected void hover(final IFigure figure, int count) {
		// if (count > 10) {
		// throw new RuntimeException("Hover doesn't show any context button!");
		// }
		Rectangle rec = BoundsCalculation.getAbsoluteBounds(getFigureCanvas(), figure);
		final Point centralPoint = BoundsCalculation.getCentralPoint(rec);
		// MouseUtils.click(centralPoint.x, centralPoint.y);
		// MouseUtils.mouseMove(centralPoint.x, centralPoint.y);

		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				try {
					Robot robot = new Robot();
					robot.setAutoWaitForIdle(true);
					robot.mouseMove(centralPoint.x, centralPoint.y);
					robot.delay(1000);
					robot.waitForIdle();
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
		});
		//
		// IFigure parent = getFigureCanvas().getContents();
		// List<IFigure> list = new FigureFinder().find(parent, new
		// IsInstanceOf(Clickable.class));
		// if (list.isEmpty()) {
		// hover(figure, count + 1);
		// }

	}

	public void doubleClick(EditPart editPart) {
		doubleClick(getFigure(editPart));
	}

	public void doubleClick(IFigure figure) {
		Rectangle rec = BoundsCalculation.getAbsoluteBounds(getFigureCanvas(), figure);
		final Point centralPoint = BoundsCalculation.getCentralPoint(rec);
		// new MyMouseUtils(getFigureCanvas()).doubleClick(centralPoint.x,
		// centralPoint.y);

		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				try {
					Robot robot = new Robot();
					robot.setAutoWaitForIdle(true);
					robot.mouseMove(centralPoint.x, centralPoint.y);
					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
					robot.waitForIdle();
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
		});

		// Display.syncExec(new Runnable() {
		//
		// @Override
		// public void run() {
		//
		// // mouse double click
		// Event dblClick = new Event();
		// dblClick.x = 156;
		// dblClick.y = 137;
		// dblClick.button = 1;
		// dblClick.type = SWT.MouseDoubleClick;
		// dblClick.count = 2;
		//
		// getFigureCanvas().removeMouseListener(listener);
		// getFigureCanvas().notifyListeners(SWT.MouseDoubleClick, dblClick);
		// }
		// });
	}

	private IFigure getFigure(EditPart editPart) {
		return ((GraphicalEditPart) editPart).getFigure();
	}

	public void selectEditPartWithLabel(String label) {
		EditPart editPart = getEditPartWithLabel(label);
		select(editPart);
	}

	public void selectEditPartWithTooltip(String tooltip) {
		List<EditPart> list = getEditParts(new WithTooltip(tooltip));
		if (list.isEmpty()) {
			throw new RuntimeException("Cannot find edit part with tooltip '" + tooltip + "'");
		}
		select(list.get(0));
	}

	public void hoverEditPartWithLabel(String label) {
		EditPart editPart = getEditPartWithLabel(label);
		hover(editPart);
	}

	public void hoverEditPartWithTooltip(String tooltip) {
		List<EditPart> list = getEditParts(new WithTooltip(tooltip));
		if (list.isEmpty()) {
			throw new RuntimeException("Cannot find edit part with tooltip '" + tooltip + "'");
		}
		hover(list.get(0));
	}

	public void deleteEditPartWithLabel(String label) {
		hoverEditPartWithLabel(label);
		new ContextButton("Delete").click();
		String deleteShellText = "Confirm Delete";
		new WaitUntil(new ShellWithTextIsActive(deleteShellText));
		new PushButton("Yes").click();
		new WaitWhile(new ShellWithTextIsActive(deleteShellText));
		new WaitWhile(new JobIsRunning());
	}

	public void deleteEditPartWithTooltip(String tooltip) {
		hoverEditPartWithTooltip(tooltip);
		new ContextButton("Delete").click();
		String deleteShellText = "Confirm Delete";
		new WaitUntil(new ShellWithTextIsActive(deleteShellText));
		new PushButton("Yes").click();
		new WaitWhile(new ShellWithTextIsActive(deleteShellText));
		new WaitWhile(new JobIsRunning());
	}

	public Palette getPalette() {
		PaletteViewer paletteViewer = viewer.getEditDomain().getPaletteViewer();
		return new Palette(paletteViewer);
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

	public void addToolFromPalette(String tool, final int x, final int y) {
		addToolFromPalette(tool, null, x, y);
	}

	public void addToolFromPalette(String tool, String container, final int x, final int y) {
		getPalette().activateTool(tool, container);

		List<EditPart> list = getEditParts(new All());
		int oldCount = list.size();

		final Rectangle rec = getAbsoluteBounds(viewer.getControl());
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				try {
					Robot robot = new Robot();
					robot.setAutoWaitForIdle(true);
					robot.mouseMove(rec.x + x, rec.y + y);
					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
					robot.waitForIdle();
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
		});
		WidgetLookup.getInstance().sendClickNotifications(viewer.getControl());

		new WaitUntil(new NewEditPartDetected(this, oldCount));
	}

	// public void click(final int x, final int y) {
	// final Rectangle rec = getAbsoluteBounds(viewer.getControl());
	// // MouseUtils.click(rec.x + x, rec.y + y);
	// // System.out.println("============= Mouse Click =================");
	// // new MyMouseUtils(getFigureCanvas()).click(rec.x + x, rec.y + y);
	//
	// // System.out.println("===========================================");
	//
	// // Display.syncExec(new Runnable() {
	// //
	// // @Override
	// // public void run() {
	// //
	// // }
	// // });
	// // AbstractWait.sleep(5 * 1000);
	// // try {
	// // } catch (Exception ex) {
	// // System.out.println();
	// // }
	// }

	public static class MyMouseUtils {

		private Widget widget;
		private int time;

		public MyMouseUtils(Widget widget) {
			this.widget = widget;
		}

		public void click(final int x, final int y) {
			Display.syncExec(new Runnable() {

				@Override
				public void run() {
					mouseMove(x, y);
					mouseDown(x, y, 1);
					mouseUp(x, y, 1);
				}
			});
		}

		public void doubleClick(final int x, final int y) {
			Display.syncExec(new Runnable() {

				@Override
				public void run() {
					// mouse move
					Event move = new Event();
					move.x = x;
					move.y = y;
					move.type = SWT.MouseMove;
					// mouse down
					Event down = new Event();
					down.x = x;
					down.y = y;
					down.button = 1;
					down.count = 1;
					down.type = SWT.MouseDown;
					// mouse up
					Event up = new Event();
					up.x = x;
					up.y = y;
					up.button = 1;
					up.count = 1;
					up.type = SWT.MouseUp;
					// mouse down
					Event down2 = new Event();
					down2.x = x;
					down2.y = y;
					down2.button = 1;
					down2.count = 2;
					down2.type = SWT.MouseDown;
					// mouse double click
					Event dblClick = new Event();
					dblClick.x = x;
					dblClick.y = y;
					dblClick.button = 1;
					dblClick.count = 2;
					dblClick.type = SWT.MouseDoubleClick;
					// mouse up
					Event up2 = new Event();
					up2.x = x;
					up2.y = y;
					up2.button = 1;
					up2.count = 2;
					up2.type = SWT.MouseUp;
					// send events
					Display.getDisplay().post(move);
					Display.getDisplay().post(down);
					Display.getDisplay().post(up);
					Display.getDisplay().post(down2);
					dblClick.time = down2.time;
					Display.getDisplay().post(dblClick);
					Display.getDisplay().post(up2);
				}
			});
		}

		protected void mouseMove(final int x, final int y) {
			Event event = new Event();
			event.x = x;
			event.y = y;
			event.type = SWT.MouseMove;
			event.widget = widget;
			Display.getDisplay().post(event);
		}

		protected void mouseDown(final int x, final int y, final int button) {
			Event event = new Event();
			event.x = x;
			event.y = y;
			event.button = button;
			event.type = SWT.MouseDown;
			event.widget = widget;
			Display.getDisplay().post(event);
		}

		protected void mouseUp(final int x, final int y, final int button) {
			Event event = new Event();
			event.x = x;
			event.y = y;
			event.button = button;
			event.type = SWT.MouseUp;
			event.widget = widget;
			Display.getDisplay().post(event);
		}

		protected void mouseDown(final int x, final int y, final int button, int count) {
			Event event = new Event();
			event.x = x;
			event.y = y;
			event.button = button;
			event.type = SWT.MouseDown;
			event.widget = widget;
			event.count = count;
			time = event.time;
			Display.getDisplay().post(event);
		}

		protected void mouseUp(final int x, final int y, final int button, int count) {
			Event event = new Event();
			event.x = x;
			event.y = y;
			event.button = button;
			event.type = SWT.MouseUp;
			event.widget = widget;
			event.count = count;
			Display.getDisplay().post(event);
		}

		protected void mouseDoubleClick(final int x, final int y, final int button, int count) {
			Event event = new Event();
			event.x = x;
			event.y = y;
			event.button = button;
			event.type = SWT.MouseDoubleClick;
			event.widget = widget;
			event.count = count;
			event.time = time;
			Display.getDisplay().post(event);
		}

	}

}
