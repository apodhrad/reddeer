package org.jboss.reddeer.core.condition;

import org.eclipse.swt.widgets.Shell;
import org.hamcrest.Matcher;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.core.lookup.ShellLookup;
import org.jboss.reddeer.core.util.InstanceValidator;

/**
 * Condition is met when a shell matching title with the specified matcher is available.  
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ShellMatchingMatcherIsAvailable extends AbstractWaitCondition {

	private Matcher<String> matcher;
	private Logger logger = Logger.getLogger(ShellMatchingMatcherIsAvailable.class);
	
	/**
	 * Creates new ShellMatchingMatcherIsAvailable wait condition with specified matcher.
	 * @param matcher matcher to match shell title
	 */
	public ShellMatchingMatcherIsAvailable(Matcher<String> matcher) {
		InstanceValidator.checkNotNull(matcher, "matcher");
		this.matcher = matcher;
	}

	@Override
	public boolean test() {
		logger.debug("Looking for shell with title matching matcher");
		Shell[] availableShells = ShellLookup.getInstance().getShells();
		for (Shell shell: availableShells) { 
			if (matcher.matches(shell)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String description() {
		return "shell matching matcher is available.";
	}
}
