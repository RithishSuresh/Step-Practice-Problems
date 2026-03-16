package multilevelcache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Problem 10: Multi-Level Cache System
 * Demonstrates intelligent layered architecture and recursive eviction using LinkedHashMap.
 */
public class MultiLevelCache {

    // Cache Maps
    private final Map<String, String> l1Cache;
    private final Map<String, String> l2Cache;
    private final Map<String, String> l3Database;

    public MultiLevelCache(int l1Capacity, int l2Capacity) {
        this.l3Database = new HashMap<>(); // Standard boundless mock database

        // Initialize L2 Cache (Evicts to L3)
        this.l2Cache = new LinkedHashMap<String, String>(l2Capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                if (size() > l2Capacity) {
                    System.out.println("  [L2 EVIC] Capacity breached. Cascading '" + eldest.getKey() + "' down to L3 Database.");
                    l3Database.put(eldest.getKey(), eldest.getValue());
                    return true;
                }
                return false;
            }
        };

        // Initialize L1 Cache (Evicts to L2)
        this.l1Cache = new LinkedHashMap<String, String>(l1Capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                if (size() > l1Capacity) {
                    System.out.println("  [L1 EVIC] Capacity breached. Cascading '" + eldest.getKey() + "' down to L2 Cache.");
                    l2Cache.put(eldest.getKey(), eldest.getValue());
                    return true;
                }
                return false;
            }
        };
    }

    /**
     * Stores data natively at the tip of the spear (L1)
     */
    public void put(String key, String value) {
        System.out.println("[WRITE] Saving " + key + " into L1 Cache...");
        l1Cache.put(key, value);
    }

    /**
     * Fetches data, bubbling it up to L1 if found in deeper layers.
     */
    public String get(String key) {
        System.out.print("[READ] Requesting " + key + "... ");

        // Check L1 (Blazing fast)
        if (l1Cache.containsKey(key)) {
            System.out.println("HIT in L1!");
            return l1Cache.get(key);
        }

        // Check L2 (Slower)
        if (l2Cache.containsKey(key)) {
            System.out.println("HIT in L2! Promoting to L1.");
            String val = l2Cache.remove(key); // Take it out of L2
            l1Cache.put(key, val);            // Put it into L1 (this might trigger L1 evictions!)
            return val;
        }

        // Check L3 (Slowest disk hit)
        if (l3Database.containsKey(key)) {
            System.out.println("HIT in L3 Database! Promoting straight to L1.");
            String val = l3Database.get(key); // Leave copy in L3 permanent storage
            l1Cache.put(key, val);            // Put it into L1
            return val;
        }

        System.out.println("MISS. Data does not exist anywhere.");
        return null;
    }


    /**
     * Prints map states roughly 
     */
    public void printArchitectureState() {
        System.out.println("\n--- Current System Architecture State ---");
        System.out.println("L1 Cache (Max 2): " + l1Cache.keySet());
        System.out.println("L2 Cache (Max 3): " + l2Cache.keySet());
        System.out.println("L3 Database     : " + l3Database.keySet());
        System.out.println("-----------------------------------------\n");
    }

    public static void main(String[] args) {
        // Tight memory: L1 holds just 2 items. L2 holds 3.
        MultiLevelCache sys = new MultiLevelCache(2, 3);

        sys.put("User_A", "{id: 1, name: Alice}");
        sys.put("User_B", "{id: 2, name: Bob}");
        sys.put("User_C", "{id: 3, name: Charlie}"); // Triggers L1 eviction of User_A
        sys.put("User_D", "{id: 4, name: Diana}");   // Triggers L1 eviction of User_B
        
        sys.printArchitectureState();

        // Reading B will fetch it from L2 and bubble it up to L1, evicting the oldest from L1 (C)
        sys.get("User_B");
        sys.printArchitectureState();

        // Push massive volume to trigger L2 cascade into L3
        sys.put("User_E", "{...}"); // Evicts D from L1 to L2
        sys.put("User_F", "{...}"); // Evicts B from L1 to L2 -> L2 now full (A,C,D,B - wait A pushed to L3!)
        sys.put("User_G", "{...}"); // Evicts E from L1 to L2 -> L2 full (C pushed to L3!)
        
        sys.printArchitectureState();
        
        // Reading A fetches it from the deepest DB and rockets it back to L1
        sys.get("User_A");
        sys.printArchitectureState();
    }
}
