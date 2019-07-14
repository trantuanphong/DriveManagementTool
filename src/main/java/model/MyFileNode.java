/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.api.services.drive.model.File;
import common.CommonString;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Phong
 */
public class MyFileNode extends DefaultMutableTreeNode {
    
    private String fileID;
    private String fileName;
    private String fileType;
    private String fileStatus;
    private String filePath;
    
    public MyFileNode() {
        super();
    }

    public MyFileNode(String fileName) {
        super();
        this.fileName = fileName;
        this.fileStatus = "";
    }

    public MyFileNode(String fileName, String fileType) {
        super();
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public MyFileNode(String fileID, String fileName, String fileType) {
        super();
        this.fileID = fileID;
        this.fileName = fileName;
        this.fileType = fileType;
    }
    
    public MyFileNode(File file) {
        super();
        this.fileID = file.getId();
        this.fileName = file.getName();
        this.fileType = file.getMimeType();
        this.fileStatus = CommonString.STATUS_IN_DRIVE_ONLY;
    }

    public String getFileID() {
        return fileID;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    @Override
    public String toString() {
        return this.fileName + " " +this.fileStatus;
    }
}
