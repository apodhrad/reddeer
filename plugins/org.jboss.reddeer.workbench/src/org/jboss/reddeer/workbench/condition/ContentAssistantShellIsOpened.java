package org.jboss.reddeer.workbench.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.core.lookup.ShellLookup;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

/**
 * Checks if content assistant shell is open.
 * @author rawagner
 *
 */
public class ContentAssistantShellIsOpened extends AbstractWaitCondition {

    private List<Shell> previousShells;
    private Table table = null;

    /**
     * Default constructor.
     * @param previousShells shells which have been opened
     * before calling action to open content assistant
     */
    public ContentAssistantShellIsOpened(final Shell[] previousShells) {
        this.previousShells = new ArrayList<Shell>(Arrays.asList(previousShells));
    }

    @Override
    public final boolean test() {
        List<Shell> s2List = new ArrayList<Shell>(Arrays.asList(ShellLookup
                .getInstance().getShells()));
        s2List.removeAll(previousShells);
        // shell with javadoc can be displayed also
        if (s2List.size() == 1 || s2List.size() == 2) {
            for (Shell s : s2List) {
                ShellHandler.getInstance().setFocus(s);
                try {
                    table = new DefaultTable();
                    return true;
                } catch (CoreLayerException ex) {
                    continue;
                }
            }
        }
        return false;
    }

    /**
     * Returns content assistant table.
     * @return content assistant table
     */
    public final Table getContentAssistTable() {
        return table;
    }

    @Override
    public final String description() {
        return "ContentAssistant shell is opened";
    }

}
