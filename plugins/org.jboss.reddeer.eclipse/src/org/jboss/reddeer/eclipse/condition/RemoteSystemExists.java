package org.jboss.reddeer.eclipse.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.eclipse.rse.ui.view.SystemView;

/**
 * Returns true, if there is remote system with specified name
 * 
 * @author Pavol Srna
 *
 */
public class RemoteSystemExists extends AbstractWaitCondition {

	private String name;
	
	/**
	 * Constructs the condition with a given text.
	 * 
	 * @param name Name
	 */
	public RemoteSystemExists(String name) {
		this.name = name;
	}

	@Override
	public boolean test() {
		try{
			new SystemView().getSystem(this.name);
			return true;
		} catch (RedDeerException rde){
			return false;
		}		
	}
	
	@Override
	public String description() {
		return "there is remote system with name: " + this.name;
	}
	
}
