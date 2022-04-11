package model;

public class Inhouse extends Part {

    private static int machineId;

    public Inhouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        Inhouse.machineId = machineId;
    }

    /** return the machineId */
    public int getInHoseMachineId() {
        return machineId;
    }

    /** param machineId the machineId to set */
    public void setInHouseMachineId(int machineId) {
        Inhouse.machineId = machineId;
    }

}
