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
import com.mike.testapp.model.MemberMembership;
import com.mike.testapp.model.Membership;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import org.controlsfx.validation.ValidationResult;
import org.hibernate.Query;
import org.hibernate.Session;

public class AddMembershipController extends Wizard {

    @FXML
    private Tab tSelect;
    @FXML
    private ListView<Membership> lvMemberships;
    @FXML
    private TextArea taMembershipDetails;

    @FXML
    private Tab tBegin;
    
    @FXML
    private Tab tEnd;

    @FXML
    private DatePicker dpBeginDate;

    @FXML
    private DatePicker dpEndDate;
    
    private MemberMembership memberMembership;
    
    @FXML
    public void initialize(){
        super.setup();
        
        dpBeginDate.setValue(LocalDate.now());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 1);
        dpEndDate.setValue(DateUtils.asLocalDate(cal.getTime()));
        
        Wizard.WizardPage pageSelect = this.new WizardPage(tSelect);
        
        Wizard.WizardPage pageBegin = this.new WizardPage(tBegin);
        pageBegin.validator.registerValidator(dpBeginDate, false, (Control c, LocalDate newValue) ->
            ValidationResult.fromErrorIf(dpBeginDate, "Enter a valid date", newValue == null));
        
        Wizard.WizardPage pageEnd = this.new WizardPage(tEnd);
        pageEnd.validator.registerValidator(dpEndDate, false, (Control c, LocalDate newValue) ->
            ValidationResult.fromErrorIf(dpEndDate, "Enter a valid date", newValue == null));
        
        lvMemberships.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.getNamedQuery("findAllMemberships");
        lvMemberships.getItems().addAll(query.list());
        session.close();
        
        
        

    }

    @Override
    protected void handleFinish() {
        Membership membership = lvMemberships.getSelectionModel().getSelectedItem();
        
        memberMembership = new MemberMembership();
        memberMembership.setMembership(membership);
        memberMembership.setBeginDate(DateUtils.asDate(dpBeginDate.getValue()));
        memberMembership.setEndDate(DateUtils.asDate(dpEndDate.getValue()));
        
        this.setResult(memberMembership);
        //parentDialog.getResultConverter().call(ButtonType.OK);
        this.close();
        
//        parentDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
//        Button b = (Button) parentDialog.getDialogPane().lookupButton(ButtonType.OK);
//        b.setVisible(false);
//        
//        b.fire();

    }
    
    @Override
    protected void handleCancel() {
        this.setResult(null);
        this.close();
    }
    
    public MemberMembership getMemberMembership(){
        return this.memberMembership;
    }

}

