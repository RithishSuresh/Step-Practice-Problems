package usernamechecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Problem 1: Social Media Username Availability Checker
 * Demonstrates the use of HashSet for O(1) lookups and HashMap for O(1) tracking.
 */
public class UsernameChecker {

    // Set to store the successfully registered usernames
    private HashSet<String> registeredUsers;

    // Map to track how many times a taken username request was attempted
    private HashMap<String, Integer> attemptTracker;

    public UsernameChecker() {
        this.registeredUsers = new HashSet<>();
        this.attemptTracker = new HashMap<>();
    }

    /**
     * Checks if a username is available. If it is, registers it.
     * If not, tracks the attempt and returns a list of suggested alternatives.
     *
     * @param username The desired username
     * @return True if registered successfully, False if taken.
     */
    public boolean register(String username) {
        // O(1) time complexity check using HashSet
        if (!registeredUsers.contains(username)) {
            registeredUsers.add(username);
            System.out.println("Success: Username '" + username + "' registered efficiently!");
            return true;
        } else {
            // Username is taken; O(1) tracking using HashMap
            attemptTracker.put(username, attemptTracker.getOrDefault(username, 0) + 1);
            
            System.out.println("Failed: Username '" + username + "' is already taken.");
            List<String> suggestions = generateSuggestions(username, 3);
            System.out.println("Try these alternatives: " + suggestions);
            return false;
        }
    }

    /**
     * Generates a list of alternative available usernames.
     *
     * @param baseName The original taken username
     * @param count    How many suggestions to generate
     * @return List of available username suggestions
     */
    private List<String> generateSuggestions(String baseName, int count) {
        List<String> suggestions = new ArrayList<>();
        int suffix = 1;

        while (suggestions.size() < count) {
            String candidate = baseName + suffix;
            // O(1) check again
            if (!registeredUsers.contains(candidate)) {
                suggestions.add(candidate);
            }
            suffix++;
        }
        return suggestions;
    }

    /**
     * Finds and prints the most attempted given username that was already taken.
     */
    public void printMostAttemptedUsername() {
        String mostAttempted = null;
        int maxAttempts = 0;

        for (Map.Entry<String, Integer> entry : attemptTracker.entrySet()) {
            if (entry.getValue() > maxAttempts) {
                maxAttempts = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }

        if (mostAttempted != null) {
            System.out.println("\nAnalytics: The most attempted taken username is '" + 
                               mostAttempted + "' with " + maxAttempts + " failed attempts.");
        } else {
            System.out.println("\nAnalytics: No failed attempts recorded yet.");
        }
    }

    public static void main(String[] args) {
        UsernameChecker checker = new UsernameChecker();

        System.out.println("--- Registering Basic Users ---");
        checker.register("john_doe");
        checker.register("jane_smith");
        checker.register("cool_coder");

        System.out.println("\n--- Handling Collisions (Taken Usernames) ---");
        checker.register("john_doe");
        checker.register("john_doe");
        checker.register("cool_coder");
        checker.register("john_doe"); // John Doe is highly sought after
        
        // If they like a suggestion, they can register it:
        checker.register("john_doe1"); 

        System.out.println("\n--- Triggering Suggestions Again ---");
        // since john_doe1 is now taken, suggestions should skip it
        checker.register("john_doe"); 

        checker.printMostAttemptedUsername();
    }
}
