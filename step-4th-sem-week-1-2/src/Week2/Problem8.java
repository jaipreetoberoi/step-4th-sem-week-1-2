package Week2;

import java.util.*;

class ParkingSpot {
    String licensePlate;
    long entryTime;
    boolean occupied;

    ParkingSpot() {
        this.occupied = false;
    }
}

public class Problem8 {

    private ParkingSpot[] table;
    private int capacity;
    private int size = 0;
    private int totalProbes = 0;
    private HashMap<Integer, Integer> hourlyCount = new HashMap<>();

    public Problem8(int capacity) {
        this.capacity = capacity;
        table = new ParkingSpot[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

    public void parkVehicle(String licensePlate) {
        int index = hash(licensePlate);
        int probes = 0;

        while (table[index].occupied) {
            index = (index + 1) % capacity;
            probes++;
        }

        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        size++;
        totalProbes += probes;

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        hourlyCount.put(hour, hourlyCount.getOrDefault(hour, 0) + 1);

        System.out.println("Assigned spot #" + index + " (" + probes + " probes)");
    }

    public void exitVehicle(String licensePlate) {
        int index = hash(licensePlate);

        while (table[index].occupied) {
            if (licensePlate.equals(table[index].licensePlate)) {

                long durationMillis = System.currentTimeMillis() - table[index].entryTime;
                double hours = durationMillis / (1000.0 * 60 * 60);
                double fee = hours * 5;

                table[index].occupied = false;
                table[index].licensePlate = null;

                size--;

                System.out.println("Spot #" + index + " freed");
                System.out.println("Duration: " + String.format("%.2f", hours) + " hours");
                System.out.println("Fee: $" + String.format("%.2f", fee));
                return;
            }

            index = (index + 1) % capacity;
        }

        System.out.println("Vehicle not found.");
    }

    public void getStatistics() {

        double occupancy = (size * 100.0) / capacity;
        double avgProbes = size == 0 ? 0 : (double) totalProbes / size;

        int peakHour = -1;
        int max = 0;

        for (Map.Entry<Integer, Integer> entry : hourlyCount.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                peakHour = entry.getKey();
            }
        }

        System.out.println("Occupancy: " + String.format("%.2f", occupancy) + "%");
        System.out.println("Avg Probes: " + String.format("%.2f", avgProbes));
        System.out.println("Peak Hour: " + peakHour + ":00 - " + (peakHour + 1) + ":00");
    }

    public static void main(String[] args) {

        Problem8 lot = new Problem8(500);

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}