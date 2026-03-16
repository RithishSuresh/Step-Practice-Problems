# Problem 8: Parking Lot System (Open Addressing Collision Handling)

## 1. Problem Understanding
- **Scenario:** A parking lot assigns parking spaces to cars based on their License Plate. Internally, the parking lot acts exactly like a Hash Table. When a car attempts to park in a space that is already taken by another car (a Hash Collision), it simply drives to the very next available space. This concept is called **Open Addressing with Linear Probing**.
- **Inputs:** License Plate String (Park/Exit).
- **Outputs:** Assigned Parking Spot ID, Parking Duration/Fee calculation.
- **Constraints:** Must natively implement a Hash Table using Arrays without utilizing Java's built-in `HashMap`. Must resolve collisions manually. 

## 2. Data Structures Used
- **Custom Hash Table (Array):** We construct our own Hash Table using a standard fixed-size Java Array: `ParkingSpot[] spots`. 
- **Tombstones:** In Open Addressing, removing an item breaks the searching chain for other items that collided and shifted past it. We use a special "Tombstone" object to mark a spot as "Deleted" rather than completely "Null".

## 3. System Design Approach
1. **Hash Function:** Converts a License Plate string into an array index. For example, summing the ASCII values of the characters and using modulo division: `sum % CAPACITY`.
2. **Parking a Car (Insertion + Linear Probing):**
   - Calculate the ideal index using the hash function.
   - If that index is `null` (Empty) or `deleted` (Tombstone), park the car there and set `entryTime`.
   - If that index is already occupied, a collision occurred. Increment the index by $+1$ iteratively (`(index + 1) % CAPACITY`) until an empty spot is found.
3. **Exiting the Lot (Search + Deletion):**
   - Calculate the ideal index for the license plate.
   - Check the car at that index. If it's a match, calculate the duration stayed (Current Time - `entryTime`).
   - If it's not a match, linear probe ($+1$) until the car is found or a `null` spot is reached (which means the car was never here).
   - Once found, mark the array index as a Tombstone so future searches don't break.

## 4. Java Implementation
See `ParkingLot.java` for a complete, from-scratch implementation of an Open Addressed Hash Table managing parking spots.

## 5. Time and Space Complexity
- **Time Complexity:**
  - **Best Case:** $O(1)$ when no hash collisions occur.
  - **Worst Case:** $O(C)$ where $C$ is the Capacity of the parking lot. If the lot is nearly full, linear probing requires shifting through almost the entire array to find an empty spot.
- **Space Complexity:**
  - $O(C)$, where $C$ is the static capacity of the Parking Lot array.

## 6. Example Execution
**Input:**
Lot Capacity: 5
- Park "CAR1" (Ideal Index: 2) -> Parks in Spot 2
- Park "CAR2" (Ideal Index: 2) -> Collision! Linear probes to Spot 3
- Exit "CAR1" -> Spot 2 becomes Tombstone
- Exit "CAR2" -> Ideal index 2 is Tombstone. Probe +1 to find "CAR2" in Spot 3.

**Output:**
```
[PARK] CAR-123 stored efficiently at spot 3
[PARK] CAR-456 collided at 3! Probing... stored at spot 4
[EXIT] CAR-123 exited from spot 3. Stayed 2000ms. Spot marked as Tombstone.
[EXIT] Searching for CAR-456... collided at Tombstone 3, found at spot 4.
```

## 7. Possible Improvements (Real-world systems)
- **Quadratic Probing & Double Hashing:** Linear probing suffers from "Primary Clustering"—cars bunch up side-by-side, causing massive performance drops. Real Hash Tables use Quadratic Probing ($i^2$ jumps) or Double Hashing ($Hash_2(X)$ jumps) to spread collisions evenly across the array.
- **Load Factor and Resizing:** Real open-addressed tables automatically allocate a larger array double the size when the system reaches 70% capacity (Load Factor of 0.70) and re-hashes every car into the new array to maintain $O(1)$ performance.
