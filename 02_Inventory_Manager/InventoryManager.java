package inventorymanager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Problem 2: E-commerce Flash Sale Inventory Manager
 * Showcases HashMap for stock management and Question (FIFO) for Waitlists.
 */
public class InventoryManager {

    // Simulating Database: Maps ProductID to remaining stock
    private Map<String, Integer> inventory;

    // Simulating Waitlist: Maps ProductID to a Queue of UserIDs waiting for it
    private Map<String, Queue<String>> waitlists;

    public InventoryManager() {
        // We use standard HashMaps here, but ConcurrentHashMap is ideal for multithreading
        this.inventory = new HashMap<>();
        this.waitlists = new HashMap<>();
    }

    /**
     * Initializes or adds stock for a given product.
     * @param productId The ID of the product.
     * @param stock     The amount of stock to add.
     */
    public synchronized void addStock(String productId, int stock) {
        inventory.put(productId, inventory.getOrDefault(productId, 0) + stock);
        waitlists.putIfAbsent(productId, new LinkedList<>()); // Prepare empty queue
        System.out.println("Stock added for " + productId + ": " + stock + " total units.");
        
        // If stock was just added and people are waiting, fulfill their orders immediately
        processWaitlist(productId);
    }

    /**
     * Attempts to purchase a product for a specific user.
     * @param userId    The user attempting the purchase
     * @param productId The product to purchase
     */
    public synchronized void buyProduct(String userId, String productId) {
        if (!inventory.containsKey(productId)) {
            System.out.println("Error: " + productId + " does not exist in our catalog.");
            return;
        }

        int currentStock = inventory.get(productId);

        if (currentStock > 0) {
            // Success: decrement stock
            inventory.put(productId, currentStock - 1);
            System.out.println("User " + userId + " successfully purchased " + productId + "!");
        } else {
            // Out of stock: add to FIFO waitlist
            waitlists.get(productId).offer(userId);
            System.out.println("Alert: " + productId + " is out of stock! User " + userId + " added to waitlist.");
        }
    }

    /**
     * A user cancels their order, returning stock to the pool,
     * which automatically triggers fulfillment for the next person on the waitlist.
     * @param userId    The user cancelling
     * @param productId The product being returned
     */
    public synchronized void cancelOrder(String userId, String productId) {
        System.out.println("Update: User " + userId + " canceled their order for " + productId + ".");
        
        // First increment stock locally
        inventory.put(productId, inventory.getOrDefault(productId, 0) + 1);

        // Immediately attempt to distribute to the first person waiting
        processWaitlist(productId);
    }

    /**
     * Helper to process the waitlist whenever stock goes up.
     * @param productId The product to process
     */
    private synchronized void processWaitlist(String productId) {
        Queue<String> productWaitlist = waitlists.get(productId);
        int currentStock = inventory.getOrDefault(productId, 0);

        while (currentStock > 0 && productWaitlist != null && !productWaitlist.isEmpty()) {
            // O(1) Dequeue using poll()
            String luckyUser = productWaitlist.poll();
            
            // Give item to user
            currentStock--;
            inventory.put(productId, currentStock);
            System.out.println("Waitlist Alert: User " + luckyUser + " automatically received " + productId + " from the waitlist!");
        }
    }

    /**
     * Main execution showing step-by-step logic
     */
    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();

        manager.addStock("PhoneX", 2);

        // Flash Sale begins
        manager.buyProduct("Alice", "PhoneX");
        manager.buyProduct("Bob", "PhoneX");

        // These users arrive late and hit the waitlist
        manager.buyProduct("Charlie", "PhoneX");
        manager.buyProduct("David", "PhoneX");

        // Alice decides she doesn't want it anymore
        System.out.println("\n--- Order Cancellation event ---");
        manager.cancelOrder("Alice", "PhoneX");

        // New stock comes in from the warehouse
        System.out.println("\n--- Warehouse Delivery event ---");
        manager.addStock("PhoneX", 1);
    }
}
