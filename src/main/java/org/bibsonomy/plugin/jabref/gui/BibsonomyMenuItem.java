package org.bibsonomy.plugin.jabref.gui;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.bibsonomy.plugin.jabref.BibsonomyGlobals;
import org.bibsonomy.plugin.jabref.BibsonomySidePaneComponent;
import org.bibsonomy.plugin.jabref.action.DeleteSelectedEntriesAction;
import org.bibsonomy.plugin.jabref.action.DownloadDocumentsAction;
import org.bibsonomy.plugin.jabref.action.ExportSelectedEntriesAction;
import org.bibsonomy.plugin.jabref.action.ImportAllMyPostsAction;
import org.bibsonomy.plugin.jabref.action.ShowSettingsDialogAction;
import org.bibsonomy.plugin.jabref.action.SynchronizeAction;
import org.bibsonomy.plugin.jabref.action.ToggleSidePaneComponentAction;

/**
 * {@link BibsonomyMenuItem} is the plugins menu item
 *
 * @author Waldemar Biller <biller@cs.uni-kassel.de>
 */
public class BibsonomyMenuItem extends JMenu {


    private static final long serialVersionUID = -9004684574235429985L;

    private BibsonomySidePaneComponent sidePaneComponent;

    public BibsonomyMenuItem(BibsonomySidePaneComponent sidePaneComponent) {

        super(BibsonomyGlobals.PLUGIN_NAME);

        this.sidePaneComponent = sidePaneComponent;

        add(getSidePaneComponentToggleMenuItem());
        add(getExportSelectedEntriesMenuItem());
        add(getDeleteSelectedEntriesMenuItem());
        addSeparator();
        add(getSynchronizeMenuItem());
        add(getDownloadDocumentsMenuItem());
        add(getAllMyPostsMenuItem());
        addSeparator();
        add(getSettingsMenuItem());
    }

    private JMenuItem getSidePaneComponentToggleMenuItem() {

        return new JMenuItem(new ToggleSidePaneComponentAction(sidePaneComponent));
    }

    private JMenuItem getExportSelectedEntriesMenuItem() {

        return new JMenuItem(new ExportSelectedEntriesAction(sidePaneComponent.getJabRefFrame()));
    }

    private JMenuItem getDeleteSelectedEntriesMenuItem() {

        return new JMenuItem(new DeleteSelectedEntriesAction(sidePaneComponent.getJabRefFrame()));
    }

    private JMenuItem getSynchronizeMenuItem() {

        return new JMenuItem(new SynchronizeAction(sidePaneComponent.getJabRefFrame()));
    }

    private JMenuItem getSettingsMenuItem() {

        return new JMenuItem(new ShowSettingsDialogAction(sidePaneComponent.getJabRefFrame()));
    }

    private JMenuItem getAllMyPostsMenuItem() {
        JMenuItem item = new JMenuItem(new ImportAllMyPostsAction(sidePaneComponent.getJabRefFrame()));
        item.setText("Import all my posts");
        return item;
    }

    private JMenuItem getDownloadDocumentsMenuItem() {

        return new JMenuItem(new DownloadDocumentsAction(sidePaneComponent.getJabRefFrame()));
    }
}