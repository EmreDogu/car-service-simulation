package simulator;

import java.util.*;

import employee.Manager;
public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> parameters = new ArrayList<>();
        
    Scanner sc = new Scanner(System.in);

    System.out.print("Enter number of mechanics to be created: ");
    int numMechanic = sc.nextInt();

    System.out.print("Enter number of customers to be created: ");
    int numCustomer = sc.nextInt();

    System.out.println();
    System.out.println("There are 3 arrival types (repair, checkup, carwash)");
    System.out.println();

    while (true) {
    System.out.print("Enter number of that will go to repair (enter a value smaller or eqaul to " + numCustomer + "): ");
    while (true) {
        int numRepair = sc.nextInt();
        if (numRepair>=0 && numRepair<=numCustomer) {
            for (int i = 0; i<numRepair; i++) {
                parameters.add(1);
            }
            break;
        }else {
            System.out.print("Please try again: ");
        }
    }

    System.out.print("Enter number of that will go to check-up (enter a value smaller or eqaul to " + (numCustomer-parameters.size()) + "): ");
    while (true) {
        int numCheckup = sc.nextInt();
        if (numCheckup>=0 && numCheckup<=numCustomer-parameters.size()) {
            for (int i = 0; i<numCheckup; i++) {
                parameters.add(2);
            }
            break;
        }else {
            System.out.print("Please try again: ");
        }
    }

    System.out.print("Enter number of that will go to car wash (enter a value smaller or eqaul to " + (numCustomer-parameters.size()) + "): ");
    while (true) {
        int numCarwash = sc.nextInt();
        if (numCarwash>=0 && numCarwash<= numCustomer-parameters.size()) {
            for (int i = 0; i<numCarwash; i++) {
                parameters.add(3);
            }
            break;
        }else {
            System.out.print("Please try again: ");
        }
    }
    if (parameters.size()==numCustomer) {
        break;
    }else {
        System.out.println("Try to fit entered parameters");
        parameters.clear();
    }
}

    Collections.shuffle(parameters);

    Manager myManager = new Manager(numMechanic, numCustomer, parameters);

    sc.close();
    myManager.operate();
    System.out.println(myManager.showLogs());
    }
}