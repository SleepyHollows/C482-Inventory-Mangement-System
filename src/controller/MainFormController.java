package controller;

import model.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

public class MainFormController implements Initializable {

    @FXML
    private Button ModifyPart;

    @FXML
    private Button ModifyProduct;

    @FXML
    private TableView<Part> partTable;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Part, Integer> partIdColumn;

    @FXML
    private TableColumn<Part, Integer> partInventoryColumn;

    @FXML
    private TableColumn<Part, String> partNameColumn;

    @FXML
    private TableColumn<Part, Double> partPriceColumn;

    @FXML
    private TextField partSearchBox;

    @FXML
    private TableColumn<Product, Integer> productIdColumn;

    @FXML
    private TableColumn<Product, Integer> productInventoryColumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, Double> productPriceColumn;

    @FXML
    private TextField productSearchBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partTable.setItems(getAllParts());
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.setItems(getAllProducts());
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
    Checks for a String and adds it, if not String is found then it checks for an Integer and adds it if found, if neither
    is found then it throws an Alert to the user.
     */
    @FXML
    void searchPartEvent() {
        String searchTerm = partSearchBox.getText();
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
            partTable.setItems(partsList);
            partSearchBox.setText("");
        }

    /**
    Checks for a String and adds it, if not String is found then it checks for an Integer and adds it if found, if neither
    is found then it throws an Alert to the user.
     */
    @FXML
    void searchProductEvent() {
        String searchTerm =  productSearchBox.getText();
        ObservableList<Product> productsList = lookUpProduct(searchTerm);

        if(productsList.size() == 0) {
            try {
                int productId = Integer.parseInt(searchTerm);
                Product product = lookUpProduct(productId);
                if(product != null) {
                    productsList.add(product);
                }
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Product Search Warning");
                alert.setHeaderText("There were no products found!");
                alert.setContentText("The search term entered does not match any product ID or Name");
                alert.showAndWait();
            }
        }
        productTable.setItems(productsList);
        productSearchBox.setText("");

    }

    /** Opens "AddPartsForm.fxml" */
    public void openAddPartsForm(ActionEvent event) throws IOException {
        Parent addPartParent = FXMLLoader.load(getClass().getResource("/view/AddPartsForm.fxml"));
        Scene addPartScene = new Scene(addPartParent);
        Stage addPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    /** Opens "AddProductsForm.fxml" */
    public void openAddProductsForm(ActionEvent event) throws IOException {
        Parent addPartParent = FXMLLoader.load(getClass().getResource("/view/AddProductsForm.fxml"));
        Scene addPartScene = new Scene(addPartParent);
        Stage addPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    /** Grabs the values from the selected item box and sends them to "ModifyProductsForm.fxml" */
    public void openModifyProductsForm(){
        try {
            Stage stage = (Stage) ModifyProduct.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyProductsForm.fxml"));
            Parent root = loader.load();
            ModifyProductsFormController controller = loader.getController();
            Product product = productTable.getSelectionModel().getSelectedItem();
            int index = productTable.getSelectionModel().getSelectedIndex();
            if(product != null) {
                controller.setProduct(product, index);
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    /** Grabs the values from the selected item box and sends them to "ModifyPartsForm.fxml" */
    public void openModifyPartsForm() {
        try {
                Stage stage = (Stage) ModifyPart.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyPartsForm.fxml"));
                Parent root = loader.load();
                ModifyPartsFormController controller = loader.getController();
                Part part = partTable.getSelectionModel().getSelectedItem();
                int index = partTable.getSelectionModel().getSelectedIndex();
                if(part != null) {
                    controller.setPart(part, index);

                }
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    /** Checks if there is a product selected, if a product is selected then it will call "deleteProduct()" from Inventory */
    @FXML
    void deleteProductHandler() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the Product, continue?");
        alert.setTitle("Confirm Delete");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Product product = productTable.getSelectionModel().getSelectedItem();
                if(product.getAssociatedPart().isEmpty()) {
                    deleteProduct(product);
                }
                else {
                    alert = new Alert(Alert.AlertType.ERROR, "Can't delete products with linked parts");
                    alert.showAndWait();
                }
            }
            catch (UnsupportedOperationException e){
                alert.setTitle("Deletion Error");
                alert.setHeaderText("Product not deleted");
                alert.setContentText("There was no product selected");
                alert.showAndWait();
            }
        }
    }

    /** Checks if there is a part selected, if a part is selected then it will call "deletePart()" from Inventory */
    @FXML
    void deletePartHandler() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the part, do you want to continue?");
        alert.setTitle("Confirm Deletion");
        Optional<ButtonType> result = alert.showAndWait();
        try {

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Part individualPart = partTable.getSelectionModel().getSelectedItem();
                deletePart(individualPart);
            }
        }
        catch (UnsupportedOperationException e){
            alert.setTitle("Deletion Error");
            alert.setHeaderText("Product not deleted");
            alert.setContentText("There was no product selected");
            alert.showAndWait();
            }
        }

    /** Confirms you want to exit, then closes the application */
    @FXML
    public void exitHandler() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure want to close the application?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
}
