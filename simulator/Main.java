package simulator;

import java.util.*;

import employee.Manager;
public class Main {
    public static void main(String[] args) {
        
    Scanner sc = new Scanner(System.in);

    System.out.println("Enter number of mechanics to be created: ");
    int numMechanic = sc.nextInt();

    System.out.println("Enter number of customers to be created: ");
    int numCustomer = sc.nextInt();

    Manager myManager = new Manager(numMechanic, numCustomer);

    sc.close();
    myManager.operate();
    System.out.println(myManager.showLogs());
    }
}