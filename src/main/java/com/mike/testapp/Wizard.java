/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mike.testapp;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;

/**
 *
 * @author Mike-MSI
 */
public abstract class Wizard extends Dialog {
    
    @FXML
    protected TabPane tpWizard;
    protected WizardPage currentPage;
    protected HashMap<Tab, WizardPage> wizardPages;
    
    @FXML
    protected Button btnNext;
    @FXML
    protected Button btnPrevious;
    @FXML
    protected Button btnCancel;
    
    protected Alert messageAlert;
    
    
    
    /**
     * Method used to set up wizard buttons, including event handles and icons
     */
    protected void setup(){
    /******* wizard buttons setup **********/
        ReadOnlyIntegerProperty selectedTabIndex = tpWizard.getSelectionModel().selectedIndexProperty();
        btnPrevious.setOnAction(e
                -> tpWizard.getSelectionModel().select(tpWizard.getSelectionModel().getSelectedIndex() - 1));
        btnPrevious.disableProperty().bind(selectedTabIndex.lessThanOrEqualTo(0));
        btnPrevious.setGraphic(GlyphUtils.getGlyph("CARET_LEFT", 20, "darkcyan"));
        

        Glyph caretRightIcon = GlyphUtils.getGlyph("CARET_RIGHT", 20, "darkcyan");
        Glyph checkIcon = GlyphUtils.getGlyph("CHECK", 20, "darkcyan");
        EventHandler<ActionEvent> finishAction = (ActionEvent event) -> {
            handleFinish();
        };
        EventHandler<ActionEvent> nextAction = (ActionEvent event) -> {
            handleNext();
        };
        btnNext.textProperty().bind(new When(selectedTabIndex.greaterThanOrEqualTo(Bindings.size(tpWizard.getTabs()).subtract(1))).then("Finish").otherwise("Next"));
        btnNext.graphicProperty().bind(new When(selectedTabIndex.greaterThanOrEqualTo(Bindings.size(tpWizard.getTabs()).subtract(1))).then(checkIcon).otherwise(caretRightIcon));
        btnNext.onActionProperty().bind(new When(selectedTabIndex.greaterThanOrEqualTo(Bindings.size(tpWizard.getTabs()).subtract(1))).then(finishAction).otherwise(nextAction));
        
        btnCancel.setGraphic(GlyphUtils.getGlyph("TIMES", "DARKRED"));
        btnCancel.setOnAction((ActionEvent e) -> {
            handleCancel();
        });
        
        wizardPages = new HashMap<>();
        
        this.getDialogPane().getButtonTypes().add(ButtonType.OK);
        this.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
//        this.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
//        this.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
        
        
    }
    
    @FXML
    protected void handleNext(){
        System.out.println("handling next-");
        WizardPage page = getCurrentPage();
        if (page != null && page.validator.isInvalid()) {
            page.validator.setErrorDecorationEnabled(true);
            Collection<ValidationMessage> messages = page.validator.getValidationResult().getMessages();
            messageAlert = new Alert(Alert.AlertType.WARNING);
            messageAlert.setHeaderText("Please properly fill every required field in the form");
            StringBuilder sb = new StringBuilder();
            for(ValidationMessage message : messages){
                sb.append("* ").append(message.getText()).append("\n");
            }
            messageAlert.setContentText(sb.toString());
            messageAlert.showAndWait();
        } else {
            tpWizard.getSelectionModel().select(tpWizard.getSelectionModel().getSelectedIndex() + 1);
        }
    }
    @FXML
    protected void handlePrevious(){
        tpWizard.getSelectionModel().select(tpWizard.getSelectionModel().getSelectedIndex() - 1);
    }
    
    protected abstract void handleFinish();
    
    protected abstract void handleCancel();
    
    protected WizardPage getCurrentPage(){
        return wizardPages.get(tpWizard.getSelectionModel().getSelectedItem());
    }
    
    protected class WizardPage{
        protected ValidationSupport validator;
        
        protected Tab tab;
        
        public WizardPage(Tab tab){
            this.validator = new ValidationSupport();
            this.validator.setErrorDecorationEnabled(false);
            
            this.tab = tab;
            wizardPages.put(tab, this);
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 67 * hash + Objects.hashCode(this.tab);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final WizardPage other = (WizardPage) obj;
            if (!Objects.equals(this.tab, other.tab)) {
                return false;
            }
            return true;
        }
    }
}
