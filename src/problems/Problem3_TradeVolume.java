package problems;

import model.Trade;
import sorting.MergeSort;
import sorting.QuickSort;

import java.util.Comparator;
import java.util.Arrays;

public class Problem3_TradeVolume {
    public static void runProblem(Trade[] trades1, Trade[] trades2) {
        System.out.println("\n--- Problem 3: Trade Volume Analysis ---");
        
        Trade[] mergeSorted = trades1.clone();
        MergeSort.sort(mergeSorted, Comparator.comparingDouble(Trade::getVolume));
        System.out.println("\nMerge Sort (Ascending Volume):");
        for (Trade t : mergeSorted) System.out.println(t);
        
        Trade[] quickSorted = trades1.clone();
        QuickSort.sort(quickSorted, Comparator.comparingDouble(Trade::getVolume).reversed(), QuickSort.PivotStrategy.LOMUTO_LAST);
        System.out.println("\nQuick Sort (Descending Volume):");
        for (Trade t : quickSorted) System.out.println(t);
        
        // Merge two sorted arrays
        Trade[] sorted1 = trades1.clone();
        Trade[] sorted2 = trades2.clone();
        MergeSort.sort(sorted1, Comparator.comparingDouble(Trade::getVolume));
        MergeSort.sort(sorted2, Comparator.comparingDouble(Trade::getVolume));
        
        Trade[] merged = new Trade[sorted1.length + sorted2.length];
        int i = 0, j = 0, k = 0;
        while (i < sorted1.length && j < sorted2.length) {
            if (sorted1[i].getVolume() <= sorted2[j].getVolume()) {
                merged[k++] = sorted1[i++];
            } else {
                merged[k++] = sorted2[j++];
            }
        }
        while (i < sorted1.length) merged[k++] = sorted1[i++];
        while (j < sorted2.length) merged[k++] = sorted2[j++];
        
        System.out.println("\nMerged two sorted trade arrays (Ascending Volume):");
        double totalVolume = 0;
        for (Trade t : merged) {
            System.out.println(t);
            totalVolume += t.getVolume();
        }
        System.out.println("\nTotal Volume Computed: " + totalVolume);
    }
}
