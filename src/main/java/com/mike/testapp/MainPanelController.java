/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mike.testapp;

import java.io.IOException;
import java.util.HashMap;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author Mike-MSI
 */
public class MainPanelController implements LayoutManager, ManagedScreen {

    LayoutManager layoutManager;

    private HashMap<String, Node> layouts = new HashMap<>();
    private HashMap<String, ManagedScreen> controllers = new HashMap();
    private StringProperty currentLayout = new SimpleStringProperty();
    private StringProperty previousLayout = new SimpleStringProperty();

    @FXML
    private BorderPane rootContainer;
    @FXML
    private StackPane spCenter;
    @FXML
    private AnchorPane sidebar;
    @FXML
    private Label menuIconMain;
    @FXML
    private Label menuIconSidebar;
    @FXML
    private Label backButton;
    @FXML
    private Label lbNewMember;
    @FXML
    private Label lbNewMembership;
    
    @FXML
    private Label lbCurrentLayout;
    @FXML
    private Label lbPreviousLayout;

    public TranslateTransition openSidebar;
    public TranslateTransition closeSidebar;

    @FXML
    private void initialize() {
        loadLayout(MainApp.newMemberLayout);
        loadLayout(MainApp.newMembershipLayout);
        loadLayout(MainApp.searchMemberLayout);

        setLayout(MainApp.searchMemberLayout);

        openSidebar = new TranslateTransition(new Duration(350), sidebar);
        openSidebar.setToX(0);
        closeSidebar = new TranslateTransition(new Duration(350), sidebar);

        lbNewMember.setGraphic(GlyphUtils.getGlyph("USER_PLUS", 22));
        lbNewMembership.setGraphic(GlyphUtils.getGlyph("CLIPBOARD", 22));
        backButton.setGraphic(GlyphUtils.getGlyph("CHEVRON_CIRCLE_LEFT", 24, "LIGHTGRAY"));
        
        menuIconMain.setGraphic(GlyphUtils.getGlyph("NAVICON", 40, "LIGHTGRAY"));
        menuIconSidebar.setGraphic(GlyphUtils.getGlyph("NAVICON", 40, "DARKGRAY"));
        
        backButton.disableProperty().bind(previousLayout.isEmpty());
        
        lbCurrentLayout.textProperty().bind(currentLayout);
        lbPreviousLayout.textProperty().bind(previousLayout);

    }

    @Override
    public void setLayoutManager(LayoutManager lm) {
        layoutManager = lm;
    }

    @Override
    public ManagedScreen loadLayout(String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadedScreen = (Parent) myLoader.load();
            ManagedScreen controller = ((ManagedScreen) myLoader.getController());
            if (controller != null) {
                controller.setLayoutManager(this);
                controllers.put(resource, controller);
            }
            addLayout(resource, loadedScreen);
            //System.out.println("Main panel loaded screen: "+resource);
            return controller;
        } catch (IOException e) {
            System.out.println("error while loading screen with resource '" + resource + "' - message: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean unloadLayout(String resource) {
        if (layouts.remove(resource) == null) {
            controllers.remove(resource);
            System.out.println("Layout didn't exist");
            return false;
        } else {
            return true;
        }
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
            FadeTransition ftIn = new FadeTransition(Duration.millis(300), layout);
            ftIn.setFromValue(0.0);
            ftIn.setToValue(1.0);
            if (!spCenter.getChildren().isEmpty()) {
                final Node currentLayout = spCenter.getChildren().get(0);
                FadeTransition ftOut = new FadeTransition(Duration.millis(300), currentLayout);
                ftOut.setFromValue(1.0);
                ftOut.setToValue(0.0);
                ftOut.setOnFinished((ActionEvent actionEvent) -> {
                    spCenter.getChildren().remove(currentLayout);
                });
                ftOut.play();
            }
            spCenter.getChildren().add(layout);
            ftIn.play();
            previousLayout.set(currentLayout.get());
            currentLayout.set(resource);

        } catch (NullPointerException npe) {
            System.out.println("MainPanelController - Error while setting layout - not found: " + npe.getMessage());
            return false;
        }
        return true;
    }
    
    @Override
    public ManagedScreen getController(String resource){
        return controllers.get(resource);
    }

    @Override
    public ManagedScreen reloadAndSetLayout(String resource) {
        if (!currentLayout.equals(resource)) {
            unloadLayout(resource);
            ManagedScreen m = loadLayout(resource);
            setLayout(resource);
            return m;
        } else {
            return null;
        }
    }

    @FXML
    public void handleMenuIconPress() {
        if (sidebar.getTranslateX() != 0) {
            openSidebar();
        } else {
            closeSidebar();
        }
    }
    
    @FXML
    public void handleBackButtonPress(){
        setLayout(previousLayout.get());
    }

    public void openSidebar() {
        openSidebar.play();
        spCenter.setDisable(true);
        sidebar.requestFocus();
        InvalidationListener sidebarUnfocused = (evt) -> {
            if (!sidebar.isFocused()) {
                closeSidebar();
            };
        };
        sidebar.focusedProperty().addListener(new WeakInvalidationListener(sidebarUnfocused));
    }

    public void closeSidebar() {
        closeSidebar.setToX(-(sidebar.getWidth() + 10));
        closeSidebar.play();
        spCenter.setDisable(false);
    }

    @FXML
    public void handleNewMember() {
        reloadAndSetLayout(MainApp.newMemberLayout);
        closeSidebar();
        //openNewMemberWizard();
    }

    @FXML
    public void handleNewMembership() {
        reloadAndSetLayout(MainApp.newMembershipLayout);
        closeSidebar();
    }

    public void openNewMemberWizard() {

    }

}
