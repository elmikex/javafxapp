/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mike.testapp;

import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Query;
import org.hibernate.Session;

//import org.apache.commons.codec.digest.DigestUtils;
/**
 * FXML Controller class
 *
 * @author Escritorio
 */
public class LoginController implements ManagedScreen {

    LayoutManager layoutManager;
    Stage primaryStage;

    public TextField tfUser;
    public PasswordField tfPass;

    @FXML
    private void initialize() {
        final Task<Void> initTask = new Task() {
            @Override
            protected Void call() throws InterruptedException {
                updateMessage("Initializing Hibernate");
                MainApp.sessionFactory = HibernateUtil.getSessionFactory();
                updateMessage("Hibernate initialized");

                return null;
            }
        };

        new Thread(initTask).start();
    }

    @Override
    public void setLayoutManager(LayoutManager lm) {
        layoutManager = lm;
    }

    @FXML
    private void login(ActionEvent event) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("from User where userName = :username and password = :pass ");
        query.setParameter("username", tfUser.getText());
        query.setParameter("pass", tfPass.getText());
        List results = query.list();

        if (results.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Wrong Credentials");
            alert.setHeaderText("Wrong Credentials");
            alert.setContentText("The combination of username and password you entered is invalid.");
            alert.showAndWait();
        } else {
            tfUser.setText("");
            tfPass.setText("");
            MainApp.primaryStage.hide();
            layoutManager.setLayout(MainApp.mainPanelLayout);
            MainApp.primaryStage.show();
        }

    }

    @FXML
    private void salir(ActionEvent event) {
        Platform.exit();
    }

}
