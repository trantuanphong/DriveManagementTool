/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handler;

import common.CommonContent;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import model.MyFileNode;

/**
 *
 * @author Phong
 */
public class FileHandler {

    private static FileHandler fileHandler;

    private FileHandler() {

    }

    public static FileHandler getInstance() {
        if (fileHandler == null) {
            fileHandler = new FileHandler();
        }
        return fileHandler;
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

    public MyFileNode mergeTree(MyFileNode sourceTree,
            MyFileNode moveTree) {
        Enumeration<MyFileNode> moveChildren = moveTree.children();
        ArrayList<MyFileNode> list = new ArrayList();
        while (moveChildren.hasMoreElements()) {
            MyFileNode childLocal = (MyFileNode) moveChildren.nextElement();
            MyFileNode result = existedInChildren(parseToList(sourceTree), childLocal);
            if (result == null) { //merge directly if not existed
                list.add(childLocal);
            } else //if existed
            {
                if (result.getFileType() == null
                        || !result.getFileType().equals(CommonContent.TYPE_FOLDER)) { //if this file is not directory
                    int compared = compareLastModification(result, childLocal);
                    if (compared == 0) {
                        result.setFileType(CommonContent.STATUS_NORMAL);
                    } else if (compared == 1) {
                        result.setFileStatus(CommonContent.STATUS_NEED_UPDATE);
                    } else {
                        result.setFileStatus(CommonContent.STATUS_NEED_DELIVER);
                    }
                } else if (result.getFileType().equals(CommonContent.TYPE_FOLDER)) { //if this file is directory
                    sourceTree.remove(result);
                    result = mergeTree(result, childLocal);
                    result.setFileStatus(CommonContent.STATUS_NORMAL);
                    sourceTree.add(result);
                }
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

    public MyFileNode listLocalFiles(String directory,
            MyFileNode parent, Boolean recursive) {
        File[] children = new File(directory).listFiles();
        for (int i = 0; i < children.length; i++) {
            MyFileNode node = new MyFileNode(children[i].getName());
            node.setFileStatus(CommonContent.STATUS_IN_LOCAL_ONLY);
            node.setFilePath(children[i].getAbsolutePath());
            node.setFileLastModified(children[i].lastModified());
            if (children[i].isDirectory() && recursive) {
                node.setFileType(CommonContent.TYPE_FOLDER);
                parent.add(node);
                listLocalFiles(children[i].getPath(), node, recursive);
            } else if (!children[i].isDirectory()) {
                parent.add(node);
            }
        }
        return parent;
    }

    public String getPathInLocal(TreePath treePath) {
        int total = treePath.getPathCount();
        String path = SettingHandler.getInstance().getLocalLocation();
        for (int i = 1; i < total; i++) {
            MyFileNode fileNode = (MyFileNode) treePath.getPathComponent(i);
            path = path + "\\" + fileNode.getFileName();
        }
        return path;
    }
}
