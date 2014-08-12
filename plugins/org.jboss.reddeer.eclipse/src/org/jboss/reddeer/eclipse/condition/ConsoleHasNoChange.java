package org.jboss.reddeer.eclipse.condition;

import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;

/**
 * Returns true if a console has no change for the specified time period.
 * 
 * @author Andrej Podhradsky
 * 
 */
public class ConsoleHasNoChange implements WaitCondition {

	private TimePeriod timePeriod;

	/**
	 * Construct the condition with {@link TimePeriod#NORMAL}.
	 */
	public ConsoleHasNoChange() {
		this(TimePeriod.NORMAL);
	}

	/**
	 * Constructs the condition with a given time period.
	 * 
	 * @param timePeriod
	 *            Time period
	 */
	public ConsoleHasNoChange(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}

	@Override
	public boolean test() {
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();

		for (int i = 0; i < timePeriod.getSeconds(); i++) {
			String consoleTextBefore = consoleView.getConsoleText();
			AbstractWait.sleep(TimePeriod.getCustom(1));
			String consoleTextAfter = consoleView.getConsoleText();
			boolean isEqual = consoleTextBefore.equals(consoleTextAfter);
			if (!isEqual) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String description() {
		return "Console is still changing";
	}
}
