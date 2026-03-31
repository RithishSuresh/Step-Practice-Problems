package problems;

import model.Asset;
import sorting.MergeSort;
import sorting.QuickSort;

import java.util.Comparator;

public class Problem4_Portfolio {
    public static void runProblem(Asset[] assets) {
        System.out.println("\n--- Problem 4: Portfolio Return Sorting ---");
        
        Asset[] mergeSorted = assets.clone();
        MergeSort.sort(mergeSorted, Comparator.comparingDouble(Asset::getReturnRate));
        System.out.println("\nMerge Sort (Ascending Return Rate):");
        for (Asset a : mergeSorted) System.out.println(a);
        
        System.out.println("\nQuick Sort (Return DESC + Volatility ASC):");
        Comparator<Asset> qCmp = Comparator.comparingDouble(Asset::getReturnRate).reversed()
                                           .thenComparingDouble(Asset::getVolatility);
        
        Asset[] qRandom = assets.clone();
        QuickSort.sort(qRandom, qCmp, QuickSort.PivotStrategy.RANDOM);
        System.out.println("-> With Random Pivot:");
        for (Asset a : qRandom) System.out.println(a);
        
        Asset[] qMedian = assets.clone();
        QuickSort.sort(qMedian, qCmp, QuickSort.PivotStrategy.MEDIAN_OF_THREE);
        System.out.println("-> With Median-of-3 Pivot:");
        for (Asset a : qMedian) System.out.println(a);
    }
}
