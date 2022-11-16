package service;

public class Repair extends Service{

    private int repairPrice;

    public Repair(int serviceID, int mechanicID, int customerID) {
        super(serviceID, mechanicID, customerID);
    }
}
