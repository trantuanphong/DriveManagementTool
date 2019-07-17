package controller;

import handler.DriveHandler;
import com.google.api.services.drive.model.Revision;
import common.CommonTheme;
import handler.SettingHandler;
import gui.LogChangesScreen;
import gui.MainScreen;
import handler.FileHandler;
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
        MyFileNode myFile = (MyFileNode) mainScreen.getJtListFile()
                .getSelectionPath().getLastPathComponent();
        new LogChangesScreen(mainScreen, true, myFile).setVisible(true);
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
