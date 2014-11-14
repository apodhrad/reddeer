package org.jboss.reddeer.generator.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jboss.reddeer.generator.WidgetCodeGenerator;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class CodeGeneratorAction implements IWorkbenchWindowActionDelegate {
	@SuppressWarnings("unused")
	private IWorkbenchWindow window;

	/**
	 * The constructor.
	 */
	public CodeGeneratorAction() {
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		Shell shell = new Shell(SWT.CLOSE | SWT.RESIZE);
		shell.setText("Code Generator");
		shell.setSize(400, 300);
		shell.setLayout(new FillLayout());

		final StyledText text = new StyledText(shell, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY);
		// text.addModifyListener(new ModifyListener() {
		// public void modifyText(ModifyEvent event) {
		// int maxLine = text.getLineCount();
		// int lineCountWidth = Math.max(String.valueOf(maxLine).length(), 3);
		//
		// StyleRange style = new StyleRange();
		// style.metrics = new GlyphMetrics(0, 0, lineCountWidth * 8 + 5);
		// Bullet bullet = new Bullet(ST.BULLET_NUMBER, style);
		// text.setLineBullet(0, text.getLineCount(), null);
		// text.setLineBullet(0, text.getLineCount(), bullet);
		// }
		// });
		text.setText("Press CTRL + SHIFT to generate the code");

		final WidgetCodeGenerator codeGenerator = new WidgetCodeGenerator();

		shell.getDisplay().addFilter(SWT.KeyDown, new Listener() {
			public void handleEvent(Event e) {
				if ((e.stateMask == SWT.CTRL) && (e.keyCode == SWT.SHIFT)) {
					if (text.isDisposed()) {
						return;
					}

					text.setText(codeGenerator.generate(new DefaultShell().getControl()));
				}

			}
		});

		shell.open();
	}

	/**
	 * Selection in the workbench has been changed. We can change the state of
	 * the 'real' action here if we want, but this can only happen after the
	 * delegate has been created.
	 * 
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}