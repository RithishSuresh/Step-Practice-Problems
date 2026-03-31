package searching;

import java.util.Comparator;

public class BinarySearch {

    public static class Result {
        public int index;
        public int comparisons;
        public Result(int index, int comparisons) {
            this.index = index;
            this.comparisons = comparisons;
        }
    }

    public static <T> Result findExactMatch(T[] arr, T target, Comparator<T> c) {
        int low = 0;
        int high = arr.length - 1;
        int comps = 0;
        
        while (low <= high) {
            int mid = low + (high - low) / 2; // Iterative approach, exact mid calc
            comps++;
            int cmp = c.compare(arr[mid], target);
            if (cmp == 0) return new Result(mid, comps);
            if (cmp < 0) low = mid + 1;
            else high = mid - 1;
        }
        return new Result(-1, comps);
    }
    
    public static <T> int countOccurrences(T[] arr, T target, Comparator<T> c) {
        Result first = findFirstOccurrence(arr, target, c);
        if (first.index == -1) return 0;
        Result last = findLastOccurrence(arr, target, c);
        return last.index - first.index + 1;
    }
    
    public static <T> Result findFirstOccurrence(T[] arr, T target, Comparator<T> c) {
        int low = 0, high = arr.length - 1;
        int result = -1, comps = 0;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            comps++;
            int cmp = c.compare(arr[mid], target);
            if (cmp == 0) {
                result = mid;
                high = mid - 1; // explore left for first
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return new Result(result, comps);
    }
    
    public static <T> Result findLastOccurrence(T[] arr, T target, Comparator<T> c) {
        int low = 0, high = arr.length - 1;
        int result = -1, comps = 0;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            comps++;
            int cmp = c.compare(arr[mid], target);
            if (cmp == 0) {
                result = mid;
                low = mid + 1; // explore right for last
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return new Result(result, comps);
    }

    public static <T> Result findFloor(T[] arr, T target, Comparator<T> c) {
        int low = 0, high = arr.length - 1;
        int result = -1, comps = 0;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            comps++;
            int cmp = c.compare(arr[mid], target);
            if (cmp == 0) return new Result(mid, comps);
            if (cmp < 0) {
                result = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return new Result(result, comps);
    }

    public static <T> Result findCeiling(T[] arr, T target, Comparator<T> c) {
        int low = 0, high = arr.length - 1;
        int result = -1, comps = 0;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            comps++;
            int cmp = c.compare(arr[mid], target);
            if (cmp == 0) return new Result(mid, comps);
            if (cmp < 0) {
                low = mid + 1;
            } else {
                result = mid;
                high = mid - 1;
            }
        }
        return new Result(result, comps);
    }
    
    // Returns index where target would be inserted to maintain sorted order
    public static <T> Result getInsertionIndex(T[] arr, T target, Comparator<T> c) {
        int low = 0, high = arr.length - 1;
        int comps = 0;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            comps++;
            if (c.compare(arr[mid], target) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return new Result(low, comps);
    }
}
