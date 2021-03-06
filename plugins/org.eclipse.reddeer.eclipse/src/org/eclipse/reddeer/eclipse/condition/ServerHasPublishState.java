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
package org.eclipse.reddeer.eclipse.condition;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.Server;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersViewEnums.ServerPublishState;

/**
 * 
 * @author odockal, jkopriva@redhat.com
 *
 */

public class ServerHasPublishState extends AbstractWaitCondition {

	private ServerPublishState expectedPublishState;
	private ServerPublishState currentPublishState;
	private ServerPublishState resultPublishState;
	private Server server;

	public ServerHasPublishState(Server server, ServerPublishState expectedState) {
		this.expectedPublishState = expectedState;
		this.server = server;
	}

	@Override
	public boolean test() {
		this.currentPublishState = server.getLabel().getPublishState();
		if (expectedPublishState.equals(this.currentPublishState)) {
			this.resultPublishState = this.currentPublishState;
			return true;
		}
		return false;
	}

	@Override
	public String description() {
		return "server's publish state is '" + expectedPublishState.getText() + "'";
	}
	
	@Override
	public String errorMessageUntil() {
		return "Server still has publish state '" + expectedPublishState.getText() + "'";
	}
	
	@Override
	public String errorMessageWhile() {
		return "server expected state was '" + expectedPublishState.getText() + "' but current state is '"
				+ currentPublishState.getText() + "'"; 
	}

	@SuppressWarnings("unchecked")
	@Override 
	public ServerPublishState getResult() {
		return this.resultPublishState;
	}
	
}
