package problems;

import searching.LinearSearch;
import searching.BinarySearch;

import java.util.Comparator;
import java.util.Arrays;

public class Problem6_RiskThreshold {
    public static void runProblem(Integer[] riskScores, int targetThreshold) {
        System.out.println("\n--- Problem 6: Risk Threshold Lookup ---");
        System.out.println("Target Threshold: " + targetThreshold);
        
        Comparator<Integer> cmp = Integer::compareTo;
        
        // Linear search (unsorted)
        System.out.println("\nLinear Search (Unsorted Array):");
        int floorLin = LinearSearch.findFloor(riskScores, targetThreshold, cmp);
        System.out.println("Floor Value Index: " + floorLin + " (Value: " + (floorLin >= 0 ? riskScores[floorLin] : "N/A") + ")");
        int ceilLin = LinearSearch.findCeiling(riskScores, targetThreshold, cmp);
        System.out.println("Ceiling Value Index: " + ceilLin + " (Value: " + (ceilLin >= 0 ? riskScores[ceilLin] : "N/A") + ")");
        
        // Binary search (sorted)
        Integer[] sortedScores = riskScores.clone();
        Arrays.sort(sortedScores);
        System.out.println("\nBinary Search (Sorted Array):");
        
        BinarySearch.Result floorBin = BinarySearch.findFloor(sortedScores, targetThreshold, cmp);
        System.out.println("Floor Value Index: " + floorBin.index + 
            " (Value: " + (floorBin.index >= 0 ? sortedScores[floorBin.index] : "N/A") + 
            ", Comparisons: " + floorBin.comparisons + ")");
            
        BinarySearch.Result ceilBin = BinarySearch.findCeiling(sortedScores, targetThreshold, cmp);
        System.out.println("Ceiling Value Index: " + ceilBin.index + 
            " (Value: " + (ceilBin.index >= 0 ? sortedScores[ceilBin.index] : "N/A") + 
            ", Comparisons: " + ceilBin.comparisons + ")");
            
        BinarySearch.Result insIdx = BinarySearch.getInsertionIndex(sortedScores, targetThreshold, cmp);
        System.out.println("Insertion Index: " + insIdx.index + " (Comparisons: " + insIdx.comparisons + ")");
    }
}
