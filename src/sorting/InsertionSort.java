package sorting;

import java.util.Comparator;

public class InsertionSort {
    /**
     * Sorts an array using stable Insertion Sort.
     * Returns the number of shifts representing swaps.
     * Time Complexity: O(n^2) worst/average, O(n) best case (adaptive).
     */
    public static <T> int sort(T[] arr, Comparator<T> c) {
        int shifts = 0;
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            T key = arr[i];
            int j = i - 1;
            
            // Shift elements of arr[0..i-1] that are greater than key
            while (j >= 0 && c.compare(arr[j], key) > 0) {
                arr[j + 1] = arr[j];
                j = j - 1;
                shifts++; // Counting as a swap operation for metrics
            }
            arr[j + 1] = key;
        }
        return shifts;
    }
}
