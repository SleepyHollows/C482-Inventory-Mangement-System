package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Inhouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPartsFormController implements Initializable {

    @FXML
    private TextField enterId;

    @FXML
    private TextField enterInv;

    @FXML
    private TextField enterSpecialValue;

    @FXML
    private TextField enterMax;

    @FXML
    private TextField enterMin;

    @FXML
    private TextField enterName;

    @FXML
    private TextField enterPrice;

    @FXML
    private Text specialValueLabel;

    /** Gets the size of the Product */
    int generatedPartsId = Inventory.getTotalParts();
    boolean isInhouse = true;

    /** This will change specialValueLabel to "Machine ID" when activated */
    public void inHouseHandler() {
        isInhouse = true;
        specialValueLabel.setText("Machine ID");
    }
    /** This will change specialValueLabel to "Company Name" when activated */
    public void OutsourcedHandler() {
        isInhouse = false;
        specialValueLabel.setText("Company Name");
    }

    /** This confirms if you want to leave before taking you back to home screen */
    public void exitHandler(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure want to exit, this will clear all data entered?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }

    /**
    Takes all entered values and parses them to their required data types. The isInhouse boolean is used to check if
    the enterSpecialValue is going to be saved as "Machine ID" or "Company Name" and converts it to an Integer or String
     */
    @FXML
    public void saveHandler(ActionEvent event) throws IOException {
        try{
            Integer.parseInt(enterInv.getText());
            Double.parseDouble(enterPrice.getText());
            if (Integer.parseInt(enterMin.getText()) > Integer.parseInt(enterMax.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Min value cannot be greater than Max value.");
                alert.showAndWait();
            } else if (Integer.parseInt(enterInv.getText()) > Integer.parseInt(enterMax.getText()) || Integer.parseInt(enterInv.getText()) < Integer.parseInt(enterMin.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory amount must be between minimum and maximum values.");
                alert.showAndWait();
            }else {
                String name = enterName.getText();
                int inventory = Integer.parseInt(enterInv.getText());
                double priceCost = Double.parseDouble(enterPrice.getText());
                int max = Integer.parseInt(enterMax.getText());
                int min = Integer.parseInt(enterMin.getText());
                if (isInhouse) {
                    int machineID = Integer.parseInt(enterSpecialValue.getText());
                    Inhouse addInHousePart = new Inhouse(generatedPartsId, name, priceCost, inventory, min, max
                            , machineID);

                    Inventory.addPart(addInHousePart);
                }
                if (!isInhouse) {
                    String companyName = enterSpecialValue.getText();
                    Outsourced addOutsourcedPart = new Outsourced(generatedPartsId, name, priceCost, inventory,
                            min, max, companyName);

                    Inventory.addPart(addOutsourcedPart);
                }
                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            }
        }
        catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter a valid value for each text field.");
            alert.showAndWait();
        }
    }

    /** Just used to show the partId that will be used */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /** fills in the "id" box with the ID value that was generated */
        enterId.setText("AUTO GEN: " + generatedPartsId);
    }
}
