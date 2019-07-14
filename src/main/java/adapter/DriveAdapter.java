package adapter;

import com.google.api.client.http.FileContent;
import java.util.List;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import common.CommonString;
import model.MyFileNode;

public class DriveAdapter {

    public static String getDriveName() {
        try {
            return MyDrive.getService().getServicePath();
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

    private static List<File> getFileList(String query) {
        List<File> files;
        try {
            FileList result = MyDrive.getService().files().list()
                    .setQ(query)
                    .setFields("nextPageToken, files(id, name, mimeType)")
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

}
