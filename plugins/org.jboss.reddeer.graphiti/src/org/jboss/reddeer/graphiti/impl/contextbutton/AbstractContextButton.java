package org.jboss.reddeer.graphiti.impl.contextbutton;

import org.eclipse.graphiti.tb.IContextButtonEntry;
import org.jboss.reddeer.graphiti.api.ContextButton;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;

/**
 * Abstract class form ContextButton implementation.
 * 
 * @author Andrej Podhradsky (andrej.podhradsky@gmail.com)
 *
 */
public abstract class AbstractContextButton implements ContextButton {

	protected IContextButtonEntry contextButtonEntry;

	public AbstractContextButton(IContextButtonEntry contextButtonEntry) {
		this.contextButtonEntry = contextButtonEntry;
	}

	@Override
	public void click() {
		Display.asyncExec(new Runnable() {
			@Override
			public void run() {
				contextButtonEntry.execute();
			}
		});
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
			}
		});
	}

	@Override
	public String getText() {
		return Display.syncExec(new ResultRunnable<String>() {
			@Override
			public String run() {
				return contextButtonEntry.getText();
			}
		});
	}

}
