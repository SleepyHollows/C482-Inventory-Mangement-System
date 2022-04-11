package controller;

import model.Part;
import model.Product;
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

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.*;

public class ModifyProductsFormController implements Initializable {

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
    private TableColumn<Part, Integer> partId;

    @FXML
    private TableColumn<Part, String> partName;

    @FXML
    private TableColumn<Part, Integer> partInv;

    @FXML
    private TableColumn<Part, Double> partPrice;

    @FXML
    private TableColumn<Part, Integer> partToProductId;

    @FXML
    private TableColumn<Part, String> partToProductName;

    @FXML
    private TableColumn<Part, Integer> partToProductInv;

    @FXML
    private TableColumn<Part, Double> partToProductPrice;

    @FXML
    private TableView<Part> tableOne;

    @FXML
    private TableView<Part> tableTwo;

    Product productSelected;
    int indexSelected;

    /** Initialize the Scene */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTableOne();
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        Product newProduct = new Product(0, null, 0.0, 0, 0, 0);
        tableTwo.setItems(newProduct.getAssociatedPart());
        partToProductId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partToProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partToProductInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partToProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /** Gets selected part and adds it to the "tableOneList" */
    public void AddProductHandler() {
        Part selectedItem = tableOne.getSelectionModel().getSelectedItem();
        if(selectedItem == null) {
            Alert nullAlert = new Alert(Alert.AlertType.ERROR, "No part is selected to add");
            nullAlert.showAndWait();
        } else {
            productSelected.addAssociatedPart(selectedItem);
        }
    }

    /** Verifies if you want to leave, then sends you back to "MainForm.fxml" */
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

    /** Removes selected part from "tableTwoList" */
    public void deleteAssociatedPartHandler() {
        Part selectedItem = tableTwo.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the entire Part, do you want to continue?");
        alert.setTitle("Confirmation of Deletion");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Part individualPart = tableTwo.getSelectionModel().getSelectedItem();
            if (productSelected.deleteAssociatedPart(individualPart)) {
                productSelected.getAssociatedPart().remove(selectedItem);
            }
        }
    }

    /**
    Checks for a String and adds it, if not String is found then it checks for an Integer and adds it if found, if neither
    is found then it throws an Alert to the user.
     */
    @FXML
    public void SaveProductHandler(ActionEvent event) throws IOException {
        try {
            Integer.parseInt(enterInv.getText());
            Double.parseDouble(enterPrice.getText());
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

                productSelected.setName(name);
                productSelected.setPrice(priceCost);
                productSelected.setMax(max);
                productSelected.setMin(min);
                productSelected.setStock(inventory);
                updateProduct(indexSelected, productSelected);

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
                alert.setHeaderText("There were no parts found!");
                alert.setContentText("The search term entered does not match any part ID or Name");
                alert.showAndWait();
            }
        }
        tableOne.setItems(partsList);
        SearchPartBox.setText("");
    }

    /** pulls data from "MainForm.fxml" selected product, converts data types and plugs them into the appropriate fields */
    public void setProduct(Product product, int index) {
        productSelected = product;
        indexSelected = index;

        if (product instanceof Product) {
            Product newProduct = productSelected;

            this.enterName.setText(newProduct.getName());
            this.enterInv.setText((Integer.toString(newProduct.getStock())));
            this.enterPrice.setText((Double.toString(newProduct.getPrice())));
            this.enterMax.setText((Integer.toString(newProduct.getMax())));
            this.enterMin.setText((Integer.toString(newProduct.getMin())));
            tableTwo.setItems(newProduct.getAssociatedPart());
            updateProduct(indexSelected, newProduct);
        }
    }
    /** Updates tableOne with */
    public void updateTableOne() {
        tableOne.setItems(getAllParts());
    }
}



