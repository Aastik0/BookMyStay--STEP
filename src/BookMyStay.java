import java.util.HashMap;
import java.util.Map;

/**
 * Book My Stay Application
 *
 * Use Case 4: Room Search & Availability Check
 * Demonstrates read-only access to centralized inventory
 * without modifying system state.
 *
 * @author Aastik
 * @version 4.0
 */

/* ---------- DOMAIN MODEL ---------- */

abstract class Room {

    protected String roomType;
    protected int beds;
    protected double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price per night: ₹" + price);
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 2000);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 3500);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 6000);
    }
}

/* ---------- INVENTORY ---------- */

class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // Example unavailable room
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }
}

/* ---------- SEARCH SERVICE ---------- */

class SearchService {

    public void searchAvailableRooms(RoomInventory inventory) {

        System.out.println("\nAvailable Rooms:\n");

        for (Map.Entry<String, Integer> entry : inventory.getInventory().entrySet()) {

            String roomType = entry.getKey();
            int available = entry.getValue();

            // Defensive check: show only available rooms
            if (available > 0) {

                Room room = null;

                if (roomType.equals("Single Room"))
                    room = new SingleRoom();
                else if (roomType.equals("Double Room"))
                    room = new DoubleRoom();
                else if (roomType.equals("Suite Room"))
                    room = new SuiteRoom();

                if (room != null) {
                    room.displayDetails();
                    System.out.println("Available Rooms: " + available);
                    System.out.println("-------------------------------");
                }
            }
        }
    }
}

/* ---------- APPLICATION ENTRY ---------- */

public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println(" Welcome to Book My Stay Application ");
        System.out.println(" Hotel Booking System v4.0 ");
        System.out.println("=====================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize search service
        SearchService search = new SearchService();

        // Guest searches rooms
        search.searchAvailableRooms(inventory);
    }
}