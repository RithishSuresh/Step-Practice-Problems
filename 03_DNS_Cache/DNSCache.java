package dnscache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Problem 3: DNS Cache with TTL
 * Uses an access-ordered LinkedHashMap to implement a fast, constant-time LRU Cache.
 */
public class DNSCache {

    // Helper class to store the IP and its strict Expiration Time
    private static class CacheEntry {
        String ipAddress;
        long expirationTime;

        public CacheEntry(String ipAddress, long ttlMillis) {
            this.ipAddress = ipAddress;
            this.expirationTime = System.currentTimeMillis() + ttlMillis;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }

    // The core storage mechanism
    private final Map<String, CacheEntry> cache;

    // Analytics components
    private final int capacity;
    private int hits = 0;
    private int misses = 0;

    /**
     * Initializes the DNS cache with a strict maximum capacity constraint.
     * @param capacity Max number of entries before LRU eviction begins.
     */
    public DNSCache(int capacity) {
        this.capacity = capacity;

        // Customizing the LinkedHashMap
        // initialCapacity=capacity, loadFactor=0.75f, accessOrder=true (crucial for LRU)
        this.cache = new LinkedHashMap<String, CacheEntry>(capacity, 0.75f, true) {
            // This method is invoked by put(). If it returns true, the eldest entry is deleted.
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, CacheEntry> eldest) {
                if (size() > DNSCache.this.capacity) {
                    System.out.println("[EVICT-LRU] Cache full. Evicting least recently used domain: " + eldest.getKey());
                    return true;
                }
                return false;
            }
        };
    }

    /**
     * Store a domain mapping in the Cache in O(1) time.
     * @param domain Domain Name (e.g. google.com)
     * @param ip The IP Address counterpart
     * @param ttlMillis How long the record should live in milliseconds
     */
    public void put(String domain, String ip, long ttlMillis) {
        cache.put(domain, new CacheEntry(ip, ttlMillis));
        System.out.println("[CACHE] Added " + domain + " -> " + ip + " (TTL: " + ttlMillis + "ms)");
    }

    /**
     * Fetch a domain from the Cache in O(1) time. Also evaluates TTL expiration dynamically.
     * @param domain Domain Name to look up
     * @return IP Address if found and valid, otherwise null.
     */
    public String get(String domain) {
        CacheEntry entry = cache.get(domain);

        if (entry == null) {
            System.out.println("[MISS] Domain " + domain + " not found in cache.");
            misses++;
            return null;
        }

        // Validate TTL
        if (entry.isExpired()) {
            System.out.println("[EXPIRED] Domain " + domain + " cache TTL expired. Evicting it now.");
            cache.remove(domain);
            misses++;
            return null;
        }

        // Valid Hit
        System.out.println("[HIT] Domain " + domain + " resolved from cache -> " + entry.ipAddress);
        hits++;
        return entry.ipAddress;
    }

    /**
     * Console output for Hit/Miss performance.
     */
    public void printStats() {
        int totalRequests = hits + misses;
        double hitRatio = totalRequests == 0 ? 0.0 : ((double) hits / totalRequests) * 100;

        System.out.println("\n--- DNS Cache Statistics ---");
        System.out.println("Total Requests: " + totalRequests);
        System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
        System.out.println("Hit Ratio: " + String.format("%.1f", hitRatio) + "%");
        System.out.println("Current Capacity Utilization: " + cache.size() + "/" + capacity);
        System.out.println("----------------------------\n");
    }

    public static void main(String[] args) throws InterruptedException {
        // Create a cache that only holds 2 items max
        DNSCache dns = new DNSCache(2);

        // Put initial values
        dns.put("google.com", "142.250.190.4", 5000);  // 5 seconds TTL
        dns.put("yahoo.com", "74.6.143.25", 2000);     // 2 seconds TTL

        // Get value immediately (Hit expected)
        dns.get("google.com");

        // Wait 3 seconds
        System.out.println("... waiting 3 seconds ...");
        Thread.sleep(3000);

        // Get google (Hit expected, 5sec > 3sec)
        dns.get("google.com");

        // Get yahoo (Expired expected, 2sec < 3sec)
        dns.get("yahoo.com");

        // Force LRU Eviction by adding new domains (Exceeding max capacity of 2)
        System.out.println("... forcing LRU eviction ...");
        dns.put("bing.com", "13.107.21.200", 10000);
        dns.put("duckduckgo.com", "52.250.42.157", 10000);

        // Because we recently accessed google.com, yahoo.com was already expired/evicted,
        // bing was added, duck duck go was added. The capacity is 2. 
        // We expect google.com might actually get evicted next if it's the oldest! Let's check:
        dns.get("google.com"); 

        dns.printStats();
    }
}
