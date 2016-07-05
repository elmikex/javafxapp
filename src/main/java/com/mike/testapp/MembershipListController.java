/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mike.testapp;

import com.mike.testapp.model.Membership;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 *
 * @author Mike-MSI
 */
public class MembershipListController {
    
    @FXML
    private ListView<Membership> list;
    @FXML
    private Button btnSelect;
    
    @FXML
    public Integer handleSelect(){
        return list.getSelectionModel().getSelectedItem().getId();
    }
}
