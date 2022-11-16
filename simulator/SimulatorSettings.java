package simulator;

public class SimulatorSettings {
    double interArrival;
    double specifyingReason;
    double checkIn;
    double driveToStation;
    double diagnostics;
    double scopeRepair;
    double Autharization;
    double orderParts;
    double gettingParts;
    double Repair;
    double qualityControl;
    double payment;
    double delivery;
    double washing;
    double paymentAmount;
    public SimulatorSettings(double interArrival, double specifyingReason, double checkIn, double driveToStation,
            double diagnostics, double scopeRepair, double autharization, double orderParts, double gettingParts,
            double repair, double qualityControl, double payment, double delivery, double washing,
            double paymentAmount) {
        this.interArrival = interArrival;
        this.specifyingReason = specifyingReason;
        this.checkIn = checkIn;
        this.driveToStation = driveToStation;
        this.diagnostics = diagnostics;
        this.scopeRepair = scopeRepair;
        Autharization = autharization;
        this.orderParts = orderParts;
        this.gettingParts = gettingParts;
        Repair = repair;
        this.qualityControl = qualityControl;
        this.payment = payment;
        this.delivery = delivery;
        this.washing = washing;
        this.paymentAmount = paymentAmount;
    }

    
    
}
