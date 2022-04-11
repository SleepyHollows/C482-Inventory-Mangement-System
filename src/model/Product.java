package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Caleb Hathaway
 */
public class Product {
    private static final ObservableList<Part> associatedPart = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getProductId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setProductId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /** return the list of associated parts */
    public static ObservableList<Part> getAssociatedPart() {
        return associatedPart;
    }

    /** Adds a part to the Associated part list */
    public static void addAssociatedPart(Part part) {
        associatedPart.add(part);
    }

    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        for (Part i : associatedPart) {
            if (i.getId() == selectedAssociatedPart.getId()) {
                associatedPart.remove(i);
                return true;
            }
        }
        return false;
    }

}

