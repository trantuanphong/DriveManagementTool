/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.api.services.drive.model.Revision;
import common.CommonTheme;
import gui.LogChangesScreen;
import handler.DriveHandler;
import javax.swing.DefaultListModel;

/**
 *
 * @author Phong
 */
public class LogChangesController {
    
    private final LogChangesScreen logChangesScreen;

    public LogChangesController(LogChangesScreen logChangesScreen) {
        this.logChangesScreen = logChangesScreen;
    }
    
    public void init() {
        CommonTheme.setTheme(logChangesScreen);
        retrieveChanges();
    }
    
    private void retrieveChanges() {
        DefaultListModel<String> liMo = new DefaultListModel();
        int i = 0;
        java.util.List<Revision> revis = DriveHandler.getInstance()
                .retrieveRevisions(logChangesScreen.getFile().getFileID());
        for (Revision revi : revis) {
            liMo.add(i++, revi.getModifiedTime().toString() + "  -  " +
                    (revi.getLastModifyingUser()==null?"ME":revi.getLastModifyingUser().getDisplayName()));
        }
        logChangesScreen.getJlRevision().setModel(liMo);
    }
    
}
