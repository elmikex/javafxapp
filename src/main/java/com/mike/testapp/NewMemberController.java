/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mike.testapp;

import com.mike.testapp.model.Member;
import com.mike.testapp.model.MemberMembership;
import com.mike.testapp.model.Membership;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Optional;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.StageStyle;
import org.controlsfx.control.MaskerPane;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.hibernate.Session;

/**
 *
 * @author Mike-MSI
 */
public class NewMemberController extends Wizard implements ManagedScreen {

    LayoutManager layoutManager;

    Member currentMember;

    @FXML
    private StackPane rootContainer;

    @FXML
    private Tab tBasic;
    @FXML
    private Tab tAdditional;
    @FXML
    private Tab tMemberships;

    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private DatePicker dpBirthdate;
    @FXML
    private ComboBox cbxGender;

    @FXML
    private TextField tfAddress;
    @FXML
    private TextField tfAddress2;
    @FXML
    private TextField tfCity;
    @FXML
    private TextField tfState;
    @FXML
    private TextField tfZipCode;
    @FXML
    private TextField tfHomePhone;
    @FXML
    private TextField tfWorkPhone;
    @FXML
    private TextField tfCellPhone;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfCompany;
    @FXML
    private TextField tfOccupation;

    @FXML
    private Button btnAddNewMembership;
    @FXML
    private ListView<MemberMembership> lvCurrentMemberships;
    private Dialog<MemberMembership> membershipPickerDialog;

    @Override
    public void setLayoutManager(LayoutManager c) {
        this.layoutManager = c;
    }

    @FXML
    public void initialize() {
        super.setup();

        Wizard.WizardPage pageBasic = this.new WizardPage(tBasic);
        pageBasic.validator.registerValidator(tfFirstName, Validator.createEmptyValidator("First name is required"));
        pageBasic.validator.registerValidator(tfLastName, Validator.createEmptyValidator("Last name is required"));
        pageBasic.validator.registerValidator(cbxGender, Validator.createEmptyValidator("Gender is required"));

        Wizard.WizardPage pageAdditional = this.new WizardPage(tAdditional);
        pageAdditional.validator.registerValidator(tfZipCode, false, Validator.createRegexValidator("Zip Code must be 5 digits", "(\\d{5})?", Severity.ERROR));

        Wizard.WizardPage pageMemberships = this.new WizardPage(tMemberships);

        cbxGender.getItems().clear();
        cbxGender.getItems().addAll("Male", "Female");

    }

    @FXML
    private void handleAddNewMembership() {
        membershipPickerDialog = DialogUtils.createAddMembershipDialog();
        Optional<MemberMembership> result = membershipPickerDialog.showAndWait();
        result.ifPresent(memberMembership -> {
            lvCurrentMemberships.getItems().add(memberMembership);
        });
    }

    @Override
    protected void handleFinish() {

        MaskerPane masker = new MaskerPane();
        masker.setText("Saving member, please wait...");
        rootContainer.getChildren().add(masker);

        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {

                Session session = null;

                try {
                    session = MainApp.sessionFactory.openSession();
                    session.beginTransaction();
                    Member member;
                    if (currentMember == null) {
                        member = new Member();
                    } else {
                        member = currentMember;
                    }
                    member.setFirstName(tfFirstName.getText());
                    member.setLastName(tfLastName.getText());
                    member.setGender(cbxGender.getValue().toString());
                    member.setBirthdate(DateUtils.asDate(dpBirthdate.getValue()));
                    member.setAddress(tfAddress.getText());
                    member.setAddress2(tfAddress2.getText());
                    member.setCity(tfCity.getText());
                    member.setState(tfState.getText());
                    member.setZipCode(tfZipCode.getText());
                    member.setHomePhone(tfHomePhone.getText());
                    member.setWorkPhone(tfWorkPhone.getText());
                    member.setCellPhone(tfCellPhone.getText());
                    member.setEmail(tfEmail.getText());
                    member.setCompany(tfCompany.getText());
                    member.setOccupation(tfOccupation.getText());

                    if (!lvCurrentMemberships.getItems().isEmpty()) {
                        for (MemberMembership m : lvCurrentMemberships.getItems()) {
                            m.setMember(member);
                        }
                        member.getMemberMemberships().addAll(lvCurrentMemberships.getItems());
                    }

                    session.saveOrUpdate(member);
                    session.getTransaction().commit();

                    Platform.runLater(() -> {
                        messageAlert = new Alert(AlertType.NONE, "Member saved to the system succesfully", new ButtonType("Go Back"));
                        messageAlert.initStyle(StageStyle.UNIFIED);
                        messageAlert.setOnHidden((EventHandler<DialogEvent>) (DialogEvent event) -> {
                            goBack();
                        });
                        messageAlert.showAndWait();
                    });

                } catch (Exception e) {
                    Platform.runLater(() -> {
                        messageAlert = new Alert(AlertType.ERROR, "", new ButtonType("Go Back"));
                        messageAlert.setHeaderText("Sorry, there was an error while trying to save this member");
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
                            handleCancel();
                        });
                        messageAlert.showAndWait();
                    });
                } finally {
                    Platform.runLater(() -> {
                        rootContainer.getChildren().remove(masker);
                    });
                    if (session != null) {
                        session.close();
                    }
                }
                return null;

            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

    public void setCurrentMember(Member m) {
        currentMember = m;

        tfFirstName.setText(m.getFirstName());
        tfLastName.setText(m.getLastName());
        cbxGender.setValue(m.getGender());
        dpBirthdate.setValue(DateUtils.asLocalDate(m.getBirthdate()));
        tfAddress.setText(m.getAddress());
        tfAddress2.setText(m.getAddress2());
        tfCity.setText(m.getCity());
        tfState.setText(m.getState());
        tfZipCode.setText(m.getZipCode());
        tfHomePhone.setText(m.getHomePhone());
        tfWorkPhone.setText(m.getWorkPhone());
        tfCellPhone.setText(m.getCellPhone());
        tfEmail.setText(m.getEmail());
        tfCompany.setText(m.getCompany());
        tfOccupation.setText(m.getOccupation());

        lvCurrentMemberships.getItems().addAll(m.getMemberMemberships());
    }

    @FXML
    @Override
    public void handleCancel() {
        //resetForm();
        goBack();
    }

    public void goBack() {
        layoutManager.setLayout(MainApp.searchMemberLayout);
    }

}
