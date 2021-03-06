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
package org.eclipse.reddeer.eclipse.rse.ui.dialogs;

import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * This class represents Remote System password prompt dialog
 * @author Pavol Srna
 *
 */
public class SystemPasswordPromptDialog extends DefaultShell{

	public static final String TITLE = "Enter Password";
	
	/**
	 * Constructs a dialog with {@value #TITLE}.
	 */
	public SystemPasswordPromptDialog() {
		super(TITLE);
	}
	
	public void setUserID(String username){
		new LabeledText("User ID:").setText(username);
	}
	
	public void setPassword(String password){
		new LabeledText("Password (optional):").setText(password);
	}
	
	public String getUserID(){
		return new LabeledText("User ID:").getText();
	}
	
	public String getPassword(){
		return new LabeledText("Password (optional):").getText();
	}
	
	public void OK(){
		new PushButton("OK").click();
		new WaitWhile(new ShellIsAvailable(this));
	}
	
}
