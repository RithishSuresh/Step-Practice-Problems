package financialanalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Problem 9: Financial Transactions Algorithms
 * Demonstrates HashMap for Two-Sum, Two-Pointers for K-Sum, and HashSet for Deduplication.
 */
public class FinancialAnalysis {

    /**
     * 1. Dynamic Classic Two-Sum (O(N) Time, O(N) Space)
     * Finds exactly two transactions that perfectly equal a target reconciliation amount.
     */
    public static int[] reconcileTwoSum(int[] transactions, int targetAmount) {
        // Map <Transaction_Amount, Array_Index>
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < transactions.length; i++) {
            int complement = targetAmount - transactions[i];

            // O(1) Check if the mathematical missing piece already passed us by
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i }; // Found it! Return the indices.
            }
            
            // Otherwise, store this amount for future math checks
            map.put(transactions[i], i);
        }

        return new int[0]; // No match found
    }

    /**
     * 2. K-Sum Variant (3-Sum implementation) (O(N^2) Time, O(1) Space)
     * Finds ALL triplets of transactions that equal a target value (e.g. 0 to balance books).
     */
    public static List<List<Integer>> reconcileThreeSum(int[] transactions, int targetAmount) {
        List<List<Integer>> results = new ArrayList<>();
        
        // Step 1: Sorting is mathematically crucial for Two-Pointer algorithm
        Arrays.sort(transactions);

        for (int i = 0; i < transactions.length - 2; i++) {
            // Prevent duplicate triplets (Optimization)
            if (i > 0 && transactions[i] == transactions[i - 1]) continue;

            int left = i + 1;
            int right = transactions.length - 1;

            while (left < right) {
                int sum = transactions[i] + transactions[left] + transactions[right];

                if (sum == targetAmount) {
                    // Match found!
                    results.add(Arrays.asList(transactions[i], transactions[left], transactions[right]));
                    
                    // Move pointers past duplicates
                    while (left < right && transactions[left] == transactions[left + 1]) left++;
                    while (left < right && transactions[right] == transactions[right - 1]) right--;
                    
                    left++;
                    right--;
                } else if (sum < targetAmount) {
                    left++; // Need a larger number, move left pointer rightwards
                } else {
                    right--; // Need a smaller number, move right pointer leftwards
                }
            }
        }
        return results;
    }


    /**
     * Helper class representing a complex banking transaction
     */
    private static class Transaction {
        String accountId;
        String amount; // String to avoid float precision loss in hashing
        long minuteTimestamp; // Truncated to the exact minute 

        public Transaction(String acc, String amt, long ts) {
            this.accountId = acc;
            this.amount = amt;
            this.minuteTimestamp = ts;
        }

        // Generate deterministic signature
        public String generateSignature() {
            return accountId + "_" + amount + "_" + minuteTimestamp;
        }
    }

    /**
     * 3. Duplicate Transaction Anomaly Detection (O(1) Check, O(N) Space)
     * If a user clicks "Buy" twice in the same minute, flag it using a HashSet.
     */
    public static void detectDuplicates(List<Transaction> stream) {
        HashSet<String> processedSignatures = new HashSet<>();

        System.out.println("\n--- Real-Time Duplicate Detection ---");
        for (Transaction tx : stream) {
            String signature = tx.generateSignature();

            // HashSet.add() returns false if the item was ALREADY in the set.
            if (!processedSignatures.add(signature)) {
                System.out.println("[ALERT] Fraud/Duplicate detected blocking: " + signature);
            } else {
                System.out.println("[OK] Processed " + signature);
            }
        }
    }

    public static void main(String[] args) {
        int[] logs = {100, -500, 250, 400, 150, 50, 100};

        System.out.println("--- Two-Sum Reconciliation (Target: 350) ---");
        int[] twoSumResult = reconcileTwoSum(logs, 350);
        if (twoSumResult.length > 0) {
            System.out.println("Reconciled Indices: " + twoSumResult[0] + " & " + twoSumResult[1]);
            System.out.println("Values: " + logs[twoSumResult[0]] + " + " + logs[twoSumResult[1]] + " = 350");
        }

        System.out.println("\n--- Three-Sum Reconciliation (Target: 0 to balance) ---");
        // We know that -500 + 400 + 100 = 0. And -150 + ... 
        System.out.println("Groups that zero out: " + reconcileThreeSum(logs, 0));


        // Testing Deduplication
        List<Transaction> stream = new ArrayList<>();
        stream.add(new Transaction("ACC-1", "50.00", 16400));
        stream.add(new Transaction("ACC-2", "99.99", 16400));
        stream.add(new Transaction("ACC-1", "50.00", 16400)); // Accidental Double Click!
        
        detectDuplicates(stream);
    }
}
