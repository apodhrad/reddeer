package org.jboss.reddeer.swt.test.impl.button;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.jboss.reddeer.swt.impl.button.LabeledCheckBox;
import org.jboss.reddeer.swt.test.SWTLayerTestCase;
import org.jboss.reddeer.swt.test.utils.LabelTestUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author apodhrad
 *
 */
public class LabeledCheckBoxTest extends SWTLayerTestCase {
	
	@Override
	protected void createControls(Shell shell) {
		LabelTestUtils.createLabel(shell, "Label 1");
		new Button(shell, SWT.CHECK);
		
		LabelTestUtils.createLabel(shell, "Label 2");
		Button checkBox = new Button(shell, SWT.CHECK);
		checkBox.setSelection(true);
		
	}
	
	@Test
	public void labeledCheckBoxTest() {
		Assert.assertFalse(new LabeledCheckBox("Label 1").isChecked());
		Assert.assertTrue(new LabeledCheckBox("Label 2").isChecked());
	}
}
