package org.jboss.reddeer.graphiti.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;
import org.jboss.reddeer.graphiti.condition.IsToolActivated;
import org.jboss.reddeer.graphiti.finder.EditPartFinder;
import org.jboss.reddeer.graphiti.finder.PaletteEntryFinder;
import org.jboss.reddeer.graphiti.utils.PrintInfo;
import org.jboss.reddeer.graphiti.view.PaletteView;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * 
 * @author apodhrad
 * 
 */
public class Palette {

	private PaletteViewer paletteViewer;

	public Palette(final PaletteViewer paletteViewer) {
		if(paletteViewer == null) {
			throw new IllegalArgumentException("Cannot create palette from null!");
		}
		this.paletteViewer = paletteViewer;
		// Display.syncExec(new Runnable() {
		//
		// @Override
		// public void run() {
		// PrintInfo.editParts(paletteViewer.getContents());
		// }
		// });
	}

	public void activateToolEntryEditPart(String label) {
		Display.syncExec(new Runnable() {
			
			@Override
			public void run() {
				
			}
		});
	}
	
	public void activateTool(String tool) {
		activateTool(tool, null);
	}

	public void activateTool(String tool, String container) {
		List<Matcher<? super PaletteEntry>> matchers = new ArrayList<Matcher<? super PaletteEntry>>();
		matchers.add(new ToolEntryWithLabel(tool));
		if (container != null) {
			matchers.add(new ToolEntryWithParent(container));
		}
		List<PaletteEntry> entries = getPaletteEntries(new AllOf<PaletteEntry>(matchers));
		if (entries.isEmpty()) {
			throw new RuntimeException("Cannot find palette entry with label '" + tool + "'");
		}
		final ToolEntry toolEntry = (ToolEntry) entries.get(0);
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				paletteViewer.setActiveTool(toolEntry);
			}
		});
		new WaitUntil(new IsToolActivated(this, tool));
	}

	public String getActiveTool() {
		return Display.syncExec(new ResultRunnable<String>() {
			@Override
			public String run() {
				return paletteViewer.getActiveTool().getLabel();
			}
		});
	}

	public List<PaletteEntry> getPaletteEntries(final Matcher<PaletteEntry> matcher) {
		new PaletteView().open();
		return Display.syncExec(new ResultRunnable<List<PaletteEntry>>() {
			@Override
			public List<PaletteEntry> run() {
				PaletteRoot paletteRoot = paletteViewer.getPaletteRoot();
				return new PaletteEntryFinder().find(paletteRoot, matcher);
			}

		});
	}

	public class ToolEntryWithLabel extends BaseMatcher<PaletteEntry> {

		private String label;

		public ToolEntryWithLabel(String label) {
			this.label = label;
		}

		@Override
		public boolean matches(Object obj) {
			if (obj instanceof ToolEntry) {
				ToolEntry toolEntry = (ToolEntry) obj;
				return toolEntry.getLabel().equals(label);
			}
			return false;
		}

		@Override
		public void describeTo(Description desc) {

		}

	}

	public class ToolEntryWithParent extends BaseMatcher<PaletteEntry> {

		private String label;

		public ToolEntryWithParent(String label) {
			this.label = label;
		}

		@Override
		public boolean matches(Object obj) {
			if (obj instanceof ToolEntry) {
				ToolEntry toolEntry = (ToolEntry) obj;
				return toolEntry.getParent().getLabel().equals(label);
			}
			return false;
		}

		@Override
		public void describeTo(Description desc) {

		}

	}
}
