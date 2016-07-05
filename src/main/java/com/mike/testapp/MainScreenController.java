
package com.mike.testapp;

import java.io.IOException;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class MainScreenController implements LayoutManager {

    private Stage primaryStage;
    private Scene primaryScene;

    @FXML
    private StackPane rootContainer;

    private HashMap<String, Node> layouts = new HashMap<>();

    @FXML
    private void initialize() {
        loadLayout(MainApp.loginLayout);
        loadLayout(MainApp.mainPanelLayout);
        
        setLayout(MainApp.loginLayout);
    }

    @Override
    public boolean addLayout(String resource, Node layout) {
        layouts.put(resource, layout);
        return true;
    }

    @Override
    public Node getLayout(String resource) {
        return layouts.get(resource);
    }

    @Override
    public boolean setLayout(String resource) {
        try {
            Node layout = getLayout(resource);
            rootContainer.getChildren().clear();
            rootContainer.getChildren().add(layout);
        } catch (NullPointerException npe) {
            System.out.println("Error while setting layout - not found: " + npe.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public ManagedScreen loadLayout(String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadedScreen = (Parent) myLoader.load();
            ManagedScreen controller = ((ManagedScreen) myLoader.getController());
            controller.setLayoutManager(this);
            addLayout(resource, loadedScreen);

            return controller;
        } catch (IOException e) {
            System.out.println("error while loading layout with resource '" + resource + "' - message: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean unloadLayout(String resource) {
        if (layouts.remove(resource) == null) {
            System.out.println("Layout didn't exist");
            return false;
        } else {
            return true;
        }
    }

    public void showPrimaryStage() {
        primaryStage.show();
    }

    public void hidePrimaryStage() {
        primaryStage.hide();
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @Override
    public ManagedScreen getController(String resource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ManagedScreen reloadAndSetLayout(String resource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
