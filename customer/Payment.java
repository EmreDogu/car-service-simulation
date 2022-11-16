package customer;
public class Payment {
    int paymentId;
    int customerId;
    int completionTime;
    int paymentAmount;

    public Payment(int paymentId, int customerId, int completionTime, int paymentAmount) {
        this.paymentId = paymentId;
        this.customerId = customerId;
        this.completionTime = completionTime;
        this.paymentAmount = paymentAmount;
    }
}
