package service;

public class CheckUp extends Service{

    private int checkupPrice;

    public CheckUp(int serviceID, int mechanicID, int customerID) {
        super(serviceID, mechanicID, customerID);
    }

}
