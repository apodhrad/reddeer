package org.jboss.reddeer.graphiti.editor;

import java.util.List;

import org.eclipse.draw2d.Clickable;
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
import org.hamcrest.core.IsInstanceOf;
import org.jboss.reddeer.graphiti.condition.NewEditPartDetected;
import org.jboss.reddeer.graphiti.finder.EditPartFinder;
import org.jboss.reddeer.graphiti.finder.FigureFinder;
import org.jboss.reddeer.graphiti.matcher.All;
import org.jboss.reddeer.graphiti.matcher.WithLabel;
import org.jboss.reddeer.graphiti.matcher.WithTooltip;
import org.jboss.reddeer.graphiti.utils.BoundsCalculation;
import org.jboss.reddeer.graphiti.utils.MouseUtils;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
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
						System.out.println("===== mouseUp");
					}

					@Override
					public void mouseDown(MouseEvent e) {
						System.out.println("===== mouseDown");
					}

					@Override
					public void mouseDoubleClick(MouseEvent e) {
						System.out.println(e);
						System.out.println("button = " + e.button);
						System.out.println("stateMask = " + e.stateMask);
						System.out.println("cont = " + e.count);
						System.out.println("===== mouseDoubleClick");
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

	protected void hover(final EditPart editPart) {
		hover(getFigure(editPart));
	}

	protected void hover(final IFigure figure) {
		hover(figure, 1);
	}

	protected void hover(final IFigure figure, int count) {
		if (count > 10) {
			throw new RuntimeException("Hover doesn't show any context button!");
		}
		Rectangle rec = BoundsCalculation.getAbsoluteBounds(getFigureCanvas(), figure);
		final Point centralPoint = BoundsCalculation.getCentralPoint(rec);
		MouseUtils.click(centralPoint.x, centralPoint.y);
		MouseUtils.mouseMove(centralPoint.x, centralPoint.y);

		IFigure parent = getFigureCanvas().getContents();
		List<IFigure> list = new FigureFinder().find(parent, new IsInstanceOf(Clickable.class));
		if (list.isEmpty()) {
			hover(figure, count + 1);
		}
	}
	
	public void doubleClick(EditPart editPart) {
		doubleClick(getFigure(editPart));
	}
	
	public void doubleClick(IFigure figure) {
		Rectangle rec = BoundsCalculation.getAbsoluteBounds(getFigureCanvas(), figure);
		final Point centralPoint = BoundsCalculation.getCentralPoint(rec);
		new MyMouseUtils(getFigureCanvas()).doubleClick(centralPoint.x, centralPoint.y);
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

	public void click(int x, int y) {
		Control control = viewer.getControl();
		Rectangle rec = BoundsCalculation.getAbsoluteBounds(control);
		// MouseUtils.click(rec.x + x, rec.y + y);
		List<EditPart> list = getEditParts(new All());
		int oldCount = list.size();
		// System.out.println("============= Mouse Click =================");
		new MyMouseUtils(getFigureCanvas()).click(rec.x + x, rec.y + y);
		// System.out.println("===========================================");

		// Display.syncExec(new Runnable() {
		//
		// @Override
		// public void run() {
		//
		// }
		// });
		// AbstractWait.sleep(5 * 1000);
		// try {
		new WaitUntil(new NewEditPartDetected(this, oldCount));
		// } catch (Exception ex) {
		// System.out.println();
		// }
	}

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
					mouseMove(x, y);
					mouseDown(x, y, 1);
					mouseUp(x, y, 1);
					mouseDown(x, y, 1, 2);
					mouseDoubleClick(x, y, 1, 2);
					mouseUp(x, y, 1, 2);
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
