/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mike.testapp;

import com.mike.testapp.model.Checkin;
import com.mike.testapp.model.Member;
import com.mike.testapp.model.MemberMembership;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanObjectProperty;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.StageStyle;
import org.controlsfx.control.MaskerPane;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Mike-MSI
 */
public class SearchMemberController implements ManagedScreen {

    LayoutManager layoutManager;

    Member currentMember;

    @FXML
    private TextField tfSearchMember;

    @FXML
    private StackPane rootContainer;

    @FXML
    private GridPane gpOverview;

    @FXML
    private ImageView ivPhoto;

    @FXML
    private Label lbID;

    @FXML
    private Label lbFullName;

    @FXML
    private Label lbMemberSince;

    @FXML
    private Label lbName;

    @FXML
    private TableView<MemberMembership> tvMemberships;

    @FXML
    private TableColumn<MemberMembership, String> tcMembership;

    @FXML
    private TableColumn<MemberMembership, String> tcBeginDate;

    @FXML
    private TableColumn<MemberMembership, Date> tcEndDate;

    @FXML
    private TableColumn<MemberMembership, String> tcStatus;

    @FXML
    private TableView<Checkin> tvLastCheckins;

    @FXML
    private TableColumn<Checkin, Date> tcCheckinDate;

    @FXML
    private Button btnCheckin;
    @FXML
    private Button btnReset;

    @FXML
    private Label lbEditMember;

    private BooleanProperty checkedIn = new SimpleBooleanProperty();
    private BooleanProperty showingMember = new SimpleBooleanProperty();

    SimpleDateFormat dfSimple = new SimpleDateFormat("dd/MM/yyyy");

