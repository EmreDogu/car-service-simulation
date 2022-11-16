package employee;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.math3.distribution.*;

import customer.Customer;

public class Manager {
    private LinkedList<Customer> mainQueue;
    private LinkedList<Customer> waitQueue;
    private Queue<String> logs;
    private Mechanic[] myMechanics;
    private Customer[] myCustomers;

    public Manager(int numMechanic, int numCustomer) {
        this.mainQueue = new LinkedList<>();
        this.waitQueue = new LinkedList<>();
        this.logs = new LinkedList<>();
        initMechanics(numMechanic);
        initCustomers(numCustomer);
    }

    public void operate() {
        while (!this.mainQueue.isEmpty() && !everyCustomerDone()) {
            Customer currentCustomer = mainQueue.poll();
            for (Mechanic m : myMechanics) {
                if (m.isIdle && waitQueue.size()>0) {
                    Customer toAdd = waitQueue.poll();
                    this.mainQueue.add(toAdd);
                    break;
                }
            }
            if (currentCustomer.isFirstWaits()) {
                registerEvent(currentCustomer);
            }
            if (!isTerminalState(currentCustomer)) {
                Customer changed = changeCustomerState(currentCustomer);
                if (changed.getCustomerStatus() == "waits" && changed.getPreviousStatus() != "waits") {
                    this.waitQueue.add(changed);
                } else if (changed.getCustomerStatus() != "waits" || !currentCustomer.equals(changed)) {
                    this.mainQueue.add(changed);
                }
            } else { // customer has terminal state of leaves or done
                if (isDoneState(currentCustomer)) {
                    Mechanic m = this.myMechanics[currentCustomer.getMechanicID() - 1];
                    mechanicHandlesDone(m, currentCustomer);
                }
            }
        }
    }

    private boolean everyCustomerDone() {
        for (Customer customer : myCustomers) {
            if (customer.getCustomerStatus() != "done") {
                return false;
            }
        }
        return true;
    }

    private void mechanicHandlesDone(Mechanic m, Customer customer) {
        for (Mechanic mec : myMechanics) {
            mec.waitingQueue.removeIf(n -> (n.getCustomerId() == customer.getCustomerId()));
        }
        m = m.doneServing();
        updateMechanicArray(m);
    }

    private void updateMechanicArray(Mechanic updatedMechanic) {
        this.myMechanics[updatedMechanic.employeeID - 1] = updatedMechanic;
    }

    public void initMechanics(int numMechanic) {
        Mechanic[] mechanics = new Mechanic[numMechanic];

        for (int i = 0; i < numMechanic; i++) {
            Mechanic m = new Mechanic(i + 1);
            mechanics[i] = m;
        }
        this.myMechanics = mechanics;
    }

    private void initCustomers(int numCustomers) {
        double now = 0;
        Customer[] customers = new Customer[numCustomers];
        for (int i = 0; i < numCustomers; i++) {
            Customer myCustomer = generateCustomer(now);
            this.mainQueue.add(myCustomer);
            customers[i] = myCustomer;
            now = getNextArrivalTime(now);
        }
        this.myCustomers = customers;
    }

    private double getNextArrivalTime(double now) {
        BetaDistribution beta = new BetaDistribution(0.485, 0.506);
        return now + 9.5 + 95 * beta.sample();
    }

    private double getServedTime(double now) {
        BetaDistribution specfyingReason = new BetaDistribution(1.05, 1.15);
        TriangularDistribution checkIn = new TriangularDistribution(14.5, 21.8, 25.5);
        BetaDistribution driveToStation = new BetaDistribution(0.784, 0.71);
        return now + (0.5 + 10 * specfyingReason.sample()) + checkIn.sample() + (6.5 + 4 * driveToStation.sample());
    }

    private double getRepairTime(double now) {
        BetaDistribution diagnostics = new BetaDistribution(0.737, 0.715);
        NormalDistribution specfyingScope = new NormalDistribution(8.3, 1.42);
        UniformRealDistribution autherization = new UniformRealDistribution(3.5, 12.5);
        BetaDistribution makeOrder = new BetaDistribution(0.735, 0.53);
        BetaDistribution getUnit = new BetaDistribution(0.83, 0.965);
        BetaDistribution performRepairs = new BetaDistribution(0.755, 0.84);
        BetaDistribution qualityAssurance = new BetaDistribution(0.702, 0.583);
        return now + (19.5 + 41 * diagnostics.sample()) + specfyingScope.sample() + autherization.sample()
                + (14.5 + 16 * makeOrder.sample()) + (14.5 + 16 * getUnit.sample())
                + (60 + 180 * performRepairs.sample()) + (19.5 + 26 * qualityAssurance.sample());
    }

