package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * I ran into a logic error  in my model.Inventory.lookupParts, the error was causing the search to only return a
 * String search and not the ID search. I fixed this my changing the return value from return parts to return null.
 * With the method returning null it allowed for the method in ModifyPartsFormController to be able to verify that there
 * was a value found so that it could print out the value found.
 * A future upgrade I would make would be to add a system to add and view more details about each part and product.
 * Currently, you get id, name, inventory level, price, but I would add details to handle returned
 * parts or products. A supplier or consumer needs to keep track on bad parts and return parts for future reference.
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage.setTitle("Inventory Management System");
        stage.setScene(new Scene(root,1200, 800));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