    @FXML
    private void initialize() {
        
        
        tvMemberships.setPlaceholder(new Label("This member hasn't registered a membership"));
        tcMembership.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMembership().getName()));
        tcBeginDate.setCellValueFactory(cellData -> new SimpleStringProperty(dfSimple.format(cellData.getValue().getBeginDate())));
        tcEndDate.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getBeginDate()));

        tvLastCheckins.setPlaceholder(new Label("This member hasn't checked-in for the first time"));
        tcCheckinDate.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getDateCreated()));

        checkedIn.set(false);
        btnCheckin.disableProperty().bind(checkedIn);
        btnCheckin.textProperty().bind(new When(checkedIn).then("Member checked-in").otherwise("Check-In"));
        
        btnCheckin.visibleProperty().bind(showingMember);
        btnReset.visibleProperty().bind(showingMember);

        lbEditMember.setGraphic(GlyphUtils.getGlyph("PENCIL", 30, "DARKGRAY"));
        
        gpOverview.setVisible(false);
    }

    @Override
    public void setLayoutManager(LayoutManager lm) {
        layoutManager = lm;
    }

    @FXML
    public void searchMember() {
        String searchInput = tfSearchMember.getText();

        Session session = MainApp.sessionFactory.openSession();
        Query query = session.createQuery("from User where firstName||' '||lastName like :input");
        query.setParameter("input", "%" + searchInput + "%");
        List<Member> results = query.list();

        if (!results.isEmpty()) {

            if (results.size() == 1) {
                Member member = results.get(0);
                showMemberOverview(member);

            } else {
                MemberPickerController memberPicker = (MemberPickerController) DialogUtils.createMemberPickerDialog();
                memberPicker.setMembersFound(results);
                Optional<Member> result = memberPicker.showAndWait();
                result.ifPresent(member -> {
                    showMemberOverview(member);
                });
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("No results found");
            alert.setHeaderText("Couldn't find a member with the name \"" + searchInput + "\"");
            alert.showAndWait();
        }
        session.close();
    }

    @FXML
    private void handleCheckin() {

        MaskerPane masker = new MaskerPane();
        masker.setText("Checking in member...");
        rootContainer.getChildren().add(masker);

        Task task = new Task<Void>() {
            @Override
            public Void call() {

                Session session = null;

                try {
                    session = MainApp.sessionFactory.openSession();
                    session.beginTransaction();

                    Checkin ch = new Checkin();
                    ch.setMember(currentMember);
                    currentMember.getCheckins().add(ch);

                    session.saveOrUpdate(currentMember);
                    session.getTransaction().commit();

                    tvLastCheckins.getItems().add(ch);
                    
                    checkedIn.set(true);

                    Platform.runLater(() -> {
                        Alert messageAlert = new Alert(Alert.AlertType.NONE, "Member checked in succesfully", new ButtonType("Ok"));
                        messageAlert.initStyle(StageStyle.UNIFIED);
                        messageAlert.setOnHidden((EventHandler<DialogEvent>) (DialogEvent event) -> {

                        });
                        messageAlert.showAndWait();
                    });

                } catch (Exception e) {

                    Platform.runLater(() -> {
                        Alert messageAlert = new Alert(Alert.AlertType.ERROR, "", new ButtonType("Go Back"));
                        messageAlert.setHeaderText("Sorry, there was an error while trying to check in this member");
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        String exceptionText = sw.toString();

                        Label label = new Label("The exception stacktrace was:");

                        TextArea textArea = new TextArea(exceptionText);
                        textArea.setEditable(false);
                        textArea.setWrapText(true);

                        textArea.setMaxWidth(Double.MAX_VALUE);
                        textArea.setMaxHeight(Double.MAX_VALUE);
                        GridPane.setVgrow(textArea, Priority.ALWAYS);
                        GridPane.setHgrow(textArea, Priority.ALWAYS);

                        GridPane expContent = new GridPane();
                        expContent.setMaxWidth(Double.MAX_VALUE);
                        expContent.add(label, 0, 0);
                        expContent.add(textArea, 0, 1);
                        messageAlert.getDialogPane().setExpandableContent(expContent);

                        messageAlert.setOnHidden((EventHandler<DialogEvent>) (DialogEvent event) -> {
                            hideOverview();
                        });
                        messageAlert.showAndWait();
                    });

                } finally {
                    if (session != null) {
                        session.close();
                    }
                    Platform.runLater(() -> {
                        rootContainer.getChildren().remove(masker);
                    });
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.start();

    }
    
    @FXML
    private void handleReset(){
        layoutManager.reloadAndSetLayout(MainApp.searchMemberLayout);
    }

    @FXML
    private void handleEditMember() {
        NewMemberController c = (NewMemberController) layoutManager.reloadAndSetLayout(MainApp.newMemberLayout);
        c.setCurrentMember(currentMember);
    }

    private void showMemberOverview(Member member) {
        setMember(member);
        showOverview();
    }

    public void setMember(Member m) {
        currentMember = m;
        ivPhoto.setImage(new Image("/img/photoplaceholder.jpg"));
        lbName.setText(m.getFirstName());
        lbID.setText(m.getId().toString());
        lbFullName.setText(m.getFirstName() + " " + m.getLastName());
        lbMemberSince.setText(dfSimple.format(m.getDateCreated()));
        tvMemberships.getItems().clear();
        tvMemberships.getItems().addAll(m.getMemberMemberships());
        tvLastCheckins.getItems().clear();
        tvLastCheckins.getItems().addAll(m.getCheckins());
        
    }

    public void showOverview() {
        gpOverview.setVisible(true);
        showingMember.set(true);
    }

    public void hideOverview() {
        gpOverview.setVisible(false);
        showingMember.set(false);
    }

    @FXML
    public void testing(){
        Checkin c = new Checkin();
        c.setDateCreated(new Date());
        c.setMember(currentMember);
        currentMember.getCheckins().add(c);
    }
    
    @FXML
    public void testing2(){
        Member m = new Member();
        m.setFirstName("nuevo");
        currentMember = m;
    }
}
