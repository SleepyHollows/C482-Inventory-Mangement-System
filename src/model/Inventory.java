package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Create an inventory class for all items in the inventory management system with functions to manipulate the inventory */
public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /** Adds a part to the part list */
    public static void addPart(Part part) {
        allParts.add(part);
    }

    /** Adds a product to the product list */
    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    /** Search for a matching String in Parts list and returns if found */
    public static ObservableList<Part> lookUpPart(String searchTerm) {
        ObservableList<Part> parts = FXCollections.observableArrayList();
        for(Part part : getAllParts()) {
            if(part.getName().contains(searchTerm)) {
                parts.add(part);
            }
        }
        return parts;
    }

    /** Search for a matching Integer in Parts list and returns if found or returns null if not */
    public static Part lookUpPart(int searchTerm) {
        for(Part part : getAllParts()) {
            if (part.getId() == searchTerm) {
                return part;
            }
        }
        return null;
    }

    /** Search for a matching String in Products list and returns if found */
    public static ObservableList<Product> lookUpProduct(String searchTerm) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        for(Product product : getAllProducts()) {
            if(product.getName().contains(searchTerm)) {
                products.add(product);
            }
        }
        return products;
    }

    /** Search for a matching Integer in Products list and returns if found or returns null if not */
    public static Product lookUpProduct(int searchTerm) {
        for(Product product : getAllProducts()) {
            if(product.getProductId() == searchTerm) {
                return product;
            }
        }
        return null;
    }


    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }


    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /** Checks for a matching part Id and removes selectedPart from list if one is found */
    public static boolean deletePart(Part selectedPart) {
        for(Part part : getAllParts()) {
            if(part.getId() == selectedPart.getId()) {
                allParts.remove(selectedPart);
                System.out.println(selectedPart + "was removed");
                System.out.println(selectedPart.getName() + " was removed.");
                return true;
            }
        }
        System.out.println(selectedPart.getName() + " was not removed.");
        return false;
    }

    /** Checks for a matching product Id and removes selectedProduct if one is found */
    public static boolean deleteProduct(Product selectedProduct) {
        for(Product product : getAllProducts()){
            if(product.getProductId() == selectedProduct.getProductId()) {
                allProducts.remove(selectedProduct);
                System.out.println(selectedProduct.getName() + " was removed.");
                return true;
            }
        }
        System.out.println(selectedProduct.getName() + " was not removed.");
        return false;
    }

    /** Get all parts in allParts list */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /** Get all products in allProducts list */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /** Used to get the size of the "getAllProducts()" list */
    public static int getTotalProducts() {
        int total = getAllProducts().size();
        return total;
    }
    /** Used to get the size of the "getAllParts()" list*/
    public static int getTotalParts() {
        int total = getAllParts().size();
        return total;
    }
}
