package adapter;

import com.google.api.client.http.FileContent;
import java.util.List;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Revision;
import com.google.api.services.drive.model.RevisionList;
import common.CommonString;
import common.FileUtility;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import model.MyFileNode;

public class DriveAdapter {

    public static String getDriveName() {
        try {
            return MyDrive.getService().getApplicationName();
        } catch (Exception e) {
            return "ERROR";
        }
    }

    public static MyFileNode getFileSharedWithMe() {
        MyFileNode sharedWithMe = new MyFileNode("Shared With Me");
        List<File> files = getFileList("sharedWithMe and trashed = false");
        if (files != null && !files.isEmpty()) {
            for (File file : files) {
                if (file.getMimeType().equals(CommonString.TYPE_FOLDER)) {
                    sharedWithMe.add(recursive(getFileInFolder(file.getId()), new MyFileNode(file)));
                } else {
                    sharedWithMe.add(new MyFileNode(file));
                }
            }
        }
        return sharedWithMe;
    }

    public static MyFileNode getSelectedFile(MyFileNode selectedFile) {
        MyFileNode treeFile = new MyFileNode(selectedFile.getFileName());
        List<File> files = getFileInFolder(selectedFile.getFileID());
        if (files != null && !files.isEmpty()) {
            for (File file : files) {
                if (file.getMimeType().equals(CommonString.TYPE_FOLDER)) {
                    treeFile.add(recursive(getFileInFolder(file.getId()), new MyFileNode(file)));
                } else {
                    treeFile.add(new MyFileNode(file));
                }
            }
        }
        return treeFile;
    }

    public static List<Revision> retrieveRevisions(String fileId) {
        try {
            RevisionList revisions = MyDrive.getService().revisions().list(fileId).execute();
            return revisions.getRevisions();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
        return null;
    }

    private static List<File> getFileList(String query) {
        List<File> files;
        try {
            FileList result = MyDrive.getService().files().list()
                    .setQ(query)
                    .setFields("nextPageToken, files(id, name, mimeType, modifiedTime)")
                    .execute();
            files = result.getFiles();
        } catch (Exception e) {
            files = null;
        }
        return files;
    }

    private static List<File> getFileInFolder(String folderID) {
        String query = "'" + folderID + "' in parents and trashed = false";
        return getFileList(query);
    }

    private static MyFileNode recursive(List<File> files, MyFileNode node) {
        if (files != null && !files.isEmpty()) {
            for (File file : files) {
                if (file.getMimeType().equals(CommonString.TYPE_FOLDER)) {
                    node.add(recursive(getFileInFolder(file.getId()), new MyFileNode(file)));
                } else {
                    node.add(new MyFileNode(file));
                }
            }
        }
        return node;
    }

    public static MyFileNode getListFile() {
        return recursive(getFileInFolder("root"), new MyFileNode("root"));
    }

    public static int uploadFile(MyFileNode myFile) {
        try {
            File fileMetadata = new File();
            fileMetadata.setName(myFile.getFileName());
            java.io.File filePath = new java.io.File(myFile.getFilePath());
            FileContent mediaContent = new FileContent(null, filePath);
            MyDrive.getService().files().create(fileMetadata, mediaContent)
                    .setFields("id").execute();
        } catch (Exception e) {
            System.out.println(e);
            return CommonString.FALSE;
        }
        return CommonString.TRUE;
    }
    
    public static int updateFile(MyFileNode myFile) {
//        try {
//            File fileMetadata = new File();
//            fileMetadata.setName(myFile.getFileName());
//            java.io.File filePath = new java.io.File(myFile.getFilePath());
//            FileContent mediaContent = new FileContent(null, filePath);
//            MyDrive.getService().files().update(myFile.getFileID(),).execute();
//        } catch (Exception e) {
//            System.out.println(e);
//            return CommonString.FALSE;
//        }
        return CommonString.TRUE;
    }

    public static void downloadFile(MyFileNode myFile, String path) {
        try {
        OutputStream outputStream = new ByteArrayOutputStream();
        MyDrive.getService().files().get(myFile.getFileID())
                .executeMediaAndDownloadTo(outputStream);
        FileUtility.writeToFile(path, outputStream.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
