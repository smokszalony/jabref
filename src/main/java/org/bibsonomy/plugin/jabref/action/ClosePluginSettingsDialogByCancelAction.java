package org.bibsonomy.plugin.jabref.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.bibsonomy.plugin.jabref.gui.BibsonomySettingsDialog;

/**
 * {@link ClosePluginSettingsDialogByCancelAction} closes the {@link BibsonomySettingsDialog}
 * without saving the properties
 *
 * @author Waldemar Biller <biller@cs.uni-kassel.de>
 */
public class ClosePluginSettingsDialogByCancelAction extends AbstractAction {

    private static final long serialVersionUID = -6587488658676754916L;

    private BibsonomySettingsDialog settingsDialog;

    public void actionPerformed(ActionEvent e) {

        settingsDialog.setVisible(false);
    }

    public ClosePluginSettingsDialogByCancelAction(BibsonomySettingsDialog settingsDialog) {

        super("Cancel", new ImageIcon(ClosePluginSettingsDialogByCancelAction.class.getResource("/images/cross.png")));
        this.settingsDialog = settingsDialog;
    }

}