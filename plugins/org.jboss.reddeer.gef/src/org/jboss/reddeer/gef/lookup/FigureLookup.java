package org.jboss.reddeer.gef.lookup;

import java.util.List;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPartViewer;
import org.hamcrest.Matcher;
import org.jboss.reddeer.gef.GEFLayerException;
import org.jboss.reddeer.gef.finder.FigureFinder;
import org.jboss.reddeer.junit.logging.Logger;

/**
 * Lookup for {@link org.eclipse.gef.EditPart}.
 * 
 * @author Andrej Podhradsky (andrej.podhradsky@gmail.com)
 *
 */
public class FigureLookup {

	protected final Logger log = Logger.getLogger(this.getClass());

	private static FigureLookup instance;

	private FigureLookup() {

	}

	public static FigureLookup getInstance() {
		if (instance == null) {
			instance = new FigureLookup();
		}
		return instance;
	}

	/**
	 * Finds an edit part which is fulfilled by the specified matcher at a given index. The edit part is searched in the
	 * active editor.
	 * 
	 * @param matcher
	 *            Matcher
	 * @param index
	 *            Index
	 * @return Edit part
	 */
	public IFigure findFigure(Matcher<IFigure> matcher, int index) {
		return findFigure(ViewerLookup.getInstance().findGraphicalViewer(), matcher, index);
	}

	public IFigure findFigure(EditPartViewer viewer, Matcher<IFigure> matcher, int index) {
		FigureCanvas canvas = (FigureCanvas) viewer.getControl();
		return findFigure(canvas.getContents(), matcher, index);
	}

	public IFigure findFigure(IFigure parent, Matcher<IFigure> matcher, int index) {
		List<IFigure> figures = new FigureFinder().find(parent, matcher);
		if (figures.size() <= index) {
			new GEFLayerException("Canno find figure with matcher " + matcher + " at " + index);
		}
		return figures.get(index);
	}

}
