package controller;

import handler.DriveHandler;
import com.google.api.services.drive.model.Revision;
import common.CommonTheme;
import handler.SettingHandler;
import gui.LogChangesScreen;
import gui.MainScreen;
import handler.FileHandler;
import javax.swing.DefaultListModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import model.MyFileNode;

public class MainController {

    private final MainScreen mainScreen;

    public MainController(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }

    public void init() {
        CommonTheme.setTheme(mainScreen);
        load();
    }

    public void load() {
        MyFileNode filesLocal = FileHandler.getInstance()
                .listLocalFiles(SettingHandler.getInstance().getLocalLocation(),
                new MyFileNode("local"), Boolean.TRUE);
        MyFileNode filesDrive = DriveHandler.getInstance()
                .getSelectedFile(mainScreen.getWorkingFile());
        mainScreen.getJtListFile().setModel(
                new DefaultTreeModel(FileHandler.getInstance()
                        .mergeTree(filesDrive, filesLocal)));
    }

    public void logChanges() {
        LogChangesScreen log = new LogChangesScreen(mainScreen, false);
        log.setVisible(true);
        DefaultListModel<String> liMo = new DefaultListModel();
        int i = 0;
        MyFileNode myFile = (MyFileNode) mainScreen.getJtListFile()
                .getSelectionPath().getLastPathComponent();
        java.util.List<Revision> revis = DriveHandler.getInstance()
                .retrieveRevisions(myFile.getFileID());
        for (Revision revi : revis) {
            liMo.add(i++, revi.toString());
        }
        log.getJlRevision().setModel(liMo);
    }

    public void deliverFile() {
        MyFileNode myFile = (MyFileNode) mainScreen.getJtListFile()
                .getSelectionPath().getLastPathComponent();
        DriveHandler.getInstance().uploadFile(myFile);
    }

    public void updateFile() {
        TreePath treePath = mainScreen.getJtListFile().getSelectionPath();
        MyFileNode myFile = (MyFileNode) treePath.getLastPathComponent();
        DriveHandler.getInstance().downloadFile(myFile, 
                FileHandler.getInstance().getPathInLocal(treePath));
    }
}
