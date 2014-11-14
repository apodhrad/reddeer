package org.jboss.reddeer.generator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.generator.finder.ControlFinder;

public class WidgetCodeGenerator implements CodeGenerator {

	private List<CodeGenerator> codeGenerators;

	private ControlFinder finder;
	private StringBuffer code;

	public WidgetCodeGenerator() {
		finder = new ControlFinder();
		code = new StringBuffer();

		codeGenerators = new ArrayList<CodeGenerator>();
		codeGenerators.add(new ShellCodeGenerator());
		codeGenerators.add(new TextCodeGenerator());
		codeGenerators.add(new ComboCodeGenerator());
		codeGenerators.add(new PushButtonCodeGenerator());
		codeGenerators.add(new CheckBoxCodeGenerator());
		codeGenerators.add(new RadioButtonCodeGenerator());
		codeGenerators.add(new TableCodeGenerator());
		codeGenerators.add(new TreeCodeGenerator());
	}

	public void addCodeGenerator(CodeGenerator codeGenerator) {
		codeGenerators.add(codeGenerator);
	}

	@Override
	public String generate(final Control control) {
		code.setLength(0);
		finder.find(control, new ControlMatcher());
		return code.toString();
	}

	private class ControlMatcher extends BaseMatcher<Control> {

		@Override
		public boolean matches(Object obj) {
			if (obj instanceof Control) {
				for (CodeGenerator codeGenerator : codeGenerators) {
					String widgetCode = codeGenerator.generate((Control) obj);
					if (widgetCode != null && !widgetCode.equals("") && !code.toString().contains(widgetCode)) {
						code.append(widgetCode);
					}
				}
			}
			return false;
		}

		@Override
		public void describeTo(Description desc) {

		}

	}
}
