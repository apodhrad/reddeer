package org.jboss.reddeer.requirements.test.autobuilding;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Andrej Podhradsky
 *
 */
@AutoBuilding(value = true, cleanup=false)
@RunWith(RedDeerSuite.class)
public class AutoBuildingRequirementWithoutCleanupTest {

	@Test
	public void autoBuildRequirementOnTest() {
		assertTrue(new ShellMenu("Project", "Build Automatically").isSelected());
	}

}
