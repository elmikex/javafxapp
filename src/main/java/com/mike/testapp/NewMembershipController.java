/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mike.testapp;

import com.mike.testapp.model.Membership;
import java.util.concurrent.Callable;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.IntegerExpression;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.RangeSlider;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;
import org.controlsfx.validation.decoration.ValidationDecoration;
import org.hibernate.Session;

/**
 *
 * @author Mike-MSI
 */
public class NewMembershipController implements ManagedScreen {

    LayoutManager layoutManager;

    ValidationSupport validationSupport;

    @FXML
    private GridPane gpMembershipForm;

    @FXML
    private TextField tfName;
    @FXML
    private Slider sliCycleAmount;
    @FXML
    private Label lbCycleAmount;
    @FXML
    private ComboBox cbxCycle;
    @FXML
    private TextField tfPrice;
    @FXML
    private CheckBox cbChargeTax;

    @FXML
    private CheckBox cbVisitsRestriction;
    @FXML
    private HBox hbVisits;
    @FXML
    private Slider sliVisitsAllowed;

    @FXML
    private CheckBox cbDaysRestriction;
    @FXML
    private HBox hbDays;
    @FXML
    private CheckComboBox ccbxDaysAllowed;

    @FXML
    private CheckBox cbTimesRestriction;
    @FXML
    private HBox hbTimes;
    @FXML
    private RangeSlider rsliTimesAllowed;
    @FXML
    private ListView<String> lvTimesAllowed;
    @FXML
    private Button btnRemoveTime;
    @FXML
    private Button btnAddTime;
    @FXML
    private Label lbFromTime;
    @FXML
    private Label lbToTime;
    @FXML
    private HBox hbContainer2;

    @FXML
    private CheckBox cbExpire;
    @FXML
    private HBox hbExpire;
    @FXML
    private DatePicker dpExpireDate;

    @FXML
    private GridPane gpActions;
    @FXML
    private Label lbInvalidForm;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnGoBack;

    @Override
    public void setLayoutManager(LayoutManager l) {
        this.layoutManager = l;
    }

    @FXML
    private void initialize() {
        cbxCycle.getItems().addAll("DAYS", "WEEKS", "MONTHS", "YEARS");
        lbCycleAmount.textProperty().bind(sliCycleAmount.valueProperty().asString("%.0f"));

        ccbxDaysAllowed = new CheckComboBox();
        ccbxDaysAllowed.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        ccbxDaysAllowed.getCheckModel().checkAll();
        hbDays.getChildren().add(ccbxDaysAllowed);

        rsliTimesAllowed = new RangeSlider(0, 1440, 60, 1000);
        rsliTimesAllowed.setShowTickMarks(true);
        rsliTimesAllowed.setMajorTickUnit(120);
        rsliTimesAllowed.setMinorTickCount(7);
        rsliTimesAllowed.setBlockIncrement(15);
        rsliTimesAllowed.setSnapToTicks(true);
        rsliTimesAllowed.setShowTickLabels(true);
        rsliTimesAllowed.setPrefWidth(500);
        StringConverter<Number> sc = new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                int value = object.intValue();
                return String.format("%02d:%02d", (int) (value / 60), (int) (value % 60));
            }

