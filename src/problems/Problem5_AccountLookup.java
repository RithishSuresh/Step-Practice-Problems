package problems;

import searching.LinearSearch;
import searching.BinarySearch;

import java.util.Comparator;
import java.util.Arrays;

public class Problem5_AccountLookup {
    public static void runProblem(String[] accountIds, String targetId) {
        System.out.println("\n--- Problem 5: Account ID Lookup ---");
        System.out.println("Target Account ID: " + targetId);
        
        // Linear Search
        System.out.println("\nLinear Search (Unsorted):");
        int firstOcc = LinearSearch.findFirst(accountIds, targetId);
        System.out.println("First Occurrence Index: " + firstOcc);
        int lastOcc = LinearSearch.findLast(accountIds, targetId);
        System.out.println("Last Occurrence Index: " + lastOcc);
        
        // Binary Search (needs sorted array)
        String[] sortedIds = accountIds.clone();
        Arrays.sort(sortedIds);
        System.out.println("\nBinary Search (Sorted Array):");
        Comparator<String> cmp = String::compareTo;
        
        BinarySearch.Result exact = BinarySearch.findExactMatch(sortedIds, targetId, cmp);
        System.out.println("Exact Match Index: " + exact.index + " (Comparisons: " + exact.comparisons + ")");
        
        int duplicatesCount = BinarySearch.countOccurrences(sortedIds, targetId, cmp);
        System.out.println("Total Occurrences Found: " + duplicatesCount);
        int duplicateCopies = duplicatesCount > 0 ? duplicatesCount - 1 : 0;
        System.out.println("Number of Duplicate Copies: " + duplicateCopies);
    }
}
