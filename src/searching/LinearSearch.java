package searching;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

public class LinearSearch {
    
    public static <T> int findFirst(T[] arr, T target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(target)) return i;
        }
        return -1;
    }
    
    public static <T> int findLast(T[] arr, T target) {
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i].equals(target)) return i;
        }
        return -1;
    }
    
    // Returns index of Floor: greatest element <= target in unsorted array
    public static <T> int findFloor(T[] arr, T target, Comparator<T> c) {
        int bestIdx = -1;
        for (int i = 0; i < arr.length; i++) {
            if (c.compare(arr[i], target) <= 0) {
                if (bestIdx == -1 || c.compare(arr[i], arr[bestIdx]) > 0) {
                    bestIdx = i;
                }
            }
        }
        return bestIdx;
    }
    
    // Returns index of Ceiling: smallest element >= target in unsorted array
    public static <T> int findCeiling(T[] arr, T target, Comparator<T> c) {
        int bestIdx = -1;
        for (int i = 0; i < arr.length; i++) {
            if (c.compare(arr[i], target) >= 0) {
                if (bestIdx == -1 || c.compare(arr[i], arr[bestIdx]) < 0) {
                    bestIdx = i;
                }
            }
        }
        return bestIdx;
    }
}
