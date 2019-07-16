package controller;

import adapter.DriveAdapter;
import com.google.api.services.drive.model.Revision;
import common.CommonString;
import common.CommonTheme;
import common.XmlUtility;
import gui.LogChangesScreen;
import gui.MainScreen;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.DefaultListModel;
import javax.swing.tree.DefaultMutableTreeNode;
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
        mainScreen.getJtListFile().setModel(loadFileToJTree());
    }
    
    public void logChanges() {
        LogChangesScreen log = new LogChangesScreen();
        log.setVisible(true);
        DefaultListModel<String> liMo = new DefaultListModel();
        int i = 0;
        MyFileNode myFile = (MyFileNode) mainScreen.getJtListFile()
                .getSelectionPath().getLastPathComponent();
        java.util.List<Revision> revis = DriveAdapter.retrieveRevisions(myFile.getFileID());
        for (Revision revi : revis) {
            liMo.add(i++, revi.toString());
        }
        log.getJlRevision().setModel(liMo);
    }
    
    public void deliverFile() {
        MyFileNode myFile = (MyFileNode) mainScreen.getJtListFile()
                .getSelectionPath().getLastPathComponent();
        DriveAdapter.uploadFile(myFile);
    }
    
    public void updateFile() {
        MyFileNode myFile = (MyFileNode) mainScreen.getJtListFile()
                .getSelectionPath().getLastPathComponent();
        DriveAdapter.downloadFile(myFile, getPathInLocal());
    }
    
    private DefaultTreeModel loadFileToJTree() {
        MyFileNode filesLocal = listLocalFiles(XmlUtility.getLocalLocation(),
                new MyFileNode("local"), Boolean.TRUE);
        MyFileNode filesDrive = DriveAdapter.getSelectedFile(mainScreen.getWorkingFile());
        return new DefaultTreeModel(mergeTree(filesDrive, filesLocal));
    }
    
    private MyFileNode existedInChildren(ArrayList<MyFileNode> children,
            MyFileNode node) {
        for (MyFileNode child : children) {
            if (child.getFileName().equals(node.getFileName())) {
                return child;
            }
        }
        return null;
    }
    
    private int compareLastModification(MyFileNode file1, MyFileNode file2) {
        long last1 = file1.getFileLastModified();
        long last2 = file2.getFileLastModified();
        if (Math.abs(last1 - last2) <= (60 * 1000)) {
            return 0;
        } else if (last1 > last2) {
            return 1;
        } else {
            return -1;
        }
    }
    
    private MyFileNode mergeTree(MyFileNode sourceTree,
            MyFileNode moveTree) {
        Enumeration<MyFileNode> moveChildren = moveTree.children();
        ArrayList<MyFileNode> list = new ArrayList();
        while (moveChildren.hasMoreElements()) {
            MyFileNode childLocal = (MyFileNode) moveChildren.nextElement();
            MyFileNode result = existedInChildren(parseToList(sourceTree), childLocal);
            if (result == null) { //merge directly if not existed
                list.add(childLocal);
            } else //if existed
             if (result.getFileType() == null
                        || !result.getFileType().equals(CommonString.TYPE_FOLDER)) { //if this file is not directory
                    int compared = compareLastModification(result, childLocal);
                    if (compared == 0) {
                        result.setFileType(CommonString.STATUS_NORMAL);
                    } else if (compared == 1) {
                        result.setFileStatus(CommonString.STATUS_NEED_UPDATE);
                    } else {
                        result.setFileStatus(CommonString.STATUS_NEED_DELIVER);
                    }
                } else if (result.getFileType().equals(CommonString.TYPE_FOLDER)) { //if this file is directory
                    sourceTree.remove(result);
                    result = mergeTree(result, childLocal);
                    result.setFileStatus(CommonString.STATUS_NORMAL);
                    sourceTree.add(result);
                }
        }
        for (DefaultMutableTreeNode node : list) {
            sourceTree.add(node);
        }
        return sourceTree;
    }
    
    private ArrayList<MyFileNode> parseToList(MyFileNode node) {
        ArrayList<MyFileNode> list = new ArrayList();
        Enumeration<MyFileNode> children = node.children();
        while (children.hasMoreElements()) {
            list.add(children.nextElement());
        }
        return list;
    }
    
    private MyFileNode listLocalFiles(String directory,
            MyFileNode parent, Boolean recursive) {
        File[] children = new File(directory).listFiles();
        for (int i = 0; i < children.length; i++) {
            MyFileNode node = new MyFileNode(children[i].getName());
            node.setFileStatus(CommonString.STATUS_IN_LOCAL_ONLY);
            node.setFilePath(children[i].getAbsolutePath());
            node.setFileLastModified(children[i].lastModified());
            if (children[i].isDirectory() && recursive) {
                node.setFileType(CommonString.TYPE_FOLDER);
                parent.add(node);
                listLocalFiles(children[i].getPath(), node, recursive);
            } else if (!children[i].isDirectory()) {
                parent.add(node);
            }
        }
        return parent;
    }
    
    private String getPathInLocal() {
        TreePath treePath = mainScreen.getJtListFile().getSelectionPath();
        int total = treePath.getPathCount();
        String path = XmlUtility.getLocalLocation();
        for (int i = 1; i < total; i++) {
            MyFileNode fileNode = (MyFileNode) treePath.getPathComponent(i);
            path = path + "\\" + fileNode.getFileName();
        }
        System.out.println(path);
        return path;
    }
}
