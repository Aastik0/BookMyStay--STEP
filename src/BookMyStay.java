import java.util.*;

/**
 * Book My Stay Application
 *
 * Use Case 7: Add-On Service Selection
 * Demonstrates attaching optional services to reservations.
 *
 * @author Aastik
 * @version 7.0
 */

/* ---------- Service Model ---------- */

class AddOnService {

    String serviceName;
    double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }
}

/* ---------- Add-On Service Manager ---------- */

class AddOnServiceManager {

    // reservationId -> list of services
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // add service to reservation
    public void addService(String reservationId, AddOnService service) {

        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println(service.serviceName + " added to reservation " + reservationId);
    }

    // calculate total add-on cost
    public double calculateTotalCost(String reservationId) {

        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());

        double total = 0;

        for (AddOnService s : services) {
            total += s.cost;
        }

        return total;
    }

    // display services
    public void displayServices(String reservationId) {

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null) {
            System.out.println("No services selected.");
            return;
        }

        System.out.println("\nServices for Reservation: " + reservationId);

        for (AddOnService s : services) {
            System.out.println("- " + s.serviceName + " : ₹" + s.cost);
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

/* ---------- Application Entry ---------- */

public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println(" Welcome to Book My Stay Application ");
        System.out.println(" Hotel Booking System v7.0 ");
        System.out.println("=====================================");

        String reservationId = "RES101";

        AddOnServiceManager manager = new AddOnServiceManager();

        // Guest selects services
        manager.addService(reservationId, new AddOnService("Breakfast", 500));
        manager.addService(reservationId, new AddOnService("Airport Pickup", 1200));
        manager.addService(reservationId, new AddOnService("Spa Access", 1500));

        // Display selected services
        manager.displayServices(reservationId);
    }
}