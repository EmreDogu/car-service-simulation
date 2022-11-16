package employee;

import java.util.*;

import customer.*;

public class Mechanic {
    protected final int employeeID;
    protected final boolean isIdle;
    protected final double nextAvailableTime;
    protected final LinkedList<Customer> waitingQueue;

    public Mechanic(int employeeID) {
        this.employeeID = employeeID;
        this.isIdle = true;
        this.nextAvailableTime = 0;
        this.waitingQueue = new LinkedList<>();
    }

    public Mechanic(int employeeID, boolean isIdle, double nextAvailableTime,
    LinkedList<Customer> waitingQueue) {
        this.employeeID = employeeID;
        this.isIdle = isIdle;
        this.nextAvailableTime = nextAvailableTime;
        this.waitingQueue = waitingQueue;
    }

    public boolean isIdle() {
        return isIdle;
    }

    public double getNextAvailableTime() {
        return nextAvailableTime;
    }

    public Queue<Customer> getWaitingQueue() {
        return waitingQueue;
    }

    protected Mechanic serveUponArrival() {
        assert this.waitingQueue.isEmpty();
        return new Mechanic(this.employeeID, false,
            this.nextAvailableTime,
            this.waitingQueue);
    }

    protected Mechanic addToWaitQueue(LinkedList<Customer> newQueue) {
        return new Mechanic(this.employeeID, this.isIdle,
            this.nextAvailableTime, newQueue);
    }

    protected boolean canQueue(double arrivalTime) {
        return arrivalTime < this.nextAvailableTime
                && this.waitingQueue.size() < 1;
    }

    protected int getQueueSize() {
        return this.waitingQueue.size();
    }

    protected boolean isIdle(double arrivalTime) {
        return this.isIdle && arrivalTime >= this.nextAvailableTime;
    }

    protected Mechanic doneServing() {
        return new Mechanic(this.employeeID,
                true,
                this.nextAvailableTime,
                this.waitingQueue);
    }

    protected Mechanic waitServing() {
        return new Mechanic(this.employeeID,
        false,
        this.nextAvailableTime,
        this.waitingQueue);
    }

    protected Mechanic actuallyDoTask(double presentTime) {
        return new Mechanic(this.employeeID, false,
                presentTime, this.waitingQueue);
    }

    @Override
    public String toString() {
        return "mechanic " + this.employeeID;
    }
}
