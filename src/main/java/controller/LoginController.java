/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import handler.DriveHandler;
import common.CommonContent;
import common.CommonTheme;
import handler.SettingHandler;
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
                .setText(SettingHandler.getInstance().getLocalLocation());
    }

    private DefaultTreeModel loadFileFromDrive() {
        MyFileNode files = 
                new MyFileNode(DriveHandler.getInstance().getDriveName());
        files.add(DriveHandler.getInstance().getListFile());
        files.add(DriveHandler.getInstance().getFileSharedWithMe());
        return new DefaultTreeModel(files);
    }

    public void login() {
        if (loginScreen.getJtListFile().getSelectionCount() == 1) {
            
            MyFileNode myFile = (MyFileNode) loginScreen.getJtListFile()
                    .getSelectionPath().getLastPathComponent();
            myFile.setFileStatus(CommonContent.STATUS_NORMAL);
            
            SettingHandler.getInstance().setLocalLocation(
                    loginScreen.getTxtLocalWorkplacePath().getText());
            
            new MainScreen(myFile).setVisible(true);
            loginScreen.dispose();
        } else {
            JOptionPane.showMessageDialog(loginScreen, 
                    "Please choose working folder.");
        }
    }
}
