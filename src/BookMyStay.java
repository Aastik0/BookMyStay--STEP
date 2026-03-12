import java.util.*;

/**
 * Book My Stay Application
 *
 * Use Case 10: Booking Cancellation & Inventory Rollback
 * Demonstrates safe cancellation and system state recovery.
 *
 * @author Aastik
 * @version 10.0
 */

/* ---------- Reservation Model ---------- */

class Reservation {

    String reservationId;
    String roomType;

    public Reservation(String reservationId, String roomType) {
        this.reservationId = reservationId;
        this.roomType = roomType;
    }
}

/* ---------- Inventory Service ---------- */

class InventoryService {

    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void displayInventory() {

        System.out.println("\nCurrent Inventory:");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

/* ---------- Booking History ---------- */

class BookingHistory {

    private Map<String, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation reservation) {
        reservations.put(reservation.reservationId, reservation);
    }

    public Reservation getReservation(String reservationId) {
        return reservations.get(reservationId);
    }

    public void removeReservation(String reservationId) {
        reservations.remove(reservationId);
    }

    public boolean exists(String reservationId) {
        return reservations.containsKey(reservationId);
    }
}

/* ---------- Cancellation Service ---------- */

class CancellationService {

    private InventoryService inventory;
    private BookingHistory history;

    // stack to track released room IDs
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(InventoryService inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {

        if (!history.exists(reservationId)) {

            System.out.println("Cancellation Failed: Reservation not found.");
            return;
        }

        Reservation reservation = history.getReservation(reservationId);

        rollbackStack.push(reservationId);

        inventory.increment(reservation.roomType);

        history.removeReservation(reservationId);

        System.out.println("Reservation " + reservationId + " cancelled successfully.");
    }

    public void showRollbackHistory() {

        System.out.println("\nRollback Stack:");

        for (String id : rollbackStack) {
            System.out.println(id);
        }
    }
}

/* ---------- Application Entry ---------- */

public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println(" Welcome to Book My Stay Application ");
        System.out.println(" Hotel Booking System v10.0 ");
        System.out.println("=====================================");

        InventoryService inventory = new InventoryService();
        BookingHistory history = new BookingHistory();

        // simulate confirmed bookings
        Reservation r1 = new Reservation("RES101", "Single Room");
        Reservation r2 = new Reservation("RES102", "Double Room");

        history.addReservation(r1);
        history.addReservation(r2);

        inventory.decrement("Single Room");
        inventory.decrement("Double Room");

        CancellationService cancellation = new CancellationService(inventory, history);

        // cancel booking
        cancellation.cancelBooking("RES101");

        // invalid cancellation
        cancellation.cancelBooking("RES999");

        cancellation.showRollbackHistory();

        inventory.displayInventory();
    }
}