            @Override
            public Number fromString(String string) {
                return Double.valueOf(string);
            }
        };
        rsliTimesAllowed.setLabelFormatter(sc);
        lbFromTime.textProperty().bindBidirectional(rsliTimesAllowed.lowValueProperty(), sc);
        lbToTime.textProperty().bindBidirectional(rsliTimesAllowed.highValueProperty(), sc);
        hbContainer2.getChildren().add(rsliTimesAllowed);
        btnRemoveTime.setGraphic(new Glyph("FontAwesome", "TIMES"));
        btnAddTime.setGraphic(new Glyph("FontAwesome", "PLUS"));

        hbVisits.disableProperty().bind(cbVisitsRestriction.selectedProperty().not());
        hbDays.disableProperty().bind(cbDaysRestriction.selectedProperty().not());
        hbTimes.disableProperty().bind(cbTimesRestriction.selectedProperty().not());
        hbExpire.disableProperty().bind(cbExpire.selectedProperty().not());

        validationSupport = new ValidationSupport();
        ValidationDecoration cssDecorator = new StyleClassValidationDecoration();
        validationSupport.setValidationDecorator(cssDecorator);
        validationSupport.setErrorDecorationEnabled(false);

        validationSupport.registerValidator(tfName, Validator.createEmptyValidator("Membership name is required"));
    }

    @FXML
    private void addTimeRestriction() {
        lvTimesAllowed.getItems().add(lbFromTime.getText() + "-" + lbToTime.getText());
    }

    @FXML
    private void removeTimeRestriction() {
        int selectedIndex = lvTimesAllowed.getSelectionModel().getSelectedIndex();
        lvTimesAllowed.getItems().remove(selectedIndex);

    }

    @FXML
    void handleSave() {
        validationSupport.setErrorDecorationEnabled(true);
        if (validationSupport.isInvalid()) {
            lbInvalidForm.visibleProperty().bind(validationSupport.invalidProperty());
            btnSave.disableProperty().bind(validationSupport.invalidProperty());
        } else {

            lbInvalidForm.setVisible(false);
            gpMembershipForm.setDisable(true);
            gpActions.getChildren().remove(btnSave);
            ImageView ivLoader = new ImageView();
            ivLoader.setImage(new Image("/img/loadingSpinner.GIF"));
            gpActions.add(ivLoader, 0, 1);

            Task task = new Task<Void>() {
                @Override
                public Void call() {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    session.beginTransaction();
                    try {
                        Membership m = new Membership();
                        m.setName(tfName.getText());
                        m.setCycleAmount(sliCycleAmount.getValue());
                        m.setCycle(cbxCycle.getSelectionModel().getSelectedItem().toString());
                        m.setPrice(Double.parseDouble(tfPrice.getText()));
                        m.setVisitsRestriction(cbVisitsRestriction.isSelected());
                        m.setVisitsAllowed(sliVisitsAllowed.valueProperty().intValue());
                        m.setDaysRestriction(cbDaysRestriction.isSelected());
                        m.setDaysAllowed(ccbxDaysAllowed.getCheckModel().getCheckedItems().toString());
                        m.setTimesRestriction(cbTimesRestriction.isSelected());
                        m.setTimesAllowed(lvTimesAllowed.getItems().toString());
                        m.setExpire(cbExpire.isSelected());
                        m.setExpireDate(DateUtils.asDate(dpExpireDate.getValue()));

                        session.save(m);
                        session.getTransaction().commit();

                        Platform.runLater(() -> {
                            gpActions.getChildren().add(new Label("Membership saved successfully."));
                        });

                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            gpActions.getChildren().add(new Label("Sorry, there was an error saving this memebership."));
                        });
                        e.printStackTrace();
                    } finally {
                        session.close();
                        
                        Platform.runLater(() -> {
                            gpActions.getChildren().remove(ivLoader);
                            btnGoBack.setText("Go back");
                        });
                    }
                    return null;

                }
            };
            Thread th = new Thread(task);
            th.start();

        }

    }

    @FXML
    public void handleGoBack() {
        goBack();
    }

    public void resetForm() {
        try {
            for (Node node : gpMembershipForm.getChildren()) {
                if (node instanceof TextField) {
                    ((TextField) node).clear();
                } else if (node instanceof CheckBox) {
                    ((CheckBox) node).setSelected(false);
                } else if (node instanceof DatePicker) {
                    ((DatePicker) node).setValue(null);
                } else if (node instanceof ChoiceBox) {
                    ((ChoiceBox) node).getSelectionModel().clearSelection();
                } else if (node instanceof ComboBox) {
                    ((ComboBox) node).getSelectionModel().clearSelection();
                } else if (node instanceof ChoiceBox) {
                    ((ChoiceBox) node).getSelectionModel().clearSelection();
                } else if (node instanceof Slider) {
                    ((Slider) node).setValue(((Slider) node).getMin());
                }
            }
        } catch (Exception e) {

        }
        gpMembershipForm.setDisable(false);
        gpActions.getChildren().clear();
        gpActions.add(btnSave, 0, 1);
        gpActions.add(btnGoBack, 0, 2);
    }

    public void goBack() {
        layoutManager.setLayout(MainApp.searchMemberLayout);
    }

}
