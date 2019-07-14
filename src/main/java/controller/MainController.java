package controller;

import adapter.DriveAdapter;
import common.CommonString;
import common.CommonTheme;
import common.XmlUtility;
import gui.MainScreen;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import model.MyFileNode;

public class MainController {
    
    private final MainScreen mainScreen;
    
    public MainController(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }
    
    public void init() {
        CommonTheme.setTheme(mainScreen);
        mainScreen.getJtListFile().setModel(loadFileToJTree());
    }
    
    public void deliverFile() {
        MyFileNode myFile = (MyFileNode) mainScreen.getJtListFile()
                .getSelectionPath().getLastPathComponent();
        DriveAdapter.uploadFile(myFile);
    }

    private DefaultTreeModel loadFileToJTree() {
        MyFileNode filesLocal = listLocalFiles(XmlUtility.getLocalLocation(), 
                mainScreen.getWorkingFile(), Boolean.TRUE);
        MyFileNode filesDrive = DriveAdapter.getSelectedFile(mainScreen.getWorkingFile());
        return new DefaultTreeModel(mergeTree(filesLocal, filesDrive));
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

    private MyFileNode mergeTree(MyFileNode sourceTree,
            MyFileNode moveTree) {
        Enumeration<MyFileNode> moveChildren = moveTree.children();
        ArrayList<MyFileNode> list = new ArrayList();
        while (moveChildren.hasMoreElements()) {
            MyFileNode childLocal = (MyFileNode) moveChildren.nextElement();
            MyFileNode result = existedInChildren(parseToList(sourceTree), childLocal);
            if (result == null) { //merge directly if not existed
                list.add(childLocal);
            } else//if existed
            if (result.getFileType() == null) { //if this file is not directory
                result.setFileStatus(CommonString.STATUS_NORMAL);
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
}
