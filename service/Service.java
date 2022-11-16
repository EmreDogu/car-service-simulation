package service;
public class Service {

    private final int serviceID;
    private int serviceFee;
    private final int mechanicID;
    private final int customerID;
    
    public Service(int serviceID, int mechanicID, int customerID) {
        this.serviceID = serviceID;
        this.mechanicID = mechanicID;
        this.customerID = customerID;
    }
}
