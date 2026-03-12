import java.util.*;

/**
 * Book My Stay Application
 *
 * Use Case 11: Concurrent Booking Simulation
 * Demonstrates thread safety using synchronized methods.
 *
 * @author Aastik
 * @version 11.0
 */

/* ---------- Reservation ---------- */

class Reservation {

    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

/* ---------- Shared Inventory ---------- */

class InventoryService {

    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    // critical section
    public synchronized void allocateRoom(Reservation reservation) {

        String roomType = reservation.roomType;

        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {

            inventory.put(roomType, available - 1);

            System.out.println(Thread.currentThread().getName() +
                    " → Booking confirmed for " +
                    reservation.guestName +
                    " (" + roomType + ")");

        } else {

            System.out.println(Thread.currentThread().getName() +
                    " → Booking failed for " +
                    reservation.guestName +
                    " (" + roomType + ")");
        }
    }

    public void displayInventory() {

        System.out.println("\nFinal Inventory:");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

/* ---------- Booking Processor Thread ---------- */

class BookingProcessor extends Thread {

    private Queue<Reservation> queue;
    private InventoryService inventory;

    public BookingProcessor(Queue<Reservation> queue, InventoryService inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {

        while (true) {

            Reservation reservation;

            synchronized (queue) {

                if (queue.isEmpty()) {
                    break;
                }

                reservation = queue.poll();
            }

            inventory.allocateRoom(reservation);
        }
    }
}

/* ---------- Application Entry ---------- */

public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println(" Welcome to Book My Stay Application ");
        System.out.println(" Hotel Booking System v11.0 ");
        System.out.println("=====================================");

        Queue<Reservation> bookingQueue = new LinkedList<>();

        // simulate multiple guest requests
        bookingQueue.add(new Reservation("Aastik", "Single Room"));
        bookingQueue.add(new Reservation("Rahul", "Single Room"));
        bookingQueue.add(new Reservation("Priya", "Single Room"));
        bookingQueue.add(new Reservation("Anita", "Double Room"));

        InventoryService inventory = new InventoryService();

        // multiple threads
        BookingProcessor t1 = new BookingProcessor(bookingQueue, inventory);
        BookingProcessor t2 = new BookingProcessor(bookingQueue, inventory);
        BookingProcessor t3 = new BookingProcessor(bookingQueue, inventory);

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.displayInventory();
    }
}