package org.jboss.reddeer.generator.finder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.hamcrest.Matcher;

public abstract class Finder<T> {

	public List<T> find(T parent, Matcher<?>... matchers) {
		List<T> list = new ArrayList<T>();
		Stack<T> stack = new Stack<T>();
		// Initial push
		stack.push(parent);
		// Depth first search
		while (!stack.isEmpty()) {
			// Pop figure
			T child = stack.pop();
			// If null then continue
			if (child == null) {
				continue;
			}
			// Does it matches?
			boolean matches = true;
			for (Matcher<?> matcher : matchers) {
				if (!matcher.matches(child)) {
					matches = false;
					break;
				}
			}
			if (matches) {
				list.add(child);
			}
			// Push another children
			for (T t : getChildren(child)) {
				stack.push(t);
			}
		}
		return list;
	}

	public abstract List<T> getChildren(T child);

}
