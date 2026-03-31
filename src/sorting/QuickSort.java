package sorting;

import java.util.Comparator;
import java.util.Random;

public class QuickSort {
    
    public enum PivotStrategy {
        LOMUTO_LAST, RANDOM, MEDIAN_OF_THREE
    }

    /**
     * Sorts an array using Quick Sort with specific Pivot Strategy.
     * Average O(n log n), worst O(n^2).
     */
    public static <T> void sort(T[] arr, Comparator<T> c, PivotStrategy strategy) {
        quickSort(arr, 0, arr.length - 1, c, strategy);
    }
    
    private static <T> void quickSort(T[] arr, int low, int high, Comparator<T> c, PivotStrategy strategy) {
        if (low < high) {
            int pi = partition(arr, low, high, c, strategy);
            quickSort(arr, low, pi - 1, c, strategy);
            quickSort(arr, pi + 1, high, c, strategy);
        }
    }
    
    private static <T> int partition(T[] arr, int low, int high, Comparator<T> c, PivotStrategy strategy) {
        int pivotIndex = getPivotIndex(arr, low, high, c, strategy);
        // Move pivot to the end
        swap(arr, pivotIndex, high);
        
        T pivot = arr[high];
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (c.compare(arr[j], pivot) <= 0) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }
    
    private static <T> int getPivotIndex(T[] arr, int low, int high, Comparator<T> c, PivotStrategy strategy) {
        if (strategy == PivotStrategy.RANDOM) {
            Random rand = new Random();
            return rand.nextInt(high - low + 1) + low;
        } else if (strategy == PivotStrategy.MEDIAN_OF_THREE) {
            int mid = low + (high - low) / 2;
            boolean lowGmid = c.compare(arr[low], arr[mid]) > 0;
            boolean lowGhigh = c.compare(arr[low], arr[high]) > 0;
            boolean midGhigh = c.compare(arr[mid], arr[high]) > 0;
            
            if (lowGmid) {
                if (midGhigh) return mid;
                if (lowGhigh) return high;
                return low;
            } else {
                if (lowGhigh) return low;
                if (midGhigh) return high;
                return mid;
            }
        }
        return high; // LOMUTO_LAST default
    }
    
    private static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
