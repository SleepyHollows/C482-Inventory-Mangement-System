package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Inhouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.util.Optional;

import static model.Inventory.updatePart;

public class ModifyPartsFormController {

    private boolean isInhouse;
    Part partSelected;
    int indexSelected;

    @FXML
    private Text specialValueLabel;

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

    /** This will change specialValueLabel to "Machine ID" when activated */
    public void inHouseHandler() {
        isInhouse = true;
        specialValueLabel.setText("Machine ID");
    }
    /** This will change specialValueLabel to "Machine ID" when activated */
    public void outsourcedHandler() {
        isInhouse = false;
        specialValueLabel.setText("Company Name");
    }

    /** pulls data from "MainForm.fxml" selected part, converts data types and plugs them into the appropriate fields*/
    public void setPart(Part part, int index) {
        partSelected = part;
        indexSelected = index;
        if(part instanceof Inhouse newPartInhouse) {
            isInhouse = true;
            this.enterName.setText(newPartInhouse.getName());
            this.enterInv.setText(Integer.toString(newPartInhouse.getStock()));
            this.enterPrice.setText(Double.toString(newPartInhouse.getPrice()));
            this.enterMax.setText(Integer.toString(newPartInhouse.getMax()));
            this.enterMin.setText(Integer.toString(newPartInhouse.getMin()));
            this.enterSpecialValue.setText(Integer.toString(newPartInhouse.getInHoseMachineId()));
            updatePart(indexSelected, newPartInhouse);
        }
        else if (part instanceof Outsourced newPartOutsourced) {
            isInhouse = false;
            specialValueLabel.setText("Company Name");
            this.enterName.setText(newPartOutsourced.getName());
            this.enterInv.setText((Integer.toString(newPartOutsourced.getStock())));
            this.enterPrice.setText((Double.toString(newPartOutsourced.getPrice())));
            this.enterMin.setText((Integer.toString(newPartOutsourced.getMin())));
            this.enterMax.setText((Integer.toString(newPartOutsourced.getMax())));
            this.enterSpecialValue.setText(newPartOutsourced.getOutsourcedCompanyName());
            updatePart(indexSelected, newPartOutsourced);
        }
    }

    /** Confirms you want to exit then sends you to "MainForm.fxml" */
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
    public void saveHandler (ActionEvent event) throws IOException {
        try{
            Integer.parseInt(enterInv.getText());
            Double.parseDouble(enterPrice.getText());
            if (Integer.parseInt(enterMin.getText()) > Integer.parseInt(enterMax.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Min cannot be greater than Max.");
                alert.showAndWait();
            } else if (Integer.parseInt(enterInv.getText()) > Integer.parseInt(enterMax.getText()) || Integer.parseInt(enterInv.getText()) < Integer.parseInt(enterMin.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory must be between Min and Max.");
                alert.showAndWait();
            }
            else {
                int id = partSelected.getId();
                String name = enterName.getText();
                int inventory = Integer.parseInt(enterInv.getText());
                double price = Double.parseDouble(enterPrice.getText());
                int max = Integer.parseInt(enterMax.getText());
                int min = Integer.parseInt(enterMin.getText());

                if (isInhouse) {

                    int machineID = Integer.parseInt(enterSpecialValue.getText());

                    Inhouse inhousePart = new Inhouse(id, name, price, inventory, min, max, machineID);
                    Inventory.getAllParts().set(indexSelected, inhousePart);
                }

                if (!isInhouse) {

                    String companyName = enterSpecialValue.getText();

                    Outsourced outsourcedPart = new Outsourced(id, name, price, inventory, min, max, companyName);
                    Inventory.getAllParts().set(indexSelected, outsourcedPart);
                }

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            }
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("You must enter a valid value");
            alert.showAndWait();
        }
    }
}
