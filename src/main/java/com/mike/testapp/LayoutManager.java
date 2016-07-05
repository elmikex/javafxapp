/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mike.testapp;

import javafx.scene.Node;
import javafx.stage.Stage;

/**
 *
 * @author Mike-MSI
 */
public interface LayoutManager {
    
    public ManagedScreen loadLayout(String resource);
    
    public boolean unloadLayout(String resource);
    
    public boolean addLayout(String resource, Node layout);
    
    public Node getLayout(String resource);
    
    public boolean setLayout(String resource);
    
    public ManagedScreen reloadAndSetLayout(String resource);
    
    public ManagedScreen getController(String resource);
    
    
    //public void setPrimaryStage(Stage stage);
    
    
}
