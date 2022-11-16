package customer;

public class Customer {
    static int CustomersEnteredCounter = 0; 
    static int CustomersServed = 0;
    static int CustomersLeft = 0;
    static double TotalWaitingTime = 0;
    static int TotalWaitCounter = 0;
    protected static final int NO_MECHANIC = 0;

    private final int customerId;
    private final double customerAcceptence;
    private final double presentTime;
    private final String serviceType;
    private final double nextTime;
    private final String customerStatus; 
    private final String previousStatus;
    protected int mechanicID;
    protected boolean firstWaits = true;
    private final double entryTime;

    public Customer(int customerId, double arrivalTime, String serviceType, Double customerAcceptence) {
        this.customerId = customerId;
        this.customerAcceptence = customerAcceptence;
        this.serviceType = serviceType;
        this.presentTime = arrivalTime;
        this.nextTime = arrivalTime;
        this.customerStatus = "arrives";
        this.previousStatus = "";
        this.mechanicID = NO_MECHANIC;
        this.entryTime = arrivalTime;
    }

    private Customer(int customerId, double customerAcceptence, double updatedPresentTime, double updatedNextTime, String previousStatus,
            String newStatus, String serviceType, int mechanicID, double entryTime) {
        this.customerId = customerId;
        this.customerAcceptence = customerAcceptence;
        this.presentTime = updatedPresentTime;
        this.nextTime = updatedNextTime;
        this.previousStatus = previousStatus;
        this.customerStatus = newStatus;
        this.serviceType = serviceType;
        this.mechanicID = mechanicID;
        this.entryTime = entryTime;
    }

    public static Customer enter(double arrivalTime, String serviceType, double acceptence) {
        return new Customer(++CustomersEnteredCounter, arrivalTime, serviceType, acceptence);
    }

    public Customer fromArrivesToServed(int mechanicID) {
        // ARRIVES to SERVED (i.e served immediately)
        return new Customer(this.customerId, this.customerAcceptence, this.presentTime, this.presentTime, this.customerStatus,
                "served", this.serviceType, mechanicID, this.entryTime);
    }

    public Customer fromServedToService(double servedTime) {
        // ARRIVES to SERVED (i.e served immediately)
        return new Customer(this.customerId, this.customerAcceptence, servedTime, servedTime, this.customerStatus,
                "service", this.serviceType, this.mechanicID, this.entryTime);
    }

    public Customer fromServiceToDelivery(double serviceTime) {
        // ARRIVES to SERVED (i.e served immediately)
        return new Customer(this.customerId, this.customerAcceptence, serviceTime, serviceTime, this.customerStatus,
                "delivery", this.serviceType, this.mechanicID, this.entryTime);
    }

    public Customer fromWaitsToWaits(double nextAvailableTime, int mechanicID) {
        assert (this.customerStatus.equals("waits"));
        Customer res = new Customer(this.customerId, this.customerAcceptence, this.presentTime,
                nextAvailableTime, this.customerStatus, "waits", this.serviceType, mechanicID, this.entryTime);
        res.firstWaits = false;
        return res;
    }

    public Customer fromWaitsToServed(double nextAvailableTime) {
        assert (this.customerStatus.equals("waits"));
        TotalWaitingTime += (nextAvailableTime - this.entryTime);
        // will def be served if there's no one else waiting:
        return new Customer(this.customerId,
        this.customerAcceptence, 
                nextAvailableTime,
                nextAvailableTime,
                this.customerStatus,
                "served",
                this.serviceType, this.mechanicID, this.entryTime);
    }

    public Customer fromDeliveryToDone(double deliveryTime) {
        // ARRIVES to SERVED (i.e served immediately)
        ++CustomersServed;
        return new Customer(this.customerId, this.customerAcceptence, deliveryTime, deliveryTime, this.customerStatus,
                "done", this.serviceType, this.mechanicID, this.entryTime);
    }

    public Customer fromArrivesToWaits(double nextAvailableTime, int mechanicID) {
        Customer.TotalWaitCounter++;
        return new Customer(this.customerId, this.customerAcceptence, this.presentTime, nextAvailableTime, this.customerStatus,
                "waits", this.serviceType, mechanicID, this.entryTime);
    }

    public Customer fromArrivesToLeaves(double servedTime) {
        Customer.CustomersLeft++;
        return new Customer(this.customerId, this.customerAcceptence, servedTime, servedTime, this.customerStatus,
                "leaves", this.serviceType, NO_MECHANIC, this.entryTime);
    }

    public int getCustomerId() {
        return customerId;
    }

    public static int getCustomersEnteredCounter() {
        return CustomersEnteredCounter;
    }

    public static void setCustomersEnteredCounter(int customersEnteredCounter) {
        CustomersEnteredCounter = customersEnteredCounter;
    }

    public static int getCustomersServed() {
        return CustomersServed;
    }

    public static void setCustomersServed(int customersServed) {
        CustomersServed = customersServed;
    }

    public static int getCustomersLeft() {
        return CustomersLeft;
    }

    public static void setCustomersLeft(int customersLeft) {
        CustomersLeft = customersLeft;
    }

    public static double getTotalWaitingTime() {
        return TotalWaitingTime;
    }

    public static void setTotalWaitingTime(double totalWaitingTime) {
        TotalWaitingTime = totalWaitingTime;
    }

    public static int getTotalWaitCounter() {
        return TotalWaitCounter;
    }

    public static void setTotalWaitCounter(int totalWaitCounter) {
        TotalWaitCounter = totalWaitCounter;
    }

    public static int getNoMechanic() {
        return NO_MECHANIC;
    }

    public double getPresentTime() {
        return presentTime;
    }

    public String getServiceType() {
        return serviceType;
    }

    public double getNextTime() {
        return nextTime;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public Double getCustomerAcceptence() {
        return customerAcceptence;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public int getMechanicID() {
        return mechanicID;
    }

    public void setMechanicID(int mechanicID) {
        this.mechanicID = mechanicID;
    }

    public boolean isFirstWaits() {
        return firstWaits;
    }

    public void setFirstWaits(boolean firstWaits) {
        this.firstWaits = firstWaits;
    }

    public double getEntryTime() {
        return entryTime;
    }

    public static String customerStats() {
        double averageWaitingTime = (CustomersServed == 0 || TotalWaitingTime == 0)
                ? 0
                : TotalWaitingTime / CustomersServed;
        return "[" + "Average waiting time: " + averageWaitingTime + " minutes, " 
                + "Customers: " + CustomersServed + ", " + "Customers left: " + (CustomersEnteredCounter - CustomersServed) + "]";
    }

    public static String prettyPrint(double x) {
        return String.format("%.3f", x);
    }

    @Override
    public String toString() {
        String status = this.customerStatus;
        String res = "Current time: " + prettyPrint(this.presentTime)
                + ", Customer ID:" + this.customerId
                + ", Status: " + status;
        switch (status) {
            case "served":
                res += " by ";
                break;
            case "service":
            res += " by ";
                break;
            case "delivery":
            res += " by ";
                break;
            case "done":
            res += " by ";
                break;
            case "waits":
            res += " because ";
                break;
            default:
                res += "";
        }
        return res;

    }
}
