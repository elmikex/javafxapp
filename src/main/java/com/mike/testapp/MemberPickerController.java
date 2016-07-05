/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mike.testapp;

/**
 *
 * @author Mike-MSI
 */

import com.mike.testapp.model.Member;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class MemberPickerController extends Dialog {

    @FXML
    private ListView<Member> lvMemberlist;

    
    @FXML
    private void initialize(){
        lvMemberlist.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    
    public void setMembersFound(List<Member> results){
        lvMemberlist.getItems().addAll(results);
    }
    
    @FXML
    protected void handleSelect() {
        System.out.println("handling finish-");
        this.setResult(lvMemberlist.getSelectionModel().getSelectedItem());
        this.close();
    }


}

