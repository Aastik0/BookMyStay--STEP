import java.util.*;

/**
 * Book My Stay Application
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 * Demonstrates safe room allocation and prevention of double-booking.
 *
 * @author Aastik
 * @version 6.0
 */

/* ---------- RESERVATION ---------- */

class Reservation {

    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

/* ---------- INVENTORY ---------- */

class RoomInventory {

    private HashMap<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

/* ---------- BOOKING SERVICE ---------- */

class BookingService {

    private Queue<Reservation> bookingQueue;
    private RoomInventory inventory;

    // Track allocated room IDs
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type → assigned rooms
    private HashMap<String, Set<String>> roomAllocations = new HashMap<>();

    public BookingService(Queue<Reservation> bookingQueue, RoomInventory inventory) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }

    public void processBookings() {

        while (!bookingQueue.isEmpty()) {

            Reservation reservation = bookingQueue.poll();

            System.out.println("\nProcessing booking for " + reservation.guestName);

            int available = inventory.getAvailability(reservation.roomType);

            if (available > 0) {

                String roomId = generateRoomId(reservation.roomType);

                allocatedRoomIds.add(roomId);

                roomAllocations
                        .computeIfAbsent(reservation.roomType, k -> new HashSet<>())
                        .add(roomId);

                inventory.decrementRoom(reservation.roomType);

                System.out.println("Reservation Confirmed!");
                System.out.println("Guest: " + reservation.guestName);
                System.out.println("Room Type: " + reservation.roomType);
                System.out.println("Assigned Room ID: " + roomId);

            } else {

                System.out.println("Reservation Failed: No available " + reservation.roomType);
            }
        }
    }

    private String generateRoomId(String roomType) {

        String prefix = roomType.substring(0, 2).toUpperCase();

        String roomId;

        do {
            roomId = prefix + (100 + new Random().nextInt(900));
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }
}

/* ---------- APPLICATION ENTRY ---------- */

public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println(" Welcome to Book My Stay Application ");
        System.out.println(" Hotel Booking System v6.0 ");
        System.out.println("=====================================");

        RoomInventory inventory = new RoomInventory();

        Queue<Reservation> bookingQueue = new LinkedList<>();

        // Guest requests
        bookingQueue.add(new Reservation("Aastik", "Single Room"));
        bookingQueue.add(new Reservation("Rahul", "Double Room"));
        bookingQueue.add(new Reservation("Priya", "Suite Room"));
        bookingQueue.add(new Reservation("Anita", "Single Room"));

        BookingService service = new BookingService(bookingQueue, inventory);

        service.processBookings();

        inventory.displayInventory();
    }
}