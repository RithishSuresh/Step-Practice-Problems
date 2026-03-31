package sorting;

import java.util.Comparator;
import java.util.Arrays;

public class MergeSort {
    /**
     * Sorts an array using stable Merge Sort.
     * Time Complexity: Always O(n log n)
     */
    public static <T> void sort(T[] arr, Comparator<T> c) {
        if (arr == null || arr.length <= 1) return;
        
        int n = arr.length;
        int mid = n / 2;
        
        // Divide into left and right sub-arrays
        T[] left = Arrays.copyOfRange(arr, 0, mid);
        T[] right = Arrays.copyOfRange(arr, mid, n);
        
        sort(left, c);
        sort(right, c);
        
        merge(arr, left, right, c);
    }
    
    // Merge two sorted arrays maintaining stability
    private static <T> void merge(T[] arr, T[] left, T[] right, Comparator<T> c) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            // <= ensures stability
            if (c.compare(left[i], right[j]) <= 0) {
                arr[k++] = left[i++];
            } else {
                arr[k++] = right[j++];
            }
        }
        while (i < left.length) {
            arr[k++] = left[i++];
        }
        while (j < right.length) {
            arr[k++] = right[j++];
        }
    }
}
