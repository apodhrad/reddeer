package org.eclipse.reddeer.eclipse.jdt.ui.javaeditor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTargetExtension;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.hamcrest.Matcher;

/**
 * 
 * @author Andrej Podhradsky (apodhrad@redhat.com)
 *
 */
public class JavaEditor extends TextEditor {

	public JavaEditor() {
		super();
	}

	public JavaEditor(String title) {
		super(title);
	}

	public JavaEditor(Matcher<String> titleMatcher) {
		super(titleMatcher);
	}

	public void toggleBreakpoint(int line) {
		setCursorPosition(line - 1, 0);
		final IToggleBreakpointsTargetExtension toggleBreakpoint = (IToggleBreakpointsTargetExtension) DebugPlugin.getAdapter(editorPart,
				IToggleBreakpointsTarget.class);
		Display.asyncExec(new Runnable() {

			@Override
			public void run() {
				ISelection selection = editorPart.getEditorSite().getSelectionProvider().getSelection();
				try {
					System.out.println("Can toggle? " + toggleBreakpoint.canToggleBreakpoints(editorPart, selection));
					toggleBreakpoint.toggleBreakpoints(editorPart, selection);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		});
		Display.asyncExec(new Runnable() {

			@Override
			public void run() {
				IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints();
				for (IBreakpoint b: breakpoints) {
					try {
						System.out.println(b.getMarker());
						System.out.println(b.getMarker().getType().toString());
						System.out.println(b.getMarker().getResource().getFullPath().toString());
						System.out.println(b.getMarker().getAttribute("lineNumber"));
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

}
