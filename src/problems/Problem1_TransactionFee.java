package problems;

import model.Transaction;
import sorting.BubbleSort;
import sorting.InsertionSort;

import java.util.Comparator;

public class Problem1_TransactionFee {
    public static void runProblem(Transaction[] transactions) {
        System.out.println("\n--- Problem 1: Transaction Fee Sorting ---");
        
        // 1. Bubble Sort (ascending fee)
        Transaction[] bubbleSorted = transactions.clone();
        int swaps = BubbleSort.sort(bubbleSorted, Comparator.comparingDouble(Transaction::getFee));
        System.out.println("\nBubble Sort (Ascending Fee):");
        System.out.println("Swaps counted: " + swaps);
        for (Transaction t : bubbleSorted) {
            String flag = t.getFee() > 50 ? " [HIGH FEE - Flagged]" : "";
            System.out.println(t + flag);
        }
        
        // 2. Insertion Sort (fee + timestamp)
        System.out.println("\nInsertion Sort (Fee Ascending then Timestamp Ascending):");
        Transaction[] insertionSorted = transactions.clone();
        int shifts = InsertionSort.sort(insertionSorted, 
            Comparator.comparingDouble(Transaction::getFee)
                      .thenComparingLong(Transaction::getTimestamp));
        System.out.println("Shifts counted (stable sort): " + shifts);
        for (Transaction t : insertionSorted) {
            String flag = t.getFee() > 50 ? " [HIGH FEE - Flagged]" : "";
            System.out.println(t + flag);
        }
    }
}
