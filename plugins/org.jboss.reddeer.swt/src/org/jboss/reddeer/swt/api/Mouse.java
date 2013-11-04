package org.jboss.reddeer.swt.api;

/**
 * 
 * @author apodhrad
 * 
 */
public interface Mouse {

	void click(int x, int y);

	void doubleClick(int x, int y);

	void hover(int x, int y);

	void dragAndDrop(int x1, int y1, int x2, int y2);
}
