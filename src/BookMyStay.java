import java.util.*;

/**
 * Book My Stay Application
 *
 * Use Case 9: Error Handling & Validation
 * Demonstrates validation and custom exception handling
 * to maintain system reliability.
 *
 * @author Aastik
 * @version 9.0
 */

/* ---------- Custom Exception ---------- */

class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}

/* ---------- Reservation Model ---------- */

class Reservation {

    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

/* ---------- Inventory Service ---------- */

class InventoryService {

    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public void validateRoomType(String roomType) throws InvalidBookingException {

        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    public void checkAvailability(String roomType) throws InvalidBookingException {

        int available = inventory.get(roomType);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }
    }

    public void allocateRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {

        System.out.println("\nCurrent Inventory:");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

/* ---------- Booking Validator ---------- */

class BookingValidator {

    private InventoryService inventory;

    public BookingValidator(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation reservation) {

        try {

            inventory.validateRoomType(reservation.roomType);
            inventory.checkAvailability(reservation.roomType);

            inventory.allocateRoom(reservation.roomType);

            System.out.println("Booking Confirmed for " + reservation.guestName +
                    " (" + reservation.roomType + ")");

        } catch (InvalidBookingException e) {

            System.out.println("Booking Failed: " + e.getMessage());
        }
    }
}

/* ---------- Application Entry ---------- */

public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println(" Welcome to Book My Stay Application ");
        System.out.println(" Hotel Booking System v9.0 ");
        System.out.println("=====================================");

        InventoryService inventory = new InventoryService();

        BookingValidator validator = new BookingValidator(inventory);

        // Valid booking
        validator.processBooking(new Reservation("Aastik", "Single Room"));

        // Invalid room type
        validator.processBooking(new Reservation("Rahul", "Luxury Room"));

        // Booking when no rooms available
        validator.processBooking(new Reservation("Priya", "Double Room"));
        validator.processBooking(new Reservation("Anita", "Double Room"));

        inventory.displayInventory();
    }
}