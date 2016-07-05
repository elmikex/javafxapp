/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mike.testapp;

import com.mike.testapp.model.Member;
import com.mike.testapp.model.MemberMembership;
import com.mike.testapp.model.Membership;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Mike-MSI
 */
public class DialogUtils {
    
    public static Dialog<MemberMembership> createAddMembershipDialog(){
        try {
            FXMLLoader myLoader = new FXMLLoader(MainApp.class.getResource("view/AddMembership.fxml"));
            Parent loadedLayout = (Parent) myLoader.load();
            AddMembershipController dialog = ((AddMembershipController) myLoader.getController());

            dialog.getDialogPane().setContent(loadedLayout);
            return dialog;
        } catch (IOException ex) {
            Logger.getLogger(DialogUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static Dialog<Member> createMemberPickerDialog(){
        try {
            FXMLLoader myLoader = new FXMLLoader(MainApp.class.getResource("view/MemberPicker.fxml"));
            Parent loadedLayout = (Parent) myLoader.load();
            MemberPickerController dialog = ((MemberPickerController) myLoader.getController());

            dialog.getDialogPane().setContent(loadedLayout);
            return dialog;
        } catch (IOException ex) {
            Logger.getLogger(DialogUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
