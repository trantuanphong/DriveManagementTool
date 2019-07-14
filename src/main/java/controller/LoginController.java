/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import adapter.DriveAdapter;
import common.CommonString;
import common.CommonTheme;
import common.XmlUtility;
import gui.LoginScreen;
import gui.MainScreen;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import model.MyFileNode;

/**
 *
 * @author Phong
 */
public class LoginController {

    private final LoginScreen loginScreen;

    public LoginController(LoginScreen loginScreen) {
        this.loginScreen = loginScreen;
    }

    public void init() {
        CommonTheme.setTheme(loginScreen);
        loginScreen.getJtListFile().setModel(loadFileFromDrive());
        loginScreen.getTxtLocalWorkplacePath()
                .setText(XmlUtility.getLocalLocation());
    }

    private DefaultTreeModel loadFileFromDrive() {
        MyFileNode files = new MyFileNode(DriveAdapter.getDriveName());
        files.add(DriveAdapter.getListFile());
        files.add(DriveAdapter.getFileSharedWithMe());
        return new DefaultTreeModel(files);
    }

    public void login() {
        if (loginScreen.getJtListFile().getSelectionCount() == 1) {
            MyFileNode myFile = (MyFileNode) loginScreen.getJtListFile()
                    .getSelectionPath().getLastPathComponent();
            myFile.setFileStatus(CommonString.STATUS_NORMAL);
            new MainScreen(myFile).setVisible(true);
            loginScreen.dispose();
        } else {
            JOptionPane.showMessageDialog(loginScreen, 
                    "Please choose working folder.");
        }
    }
}
