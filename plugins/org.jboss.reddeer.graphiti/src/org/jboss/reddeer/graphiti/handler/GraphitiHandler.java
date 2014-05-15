package org.jboss.reddeer.graphiti.handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.graphiti.features.context.IDoubleClickContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.tb.IContextButtonEntry;
import org.eclipse.graphiti.tb.IContextButtonPadData;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;
import org.eclipse.graphiti.ui.platform.GraphitiShapeEditPart;
import org.jboss.reddeer.graphiti.GraphitiLayerException;
import org.jboss.reddeer.graphiti.api.ContextButton;
import org.jboss.reddeer.graphiti.impl.contextbutton.internal.BasicContextButton;
import org.jboss.reddeer.graphiti.lookup.DiagramLookup;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;

/**
 * Handler for Graphiti UI operations.
 * 
 * @author Andrej Podhradsky (andrej.podhradsky@gmail.com)
 *
 */
public class GraphitiHandler {

	protected final Logger log = Logger.getLogger(this.getClass());

	private static GraphitiHandler instance;

	private GraphitiHandler() {

	}

	public static GraphitiHandler getInstance() {
		if (instance == null) {
			instance = new GraphitiHandler();
		}
		return instance;
	}

	public List<ContextButton> getContextButtonEntries(final org.eclipse.gef.EditPart editPart) {
		return getContextButtonEntries(DiagramLookup.getInstance().findDiagramEditor(), editPart);
	}

	public List<ContextButton> getContextButtonEntries(
			final org.eclipse.graphiti.ui.editor.DiagramEditor diagramEditor, final org.eclipse.gef.EditPart editPart) {
		return Display.syncExec(new ResultRunnable<List<ContextButton>>() {

			@Override
			public List<ContextButton> run() {
				List<IContextButtonEntry> entries = new ArrayList<IContextButtonEntry>();
				IToolBehaviorProvider[] tool = diagramEditor.getDiagramTypeProvider()
						.getAvailableToolBehaviorProviders();
				for (int i = 0; i < tool.length; i++) {
					IPictogramElementContext context = createPictogramContext(editPart);
					IContextButtonPadData pad = tool[i].getContextButtonPad(context);
					entries.addAll(pad.getDomainSpecificContextButtons());
					entries.addAll(pad.getGenericContextButtons());
				}
				List<ContextButton> contextButtonEntries = new ArrayList<ContextButton>();
				for (IContextButtonEntry entry : entries) {
					ContextButton contextButtonEntry = new BasicContextButton(entry);
					contextButtonEntries.add(contextButtonEntry);
				}
				return contextButtonEntries;
			}
		});
	}

	public void doubleClick(final org.eclipse.gef.EditPart editPart) {
		doubleClick(DiagramLookup.getInstance().findDiagramEditor(), editPart);
	}

	public void doubleClick(final org.eclipse.graphiti.ui.editor.DiagramEditor diagramEditor,
			final org.eclipse.gef.EditPart editPart) {
		List<ICustomFeature> features = Display.syncExec(new ResultRunnable<List<ICustomFeature>>() {

			@Override
			public List<ICustomFeature> run() {
				List<ICustomFeature> features = new ArrayList<ICustomFeature>();
				IToolBehaviorProvider[] tool = diagramEditor.getDiagramTypeProvider()
						.getAvailableToolBehaviorProviders();
				for (int i = 0; i < tool.length; i++) {
					IDoubleClickContext context = createPictogramContext(editPart);
					ICustomFeature feature = tool[i].getDoubleClickFeature(context);
					if (feature != null) {
						features.add(feature);
					}
				}
				return features;
			}
		});
		if (features.isEmpty()) {
			throw new GraphitiLayerException("Cannot call double click");
		}
		for (final ICustomFeature feature : features) {
			Display.getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					feature.execute(createPictogramContext(editPart));
				}
			});
		}
		Display.getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {

			}
		});
	}

	private PictogramContext createPictogramContext(final org.eclipse.gef.EditPart part) {
		PictogramElement pe = null;
		if (part instanceof GraphitiShapeEditPart) {
			GraphitiShapeEditPart gsed = (GraphitiShapeEditPart) part;
			pe = gsed.getPictogramElement();
		}
		if (pe == null) {
			throw new RuntimeException("Cannot create PictogramElementContext, pe is null");
		}
		return new PictogramContext(pe);
	}

	private class PictogramContext extends CustomContext implements IPictogramElementContext, IDoubleClickContext {

		public PictogramContext(PictogramElement pictogramElement) {
			super(new PictogramElement[] { pictogramElement });
		}

		@Override
		public PictogramElement getPictogramElement() {
			return getPictogramElements()[0];
		}

	}
}
