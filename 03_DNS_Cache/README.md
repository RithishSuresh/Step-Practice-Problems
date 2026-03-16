# Problem 3: DNS Cache with Time-To-Live (TTL)

## 1. Problem Understanding
- **Scenario:** Domain Name System (DNS) resolution is slow because it requires querying external servers to map a domain (e.g., `www.google.com`) to an IP address (e.g., `142.250.190.4`). To speed this up, a local DNS Cache is used. The cache has a maximum capacity and uses a Least Recently Used (LRU) policy. Furthermore, DNS records change, so cached entries must expire after a specific Time-To-Live (TTL).
- **Inputs:** Domain name (Get), Domain + IP + TTL (Put).
- **Outputs:** IP address (if cache hit), or Null (if cache miss or expired). Cache Hit/Miss statistics.
- **Constraints:** $O(1)$ lookups and insertions. Memory is strictly bounded to max capacity.

## 2. Data Structures Used
- **LinkedHashMap (`LinkedHashMap<String, CacheEntry>`):** A standard `HashMap` does not maintain order, making LRU eviction $O(N)$. `LinkedHashMap` maintains a doubly-linked list running through all its entries. By initializing it with `accessOrder = true`, it naturally tracks the Least Recently Used element and brings accessed elements to the front in $O(1)$ time.
- **Custom Object (`CacheEntry`):** A helper class to store both the `ipAddress` and the `expirationTime` (current system time + TTL) together.

## 3. System Design Approach
1. **LRU Cache Implementation:** Extend or compose `LinkedHashMap`. Override the `removeEldestEntry` method so that when `size() > CAPACITY`, the map automatically drops the oldest (Least Recently Used) entry during a `put()`.
2. **Put Operation ($O(1)$):**
   - Calculate absolute expiration time: `System.currentTimeMillis() + TTL`.
   - Store the Domain and the new `CacheEntry`.
3. **Get Operation ($O(1)$):**
   - Lookup the Domain in the map.
   - If **absent**, increment `missCount` and return null.
   - If **present**, check `System.currentTimeMillis() > entry.expirationTime`.
     - If **expired**: remove from map, increment `missCount`, return null.
     - If **valid**: increment `hitCount`, return the IP address.
4. **Statistics Tracking:** Increment integer counters on reads to compute hit/miss ratios dynamically.

## 4. Java Implementation
See `DNSCache.java` for the full, structured implementation showing the overridden `LinkedHashMap`.

## 5. Time and Space Complexity
- **Time Complexity:**
  - **Best/Average Case:** $O(1)$ for both `get` and `put` operations. The doubly-linked list inside `LinkedHashMap` allows $O(1)$ repointing of nodes during access, and HashMap provides $O(1)$ lookups.
  - **Worst Case:** $O(1)$ ignoring rare harsh hash collisions.
- **Space Complexity:**
  - **Best/Average/Worst Case:** $O(K)$ where $K$ is the fixed `CAPACITY` of the cache. Memory usage will not exceed this limit due to the LRU eviction policy.

## 6. Example Execution
**Input:**
- Put `google.com` -> `1.1.1.1` (TTL: 5 seconds)
- Get `google.com` (Immediate)
- Wait 6 seconds...
- Get `google.com`
- Print Stats

**Output:**
```
[CACHE] Added google.com -> 1.1.1.1 (TTL: 5000ms)
[HIT] Domain google.com resolved from cache -> 1.1.1.1
...waiting 6 seconds...
[EXPIRED] Domain google.com cache TTL expired. Evicting.
[MISS] Domain google.com not found in cache.
--- DNS Cache Statistics ---
Total Requests: 2
Hits: 1
Misses: 1
Hit Ratio: 50.0%
Capacity: 1/100
```

## 7. Possible Improvements (Real-world systems)
- **Active Expiration Thread:** Instead of passively waiting for a `get()` call to discover an entry is expired (Passive Expiration), use a background `ScheduledExecutorService` thread that sweeps and actively deletes expired keys to free memory faster.
- **ConcurrentLRUCache:** `LinkedHashMap` is not thread-safe. Standard `Collections.synchronizedMap` creates a global lock bottleneck. Real systems (like **Guava Cache** or **Caffeine**) use segmented locks or non-blocking algorithms for high-throughput concurrency.
- **Negative Caching:** Cache "Domain Not Found" (NXDOMAIN) responses with a short TTL to prevent repeated expensive external lookups for invalid domains.
