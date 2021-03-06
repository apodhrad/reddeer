/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat, Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.reddeer.core.util;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.util.ResultRunnable;
import org.eclipse.reddeer.core.handler.ControlHandler;
import org.eclipse.reddeer.core.handler.ShellHandler;
import org.eclipse.reddeer.core.lookup.ShellLookup;
import org.eclipse.reddeer.core.lookup.WidgetLookup;

/**
 * A diagnostic tool which can be used for getting information about available
 * shells or widgets.
 * 
 * @author apodhrad
 *
 */
public class DiagnosticTool {

	public static final String DEFAULT_LINE_DELIMITER = "\n";
	public static final String DEFAULT_INDENT = "\t";

	private int initialIndentation;
	private String indentation;
	private String lineDelimiter;

	/**
	 * Constructs a diagnostic tool with default settings.
	 */
	public DiagnosticTool() {
		this(0, DEFAULT_INDENT, DEFAULT_LINE_DELIMITER);
	}

	/**
	 * Constructs a diagnostic tool with default settings.
	 * 
	 * @param initialIndentation
	 *            Initial indentation
	 * @param indentation
	 *            Indentation string
	 * @param lineDelimiter
	 *            Line delimiter string
	 */
	public DiagnosticTool(int initialIndentation, String indentation, String lineDelimiter) {
		this.initialIndentation = initialIndentation;
		this.indentation = indentation;
		this.lineDelimiter = lineDelimiter;
	}

	/**
	 * Returns diagnostic information about all available shells.
	 * 
	 * @return Information about all available shells
	 */
	public String getShellsDiagnosticInformation() {
		StringBuffer result = new StringBuffer();
		result.append("The following shells are available").append(lineDelimiter);
		for (Shell shell : ShellLookup.getInstance().getShells()) {
			result.append(indentation).append(getWidgetInformation(shell)).append(lineDelimiter);
		}
		return result.toString();
	}

	/**
	 * Returns diagnostic information about all available widgets in a given
	 * parent.
	 * 
	 * @param parent
	 *            Parent control
	 * @return information about all available widgets in the parent
	 */
	public String getDiagnosticInformation(final Control parent) {
		return Display.syncExec(new ResultRunnable<String>() {

			@Override
			public String run() {
				return getDiagnosticInformation(parent, initialIndentation);
			}

		});
	}

	/**
	 * Returns diagnostic information about all available widgets in a given
	 * parent with the specified depth.
	 * 
	 * @param parent
	 *            Parent control
	 * @param depth
	 *            Depth in the widget tree; used for indentation
	 * @return information about all available widgets in the parent
	 */
	private String getDiagnosticInformation(Control parent, int depth) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < depth; i++) {
			result.append(indentation);
		}
		result.append(getWidgetInformation(parent)).append(lineDelimiter);
		if (parent instanceof Composite) {
			Composite composite = (Composite) parent;
			if (composite != null && !composite.isDisposed() && composite.getChildren() != null){
				for (Control child : composite.getChildren()) {
					result.append(getDiagnosticInformation(child, depth + 1));
				}
			}
		}
		return result.toString();
	}

	/**
	 * Returns one line information about a given widget.
	 * 
	 * @param widget
	 *            Widget
	 * @return one line information about the widget
	 */
	protected String getWidgetInformation(Control widget) {
		if (widget == null) {
			return "null";
		}
		StringBuffer result = new StringBuffer();
		result.append(widget.getClass());
		try{
			Shell controlShell = ControlHandler.getInstance().getShell(widget);
			if(controlShell != null && !controlShell.isDisposed()){
				String shellText = null;
				shellText = ShellHandler.getInstance().getText(controlShell);
				result.append("[shell: '"+shellText+"']");
			}
		} catch (Exception e) {
			// ignore, just provide as much information as possible
		}
		
		try {
			String label = WidgetLookup.getInstance().getLabel(widget);
			if (label != null) {
				result.append(" with label '" + label + "'");
			}
		} catch (Exception e) {
			// ignore, just provide as much information as possible
		}
		try {
			String text = TextWidgetUtil.getText(widget);
			if (text != null) {
				result.append(" with text '" + text + "'");
			}
		} catch (Exception e) {
			// ignore, just provide as much information as possible
		}
		return result.toString();
	}
}
