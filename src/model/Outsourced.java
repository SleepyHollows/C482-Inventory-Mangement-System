package model;

public class Outsourced extends Part {

    private static String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        Outsourced.companyName = companyName;
    }

    /** return the companyName */
    public String getOutsourcedCompanyName() {
        return companyName;
    }

    /** param companyName the companyName to set */
    public void setOutsourcedCompanyName(String companyName) {
        Outsourced.companyName = companyName;
    }
}
