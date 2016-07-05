/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mike.testapp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.hibernate.SessionFactory;

/**
 *
 * @author Mike-MSI
 */
public class MainApp extends Application {
    
    public static Stage primaryStage;
    public static SessionFactory sessionFactory;
    
    public static String loginLayout = "view/Login.fxml";
    public static String mainPanelLayout = "view/MainPanel.fxml";
    public static String newMemberLayout = "view/NewMember.fxml";
    public static String newMembershipLayout = "view/NewMembership.fxml";
    public static String searchMemberLayout = "view/SearchMember.fxml";
    
    
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        //this.sessionFactory = HibernateUtil.getSessionFactory();
        
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("view/MainScreen.fxml"));
        try {
            Parent mainScreen = (Parent) myLoader.load();
            Scene mainScene = new Scene(mainScreen);
            stage.setScene(mainScene);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage.show();
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            public void handle(WindowEvent e) {
                if(HibernateUtil.getSessionFactory() != null){
                    HibernateUtil.shutdown();
                }
            }
        });
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
