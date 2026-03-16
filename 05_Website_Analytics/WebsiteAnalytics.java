package websiteanalytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Problem 5: Real-Time Website Analytics Dashboard
 * Aggregates visit data using HashMaps and HashSets, and generates top-k using a Min-Heap.
 */
public class WebsiteAnalytics {

    private final Map<String, Integer> pageViews;
    private final Map<String, Integer> trafficSources;
    private final HashSet<String> uniqueVisitors;

    public WebsiteAnalytics() {
        this.pageViews = new HashMap<>();
        this.trafficSources = new HashMap<>();
        this.uniqueVisitors = new HashSet<>();
    }

    /**
     * Records a single page visit event in O(1) time.
     * @param pageUrl       The URL visited
     * @param visitorId     Unique identifier for the user (Session / IP)
     * @param trafficSource Where the user came from
     */
    public synchronized void recordVisit(String pageUrl, String visitorId, String trafficSource) {
        // Track Page Views
        pageViews.put(pageUrl, pageViews.getOrDefault(pageUrl, 0) + 1);

        // Track Unique Visitors
        uniqueVisitors.add(visitorId);

        // Track Traffic Sources
        trafficSources.put(trafficSource, trafficSources.getOrDefault(trafficSource, 0) + 1);
    }

    /**
     * Retrieves the top K most visited pages using a Min-Heap for O(P log K) efficiency.
     * @param k The number of top pages to return
     * @return A list of map entries sorted by most views
     */
    public List<Map.Entry<String, Integer>> getTopKPages(int k) {
        // Create Min-Heap prioritizing smallest view counts
        PriorityQueue<Map.Entry<String, Integer>> minHeap = new PriorityQueue<>(
            (a, b) -> a.getValue().compareTo(b.getValue())
        );

        for (Map.Entry<String, Integer> entry : pageViews.entrySet()) {
            minHeap.offer(entry);
            // If heap size exceeds K, drop the smallest element
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        // Extract elements from heap into a list
        List<Map.Entry<String, Integer>> topPages = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            topPages.add(minHeap.poll());
        }

        // Min-Heap pops from smallest to largest. Reverse it for descending UI display.
        Collections.reverse(topPages);
        return topPages;
    }

    /**
     * Simulates displaying the live dashboard.
     */
    public void displayDashboard() {
        // Count total views by summing all page views.
        int totalViews = 0;
        for (int count : pageViews.values()) {
            totalViews += count;
        }

        System.out.println("====== Live Analytics Dashboard ======");
        System.out.println("Total Page Views:      " + totalViews);
        System.out.println("Unique Visitors:       " + uniqueVisitors.size());
        
        System.out.println("\n--- Traffic Sources ---");
        for (Map.Entry<String, Integer> source : trafficSources.entrySet()) {
            System.out.println("- " + source.getKey() + ": " + source.getValue());
        }

        System.out.println("\n--- Top 3 Pages ---");
        List<Map.Entry<String, Integer>> top3Pages = getTopKPages(3);
        int rank = 1;
        for (Map.Entry<String, Integer> page : top3Pages) {
            System.out.println(rank + ". " + page.getKey() + " (" + page.getValue() + " views)");
            rank++;
        }
        System.out.println("======================================\n");
    }

    public static void main(String[] args) {
        WebsiteAnalytics analytics = new WebsiteAnalytics();

        // Simulate traffic
        analytics.recordVisit("/home", "user1_ip", "Google");
        analytics.recordVisit("/home", "user2_ip", "Direct");
        analytics.recordVisit("/about", "user1_ip", "Google"); // Same user, different page
        analytics.recordVisit("/pricing", "user3_ip", "Twitter");
        analytics.recordVisit("/home", "user4_ip", "Facebook");
        analytics.recordVisit("/pricing", "user5_ip", "Direct");
        analytics.recordVisit("/pricing", "user6_ip", "Google");
        analytics.recordVisit("/contact", "user7_ip", "Direct");

        // Display results
        analytics.displayDashboard();
    }
}