    private double getCheckupTime(double now) {
        BetaDistribution diagnostics = new BetaDistribution(0.737, 0.715);
        return now + (19.5 + 41 * diagnostics.sample());
    }

    private double getCarwashTime(double now) {
        BetaDistribution carWash = new BetaDistribution(0.834, 0.787);
        return now + (1.5 + 14 * carWash.sample());
    }

    private double getDeliveryTime(double now) {
        BetaDistribution payment = new BetaDistribution(0.708, 0.978);
        BetaDistribution delivery = new BetaDistribution(0.546, 0.668);
        return now + (5.5 + 5 * payment.sample()) + (4.5 + 4 * delivery.sample());
    }

    private Customer generateCustomer(double arrivalTime) {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 3 + 1);

        if (randomNum == 1) {
            return Customer.enter(arrivalTime, "repair");
        } else if (randomNum == 2) {
            return Customer.enter(arrivalTime, "checkup");
        } else {
            return Customer.enter(arrivalTime, "carwash");
        }
    }

    private boolean isArrivesState(Customer c) {
        return c.getCustomerStatus().equals("arrives");
    }

    private boolean isLeavesState(Customer c) {
        return c.getCustomerStatus().equals("leaves");
    }

    private boolean isServiceState(Customer c) {
        return c.getCustomerStatus().equals("service");
    }

    private boolean isDeliveryState(Customer c) {
        return c.getCustomerStatus().equals("delivery");
    }

    private boolean isWaitsState(Customer c) {
        return c.getCustomerStatus().equals("waits");
    }

    private boolean isDoneState(Customer c) {
        return c.getCustomerStatus().equals("done");
    }

    private boolean isServedState(Customer c) {
        return c.getCustomerStatus().equals("served");
    }

    private void registerEvent(Customer c) {
        String log = c.toString();
        if (!isArrivesState(c) && !isLeavesState(c) && c.getMechanicID() > 0) {
            log += "" + this.myMechanics[c.getMechanicID() - 1];
        } else {
            log += "" + " no mechanic";
        }
        this.logs.add(log);
    }

    private boolean isTerminalState(Customer c) {
        return isDoneState(c) || isLeavesState(c);
    }

    private Customer changeCustomerState(Customer c) {
        Customer decided = null;
        if (isArrivesState(c)) {
            return handleArrivalState(c);
        } else {
            if (isServedState(c)) { // served --> done, server depends on what kind:
                double servedTime = this.getServedTime(c.getPresentTime());
                decided = c.fromServedToService(servedTime);
                Mechanic m = this.myMechanics[c.getMechanicID() - 1];
                Mechanic newMechanic = m.actuallyDoTask(decided.getPresentTime());
                this.myMechanics[m.employeeID - 1] = newMechanic;
            }
            if (isServiceState(c)) {
                double serviceTime = 0;
                if (c.getServiceType() == "repair") {
                    serviceTime = this.getRepairTime(c.getPresentTime());
                } else if (c.getServiceType() == "checkup") {
                    serviceTime = this.getCheckupTime(c.getPresentTime());
                } else {
                    serviceTime = this.getCarwashTime(c.getPresentTime());
                }
                decided = c.fromServiceToDelivery(serviceTime);
                Mechanic m = this.myMechanics[c.getMechanicID() - 1];
                Mechanic newMechanic = m.actuallyDoTask(decided.getPresentTime());
                this.myMechanics[m.employeeID - 1] = newMechanic;
            }
            if (isDeliveryState(c)) {
                double deliveryTime = this.getDeliveryTime(c.getPresentTime());
                decided = c.fromDeliveryToDone(deliveryTime);
                Mechanic m = this.myMechanics[c.getMechanicID() - 1];
                Mechanic newMechanic = m.actuallyDoTask(decided.getPresentTime());
                this.myMechanics[m.employeeID - 1] = newMechanic;
            }
            if (isWaitsState(c)) {
                if (c.getMechanicID() > 0) {
                    Mechanic assignedMechanic = this.myMechanics[c.getMechanicID() - 1];
                    if (assignedMechanic.isIdle
                            && c.getCustomerId() == assignedMechanic.waitingQueue.peek().getCustomerId()) {

                        assignedMechanic.waitingQueue.remove();
                        assignedMechanic = assignedMechanic.waitServing();
                        updateMechanicArray(assignedMechanic.addToWaitQueue(assignedMechanic.waitingQueue));
                        decided = c.fromWaitsToServed(assignedMechanic.nextAvailableTime);
                    } else {
                        HashMap<Double, Mechanic> nextAvailable = new HashMap<>();
                        for (Mechanic m : myMechanics) {
                            if (m.isIdle) {
                                nextAvailable.put(m.nextAvailableTime, m);
                            }
                        }
                        TreeMap<Double, Mechanic> sorted = new TreeMap<>(nextAvailable);
                        if (sorted.size() > 0) {
                            decided = c.fromWaitsToWaits(sorted.get(sorted.firstKey()).nextAvailableTime,
                                    sorted.get(sorted.firstKey()).employeeID);
                            LinkedList<Customer> newQueue = new LinkedList<>(
                                    sorted.get(sorted.firstKey()).waitingQueue);
                            newQueue.addLast(decided);
                            updateMechanicArray(sorted.get(sorted.firstKey()).addToWaitQueue(newQueue));
                        } else {
                            decided = c.fromWaitsToWaits(c.getPresentTime(), 0);
                        }
                    }
                } else {
                    HashMap<Double, Mechanic> nextAvailable = new HashMap<>();
                    for (Mechanic m : myMechanics) {
                        if (m.isIdle) {
                            nextAvailable.put(m.nextAvailableTime, m);
                        }
                    }
                    TreeMap<Double, Mechanic> sorted = new TreeMap<>(nextAvailable);
                    if (sorted.size() > 0) {
                        decided = c.fromWaitsToWaits(sorted.get(sorted.firstKey()).nextAvailableTime,
                                sorted.get(sorted.firstKey()).employeeID);
                        LinkedList<Customer> newQueue = new LinkedList<>(sorted.get(sorted.firstKey()).waitingQueue);
                        newQueue.add(decided);
                        updateMechanicArray(sorted.get(sorted.firstKey()).addToWaitQueue(newQueue));
                    } else {
                        decided = c.fromWaitsToWaits(c.getPresentTime(), 0);
                    }
                }
            }
            return decided;
        }
    }

    private Customer handleArrivalState(Customer c) {
        Mechanic[] queriedMechanics = queryMechanics(c);
        Customer changedCustomer; // to be assigned based on query results
        if (queriedMechanics[0] != null) { // idleMechanic exists:
            Mechanic m = queriedMechanics[0];
            changedCustomer = c.fromArrivesToServed(m.employeeID);
            updateMechanicArray(m.serveUponArrival());
            return changedCustomer;
        } else if (queriedMechanics[1] != null) { // queueableServer exists, need to queue:
            Mechanic queueableMechanic = queriedMechanics[1];
            Mechanic shortestMechanic = queriedMechanics[2];
            // if customer greedy, shall take the shortest server that exists:
            changedCustomer = (shortestMechanic != null)
                    ? c.fromArrivesToWaits(shortestMechanic.nextAvailableTime,
                            shortestMechanic.employeeID)
                    : c.fromArrivesToWaits(queueableMechanic.nextAvailableTime,
                            queueableMechanic.employeeID);
            if (shortestMechanic != null) {
                queueableMechanic = shortestMechanic;
            }
            LinkedList<Customer> newQueue = new LinkedList<>(queueableMechanic.waitingQueue);
            newQueue.add(changedCustomer);
            updateMechanicArray(queueableMechanic.addToWaitQueue(newQueue));
            return changedCustomer;
        } else { // create terminal state of leaving, server needn't bother:
            changedCustomer = c.fromArrivesToWaits(c.getPresentTime(), 0);
        }
        return changedCustomer;
    }

    private Mechanic[] queryMechanics(Customer c) {
        Optional<? extends Mechanic> idleMechanic = Optional.empty(),
                queueableMechanic = Optional.empty(),
                shortestMechanic = Optional.empty();
        boolean foundIdle = false, foundQueueable = false;
        for (Mechanic m : this.myMechanics) {
            double now = c.getPresentTime();

            if (!foundIdle && m.isIdle(now)) {
                foundIdle = true;
                idleMechanic = Optional.of(m);
            }
            if (!foundIdle && !foundQueueable && m.canQueue(now)) {
                foundQueueable = true;
                queueableMechanic = Optional.of(m);
                shortestMechanic = Optional.of(m); // to init first
            }
            if (!foundIdle && foundQueueable && m.canQueue(now)) { // try looking for shortest
                if (m.getQueueSize() < shortestMechanic.get().getQueueSize()) {
                    shortestMechanic = Optional.of(m);
                }
            }
        }
        return new Mechanic[] { idleMechanic.orElse(null),
                queueableMechanic.orElse(null),
                shortestMechanic.orElse(null) };
        // # todo: question: it said that generic array creation is not allowed why ah?
        // **important**
    }

    public String showLogs() {
        StringBuilder res = new StringBuilder();
        for (String s : logs) {
            res.append(s).append("\n");
        }
        res.append(Customer.customerStats());
        return res.toString();
    }
}
