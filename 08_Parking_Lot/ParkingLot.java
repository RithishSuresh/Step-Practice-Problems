package parkinglot;

/**
 * Problem 8: Parking Lot System
 * Builds a custom Hash Table from scratch using Open Addressing / Linear Probing.
 */
public class ParkingLot {

    /**
     * Internal Structure representing the Data Value in the Hash Table.
     */
    private static class ParkingSpot {
        String licensePlate;
        long entryTime;
        boolean isTombstone; // Used to mark a spot as "Deleted" safely

        public ParkingSpot(String licensePlate) {
            this.licensePlate = licensePlate;
            this.entryTime = System.currentTimeMillis();
            this.isTombstone = false;
        }

        // Special Tombstone Marker
        public ParkingSpot() {
            this.isTombstone = true;
        }
    }

    private final ParkingSpot[] spots;
    private final int capacity;
    private int currentSize;

    // A static tombstone object to avoid creating multiple instances
    private final ParkingSpot TOMBSTONE = new ParkingSpot();

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        this.spots = new ParkingSpot[capacity];
        this.currentSize = 0;
    }

    /**
     * Our core Hash Function: Converts a String to an Integer Array Index [0 to Capacity-1]
     */
    private int hashFunction(String licensePlate) {
        int hash = 0;
        for (char c : licensePlate.toCharArray()) {
            hash = (hash * 31 + c) % capacity;
        }
        return Math.abs(hash);
    }

    /**
     * Parks a car. Demonstrates `PUT` with Linear Probing Collision Handling.
     */
    public boolean parkCar(String licensePlate) {
        if (currentSize >= capacity) {
            System.out.println("[ERROR] Lot is completely full. Cannot park " + licensePlate);
            return false;
        }

        int index = hashFunction(licensePlate);
        int originalIndex = index;

        // Linear Probing: While spot is occupied AND not a tombstone
        while (spots[index] != null && !spots[index].isTombstone) {
            
            // Allow idempotency (don't park the exact same car twice)
            if (spots[index].licensePlate.equals(licensePlate)) {
                System.out.println("[WARN] Car " + licensePlate + " is already parked at spot " + index);
                return false;
            }

            // Move to the next spot, wrapping around using Modulo
            index = (index + 1) % capacity;
        }

        spots[index] = new ParkingSpot(licensePlate);
        currentSize++;
        
        if (index == originalIndex) {
            System.out.println("[PARK] " + licensePlate + " parked successfully at spot " + index);
        } else {
            System.out.println("[PARK-COLLISION] " + licensePlate + " collided! Probed and parked at spot " + index);
        }
        
        return true;
    }

    /**
     * Removes a car. Demonstrates `DELETE` with Linear Probing Search.
     */
    public boolean exitLot(String licensePlate) {
        int index = hashFunction(licensePlate);
        int originalIndex = index;

        // Linear Probing Search: Stop if we hit true NULL (meaning chain gracefully ended)
        while (spots[index] != null) {
            
            // Found the car!
            if (!spots[index].isTombstone && spots[index].licensePlate.equals(licensePlate)) {
                
                long duration = System.currentTimeMillis() - spots[index].entryTime;
                
                // Mark as Tombstone instead of Null
                spots[index] = TOMBSTONE; 
                currentSize--;
                
                System.out.println("[EXIT] " + licensePlate + " found at spot " + index 
                                    + " and exited. Stayed for " + duration + "ms.");
                return true;
            }

            index = (index + 1) % capacity;
            
            // Broken loop check (searched entire array and came back to start)
            if (index == originalIndex) {
                break;
            }
        }

        System.out.println("[ERROR] " + licensePlate + " not found in the parking lot.");
        return false;
    }


    public static void main(String[] args) throws InterruptedException {
        // Build a tiny parking lot to easily force artificial Hash Collisions
        ParkingLot lot = new ParkingLot(5);

        // Park cars
        lot.parkCar("ABC-111");
        lot.parkCar("XYZ-999");
        lot.parkCar("JKL-555"); // Assume this collides with ABC-111
        lot.parkCar("MNO-333");

        System.out.println("...time passes...");
        Thread.sleep(1500);

        // XYZ-999 leaves. Its spot becomes a Tombstone.
        lot.exitLot("XYZ-999");

        // Assume DEF-222 hashes to the same spot XYZ-999 just left. 
        // Thanks to Tombstone/Open Addressing it should park fine.
        lot.parkCar("DEF-222");

        // Validating search algorithm works PAST a tombstone
        lot.exitLot("JKL-555");
    }
}
