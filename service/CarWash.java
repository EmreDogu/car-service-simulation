package service;

public class CarWash extends Service{

    private int carwashPrice;
    
    public CarWash(int serviceID, int mechanicID, int customerID) {
        super(serviceID, mechanicID, customerID);
    }
}
