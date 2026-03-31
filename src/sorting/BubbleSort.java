package sorting;

import java.util.Comparator;

public class BubbleSort {
    /**
     * Sorts an array using optimized Bubble Sort.
     * Counts and returns the number of swaps.
     * Time Complexity: O(n^2) worst/average, O(n) best case.
     */
    public static <T> int sort(T[] arr, Comparator<T> c) {
        int swaps = 0;
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            // Adjacent swaps
            for (int j = 0; j < n - i - 1; j++) {
                if (c.compare(arr[j], arr[j + 1]) > 0) {
                    T temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swaps++;
                    swapped = true;
                }
            }
            // Early termination if no swaps
            if (!swapped) break;
        }
        return swaps;
    }
}
