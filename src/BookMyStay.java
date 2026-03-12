import java.io.*;
import java.util.*;

/**
 * Book My Stay Application
 *
 * Use Case 12: Data Persistence & System Recovery
 * Demonstrates saving and restoring system state using serialization.
 *
 * @author Aastik
 * @version 12.0
 */

/* ---------- Reservation Model ---------- */

class Reservation implements Serializable {

    String reservationId;
    String guestName;
    String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

/* ---------- System State ---------- */

class SystemState implements Serializable {

    Map<String, Integer> inventory;
    List<Reservation> bookings;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

/* ---------- Persistence Service ---------- */

class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save system state
    public void saveState(SystemState state) {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(state);

            System.out.println("System state saved successfully.");

        } catch (IOException e) {

            System.out.println("Error saving system state.");
        }
    }

    // Load system state
    public SystemState loadState() {

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("System state restored from file.");

            return (SystemState) in.readObject();

        } catch (Exception e) {

            System.out.println("No saved state found. Starting fresh.");

            return null;
        }
    }
}

/* ---------- Application Entry ---------- */

public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println(" Welcome to Book My Stay Application ");
        System.out.println(" Hotel Booking System v12.0 ");
        System.out.println("=====================================");

        PersistenceService persistence = new PersistenceService();

        SystemState state = persistence.loadState();

        Map<String, Integer> inventory;
        List<Reservation> bookings;

        if (state == null) {

            inventory = new HashMap<>();
            inventory.put("Single Room", 2);
            inventory.put("Double Room", 1);
            inventory.put("Suite Room", 1);

            bookings = new ArrayList<>();

        } else {

            inventory = state.inventory;
            bookings = state.bookings;
        }

        // simulate booking
        Reservation r1 = new Reservation("RES201", "Aastik", "Single Room");
        bookings.add(r1);

        inventory.put("Single Room", inventory.get("Single Room") - 1);

        System.out.println("\nCurrent Bookings:");

        for (Reservation r : bookings) {
            System.out.println(r);
        }

        System.out.println("\nCurrent Inventory:");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }

        // Save state before shutdown
        persistence.saveState(new SystemState(inventory, bookings));
    }
}