package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.*;

public class AddProductsFormController implements Initializable {

    public TextField enterId;
    @FXML
    private TextField SearchPartBox;

    @FXML
    private TextField enterInv;

    @FXML
    private TextField enterMax;

    @FXML
    private TextField enterMin;

    @FXML
    private TextField enterName;

    @FXML
    private TextField enterPrice;

    @FXML
    private TableColumn<Part, Integer> partToProductId;

    @FXML
    private TableColumn<Part, Integer> partId;

    @FXML
    private TableColumn<Part, Integer> partToProductInv;

    @FXML
    private TableColumn<Part, Integer> partInv;

    @FXML
    private TableColumn<Part, String> partToProductName;

    @FXML
    private TableColumn<Part, String> partName;

    @FXML
    private TableColumn<Part, Double> partToProductPrice;

    @FXML
    private TableColumn<Part, Double> partPrice;

    @FXML
    private TableView<Part> tableOne;

    @FXML
    private TableView<Part> tableTwo;

    /** Generates an ID based on the numbers of objects in the full product list */
    int generatedProductId = Inventory.getTotalProducts();
    Product newProduct;

    /** Static bottom table list that stores all "associated parts" for passing. */
    private static ObservableList<Part> tableTwoList = FXCollections.observableArrayList();

    /** Initializes all the values for each table */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateTableOne();
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        newProduct = new Product(0, null, 0.0, 0, 0, 0);
        tableTwoList = newProduct.getAssociatedPart();
        tableTwo.setItems(newProduct.getAssociatedPart());
        partToProductId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partToProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partToProductInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partToProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        /** fills in the "id" box with the ID value that was generated */
        enterId.setText("AUTO GEN: " + generatedProductId);

    }

    /** Takes the selected Item and adds it to the static bottom table's list then updates that table */
    @FXML
    public void AddPartHandler() {
        Part selectedItem = tableOne.getSelectionModel().getSelectedItem();
        if(selectedItem == null) {
            Alert nullAlert = new Alert(Alert.AlertType.ERROR, "No part is selected to add");
            nullAlert.showAndWait();
        } else {
            tableTwoList.add(selectedItem);
            updateTableTwo();
        }
    }

    /** Takes you back to the main menu and deletes any enter info */
    @FXML
    void exitButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure want to exit, this will clear all data entered?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }

    /** Takes selected item and just removes it from the static list of the bottom table */
    @FXML
    public void deletePartHandler() {
        Part selectedItem = tableTwo.getSelectionModel().getSelectedItem();
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the Part, continue?");
        deleteAlert.setTitle("Confirm Delete");
        Optional<ButtonType> result = deleteAlert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            Part singlePart = tableTwo.getSelectionModel().getSelectedItem();
            if(singlePart == null) {
                Alert nullAlert = new Alert(Alert.AlertType.CONFIRMATION, "There was no item selected");
                nullAlert.showAndWait();
            }
            else {
                tableTwoList.remove(selectedItem);
            }
        }
    }

    /**
        This Parses all ints and doubles and then plugs them into values that will then be stored in "newProduct"
        I then add "newProduct" to the full product list.
     */
    @FXML
    public void saveHandler(ActionEvent event) throws IOException {
        try {
            if (Integer.parseInt(enterMin.getText()) > Integer.parseInt(enterMax.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Min value cannot be greater than Max value.");
                alert.showAndWait();
            } else if (Integer.parseInt(enterInv.getText()) > Integer.parseInt(enterMax.getText()) || Integer.parseInt(enterInv.getText()) < Integer.parseInt(enterMin.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory amount must be between minimum and maximum values.");
                alert.showAndWait();
            } else {
                String name = enterName.getText();
                int inventory = Integer.parseInt(enterInv.getText());
                double priceCost = Double.parseDouble(enterPrice.getText());
                int max = Integer.parseInt(enterMax.getText());
                int min = Integer.parseInt(enterMin.getText());

                Product newProduct = new Product(generatedProductId, name, priceCost, inventory, min, max);
                Inventory.addProduct(newProduct);

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter a valid value for each text field.");
            alert.showAndWait();
        }
    }

    /**
    Checks for a String and adds it, if not String is found then it checks for an Integer and adds it if found, if neither
    is found then it throws an Alert to the user.
     */
    @FXML
    void searchPartEvent() {
        String searchTerm = SearchPartBox.getText();
        ObservableList<Part> partsList = lookUpPart(searchTerm);

        if(partsList.size() == 0) {
            try {
                int partId = Integer.parseInt(searchTerm);
                Part part = lookUpPart(partId);
                if(part != null) {
                    partsList.add(part);
                }
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Part Search Warning");
                alert.setHeaderText("There were no parts found.");
                alert.setContentText("The search term entered does not match any part ID or Name");
                alert.showAndWait();
            }
        }
        tableOne.setItems(partsList);
        SearchPartBox.setText("");
    }

/** Update the top Part table with the full inventory list */
    public void updateTableOne() {
        tableOne.setItems(Inventory.getAllParts());
    }
/** Update the bottom Part table with the static list */
    public void updateTableTwo() {
        tableTwo.setItems(tableTwoList);
    }
}


