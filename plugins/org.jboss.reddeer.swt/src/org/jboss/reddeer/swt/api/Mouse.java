package org.jboss.reddeer.swt.api;

/**
 * API for mouse manipulation
 * 
 * @author apodhrad
 * 
 */
public interface Mouse {

	/**
	 * Mouse simple click
	 * 
	 * @param x
	 * @param y
	 */
	void click(int x, int y);

	/**
	 * Mouse double click
	 * 
	 * @param x
	 * @param y
	 */
	void doubleClick(int x, int y);

	/**
	 * Mouse hover
	 * 
	 * @param x
	 * @param y
	 */
	void hover(int x, int y);

	/**
	 * Drag and drop
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	void dragAndDrop(int x1, int y1, int x2, int y2);
}
