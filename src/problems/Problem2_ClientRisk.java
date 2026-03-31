package problems;

import model.Client;
import sorting.BubbleSort;
import sorting.InsertionSort;

import java.util.Comparator;

public class Problem2_ClientRisk {
    public static void runProblem(Client[] clients) {
        System.out.println("\n--- Problem 2: Client Risk Ranking ---");
        
        // Bubble Sort (ascending riskScore)
        Client[] bubbleSorted = clients.clone();
        int swaps = BubbleSort.sort(bubbleSorted, Comparator.comparingInt(Client::getRiskScore));
        System.out.println("\nBubble Sort (Ascending Risk Score) - Swaps: " + swaps);
        for (Client c : bubbleSorted) System.out.println(c);
        
        // Insertion Sort (riskScore DESC + balance ASC)
        Client[] insertionSorted = clients.clone();
        int shifts = InsertionSort.sort(insertionSorted, 
            Comparator.comparingInt(Client::getRiskScore).reversed()
                      .thenComparingDouble(Client::getAccountBalance));
        System.out.println("\nInsertion Sort (Risk DESC, Balance ASC) - Shifts: " + shifts);
        
        // Identify top 10 highest risk clients
        System.out.println("\nTop Highest Risk Clients (up to 10):");
        int limit = Math.min(10, insertionSorted.length);
        for (int i = 0; i < limit; i++) {
            System.out.println((i + 1) + ". " + insertionSorted[i]);
        }
    }
}
