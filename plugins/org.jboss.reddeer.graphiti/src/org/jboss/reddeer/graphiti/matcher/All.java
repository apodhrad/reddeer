package org.jboss.reddeer.graphiti.matcher;

import org.eclipse.draw2d.IFigure;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class All extends BaseMatcher<IFigure> {

	@Override
	public boolean matches(Object obj) {
		System.out.println(obj);
		return true;
	}

	@Override
	public void describeTo(Description desc) {
		desc.appendText("all objects");
	}

}