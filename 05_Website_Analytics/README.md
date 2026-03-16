# Problem 5: Real-Time Website Analytics Dashboard

## 1. Problem Understanding
- **Scenario:** A website needs a live backend dashboard to track visitor metrics in real-time. Crucial metrics include the total number of page views across the site, the total number of strictly unique visitors, where the traffic is originating from (referrals), and a live ranking of the top 10 most visited pages.
- **Inputs:** A stream of visiting events containing `pageUrl`, `visitorId` (e.g., IP or Session ID), and `trafficSource` (e.g., "Google", "Direct").
- **Outputs:** Aggregated stats (Total Views, Unique Visitors count, Source breakdown) and Top K pages list.
- **Constraints:** Must process high-volume incoming streams quickly. $O(1)$ tracking for views and unique visitors. Calculating Top 10 should be efficient, avoiding full $O(N \log N)$ sorting if possible.

## 2. Data Structures Used
- **HashMap (`HashMap<String, Integer>`):**
  - **Page Views:** Tracks views per specific URL ($O(1)$).
  - **Traffic Sources:** Tracks conversions per referring website ($O(1)$).
- **HashSet (`HashSet<String>`):** 
  - **Unique Visitors:** Stores `visitorIds`. A HashSet automatically ignores duplicates and returns its `.size()` in $O(1)$, which perfectly represents total unique users.
- **PriorityQueue (Min-Heap):** 
  - **Top K Pages:** Used strictly during the reporting phase to calculate the top pages in $O(N \log K)$ instead of $O(N \log N)$.

## 3. System Design Approach
1. **Event Reception (`visitPage`):**
   - The method activates when a user hits a page.
   - Updates `pageViews` map by incrementing the URL count.
   - Adds `visitorId` to the `uniqueVisitors` HashSet.
   - Updates `trafficSources` map by incrementing the given source.
2. **Generating Top K Pages:**
   - Iterate over the `pageViews` map entries.
   - Push each entry into a Min-Heap (`PriorityQueue`). The heap prioritizes the smallest view counts.
   - If the Priority Queue size exceeds $K$, `poll()` the root (which drops the smallest value).
   - By the end of the iteration, only the $K$ largest elements remain in the heap.
3. **Report Generation:** Read `.size()` from the HashSet and print the HashMaps.

## 4. Java Implementation
See `WebsiteAnalytics.java` for the logic demonstrating multiple combined data structures functioning as an analytics aggregation engine.

## 5. Time and Space Complexity
- **Time Complexity:**
  - **Recording a visit (`visitPage`):** $O(1)$ amortized, as we perform 3 constant-time Hash operations.
  - **Fetching Top K Pages:** $O(P \log K)$ where $P$ is the number of distinct pages and $K$ is the size of the Top list (10). If $K \ll P$, this is drastically faster than an $O(P \log P)$ complete sort.
- **Space Complexity:**
  - **Worst Case:** $O(P + U + S)$ where $P$ is distinct pages, $U$ is unique visitors, and $S$ is distinct traffic sources. Generally bounded by available heap memory.

## 6. Example Execution
**Input:**
- User1 visits "/home" from "Google"
- User2 visits "/home" from "Direct"
- User1 visits "/about" from "Google"
- User3 visits "/contact" from "Twitter"

**Output:**
```
--- Live Analytics Dashboard ---
Total Page Views: 4
Total Unique Visitors: 3
Traffic Sources: {Google=2, Twitter=1, Direct=1}

--- Top 2 Pages ---
1. /home (2 views)
2. /about (1 views)
```

## 7. Possible Improvements (Real-world systems)
- **HyperLogLog:** A HashSet storing millions of `visitorId` strings requires gigs of RAM. Real-world systems like Redis use `HyperLogLog`, a probabilistic algorithm estimating unique visitors with 99% accuracy using only 12KB of memory.
- **Stream Processing:** Use Apache Kafka and Apache Flink / Spark Streaming to aggregate metrics continuously over tumbling windows (e.g., updating stats every 5 seconds).
- **Count-Min Sketch:** A probabilistic data structure used to track frequencies of events (like page views) in a massive data stream using minimal memory.
