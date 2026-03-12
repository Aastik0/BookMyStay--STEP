import java.util.*;

/**
 * Book My Stay Application
 *
 * Use Case 8: Booking History & Reporting
 * Demonstrates storing confirmed bookings and generating reports.
 *
 * @author Aastik
 * @version 8.0
 */

/* ---------- Reservation Model ---------- */

class Reservation {

    String reservationId;
    String guestName;
    String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public void display() {
        System.out.println(
                "Reservation ID: " + reservationId +
                        " | Guest: " + guestName +
                        " | Room Type: " + roomType
        );
    }
}

/* ---------- Booking History ---------- */

class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    // store confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Reservation stored in booking history.");
    }

    // retrieve history
    public List<Reservation> getHistory() {
        return history;
    }
}

/* ---------- Reporting Service ---------- */

class BookingReportService {

    public void generateReport(List<Reservation> reservations) {

        System.out.println("\n===== Booking History Report =====");

        for (Reservation r : reservations) {
            r.display();
        }

        System.out.println("Total Reservations: " + reservations.size());
    }
}

/* ---------- Application Entry ---------- */

public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println(" Welcome to Book My Stay Application ");
        System.out.println(" Hotel Booking System v8.0 ");
        System.out.println("=====================================");

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // simulate confirmed reservations
        history.addReservation(new Reservation("RES101", "Aastik", "Single Room"));
        history.addReservation(new Reservation("RES102", "Rahul", "Double Room"));
        history.addReservation(new Reservation("RES103", "Priya", "Suite Room"));

        // admin requests report
        reportService.generateReport(history.getHistory());
    }
}