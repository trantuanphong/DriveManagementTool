/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.MainController;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import model.MyFileNode;

/**
 *
 * @author Phong
 */
public class MainScreen extends javax.swing.JFrame {

    private final MainController controller;
    private final MyFileNode workingFile;
    
    /**
     * Creates new form MainScreen
     */
    public MainScreen(MyFileNode workingFile) {
        initComponents();
        this.workingFile = workingFile;
        this.controller = new MainController(this);
        controller.init();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jtListFile = new javax.swing.JTree();
        btnUpdate = new javax.swing.JButton();
        btnDeliver = new javax.swing.JButton();
        btnLog = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(jtListFile);

        btnUpdate.setText("Update");

        btnDeliver.setText("Deliver");
        btnDeliver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeliverActionPerformed(evt);
            }
        });

        btnLog.setText("Log Changes");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnLog)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeliver)
                        .addGap(0, 195, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdate)
                    .addComponent(btnDeliver)
                    .addComponent(btnLog))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeliverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeliverActionPerformed
        // TODO add your handling code here:
        controller.deliverFile();
    }//GEN-LAST:event_btnDeliverActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeliver;
    private javax.swing.JButton btnLog;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree jtListFile;
    // End of variables declaration//GEN-END:variables

    public JButton getBtnDeliver() {
        return btnDeliver;
    }

    public void setBtnDeliver(JButton btnDeliver) {
        this.btnDeliver = btnDeliver;
    }

    public JButton getBtnLog() {
        return btnLog;
    }

    public void setBtnLog(JButton btnLog) {
        this.btnLog = btnLog;
    }

    public JButton getBtnUpdate() {
        return btnUpdate;
    }

    public void setBtnUpdate(JButton btnUpdate) {
        this.btnUpdate = btnUpdate;
    }

    public JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    public void setjScrollPane1(JScrollPane jScrollPane1) {
        this.jScrollPane1 = jScrollPane1;
    }

    public JTree getJtListFile() {
        return jtListFile;
    }

    public void setJtListFile(JTree jtListFile) {
        this.jtListFile = jtListFile;
    }

    public MyFileNode getWorkingFile() {
        return workingFile;
    }
}